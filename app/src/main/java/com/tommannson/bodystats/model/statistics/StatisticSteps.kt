package com.tommannson.bodystats.model.statistics

val BASIC_PARAMS = listOf(
    Statistic.WEIGHT,
    Statistic.ARM_STATISTIC,
    Statistic.BUST_STATISTIC,
    Statistic.WAIST_STATISTIC,
    Statistic.BELLY_STATISTIC,
    Statistic.HIPS_STATISTIC,
    Statistic.THIGH_STATISTIC, //udo
    Statistic.CALF_STATISTIC
)
val BASIC_PARAMS_FOR_CREATE = listOf(
    Statistic.ARM_STATISTIC,
    Statistic.BUST_STATISTIC,
    Statistic.WAIST_STATISTIC,
    Statistic.BELLY_STATISTIC,
    Statistic.HIPS_STATISTIC,
    Statistic.THIGH_STATISTIC, //udo
    Statistic.CALF_STATISTIC
)
val BODY_COMPOSITION_PARAMS = listOf(
    Statistic.WEIGHT_COMPOSITION,
    Statistic.FAT_MASS,
    Statistic.FAT_PERCENT,
    Statistic.FFM,
    Statistic.MUSCLE_MASS,
    Statistic.TBW,
    Statistic.TBW_PERCENT,
    Statistic.BONE_MASS, //udo
    Statistic.BMR,
    Statistic.METABOLIC_AGE,
    Statistic.VISCELAR_FAT_RATING,
    Statistic.BMI,
)
val FULL_LIST_OF_STATS = mutableListOf<String>()
    .also {
        it.addAll(BASIC_PARAMS)
        it.addAll(BODY_COMPOSITION_PARAMS)
    }