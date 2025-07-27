package app.grocery.list.notifications.internal.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationsModule {

    @Provides
    @Singleton
    fun notificationManager(
        @ApplicationContext
        context: Context,
    ): NotificationManagerCompat =
        NotificationManagerCompat.from(context)
}
