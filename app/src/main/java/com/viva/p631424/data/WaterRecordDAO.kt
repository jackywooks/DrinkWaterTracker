package com.viva.p631424.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface WaterRecordDAO {
    @Insert
    suspend fun insert(waterRecord: WaterRecord)

    @Query("SELECT * FROM water_records631424 ORDER BY timestamp DESC")
    fun getAllWaterRecords(): LiveData<List<WaterRecord>>

    @Query("SELECT * FROM water_records631424 WHERE DATE(timestamp) = :date ORDER BY timestamp DESC")
    fun getWaterRecordsByDate(date: LocalDate): LiveData<List<WaterRecord>>

    @Query("SELECT COALESCE(SUM(cup), 0.0) FROM water_records631424 WHERE DATE(timestamp) = :date")
    fun getTotalCupsByDate(date: LocalDate): LiveData<Float>
}