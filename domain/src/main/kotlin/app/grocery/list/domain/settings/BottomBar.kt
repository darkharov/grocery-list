package app.grocery.list.domain.settings

enum class BottomBarMode {
    Buttons,
    Icons,
}

// Do not reorder. The 'ordinal' field is used as a key for read/write operations
enum class BottomBarRoadmapStep(
    val state: BottomBarMode = BottomBarMode.Buttons,
    val shouldOfferToSwitchToIcons: Boolean = false,
) {
    Initial,
    ProductListPostedAtLeastOnce(
        shouldOfferToSwitchToIcons = true,
    ),
    IconsModeIsExplicitlySelected(
        state = BottomBarMode.Icons,
    ),
    ButtonsIsExplicitlySelected,
    ;
    val useIcons get() = (state == BottomBarMode.Icons)
}
