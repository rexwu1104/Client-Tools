package clienttools.tools

import clienttools.configs.world.WorldConfig
import clienttools.utils.storage.Constants
import clienttools.utils.storage.GlobalStorage
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Blocks
import net.minecraft.inventory.Inventory
import net.minecraft.util.ActionResult

object InventoryTool : BaseTool() {
    override val name: String
        get() = "InventoryTool"

    override fun init() {
        ClientPlayConnectionEvents.JOIN.register { handler, _, _ ->
            GlobalStorage[Constants.STORAGE_WORLD_CONFIG_KEY] = WorldConfig.getOrCreateConfig(handler)
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            GlobalStorage.remove(Constants.STORAGE_WORLD_CONFIG_KEY)
        }

        UseBlockCallback.EVENT.register { _, world, _, hitResult ->
            val block = world.getBlockState(hitResult.blockPos)
            val blockEntity = world.getBlockEntity(hitResult.blockPos)
            if (block.isOf(Blocks.ENDER_CHEST) || blockEntity is Inventory)
                GlobalStorage[Constants.STORAGE_INVENTORY_POS] = hitResult.blockPos

            ActionResult.PASS
        }
    }
}