package app.grocery.list.commons.compose

interface OnDialogDismiss {
    fun onDialogDismiss()
}

object OnDialogDismissMock : OnDialogDismiss {
    override fun onDialogDismiss() {}
}
