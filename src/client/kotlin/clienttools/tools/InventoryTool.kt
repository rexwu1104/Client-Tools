package clienttools.tools

import clienttools.configs.world.WorldConfig
import clienttools.utils.Constants
import clienttools.utils.GlobalStorage
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

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
    }
}