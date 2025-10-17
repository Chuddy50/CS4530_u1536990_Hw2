package com.example.cs_hw2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cs_hw2.data.local.dao.CourseDao
import com.example.cs_hw2.data.local.entity.CourseEntity

@Database(entities = [CourseEntity::class], version = 1, exportSchema = true)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile private var INSTANCE: CourseDatabase? = null

        fun getInstance(context: Context): CourseDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CourseDatabase::class.java,
                    "course-db"
                ).build().also { INSTANCE = it }
            }
    }
}
