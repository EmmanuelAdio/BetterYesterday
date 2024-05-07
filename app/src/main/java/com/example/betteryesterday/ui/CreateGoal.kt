package com.example.betteryesterday.ui

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import java.util.Calendar
import java.util.Date

@Composable
fun NewGoalScreen(
    navController: NavHostController,
    goalViewModel: GoalViewModel
) {
    val goalTitle = rememberSaveable { mutableStateOf("") }
    val deadline = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current // Get the current Compose context

    val showPicker = remember{ mutableStateOf(false) }

    if (showPicker.value){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //declare the DatePickerDialog and set the initial values as current values (present year, month  and day)
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                // Format the date selected and pass it back via the callback
                deadline.value = "$day/${month + 1}/$year"
                showPicker.value = false
            }, year, month, day
        ).apply {
            setOnCancelListener {
                showPicker.value = false // Set to false when dialog is canceled
            }
        }.show()
    }

    Column {
        Text("New Goal", modifier = Modifier.align(Alignment.CenterHorizontally))

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                OutlinedTextField(
                    value = goalTitle.value,
                    onValueChange = { goalTitle.value = it },
                    label = { Text("Goal Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                OutlinedTextField(
                    value = deadline.value,
                    onValueChange = { deadline.value = it },
                    label = { Text("Deadline") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true, // Makes the text field non-editable
                    trailingIcon = {
                        IconButton(onClick = { showPicker.value = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange, // Use appropriate icon
                                contentDescription = "Select Date"
                            )
                        }
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(
                        onClick = {
                            //insert the new goal into the database
                                  goalViewModel.insertGoal(
                                      Goals(
                                          title = goalTitle.value,
                                          deadline = deadline.value,
                                          description = description.value))
                            navController.popBackStack()
                        },
                    ) {
                        Text("Create")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            Log.v("HERE", "THIS SHOULD WORK")
                            navController.popBackStack() },
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNewGoalScreen() {
   // NewGoalScreen(goalViewModel)
}