package clienttools.utils.gson

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.function.Supplier

class DynamicTypeAdapter<T> private constructor(field: String, getter: Supplier<Gson>) : TypeAdapter<T>() {
    private val checkField: String = field
    private val gsonSupplier: Supplier<Gson> = getter
    private var gson: Gson? = null

    private val subTypes = hashMapOf<String, Class<out T>>()

    fun registerSubType(subClass: Class<out T>, fieldValue: String): DynamicTypeAdapter<T> {
        subTypes[fieldValue] = subClass
        return this
    }

    override fun write(writer: JsonWriter, value: T) {
        if (gson == null)
            gson = gsonSupplier.get()

        val obj = gson!!.toJsonTree(value).asJsonObject
        val type = subTypes.entries.find { it.value == value!!::class.java } ?: return
        obj.addProperty(checkField, type.key)
        gson!!.toJson(obj, writer)
    }

    override fun read(reader: JsonReader): T? {
        if (gson == null)
            gson = gsonSupplier.get()

        val ele = JsonParser.parseReader(reader)
        if (!ele.isJsonObject)
            return null

        val obj = ele.asJsonObject
        val type = obj.get(checkField).asString
        val typeToken = subTypes[type] ?: return null
        val t = gson!!.fromJson(ele, typeToken)
        return t
    }

    companion object {
        fun <T> of(field: String, gson: Supplier<Gson>): DynamicTypeAdapter<T> {
            return DynamicTypeAdapter(field, gson)
        }
    }
}