package app.grocery.list.domain.notification

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs
import kotlinx.coroutines.flow.first
import org.joda.time.DateTime

@Singleton
class GetNotificationListActionUseCase @Inject internal constructor(
    private val collectNotifications: CollectNotificationsUseCase,
) {
    suspend fun execute(
        appState: AppState,
        screenLockedAt: ScreenLockedAt,
        isUserOnFinalScreen: Boolean,
        maxNumberOfItems: Int,
    ): Result =
        if (isUserOnFinalScreen) {
            when (appState) {
                is AppState.Paused -> {
                    if (
                        happenedRecently(appState.timeMs) &&
                        happenedRecently(screenLockedAt.timeMs)
                    ) {
                        val notifications = collectNotifications
                            .execute(maxNumberOfItems = maxNumberOfItems)
                            .first()
                        Result.Repost(notifications)
                    } else {
                        Result.NothingToDo
                    }
                }
                is AppState.Resumed -> {
                    Result.CancelAll
                }
            }
        } else {
            Result.CancelAll
        }

    fun happenedRecently(timeMs: Long): Boolean =
        abs(DateTime().millis - timeMs) < THRESHOLD_DURATION_MS

    sealed class AppState {
        data object Resumed : AppState()
        data class Paused(val timeMs: Long) : AppState()
    }

    data class ScreenLockedAt(val timeMs: Long)

    sealed class Result {
        data class Repost(val notifications: List<NotificationContent>) : Result()
        data object CancelAll : Result()
        data object NothingToDo : Result()
    }

    companion object {
        private const val THRESHOLD_DURATION_MS = 850
    }
}
