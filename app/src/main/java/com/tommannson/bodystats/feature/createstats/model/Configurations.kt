package com.tommannson.bodystats.feature.createstats.model

import com.tommannson.bodystats.R
import com.tommannson.bodystats.infrastructure.configuration.Statistic

data class Configuration(val name: String, val image: Int, val shortName: String = "")

object Configurations {

    val PARAMS_UI_MAP = mapOf<String, Configuration>(
        Statistic.BUST_STATISTIC to Configuration("Biust", R.drawable.biust_300),
        Statistic.ARM_STATISTIC to Configuration("Ramie", R.drawable.ramie_300),
        Statistic.WAIST_STATISTIC to Configuration("Talia", R.drawable.talia_300),
        Statistic.BELLY_STATISTIC to Configuration("Brzuch", R.drawable.brzuch_300),
        Statistic.HIPS_STATISTIC to Configuration("Biodra", R.drawable.biodra_300),
        Statistic.THIGH_STATISTIC to Configuration("Łudo", R.drawable.udo_300),
        Statistic.CALF_STATISTIC to Configuration("Łydka", R.drawable.lydka_300),

        Statistic.WEIGHT to Configuration("Waga", R.drawable.body_composition_analyzer, ),
        Statistic.WEIGHT_COMPOSITION to Configuration("Waga składu ciała", R.drawable.body_composition_analyzer, "Waga kg"),
        Statistic.BMI to Configuration("BMI", R.drawable.body_composition_analyzer, "BMI"),
        Statistic.BMR to Configuration("BMR", R.drawable.body_composition_analyzer, "BMR kcal"),
        Statistic.BONE_MASS to Configuration("Waga Kości", R.drawable.body_composition_analyzer, "Kości kg"),
        Statistic.FAT_MASS to Configuration("Waga Tłuszczy", R.drawable.body_composition_analyzer, "Tłuszcz kg"),
        Statistic.FAT_PERCENT to Configuration("% tłuszczu", R.drawable.body_composition_analyzer, "Tłuszcz %"),
        Statistic.FFM to Configuration("FFM", R.drawable.body_composition_analyzer, "FTM kg"),
        Statistic.IDEAL_BODY_WEIGHT to Configuration("Idealna Waga", R.drawable.body_composition_analyzer, ),
        Statistic.METABOLIC_AGE to Configuration("Wiek metaboliczny", R.drawable.body_composition_analyzer, "Wiek meteb."),
        Statistic.MUSCLE_MASS to Configuration("Waga mięśni", R.drawable.body_composition_analyzer, "Mięśnie kg"),
        Statistic.TBW to Configuration("TBW", R.drawable.body_composition_analyzer, "TBW kg"),
        Statistic.TBW_PERCENT to Configuration("% TBW", R.drawable.body_composition_analyzer, "TBW %"),
        Statistic.VISCELAR_FAT_RATING to Configuration("Tłuszcz Viscelarny", R.drawable.body_composition_analyzer, "Tł wisceral."),
    )

}