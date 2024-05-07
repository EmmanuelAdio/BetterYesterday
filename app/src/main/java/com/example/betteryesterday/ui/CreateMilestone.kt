package com.example.betteryesterday.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Milestones
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel
import java.util.Calendar

@Composable
fun NewMileStone(
    navController: NavHostController,
    id: Any?,
    milestonesViewModel: MilestoneViewModel
) {
    val milestoneSummary = rememberSaveable { mutableStateOf("") }
    val deadline = rememberSaveable { mutableStateOf("") }

    val milestonesViewModel : MilestoneViewModel = viewModel()
    val context = LocalContext.current // Get the current Compose context

    val showPicker = rememberSaveable { mutableStateOf(false) }

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
        Text("New Milestone Summary", modifier = Modifier.align(Alignment.CenterHorizontally))

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                OutlinedTextField(
                    value = milestoneSummary.value,
                    onValueChange = { milestoneSummary.value = it },
                    label = { Text("Milestone Summary") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
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

            item{
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(
                        onClick = { milestonesViewModel.insertMilestone(
                            Milestones(
                                goalID = id as Int,
                                summary = milestoneSummary.value,
                                deadline = deadline.value,
                                complete = false
                        ))
                            navController.popBackStack() },
                    ) {
                        Text("Create")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.popBackStack() },
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
fun PreviewNewMilestone() {
    //NewMileStone(navController)
}