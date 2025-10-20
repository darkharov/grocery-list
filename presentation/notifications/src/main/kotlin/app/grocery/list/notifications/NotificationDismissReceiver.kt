package app.grocery.list.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class NotificationDismissReceiver : BroadcastReceiver() {

    @Inject lateinit var repository: AppRepository

    override fun onReceive(context: Context, intent: Intent) {
        ProcessLifecycleOwner
            .get()
            .lifecycleScope
            .launch(Dispatchers.IO) {
                val productIds = intent.getIntArrayExtra(EXTRA_PRODUCT_IDS)!!
                repository.setProductsEnabled(productIds.toList(), false)
            }
    }

    companion object {
        const val EXTRA_PRODUCT_IDS = "app.grocery.list.notifications.EXTRA_PRODUCT_IDS"
    }
}
