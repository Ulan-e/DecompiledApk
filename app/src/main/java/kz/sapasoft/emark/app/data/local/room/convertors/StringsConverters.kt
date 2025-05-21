package kz.sapasoft.emark.app.data.local.room.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.sapasoft.emark.app.utils.Constants

class StringsConverters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}

class StringListConvertor {

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}

class MarkerStatusConverter {

    @TypeConverter
    fun fromMarkerStatus(status: Constants.MarkerStatus?): String {
        return status?.name ?: Constants.MarkerStatus.NORMAL.name
    }

    @TypeConverter
    fun toMarkerStatus(value: String?): Constants.MarkerStatus {
        return try {
            Constants.MarkerStatus.valueOf(value ?: Constants.MarkerStatus.NORMAL.name)
        } catch (e: IllegalArgumentException) {
            Constants.MarkerStatus.NORMAL
        }
    }
}