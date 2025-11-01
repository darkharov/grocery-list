package app.grocery.list.assembly

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.grocery.list.data.InitializeData
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltAndroidApp
class ApplicationImpl : Application() {

    @Inject
    fun initializeData(initializeData: InitializeData) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            initializeData.execute()
        }
    }
}
