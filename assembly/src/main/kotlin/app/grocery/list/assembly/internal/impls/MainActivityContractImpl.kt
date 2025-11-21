package app.grocery.list.assembly.internal.impls

import app.grocery.list.assembly.BuildConfig
import app.grocery.list.main.activity.ui.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityContractImpl @Inject constructor() : MainActivity.Contract {
    override val versionName = BuildConfig.VERSION_NAME
    override val versionCode = BuildConfig.VERSION_CODE
}
