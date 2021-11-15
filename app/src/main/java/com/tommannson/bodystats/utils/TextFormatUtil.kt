package com.tommannson.bodystats.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

val formatSymbols = DecimalFormatSymbols.getInstance().apply {
    decimalSeparator = ','
}




infix fun Double.fmt(fmt: String) = "%$fmt".format(this)
infix fun Float.fmt(fmt: String): String {

    val twoDecimalDigitsFormat = DecimalFormat(fmt).apply {
        decimalFormatSymbols = formatSymbols
    }

    return twoDecimalDigitsFormat.format(this.toDouble())
}
infix fun Float?.fmt(format: String): String {

    return if(this == null ) "" else this fmt format
}