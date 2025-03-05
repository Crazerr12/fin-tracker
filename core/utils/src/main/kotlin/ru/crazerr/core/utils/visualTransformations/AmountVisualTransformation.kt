package ru.crazerr.core.utils.visualTransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class AmountVisualTransformation(private val sign: Char) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val (wholePart, decimalPart) = splitParts(text.text)

        val formattedWhole = formatWholePart(part = wholePart)

        val result = buildString {
            append(formattedWhole)
            if (decimalPart.isNotEmpty()) {
                append(".${decimalPart.take(2)}")
            }
            append(" $sign")
        }

        return TransformedText(
            text = AnnotatedString(result),
            offsetMapping = AmountOffsetMapping(
                wholeLength = formattedWhole.length,
                decimalLength = decimalPart.length
            )
        )
    }

    private fun splitParts(input: String): Pair<String, String> {
        val parts = input.split('.')

        return Pair(
            parts.getOrElse(0) { "" },
            parts.getOrElse(1) { "" }
        )
    }

    private fun formatWholePart(part: String): String {
        if (part.isEmpty()) return ""
        return part.reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()
    }

    private class AmountOffsetMapping(
        private val wholeLength: Int,
        private val decimalLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return if (decimalLength == 0) {
                offset - 2
            } else {
                offset - 2
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset + 2
        }
    }
}