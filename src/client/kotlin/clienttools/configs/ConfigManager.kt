package clienttools.configs

import clienttools.ClientToolsClient
import clienttools.configs.world.WorldConfig
import clienttools.utils.Constants
import clienttools.utils.GlobalStorage
import clienttools.utils.gson.GlobalGson
import clienttools.utils.gson.GlobalGson.toJson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import java.io.FileReader
import java.io.FileWriter

object ConfigManager {
    private val configPath = FabricLoader.getInstance()
        .configDir
        .resolve("client-tools")
        .resolve("config.json")

    private val worlds: ArrayList<WorldConfig> = arrayListOf()

    fun init() {
        ClientLifecycleEvents.CLIENT_STARTED.register { load() }
        ClientLifecycleEvents.CLIENT_STOPPING.register { save() }
        ClientPlayConnectionEvents.DISCONNECT.register { _, _ -> save() }
        GlobalStorage.observe<WorldConfig>(Constants.STORAGE_WORLD_CONFIG_KEY) { config ->
            if (!worlds.any { it.getIdentity() == config.getIdentity() })
                worlds.add(config)

            ClientToolsClient.logger.info("Current worlds length: ${worlds.size}")
        }
    }

    private fun load() {
        val file = configPath.toFile()
        if (!file.exists())
            return

        FileReader(file).use { reader ->
            JsonReader(reader).use { jsonReader ->
                val fullElement = JsonParser.parseReader(jsonReader)
                if (!fullElement.isJsonObject)
                    return
                val obj = fullElement.asJsonObject

                // read config contents
                val worlds = obj.getAsJsonArray("worlds")?.mapNotNull {
                    GlobalGson.worldGson.fromJson(it, WorldConfig::class.java)
                } ?: return

                // read complete
                this.worlds.clear()
                this.worlds.addAll(worlds)
            }
        }
    }

    private fun save() {
        val file = configPath.toFile()
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        FileWriter(file).use { writer ->
            JsonWriter(writer).use { jsonWriter ->
                val obj = JsonObject()

                // write config contents
                obj.add("worlds",
                    worlds.toJson(GlobalGson.worldGson))

                // write complete
                GlobalGson.worldGson.toJson(obj, jsonWriter)
            }
        }
    }

    fun <T : Config> checkExist(configType: Type, config: T) : Config {
        return when (configType) {
            Type.World -> worlds.find { it.getIdentity() == config.getIdentity() } ?: config
        }
    }

    enum class Type {
        World
    }
}