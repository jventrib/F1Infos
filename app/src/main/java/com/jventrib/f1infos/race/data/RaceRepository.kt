package com.jventrib.f1infos.race.data

import androidx.lifecycle.LiveData
import com.jventrib.f1infos.common.data.Resource
import com.jventrib.f1infos.common.utils.performGetOperation
import com.jventrib.f1infos.race.data.db.RaceDao
import com.jventrib.f1infos.race.model.Race
import com.jventrib.f1infos.race.data.remote.RaceRemoteDataSource
import kotlinx.coroutines.Dispatchers
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RaceRepository(
    private val raceDao: RaceDao,
    private val raceRemoteDataSource: RaceRemoteDataSource
) {

    fun getAllRaces(): LiveData<Resource<List<Race>>> =
        performGetOperation({ raceDao.getAllRaces() },
            { raceRemoteDataSource.getRaces() },
            {
                raceDao.insertAll(it.mrData.table.races.map { r ->
                    r.apply { datetime = buildDatetime(r) }
                })
            })


    suspend fun insert(race: Race) {
        raceDao.insert(race)
    }

    private fun buildDatetime(r: Race): Instant =
        ZonedDateTime.parse("${r.date}T${r.time}").toInstant()


}

