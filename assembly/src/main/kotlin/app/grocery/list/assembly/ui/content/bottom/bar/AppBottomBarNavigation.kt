package app.grocery.list.assembly.ui.content.bottom.bar

internal interface AppBottomBarNavigation {
    fun goToActions()
    fun goToProductInputForm()
}

internal object AppBottomBarNavigationMock : AppBottomBarNavigation {
    override fun goToActions() {}
    override fun goToProductInputForm() {}
}
