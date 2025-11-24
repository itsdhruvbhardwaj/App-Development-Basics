package com.hackathon.todolist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlin.collections.plus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




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
    var showDialog by remember { mutableStateOf(false) }
    var editTask by remember { mutableStateOf<Tasks?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center)
    {
        Button(
            onClick = {showDialog=true},
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
            items(tasks) {
                TaskList(it,
                    onRowClick = {
                        // Open edit dialog
                        showEditDialog = true
                        editTask = it
                        // Load selected task into fields
                        taskName = it.name
                        taskDescription = it.description
                    },
                    onDeleteClick = { },
                    )
            }
        }
    }

    if(showDialog)
    {
        AlertDialog(onDismissRequest = { showDialog=false }, confirmButton =
            {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                    )
                {
                    Button(onClick = {showDialog=false})
                    {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        if(taskName.isNotBlank())
                        {
                            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(Date())

                            val newTask = Tasks(
                                id = tasks.size + 1,
                                name = taskName,
                                description = taskDescription,
                                date = currentDate
                            )
                            tasks = tasks + newTask
                            showDialog=false
                            taskName=""
                            taskDescription=""
                        }
                    }) {
                        Text("Add")
                    }

                }
            },
            title = {Text("Add Your Task Here")},
            text = {
                Column {
                    OutlinedTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = true,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                        )

                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Description") },
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = true,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )
    }

    if (showEditDialog && editTask != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Button(onClick = { showEditDialog = false }) {
                        Text("Cancel")
                    }

                    Button(onClick = {
                        editTask?.let {
                            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(Date())

                            val id = editTask!!.id

                            tasks = tasks.map { t ->
                                if (t.id == id)
                                    t.copy(
                                        name = taskName,
                                        description = taskDescription,
                                        date = currentDate
                                    )
                                else t
                            }
                        }

                        showEditDialog = false
                    }) {
                        Text("Save")
                    }
                }
            },
            title = { Text("Edit Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = { Text("Task Name") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = true,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        )
                    )
                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = true,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        )
                    )
                }
            },

        )
    }
}

@Composable
fun TaskList(
    item: Tasks,
    onRowClick: ()-> Unit,
    onDeleteClick: ()-> Unit,
    ){
    Row (  //outer Row for the entire item
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{onRowClick()}
            .shadow(
                elevation = 8.dp,                 // shadow height
                shape = RoundedCornerShape(20),   // match your border shape
                clip = false
            )
            .background(
                color = Color(0xFFFFF1F4),          // your desired background color
                shape = RoundedCornerShape(20)
            )
            .border(
                border = BorderStroke(2.dp, Color(0xFFE91E63)),
                shape = RoundedCornerShape(20)
            )
    ){
        Column(modifier = Modifier.fillMaxWidth())
        {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(text = item.name,fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 2.dp))

                Text(text = item.date,color = Color.Black, modifier = Modifier.padding(10.dp))
            }

            Text(text = item.description,color = Color.Black, modifier = Modifier.padding(10.dp, 0.dp, 8.dp, 8.dp))
        }
    }
}

@Preview(showBackground = true, name = "To-Do List App Preview")
@Composable
fun ToDoListAppPreview() {
    // We call the main app composable for the preview
    ToDoListApp()
}


