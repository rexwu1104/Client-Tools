package clienttools.tools

import clienttools.ClientToolsClient

object ToolManager {
    private val tools = arrayListOf<BaseTool>()

    private fun preInit() {
        tools.add(InventoryTool)
    }

    fun init() {
        preInit()

        tools.forEach { tool ->
            ClientToolsClient.logger.info("Initializing ${tool.name}...")
            tool.init()
        }
    }
}