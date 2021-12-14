package com.tommannson.bodystats.infrastructure.export

import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.infrastructure.configuration.StatsDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import org.threeten.bp.LocalDate
import java.io.*
import javax.inject.Inject


class ExportProcessor
@Inject
constructor(
    val user: UserDao,
    val starDao: StatsDao
) {

    fun processExport(fileStream: OutputStream) {
        user.getAll().firstOrNull()?.let {

            val fullList = starDao.getAll()

            val printWriter = PrintWriter(fileStream ?: throw IOException("invalidStream"))
            val writer = CSVWriter(printWriter)

            val data: MutableList<Array<String>> = ArrayList()


            fullList.forEach {
                data.add(
                    arrayOf(
                        it.statName,
                        it.submitedAt.toEpochDay().toString(),
                        it.value.toString()
                    )
                )
            }

            writer.writeAll(data)
            writer.flush()
            writer.close()
        }
    }

    fun processImport(fileStream: InputStream) {
        user.getAll().firstOrNull()?.let { selectedUser ->

            val printWriter = BufferedReader(InputStreamReader(fileStream))
            val reader = CSVReader(printWriter)
            val listOfLines = reader.readAll()

            val data: MutableList<SavedStats> = ArrayList()

            listOfLines.forEach {
                data.add(
                    SavedStats(
                        it[0],
                        it[2].toFloat(),
                        LocalDate.ofEpochDay(it[1].toLong()),
                        selectedUser.id
                    )
                )
            }

            starDao.createNewStats(data)
        }
    }

}