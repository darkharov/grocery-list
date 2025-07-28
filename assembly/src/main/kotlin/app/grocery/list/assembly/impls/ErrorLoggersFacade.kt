package app.grocery.list.assembly.impls

import app.grocery.list.commons.format.ProductTitleFormatter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorLoggersFacade @Inject constructor(): ProductTitleFormatter.ErrorLogger {

    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }
}
