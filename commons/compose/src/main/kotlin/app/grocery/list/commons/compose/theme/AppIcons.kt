package app.grocery.list.commons.compose.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.ToggleOff
import androidx.compose.runtime.Immutable

@Immutable
object AppIcons {
    val delete = Icons.Outlined.DeleteOutline
    val cart = Icons.Outlined.ShoppingCart
    val share = Icons.Outlined.Share
    val paste = Icons.Outlined.ContentPaste
    val exit = Icons.AutoMirrored.Outlined.Logout
    val close = Icons.Outlined.Close
    val toggleOff = Icons.Outlined.ToggleOff
    val done = Icons.Outlined.Done
    val fingerprint = Icons.Outlined.Fingerprint
}
