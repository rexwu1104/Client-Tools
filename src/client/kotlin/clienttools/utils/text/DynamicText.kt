package clienttools.utils.text

import net.minecraft.text.OrderedText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TextContent

class DynamicText : Text {
    private var text: Text? = null

    fun setText(text: Text) {
        this.text = text
    }

    override fun getStyle(): Style {
        return text?.style ?: Style.EMPTY
    }

    override fun getContent(): TextContent {
        return text?.content ?: TextContent.EMPTY
    }

    override fun getSiblings(): MutableList<Text> {
        return text?.siblings ?: mutableListOf()
    }

    override fun asOrderedText(): OrderedText {
        return text?.asOrderedText() ?: OrderedText.EMPTY
    }
}