package com.tommannson.bodystats.model.paramrecalculation

import com.tommannson.bodystats.infrastructure.configuration.ApplicationUser
import com.tommannson.bodystats.infrastructure.configuration.Statistic

class ProcessRecalculator {

    val factory = RecalculatorFactory()

    fun performCalculations(
        paramName: String,
        map: MutableMap<String, Float>,
        user: ApplicationUser,
    ) {
        val recalculators = factory.createRecalculator(paramName)
        recalculators.forEach {
            it.recalculate(map, user = user)
        }
    }

}


class RecalculatorFactory {

    fun createRecalculator(paramChanged: String): List<Recalculator> {
        return when (paramChanged) {
            Statistic.WEIGHT_COMPOSITION -> listOf(
                BmiRecalculator(),
                FatPercentRecalculator(),
                TbwPercentRecalculator()
            )
            Statistic.FAT_MASS -> listOf(FatPercentRecalculator())
            Statistic.TBW -> listOf(TbwPercentRecalculator())
            else -> listOf(NoRecalculator)
        }
    }
}


object NoRecalculator : Recalculator {
    override fun recalculate(values: MutableMap<String, Float>, user: ApplicationUser) {

    }

}

class BmiRecalculator : Recalculator {
    override fun recalculate(values: MutableMap<String, Float>, user: ApplicationUser) {
        val userHeightInMeters = user.height / 100;

        val result =
            (values[Statistic.WEIGHT_COMPOSITION] ?: 0f) / (userHeightInMeters * userHeightInMeters)
        values[Statistic.BMI] = result
    }
}

class FatPercentRecalculator : Recalculator {
    override fun recalculate(values: MutableMap<String, Float>, user: ApplicationUser) {
        val result =
            (values[Statistic.FAT_MASS] ?: 0f) / values[Statistic.WEIGHT_COMPOSITION]!!
        values[Statistic.FAT_PERCENT] = result * 100
    }
}

class TbwPercentRecalculator : Recalculator {
    override fun recalculate(values: MutableMap<String, Float>, user: ApplicationUser) {
        val result =
            (values[Statistic.TBW] ?: 0f) / values[Statistic.WEIGHT_COMPOSITION]!!
        values[Statistic.TBW_PERCENT] = result * 100
    }
}