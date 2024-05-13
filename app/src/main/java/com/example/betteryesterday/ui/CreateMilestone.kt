package com.example.betteryesterday.ui

import android.app.DatePickerDialog
import android.util.Log
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.data.Milestones
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun NewMileStone(
    navController: NavHostController,
    id: Any?,
    milestonesViewModel: MilestoneViewModel
) {
    val milestoneSummary = rememberSaveable { mutableStateOf("") }
    val deadline = rememberSaveable { mutableStateOf("") }

    //val milestonesViewModel : MilestoneViewModel = viewModel()
    val context = LocalContext.current // Get the current Compose context

    var showErrorDialog by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val showPicker = rememberSaveable { mutableStateOf(false) }

    //function to turn the strign date into mtime millis so thaht e=they can be compared.
    fun convertStringDateToMillis(dateString: String, dateFormat: String = "dd/MM/yyyy"): Long {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return formatter.parse(dateString)?.time ?: 0
    }

    fun compareDates(dateMillis1: Long, dateMillis2: Long): Int {
        return dateMillis1.compareTo(dateMillis2)
    }

    val goalViewModel : GoalViewModel = viewModel()

    val goal = goalViewModel.getGoal(id as Int).observeAsState()
    val goalDeadline = goal.value?.deadline

    // Function to validate input and show errors if necessary
    fun validateInputAndCreateMilestone() {
        when {
            milestoneSummary.value.isBlank() -> {
                errorMessage = "Please enter a Milestone Summary."
                showErrorDialog = true
            }
            deadline.value.isBlank() -> {
                errorMessage = "Please select a deadline."
                showErrorDialog = true
            }
            else -> {
                val convertedDeadline = convertStringDateToMillis(deadline.value)
                val convertedGoalDeadline = goalDeadline?.let { convertStringDateToMillis(it) }

                val result = convertedGoalDeadline?.let { compareDates(convertedDeadline, it) }

                Log.d("goal deadline", "$goalDeadline")
                Log.d("milestone deadline", "${deadline.value}")
                if (result != null) {
                    if (result <= 0 ){
                        //this means the milestone comes before the goal dealine to create milestone
                        // All inputs are valid, proceed to create the goal
                        milestonesViewModel.insertMilestone(
                            Milestones(
                                goalID = id as Int,
                                summary = milestoneSummary.value,
                                deadline = deadline.value,
                                complete = false
                            ))
                        navController.popBackStack()
                    } else {
                        errorMessage = "Deadline must be before $goalDeadline"
                        showErrorDialog = true
                    }
                }

            }
        }
    }

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

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
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
                        onClick = { navController.popBackStack() },
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { validateInputAndCreateMilestone() },
                    ) {
                        Text("Create")
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