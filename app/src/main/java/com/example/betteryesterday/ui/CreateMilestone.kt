package com.example.betteryesterday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Milestones
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel

@Composable
fun NewMileStone(navController: NavHostController, id: Any?) {
    val milestoneSummary = rememberSaveable { mutableStateOf("") }
    val deadline = rememberSaveable { mutableStateOf("") }

    val milestonesViewModel : MilestoneViewModel = viewModel()
    //val milestonesList : LiveData<List<Milestones>>? = id?.let { milestonesViewModel.getGoalMilestones(it) }
    //val milestoneData = milestonesList?.observeAsState(initial = emptyList())?.value

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
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
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