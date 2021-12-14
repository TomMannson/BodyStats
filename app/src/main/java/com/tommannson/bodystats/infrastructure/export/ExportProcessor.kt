package com.tommannson.bodystats.infrastructure.export

import com.tommannson.bodystats.infrastructure.configuration.StatsDao
import com.tommannson.bodystats.infrastructure.configuration.UserDao
import javax.inject.Inject

class ExportProcessor
@Inject
constructor(
    val user: UserDao,
    val starDao: StatsDao
) {


    fun processExport() {
        user.getAll().firstOrNull()?.let {

            val fullList = starDao.getAll()







        }
    }

}