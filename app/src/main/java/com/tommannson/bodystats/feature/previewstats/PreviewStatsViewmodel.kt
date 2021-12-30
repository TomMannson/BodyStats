package com.tommannson.bodystats.feature.previewstats

import androidx.lifecycle.viewModelScope
import com.tommannson.bodystats.base.BaseViewmodel
import com.tommannson.bodystats.feature.home.MeasurementsProgress
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.infrastructure.configuration.StatsDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import com.tommannson.bodystats.model.paramrecalculation.progress.MeasureProgressCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PreviewStatsViewmodel
@Inject constructor(
    val statsDao: StatsDao,
    val userDao: UserDao,
) : BaseViewmodel() {

    private val _state = MutableStateFlow<State>(State.Init)
    val state: StateFlow<State> = _state

    val progressCalculator = MeasureProgressCalculator()

    fun initPreview(listToDisplay: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getAll().firstOrNull()

            if (user != null) {
                statsDao.getParamLive(user.id!!, listToDisplay).collect {

                    val groupedStats = mutableMapOf<String, MutableList<SavedStats>>()

                    for (statItem in it) {
                        val foundList = groupedStats[statItem.statName] ?: mutableListOf()
                        foundList.add(statItem)
                        groupedStats[statItem.statName] = foundList
                    }

                    val groupedStatsByTimeRange = mapOf(
                        TimeKind.DAYLY to calculateDailyScope(groupedStats),
                        TimeKind.WEAKLY to calculateWeaklyScope(groupedStats, listToDisplay),
                        TimeKind.MONTHLY to calculateMonthlyScope(groupedStats, listToDisplay),
                    )

                    val progress: MeasurementsProgress =
                        progressCalculator.calculateDifference(groupedStats)

                    _state.emit(
                        State.DataLoaded(
                            groupedStats,
                            allCalculatedWariants = groupedStatsByTimeRange,
                            selectedMethod = groupedStatsByTimeRange.get(TimeKind.DAYLY)!!,
                            progressInfo = progress
                        )
                    )
                }
            }
        }
    }

    fun selectChart(timeKind: TimeKind) {
        val localState = state.value as State.DataLoaded
        _state.value = localState.copy(
            selectedMethod = localState.allCalculatedWariants[timeKind]!!
        )
    }

    private fun calculateMonthlyScope(
        groupedStats: MutableMap<String, MutableList<SavedStats>>,
        listToDisplay: List<String>
    ): StatsShowInTime.MonthlyStats {
        val mapWithGrids = mutableMapOf<String, MonthGridData>()

        for (typeOfStat in listToDisplay) {
            val calculatedGrid = calculateMonthGrid(groupedStats.getOrDefault(typeOfStat, listOf()))
            mapWithGrids[typeOfStat] = calculatedGrid
        }
        return StatsShowInTime.MonthlyStats(
            gridInfo = mapWithGrids
        )
    }

    private fun calculateMonthGrid(list: List<SavedStats>): MonthGridData {

        val reversedCopyOfData = list.reversed()
        val currendDay = LocalDate.now()
        val latestMeasuringDay = currendDay.minusMonths(24)
        var endOfScope = currendDay
        var startOfScope = currendDay.minusDays(endOfScope.dayOfMonth.toLong() - 1)
        val mapWithResults = mutableMapOf<MonthOfYear, MutableList<Float>>()

        val listOfGrid = mutableListOf<Float>()
        for (indexOfGrid in 0..2) {
            listOfGrid.add(1 - (startOfScope.monthValue * (0.5f / 12)) - (indexOfGrid * 0.5f))
        }

        val listOfLabels = mutableListOf<ValueScale<String>>()
        for (indexOfGrids in 0 until listOfGrid.filter { it > 0 }.size) {
            listOfLabels.add(
                ValueScale(
                    "${startOfScope.year - indexOfGrids}",
                    listOfGrid[indexOfGrids] + 0.25f
                )
            )
            if (indexOfGrids == listOfGrid.size - 1) {
                listOfLabels.add(
                    ValueScale(
                        "${startOfScope.year - (indexOfGrids + 1)}",
                        listOfGrid[indexOfGrids] - 0.25f
                    )
                )
            }
        }


        for (month in 0 until 24) {
            val fistDayOfMonth = startOfScope.minusMonths(month.toLong())
            mapWithResults[MonthOfYear(monyh = fistDayOfMonth.month, year = fistDayOfMonth.year)] =
                mutableListOf()
        }


        stats@ for (statItem in reversedCopyOfData) {
            while (!statItem.submitedAt.isBetween(startOfScope, currendDay)) {
                endOfScope = startOfScope.minusDays(1)
                startOfScope = endOfScope.minusDays(endOfScope.dayOfMonth.toLong() - 1)

                if (startOfScope.isBefore(latestMeasuringDay)) {
                    break@stats
                }
            }

            val list =
                mapWithResults[MonthOfYear(monyh = startOfScope.month, year = startOfScope.year)]

            list?.add(statItem.value)
        }

        val result = mapWithResults.map { it.key to calculateValue(it) }
        val resultPoints = mutableListOf<ValueScale<Float>>()

        val startYear = currendDay.year
        for (resultItem in result.filter { it.second > 0 }) {
            val month = resultItem.first.monyh
            val yeardifference = startYear - resultItem.first.year
            val pointX =
                1 - ((12 - month.value) * (0.5f / 12)) - (yeardifference * 0.5f) + (.25f / 12)
            resultPoints.add(ValueScale(resultItem.second, pointX))
        }

        return MonthGridData(
            labels = listOfLabels,
            calculatedValues = resultPoints,
            gridLocation = listOfGrid,
            collectionOfMonths = result.filter { it.second > 0 }
        )
    }

    private fun calculateValue(it: Map.Entry<MonthOfYear, MutableList<Float>>): Float {
        if (it.value.size == 0) {
            return -1f;
        }
        return (it.value.sum() / it.value.size)
    }

    private fun calculateWeek(it: Map.Entry<WeekScope, MutableList<Float>>): Float {
        if (it.value.size == 0) {
            return -1f;
        }
        return (it.value.sum() / it.value.size)
    }

    private fun calculateWeaklyScope(
        groupedStats: MutableMap<String, MutableList<SavedStats>>,
        listToDisplay: List<String>
    ): StatsShowInTime.WeaklyStats {
        val mapWithGrids = mutableMapOf<String, WeekGridData>()

        for (typeOfStat in listToDisplay) {
            val calculatedGrid =
                calculateWeeklyGrid(groupedStats.getOrDefault(typeOfStat, listOf()))
            mapWithGrids[typeOfStat] = calculatedGrid
        }
        return StatsShowInTime.WeaklyStats(
            gridInfo = mapWithGrids
        )
    }

    private fun calculateWeeklyGrid(list: List<SavedStats>): WeekGridData {
        val reversedCopyOfData = list.reversed()
        val currendDay = LocalDate.now()
        val latestMeasuringDay = currendDay.minusMonths(6)
        var endOfScope = currendDay
        var startOfScope = currendDay.minusDays(endOfScope.dayOfWeek.value.toLong() - 1)
        val mapWithResults = mutableMapOf<WeekScope, MutableList<Float>>()
        val numberOfSplits = 6

        val listOfGrid = mutableListOf<Float>()
        var start = currendDay.minusDays(currendDay.dayOfMonth.toLong() - 1)

        while (start.isAfter(latestMeasuringDay)) {
            val calculatedGrixPosition = start.getRangeInScope(latestMeasuringDay, currendDay)
            listOfGrid.add(calculatedGrixPosition)
            start = start.minusMonths(1)
        }

        start = currendDay.minusDays(currendDay.dayOfMonth.toLong() - 1)

        val listOfLabels = mutableListOf<ValueScale<String>>()
        while (start.isAfter(latestMeasuringDay)) {
            val calculatedGrixPosition = start.getRangeInScope(latestMeasuringDay, currendDay)
            listOfLabels.add(
                ValueScale(
                    "${start.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).take(3)}",
                    calculatedGrixPosition + (1f / 6) - (1f / 12)
                )
            )
            start = start.minusMonths(1)
        }
        val calculatedGrixPosition = start.getRangeInScope(latestMeasuringDay, currendDay)
        listOfLabels.add(
            ValueScale(
                "${start.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).take(3)}",
                calculatedGrixPosition + (1f / 6) - (1f / 12)
            )
        )

        var end = currendDay
        start = startOfScope
        while (start.isAfter(latestMeasuringDay)) {
            mapWithResults[WeekScope(start = start, end = end)] =
                mutableListOf()
            start = start.minusDays(7)
            end = start.plusDays(6)
        }

        stats@ for (statItem in reversedCopyOfData) {
            while (!statItem.submitedAt.isBetween(startOfScope, endOfScope)) {
                endOfScope = startOfScope.minusDays(1)
                startOfScope = endOfScope.minusDays(endOfScope.dayOfWeek.value.toLong() - 1)

                if (startOfScope.isBefore(latestMeasuringDay)) {
                    break@stats
                }
            }

            val list =
                mapWithResults[WeekScope(start = startOfScope, end = endOfScope)]

            list?.add(statItem.value)
        }

        val result = mapWithResults.map { it.key to calculateWeek(it) }
        val resultPoints = mutableListOf<ValueScale<Float>>()

        for (resultItem in result.filter { it.second > 0 }) {
            val weekInfo = resultItem.first
            val pointX = weekInfo.getRangeInScope(latestMeasuringDay, currendDay)
            resultPoints.add(ValueScale(resultItem.second, pointX))
        }

        return WeekGridData(
            labels = listOfLabels,
            calculatedValues = resultPoints,
            gridLocation = listOfGrid,
            collectionOfWeeks = result.filter { it.second > 0 }
        )
    }


    private fun calculateDailyScope(data: Map<String, List<SavedStats>>): StatsShowInTime.DailyStats {
        return StatsShowInTime.DailyStats(
            data
        )
    }


}

fun LocalDate.isBetween(min: LocalDate, max: LocalDate): Boolean {
    return !(this.isBefore(min) || this.isAfter(max))
}

sealed class State {
    object Init : State()
    data class DataLoaded(
        val groupded: Map<String, List<SavedStats>>,
        val selectedMethod: StatsShowInTime = StatsShowInTime.DailyStats(groupded),
        val allCalculatedWariants: Map<TimeKind, StatsShowInTime> = mapOf(),
        val progressInfo: MeasurementsProgress
    ) : State()
}

enum class TimeKind {
    DAYLY,
    WEAKLY,
    MONTHLY,
}

sealed class StatsShowInTime {
    data class DailyStats(
        val groupded: Map<String, List<SavedStats>>,
    ) : StatsShowInTime()

    data class WeaklyStats(
        val gridInfo: Map<String, WeekGridData>
    ) : StatsShowInTime()

    data class MonthlyStats(
        val gridInfo: Map<String, MonthGridData>
    ) : StatsShowInTime()
}

data class MonthGridData(
    val labels: List<ValueScale<String>>,
    val calculatedValues: List<ValueScale<Float>>,
    val gridLocation: List<Float> = listOf(),
    val collectionOfMonths: List<Pair<MonthOfYear, Float>>,
)

data class WeekGridData(
    val labels: List<ValueScale<String>>,
    val calculatedValues: List<ValueScale<Float>>,
    val gridLocation: List<Float> = listOf(),
    val collectionOfWeeks: List<Pair<WeekScope, Float>>,
)

data class MonthOfYear(val monyh: Month, val year: Int)
data class WeekScope(val start: LocalDate, val end: LocalDate) {

    val middleOfWeek get() = start.toEpochDay() + ((end.toEpochDay() - start.toEpochDay()) / 2)

    fun getRangeInScope(beginScope: LocalDate, endScope: LocalDate): Float {
        val middlePositionBasedOnScopeStart = middleOfWeek - beginScope.toEpochDay()
        return middlePositionBasedOnScopeStart.toFloat() / (endScope.toEpochDay() - beginScope.toEpochDay())
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (start sameMonthAs end) {
            sb.append("${start.dayOfMonth}-${end.dayOfMonth}")
            sb.append(" ")
            sb.append(start.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).take(3))
        } else {
            sb.append("${start.dayOfMonth}")
            sb.append(" ")
            sb.append(start.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).take(3))
            sb.append("-")
            sb.append("${end.dayOfMonth}")
            sb.append(" ")
            sb.append(end.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).take(3))
        }
        return sb.toString()
    }

}

data class ValueScale<T>(
    val value: T,
    val scale: Float
)

infix fun LocalDate.sameMonthAs(anotherDay: LocalDate): Boolean {
    return this.year == anotherDay.year && this.month == anotherDay.month
}

fun LocalDate.getRangeInScope(beginScope: LocalDate, endScope: LocalDate): Float {
    val epochDay = this.toEpochDay()
    val middlePositionBasedOnScopeStart = epochDay - beginScope.toEpochDay()
    return middlePositionBasedOnScopeStart.toFloat() / (endScope.toEpochDay() - beginScope.toEpochDay())
}
