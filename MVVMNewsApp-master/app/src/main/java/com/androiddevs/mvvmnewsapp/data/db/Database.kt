package com.androiddevs.mvvmnewsapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.data.convertors.ArticleSourceConvertor
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.data.dao.ArticlesDao

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ArticleSourceConvertor::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract val articleDao : ArticlesDao

    companion object{
        @Volatile
        private var INSTANCE : ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase {
                synchronized(this) {
                    var instance = INSTANCE
                    if(instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ArticleDatabase::class.java,
                            "article_database"
                        ).build()
                        INSTANCE = instance
                    }
                    return instance
                }
        }
    }
}