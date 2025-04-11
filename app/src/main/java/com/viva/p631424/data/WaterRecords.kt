package com.viva.p631424.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "water_records631424")
data class WaterRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cup : Float = 0.0f,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

class Converters {
    //For DateTime Timestamp
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun toTimestamp(dateTime: LocalDateTime?): String? {
        return dateTime?.format(formatter)
    }

    // For Date
    private val formatter2 = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun toDate(dateTime: LocalDate?): String? {
        return dateTime?.format(formatter2)
    }
}