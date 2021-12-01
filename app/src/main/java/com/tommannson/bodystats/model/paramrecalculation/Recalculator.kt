package com.tommannson.bodystats.model.paramrecalculation

import com.tommannson.bodystats.infrastructure.ApplicationUser


interface Recalculator {

    fun recalculate(values: MutableMap<String, Float>, user: ApplicationUser)
}

