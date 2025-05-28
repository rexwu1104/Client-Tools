package clienttools.utils.gson

import clienttools.configs.world.ClientWorldConfig
import clienttools.configs.world.ServerWorldConfig
import clienttools.configs.world.WorldConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import net.minecraft.util.math.BlockPos
import java.lang.reflect.Modifier

object GlobalGson {
    val baseGson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val worldGson: Gson by lazy {
        GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .registerTypeAdapter(WorldConfig::class.java,
                DynamicTypeAdapter.of<WorldConfig>("type") { GlobalGson.worldGson }
                    .registerSubType(ClientWorldConfig::class.java, WorldConfig.Type.Client.name)
                    .registerSubType(ServerWorldConfig::class.java, WorldConfig.Type.Server.name))
            .registerTypeAdapter(BlockPos::class.java, BlockPosAdapter)
            .setPrettyPrinting()
            .setLenient()
            .create()
    }

    inline fun <reified T> List<T>.toJson(gson: Gson): JsonElement {
        val array = JsonArray(this.size)
        this.forEach { t ->
            array.add(gson.toJsonTree(t, T::class.java))
        }

        return array
    }
}