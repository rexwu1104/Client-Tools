package clienttools.gui.handlers

import clienttools.ClientToolsClient
import clienttools.configs.world.WorldConfig
import clienttools.gui.Types
import clienttools.tools.inventory.Inventories
import clienttools.utils.storage.Constants
import clienttools.utils.storage.GlobalStorage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler

class BoundInventoriesScreenHandler(playerInventory: PlayerInventory) : ScreenHandler(Types.BOUND_INVENTORY_HANDLER_TYPE, -1) {
    private val inventories: Inventories = GlobalStorage
            .get<WorldConfig>(Constants.STORAGE_WORLD_CONFIG_KEY)!!
            .getInventories()

    init {
        ClientToolsClient.logger.info(inventories.toString())
    }

    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return true
    }
}