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
import androidx.lifecycle.ViewModelProvider
import com.example.cs_hw2.ui.CourseUi
import com.example.cs_hw2.ui.CourseViewModel
import com.example.cs_hw2.ui.CourseViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as CourseApp
        val vm = ViewModelProvider(this, CourseViewModelFactory(app.repository))
            .get(CourseViewModel::class.java)

        setContent { CourseAppScreen(vm) }
    }
}

@Composable
fun CourseAppScreen(viewModel: CourseViewModel) {
    var dept by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("") }
    var showDetails by remember { mutableStateOf<CourseUi?>(null) }

    val courses by viewModel.courses.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = dept, onValueChange = { dept = it }, label = { Text("Department") })
        OutlinedTextField(value = num, onValueChange = { num = it }, label = { Text("Number") })
        OutlinedTextField(value = loc, onValueChange = { loc = it }, label = { Text("Location") })

        Button(
            onClick = {
                if (dept.isNotBlank() && num.isNotBlank() && loc.isNotBlank()) {
                    viewModel.addCourse(dept, num, loc)
                    dept = ""; num = ""; loc = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) { Text("Add Course") }

        LazyColumn {
            items(courses, key = { it.id }) { course ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDetails = course }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(course.title)
                    TextButton(onClick = { viewModel.deleteById(course.id) }) {
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
                    TextButton(onClick = { showDetails = null }) { Text("Close") }
                }
            )
        }
    }
}
