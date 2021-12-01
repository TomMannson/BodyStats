package com.tommannson.bodystats.model.statistics

fun getStatUnit(stat: String) = when (stat) {
    Statistic.WEIGHT -> "kg"
    Statistic.WEIGHT_COMPOSITION -> "kg"
    in BASIC_PARAMS -> "cm"
    in listOf(Statistic.FAT_PERCENT, Statistic.TBW_PERCENT) -> "%"
    Statistic.BMR -> "kcal"
    Statistic.METABOLIC_AGE -> "lat"
    in listOf(
        Statistic.BONE_MASS,
        Statistic.FAT_MASS,
        Statistic.MUSCLE_MASS,
        Statistic.IDEAL_BODY_WEIGHT,
        Statistic.FFM,
        Statistic.TBW
    ) -> "kg"

    else -> ""
}

fun getStatShift(stat: String) = when (stat) {
    in BASIC_PARAMS -> .5
    in listOf(Statistic.FAT_PERCENT, Statistic.TBW_PERCENT) -> .1
    in listOf(Statistic.BMR, Statistic.VISCELAR_FAT_RATING, Statistic.METABOLIC_AGE) -> 1.0
    Statistic.BMI -> .1
    in listOf(
        Statistic.WEIGHT_COMPOSITION,
        Statistic.BONE_MASS,
        Statistic.FAT_MASS,
        Statistic.MUSCLE_MASS,
        Statistic.IDEAL_BODY_WEIGHT,
        Statistic.FFM,
        Statistic.TBW
    ) -> .1
    else -> .1
}

fun getStatFormatter(stat: String) = when (stat) {
    in listOf(Statistic.BMR, Statistic.METABOLIC_AGE, Statistic.VISCELAR_FAT_RATING) -> "#"
    else -> "#.#"
}

fun getStatFormatterModification(stat: String) = when (stat) {
    in listOf(Statistic.BMR, Statistic.METABOLIC_AGE, Statistic.VISCELAR_FAT_RATING) -> "#"
    else -> "#.#"
}