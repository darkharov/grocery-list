package app.grocery.list.assembly.impls

import app.grocery.list.domain.AppErrorLogger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppErrorLoggerImpl @Inject constructor(): AppErrorLogger {

    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }
}
