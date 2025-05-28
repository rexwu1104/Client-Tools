package clienttools

import clienttools.configs.ConfigManager
import clienttools.tools.ToolManager
import com.mojang.logging.LogUtils
import net.fabricmc.api.ClientModInitializer
import org.slf4j.Logger

object ClientToolsClient : ClientModInitializer {
    val logger: Logger = LogUtils.getLogger()

    override fun onInitializeClient() {
        ConfigManager.init()
        ToolManager.init()
    }
}