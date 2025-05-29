package clienttools.bindings

import clienttools.ClientTools
import clienttools.ClientToolsClient
import clienttools.configs.world.WorldConfig
import clienttools.gui.handlers.BoundInventoriesScreenHandler
import clienttools.gui.screens.BoundInventoriesScreen
import clienttools.utils.storage.Constants
import clienttools.utils.storage.GlobalStorage
import clienttools.utils.text.DynamicText
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil

object KeyManager {
    private val keys = arrayListOf<Key>()

    val OPEN_BINDING_INVENTORY_KEY =
        Key(ClientTools.MOD_ID, "open_bind_inv", "inv", InputUtil.GLFW_KEY_I) {
            val client = MinecraftClient.getInstance()
            client.setScreen(BoundInventoriesScreen(
                BoundInventoriesScreenHandler(client.player!!.inventory),
                client.player!!.inventory,
                DynamicText()
            ))
        }

    val BIND_OPENING_INVENTORY_KEY =
        Key(ClientTools.MOD_ID, "bind_open_inv", "inv", InputUtil.GLFW_KEY_B) {
            if (GlobalStorage[Constants.STORAGE_INGAME_SCREEN]!!) {
                GlobalStorage
                    .get<WorldConfig>(Constants.STORAGE_WORLD_CONFIG_KEY)!!
                    .bindBlock(GlobalStorage[Constants.STORAGE_INVENTORY_POS]!!)
            }
        }

    fun registerKey(key: Key) {
        if (keys.find { it.isSame(key) } != null) {
            ClientToolsClient.logger.error("register a repeat keyBinding: $key")
            return
        }

        keys.add(key)
    }

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register {
            keys.forEach(Key::tick)
        }
    }
}