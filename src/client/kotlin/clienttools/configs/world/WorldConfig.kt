package clienttools.configs.world

import clienttools.configs.Config
import clienttools.configs.ConfigManager
import clienttools.utils.Constants
import clienttools.utils.GlobalStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.util.math.BlockPos

abstract class WorldConfig : Config() {
    protected abstract val hash: String
    protected val boundInventories: ArrayList<BlockPos> = arrayListOf()

    abstract fun getType(): Type

    override fun getIdentity(): String = hash

    enum class Type {
        Client,
        Server;
    }

    companion object {
        fun getOrCreateConfig(handler: ClientPlayNetworkHandler): WorldConfig {
            val client =  MinecraftClient.getInstance()
            val type = if (client.server == null) Type.Server else Type.Client
            val sha256seed: Long = GlobalStorage[Constants.STORAGE_SEED_KEY]!!

            val config = when (type) {
                Type.Client -> ClientWorldConfig(sha256seed, client.world!!.dimension, client.server!!.saveProperties.levelName)
                Type.Server -> ServerWorldConfig(sha256seed, client.world!!.dimension, handler.serverInfo!!.address)
            }

            return ConfigManager.checkExist(ConfigManager.Type.World, config) as WorldConfig
        }
    }
}