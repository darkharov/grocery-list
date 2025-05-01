package app.grocery.list.assembly

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationImpl : Application() {

    @Inject lateinit var activityDecorator: ActivityDecorator

    override fun onCreate() {
        super.onCreate()
        activityDecorator.onApplicationCreate()
    }
}
