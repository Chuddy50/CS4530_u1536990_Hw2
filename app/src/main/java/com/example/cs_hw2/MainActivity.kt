package com.example.cs_hw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

    data class Course
        (
    val department: String,
    val number: String,
    val location: String
) {
    val title: String get() = "$department $number"
}

class CourseViewModel : ViewModel() {
    private val _courses = mutableStateListOf<Course>()
    val courses: List<Course> get() = _courses

    fun addCourse(course: Course) {
        _courses.add(course)
    }

    fun deleteCourse(course: Course) {
        _courses.remove(course)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: CourseViewModel = viewModel()
            CourseApp(vm)
        }
    }
}

@Composable
fun CourseApp(viewModel: CourseViewModel)
{
    var dept by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("") }
    var showDetails by remember { mutableStateOf<Course?>(null) }

    Column(modifier = Modifier.padding(16.dp))
    {
        // this is input fields where user inputs information
        OutlinedTextField(value = dept, onValueChange = { dept = it }, label = { Text("Department") })
        OutlinedTextField(value = num, onValueChange = { num = it }, label = { Text("Number") })
        OutlinedTextField(value = loc, onValueChange = { loc = it }, label = { Text("Location") })

        Button(
            onClick =
                {
                if (dept.isNotBlank() && num.isNotBlank() && loc.isNotBlank())
                {
                    viewModel.addCourse(Course(dept, num, loc))
                    dept = ""; num = ""; loc = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Add Course")
        }

        // Course list
        LazyColumn {
            items(viewModel.courses) { course ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDetails = course }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(course.title)
                    TextButton(onClick = { viewModel.deleteCourse(course) }) {
                        Text("Delete")
                    }
                }
            }
        }

        showDetails?.let { course ->
            AlertDialog(
                onDismissRequest = { showDetails = null },
                title = { Text(course.title) },
                text = {
                    Text("Department: ${course.department}\nNumber: ${course.number}\nLocation: ${course.location}")
                },
                confirmButton = {
                    TextButton(onClick = { showDetails = null }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
