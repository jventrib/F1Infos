package com.jventrib.f1infos.race.data.db

import androidx.room.*
import com.jventrib.f1infos.race.model.db.RaceResult
import com.jventrib.f1infos.race.model.db.RaceResultWithDriver
import com.jventrib.f1infos.race.model.remote.RaceResultRemote
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceResultDao {

    @Transaction
    @Query("SELECT * from race_result WHERE season = :season and round = :round ORDER BY position ASC")
    fun getRaceResultsWithDrivers(season: Int, round: Int): Flow<List<RaceResultWithDriver>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(raceRemotes: List<RaceResult>)

    @Query("DELETE FROM race_result")
    suspend fun deleteAll()
}