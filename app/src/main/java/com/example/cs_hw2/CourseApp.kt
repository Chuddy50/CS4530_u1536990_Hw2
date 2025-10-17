package com.example.cs_hw2

import android.app.Application
import com.example.cs_hw2.data.CourseRepository
import com.example.cs_hw2.data.local.CourseDatabase

class CourseApp : Application() {
    val database by lazy { CourseDatabase.getInstance(this) }
    val repository by lazy { CourseRepository.getInstance(database) }
}
