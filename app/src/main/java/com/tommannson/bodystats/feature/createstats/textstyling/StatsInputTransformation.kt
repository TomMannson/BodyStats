package com.tommannson.bodystats.feature.createstats.textstyling

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class UnitVisualTransformation(val unit: String = "") : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            AnnotatedString(text.toString() + " " + unit),
            OffsetMapping.Identity
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnitVisualTransformation) return false
        if (unit != other.unit) return false
        return true
    }

    override fun hashCode(): Int {
        return unit.hashCode()
    }
}