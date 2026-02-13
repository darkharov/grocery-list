package app.grocery.list.assembly.internal.di

import android.content.Context
import app.grocery.list.assembly.BuildConfig
import app.grocery.list.assembly.R
import app.grocery.list.domain.AppInfo
import app.grocery.list.domain.sharing.SharingStringFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Named(SharingStringFormatter.RECOMMENDATION_TO_USE_APP)
    @Singleton
    fun providesRecommendationToUseApp(
        @ApplicationContext
        context: Context,
    ): String =
        with(context) {
            getString(
                R.string.recommendation_to_use_this_app,
                getString(R.string.paste_copied_list),
                getString(R.string.actions),
                "https://play.google.com/store/apps/details?id=app.grocery.list",
            )
        }

    @Provides
    @Singleton
    fun providesAppInfo(): AppInfo =
        AppInfo(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
        )
}
