package com.jventrib.formulainfo.race.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jventrib.formulainfo.race.model.db.Circuit

@Dao
interface CircuitDao : IDao<Circuit> {

    @Query("DELETE FROM circuit")
    fun deleteAll()

}
