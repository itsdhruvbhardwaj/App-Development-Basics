package com.hackathon.todolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent

data class Tasks(
    val id:Int,
    var name:String,
    var description:String,
    var date:String,
    var isEditing:Boolean=false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListApp()
{
    var tasks by remember { mutableStateOf(listOf<Tasks>()) }
    var showDilog by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDiscription by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center)
    {
        Button(
            onClick = {showDilog=true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add a task");
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {

        }
    }

    if(showDilog)
    {
        AlertDialog(onDismissRequest = { showDilog=false },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                    Button(onClick = {
                        if(taskName.isNotBlank())
                        {
                            val newTask = Tasks(
                                id = tasks.size + 1,
                                name = taskName,
                                description = taskDiscription,
                                date = "12/12/2025"
                            )
                            tasks = tasks + newTask
                            showDilog=false
                            taskName=""
                            taskDiscription=""
                        }
                    }) {
                        Text("Add")
                    }
                    Button(onClick = {showDilog=false}) {
                        Text("Cancel")
                    }
                }

            },
            title = {Text("Add Your Task Here")},
            text = {
                Column {
                    OutlinedTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )

                    OutlinedTextField(
                        value = taskDiscription,
                        onValueChange = { taskDiscription = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            }
        )
    }
}
