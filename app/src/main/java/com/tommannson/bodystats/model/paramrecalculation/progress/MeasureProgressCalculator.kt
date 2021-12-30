package com.tommannson.bodystats.model.paramrecalculation.progress

import com.tommannson.bodystats.feature.home.MeasurementsProgress
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.model.statistics.Statistic
import org.threeten.bp.LocalDate

class MeasureProgressCalculator {

    fun calculateDifference(groupedStats: MutableMap<String, MutableList<SavedStats>>): MeasurementsProgress {
        val statisiticsToCalculate = listOf(
            Statistic.ARM_STATISTIC,
            Statistic.BUST_STATISTIC,
            Statistic.WAIST_STATISTIC,
            Statistic.BELLY_STATISTIC,
            Statistic.HIPS_STATISTIC,
            Statistic.THIGH_STATISTIC, //udo
            Statistic.CALF_STATISTIC
        )

        val calculatedMap = mutableMapOf<String, Float>()
        var dateOfSignIn = LocalDate.now()

        for (stat in statisiticsToCalculate) {
            val loadedChartData = groupedStats[stat]

            if (loadedChartData == null) {
                return MeasurementsProgress(
                    partialProgress = mapOf(),
                    submitDate = dateOfSignIn
                )
            }

            val temp =
                calculateProgressStartDate(groupedStats.getOrDefault(stat, mutableListOf()))
            if (dateOfSignIn > temp) {
                dateOfSignIn = temp
            }
            calculatedMap[stat] =
                calculateProgressForStat(loadedChartData)
        }

        return MeasurementsProgress(
            partialProgress = calculatedMap,
            submitDate = dateOfSignIn
        )
    }


    private fun calculateProgressForStat(statList: MutableList<SavedStats>): Float {
        if (statList.isEmpty()) {
            return 0f
        }

        return Math.abs(statList.last().value) - Math.abs(statList.first().value)
    }

    private fun calculateProgressStartDate(statList: MutableList<SavedStats>): LocalDate {
        if (statList.isEmpty()) {
            return LocalDate.now()
        }

        return statList.first().submitedAt
    }
}