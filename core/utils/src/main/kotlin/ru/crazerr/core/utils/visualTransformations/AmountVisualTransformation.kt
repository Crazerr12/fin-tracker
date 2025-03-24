package ru.crazerr.core.utils.visualTransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class AmountVisualTransformation(private val sign: Char) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        if (text.isEmpty()) {
            return TransformedText(AnnotatedString(""), object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = offset
                override fun transformedToOriginal(offset: Int): Int = offset
            })
        }

        val (wholePart, decimalPart) = splitParts(text.text)

        val formattedWhole = formatWholePart(part = wholePart)

        val result = buildString {
            append(formattedWhole)
            if (text.text.contains('.')) {
                append('.')
            }
            if (decimalPart.isNotEmpty()) {
                append(decimalPart.take(2))
            }
            append(" $sign")
        }

        return TransformedText(
            text = AnnotatedString(result),
            offsetMapping = AmountOffsetMapping(
                formattedWholeLength = formattedWhole.length,
                decimalLength = (if (text.text.contains('.')) 1 else 0) + decimalPart.length,
                wholeLength = wholePart.length,
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
        return part.reversed().chunked(3)
            .joinToString(" ").reversed()
    }

    private class AmountOffsetMapping(
        private val formattedWholeLength: Int,
        private val wholeLength: Int,
        private val decimalLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset >= wholeLength && decimalLength != 0) {
                return offset + (wholeLength - 1) / 3
            }

            val k = wholeLength - 1 - offset
            val spaces = formattedWholeLength - wholeLength

            return offset + (spaces - k / 3)
        }

        override fun transformedToOriginal(offset: Int): Int {
            val spaceCount = formattedWholeLength - wholeLength

            return if (offset > formattedWholeLength) {
                (offset - spaceCount).coerceAtMost(wholeLength + decimalLength)
            } else {
                (offset - (offset / 4)).coerceAtLeast(0)
            }
        }

    }
}