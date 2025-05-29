package clienttools.gui

import clienttools.gui.handlers.BoundInventoriesScreenHandler
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.screen.ScreenHandlerType

object Types {
    val BOUND_INVENTORY_HANDLER_TYPE = ScreenHandlerType(
        { _, playerInventory -> BoundInventoriesScreenHandler(playerInventory) },
        FeatureSet.empty()
    )
}