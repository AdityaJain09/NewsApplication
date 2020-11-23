package com.androiddevs.mvvmnewsapp.data.convertors

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.models.Source

class ArticleSourceConvertor {

    @TypeConverter
    fun fromSourceToString(source: Source) : String = source.name

    @TypeConverter
    fun fromStringToSource(name : String) : Source = Source(name, name)
}