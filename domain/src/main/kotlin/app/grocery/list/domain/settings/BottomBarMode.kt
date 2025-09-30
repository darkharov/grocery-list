package app.grocery.list.domain.settings

enum class BottomBarMode {
    Initial,
    ShouldOfferToSwitchToIcons,
    Icons,
    Buttons,
    ;
    val isInitial get() = (this == Initial)
    val useIcons get() = (this == Icons)
    val shouldOfferToSwitchToIcons get() = (this == ShouldOfferToSwitchToIcons)
}
