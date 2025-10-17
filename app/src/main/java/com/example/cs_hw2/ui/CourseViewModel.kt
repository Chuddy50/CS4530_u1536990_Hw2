package com.example.cs_hw2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cs_hw2.data.CourseRepository
import com.example.cs_hw2.data.local.entity.CourseEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CourseUi(
    val id: Long,
    val department: String,
    val number: String,
    val location: String
) {
    val title: String get() = "$department $number"
}

private fun CourseEntity.toUi() = CourseUi(id, department, number, location)

class CourseViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    val courses: StateFlow<List<CourseUi>> =
        repository.observeCourses()
            .map { it.map { e -> e.toUi() } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addCourse(dept: String, num: String, loc: String) {
        viewModelScope.launch { repository.addCourse(dept, num, loc) }
    }

    fun deleteById(id: Long) {
        viewModelScope.launch { repository.deleteById(id) }
    }
}

class CourseViewModelFactory(
    private val repository: CourseRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            return CourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
