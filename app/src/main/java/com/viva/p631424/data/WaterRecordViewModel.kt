package com.viva.p631424.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class WaterRecordViewModel(private val database: AppDatabase) : ViewModel() {
    val allRecord: LiveData<List<WaterRecord>> = database.waterRecordDao().getAllWaterRecords()

    fun insert(waterRecord: WaterRecord) = viewModelScope.launch {
        database.waterRecordDao().insert(waterRecord)
    }

    fun getWaterRecordsByDate(date: LocalDate): LiveData<List<WaterRecord>> {
        return database.waterRecordDao().getWaterRecordsByDate(date)
    }

    fun getTotalCupsByDate(date: LocalDate): LiveData<Float> {
        return database.waterRecordDao().getTotalCupsByDate(date)
    }

    fun addWaterRecord(cups: Float) = viewModelScope.launch {
        val newRecord = WaterRecord(cup = cups)
        database.waterRecordDao().insert(newRecord)
    }
}

class WaterRecordViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WaterRecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WaterRecordViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}