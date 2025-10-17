package com.example.cs_hw2.data

import com.example.cs_hw2.data.local.CourseDatabase
import com.example.cs_hw2.data.local.dao.CourseDao
import com.example.cs_hw2.data.local.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun observeCourses(): Flow<List<CourseEntity>>
    suspend fun addCourse(dept: String, number: String, location: String)
    suspend fun deleteById(id: Long)

    companion object {
        @Volatile private var INSTANCE: CourseRepository? = null

        fun getInstance(db: CourseDatabase): CourseRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DefaultCourseRepository(db.courseDao()).also { INSTANCE = it }
            }
    }
}

private class DefaultCourseRepository(
    private val dao: CourseDao
) : CourseRepository {
    override fun observeCourses(): Flow<List<CourseEntity>> = dao.observeAll()

    override suspend fun addCourse(dept: String, number: String, location: String) {
        dao.insert(CourseEntity(department = dept, number = number, location = location))
    }

    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}
