package clienttools.bindings

import clienttools.utils.StringUtils
import net.minecraft.client.option.KeyBinding

class Key(
    modId: String,
    key: String,
    category: String,
    rawKey: Int,
    private val callback: () -> Unit
) {
    private val keyBinding = KeyBinding(
        StringUtils.keyBinding(modId, key),
        rawKey,
        StringUtils.keyCategory(modId, category)
    )

    fun press() {
        KeyBinding.onKeyPressed(keyBinding.defaultKey)
    }

    fun tick() {
        if (keyBinding.wasPressed())
            onPressed()
    }

    fun isSame(other: Key): Boolean {
        return this.keyBinding.translationKey == other.keyBinding.translationKey &&
                this.keyBinding.category == other.keyBinding.category
    }

    fun matches(keyCode: Int, scanCode: Int): Boolean {
        return this.keyBinding.matchesKey(keyCode, scanCode)
    }

    private fun onPressed() = callback()

    init {
        KeyManager.registerKey(this)
    }
}