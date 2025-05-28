package clienttools.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.minecraft.util.math.BlockPos

object BlockPosAdapter : TypeAdapter<BlockPos>() {
    override fun write(writer: JsonWriter, pos: BlockPos) {
        writer.beginArray()
        writer.value(pos.x)
        writer.value(pos.y)
        writer.value(pos.z)
        writer.endArray()
    }

    override fun read(reader: JsonReader): BlockPos? {
        if (reader.peek() != JsonToken.BEGIN_ARRAY)
            return null

        reader.beginArray()
        return BlockPos(reader.nextInt(), reader.nextInt(), reader.nextInt()).apply {
            reader.endArray()
        }
    }
}