package clienttools.utils

object GlobalStorage {
    private val data = hashMapOf<String, Wrapper<*>>()
    private val callbacks = hashMapOf<String, ArrayList<ValueUpdateCallback<*>>>()

    inline operator fun <reified T> get(key: String): T? {
        return get(key, T::class.java)
    }

    fun <T> get(key: String, clazz: Class<T>): T? {
        if (key !in data)
            return null

        val wrapper = data[key]!!
        if (!wrapper.isSameType(clazz))
            return null

        @Suppress("UNCHECKED_CAST")
        return wrapper.get() as T
    }

    operator fun <T> set(key: String, value: T) {
        val wrapper = data[key]
        if (wrapper == null || !wrapper.isSameType(value!!::class.java)) {
            data[key] = Wrapper(value)
        }

        @Suppress("UNCHECKED_CAST")
        (data[key]!! as Wrapper<T>).onUpdate(value, callbacks[key] ?: listOf())
    }

    fun remove(key: String): Boolean {
        return data.remove(key) != null
    }

    inline fun <reified T> observe(key: String, crossinline block: (T) -> Unit) {
        addListener(key, object : ValueUpdateCallback<T> {
            override fun onValueChange(value: T) = block(value)
        })
    }

    fun <T> addListener(key: String, callback: ValueUpdateCallback<T>) {
        callbacks[key]?.add(callback) ?: callbacks.set(key, arrayListOf(callback))
    }

    class Wrapper<T>(private var value: T) {
        private val type = value!!::class.java

        fun get(): T {
            return value
        }

        fun onUpdate(newValue: T, callbacks: List<ValueUpdateCallback<*>>) {
            value = newValue
            callbacks
                .filterIsInstance<ValueUpdateCallback<T>>()
                .forEach { it.onValueChange(value) }
        }

        fun <T> isSameType(clazz: Class<T>): Boolean {
            return type == clazz
        }
    }

    @FunctionalInterface
    interface ValueUpdateCallback<T> {
        fun onValueChange(value: T)
    }
}