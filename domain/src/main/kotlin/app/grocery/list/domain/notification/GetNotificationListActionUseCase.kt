package app.grocery.list.domain.notification

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import org.joda.time.DateTime
import org.joda.time.Seconds

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
        Seconds.secondsBetween(
            DateTime(timeMs),
            DateTime.now(),
        ).seconds <= THRESHOLD_DURATION_SECONDS

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
        private const val THRESHOLD_DURATION_SECONDS = 3
    }
}
