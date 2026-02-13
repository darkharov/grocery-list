package app.grocery.list.domain

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

@Singleton
class ScreenLockEventBuffer @Inject constructor() {

    private val channel = Channel<ScreenLockEvent>()

    fun events(): ReceiveChannel<ScreenLockEvent> =
        channel

    fun notifyScreenLocked() {
        channel.trySend(ScreenLockEvent)
    }

    data object ScreenLockEvent
}
