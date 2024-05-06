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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.ui.viewModels.GoalViewModel

@Composable
fun NewGoalScreen(
    navController: NavHostController,
    goalViewModel: GoalViewModel
) {
    val goalTitle = rememberSaveable { mutableStateOf("") }
    val deadline = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }


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
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
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
fun PreviewNewGoalScreen() {
   // NewGoalScreen(goalViewModel)
}