package com.succiue.myapplication.data.model

import androidx.room.*
import com.google.gson.Gson
import java.util.*

@Entity
@TypeConverters(Converters::class)
data class Objectif(
    @PrimaryKey val userId: String,
    @ColumnInfo(name = "startDate") val startDate: Date,
    @ColumnInfo(name = "endDate") val endDate: Date,
    @ColumnInfo(name = "category") val category: List<String>,
    @ColumnInfo(name = "amount") val amount: Double,
)

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}