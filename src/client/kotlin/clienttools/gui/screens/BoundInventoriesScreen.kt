package clienttools.gui.screens

import clienttools.gui.handlers.BoundInventoriesScreenHandler
import clienttools.utils.text.DynamicText
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory

class BoundInventoriesScreen(handler: BoundInventoriesScreenHandler, inventory: PlayerInventory, title: DynamicText) : HandledScreen<BoundInventoriesScreenHandler>(
    handler, inventory, title
) {
    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {

    }
}