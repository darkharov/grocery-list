package app.grocery.list.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.grocery.list.domain.notification.GetNotificationListActionUseCase
import app.grocery.list.domain.notification.GetNotificationListActionUseCase.AppState
import app.grocery.list.domain.notification.GetNotificationListActionUseCase.ScreenLockedAt
import app.grocery.list.domain.notification.HandleProductListPostedUseCase
import app.grocery.list.domain.notification.NotificationContent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@Singleton
class NotificationPublisher @Inject internal constructor(
    @param:ApplicationContext
    private val context: Context,
    private val mapper: NotificationMapper,
    private val getNotificationListAction: GetNotificationListActionUseCase,
    private val notificationManager: NotificationManagerCompat,
    private val handleProductListPublished: HandleProductListPostedUseCase,
) {
    private val appState = MutableSharedFlow<AppState>(replay = 1)
    private val screenLockedAt = MutableSharedFlow<ScreenLockedAt>(replay = 1)
    private val isUserOnFinalScreen = MutableSharedFlow<Boolean>(replay = 1)

    fun onApplicationCreate() {
        ensureDefaultNotificationChannel()
        performNotificationListActions()
        context.registerReceiver(ScreenLockReceiver(), IntentFilter(Intent.ACTION_SCREEN_OFF))
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleObserverImpl())
    }

    private fun ensureDefaultNotificationChannel() {
        val defaultChannel = NotificationChannel(
            NotificationConfigs.DEFAULT_CHANNEL_ID,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager.createNotificationChannel(defaultChannel)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun performNotificationListActions() {
        ProcessLifecycleOwner.get()
            .lifecycleScope
            .launch {
                combine(
                    appState,
                    screenLockedAt,
                    isUserOnFinalScreen,
                ) { appState, screenLockedAt, isUserOnFinalScreen ->
                    Log.e(TAG, "$appState, $screenLockedAt, isUserOnFinalScreen=$isUserOnFinalScreen")
                    getNotificationListAction.execute(
                        appState = appState,
                        screenLockedAt = screenLockedAt,
                        isUserOnFinalScreen = isUserOnFinalScreen,
                        maxNumberOfItems = NotificationConfigs.MAX_VISIBLE_AT_THE_SAME_TIME,
                    )
                }.collectLatest { action ->
                    Log.e(TAG, "action: $action")
                    when (action) {
                        is GetNotificationListActionUseCase.Result.Repost -> {
                            tryToRepost(notifications = action.notifications)
                        }
                        is GetNotificationListActionUseCase.Result.CancelAll -> {
                            cancelAllNotifications()
                        }
                        is GetNotificationListActionUseCase.Result.NothingToDo -> {
                            // nothing to do
                        }
                    }
                }
            }
    }

    private suspend fun tryToRepost(notifications: List<NotificationContent>): Boolean =
        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            cancelAllNotifications()
            post(notifications)
            true
        } else {
            false
        }

    private fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    private suspend fun post(notifications: List<NotificationContent>) {
        val reversed = notifications.reversed() // The last ones rise to the top, but we need opposite behavior
        for (notification in reversed) {
            val androidNotification = mapper.transform(notification)
            notificationManager.notify(
                NotificationConfigs.TYPE_PRODUCT,
                notification.groupKey,
                androidNotification,
            )
            handleProductListPublished.execute()
        }
    }

    fun notifyIsUserOnFinalScreenChange(newValue: Boolean) {
        Log.e(TAG, "notifyIsUserOnFinalScreenChange: $newValue")
        isUserOnFinalScreen.tryEmit(newValue)
    }

    private inner class ScreenLockReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            screenLockedAt.tryEmit(ScreenLockedAt(timeMs = System.currentTimeMillis()))
        }
    }

    private inner class LifecycleObserverImpl : DefaultLifecycleObserver {

        override fun onResume(owner: LifecycleOwner) {
            appState.tryEmit(AppState.Resumed)
        }

        override fun onPause(owner: LifecycleOwner) {
            appState.tryEmit(AppState.Paused(timeMs = System.currentTimeMillis()))
        }
    }

    companion object {
        const val TAG = "NotificationPublisher"
    }
}
