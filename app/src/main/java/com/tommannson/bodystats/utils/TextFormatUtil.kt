package com.tommannson.bodystats.utils

infix fun Double.fmt(fmt: String) = "%$fmt".format(this)
infix fun Float.fmt(fmt: String) = "%$fmt".format(this)
infix fun Float?.fmt(fmt: String) = if(this == null ) "" else "%$fmt".format(this)