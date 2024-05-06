package com.example.betteryesterday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.data.Milestones
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel

@Composable
fun GoalsDetailsScreen(
    navController: NavHostController,
    goalViewModel: GoalViewModel,
    goalIndex: Int?
){
    val goal : LiveData<Goals>? = goalIndex?.let { goalViewModel.getGoal(it) }//this is good practice so if the goal index in the route is null the program handles ir
    val goalState = goal?.observeAsState() // This handles observing and gets the current state
    val goalData = goalState?.value // Extract the current goal, may be null

    val milestonesViewModel : MilestoneViewModel = viewModel()
    val milestonesList : LiveData<List<Milestones>>? = goalIndex?.let { milestonesViewModel.getGoalMilestones(it) }
    val milestoneData = milestonesList?.observeAsState(initial = emptyList())?.value

    Column {
        LazyColumn {
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    displayGoalInfo(goalData)
                    Spacer(modifier = Modifier.width(16.dp))
                    displayGoalPieChart(goalData)
                }
            }
            item {
                buttonRow(
                    navController,
                    modifier = Modifier// Align the button row at the bottom of the Box
                        .fillMaxWidth(),  // Take up full width at the bottom
                    goalData
                )
            }
        }
        MilestonesBox(milestoneData)
    }

}

@Composable
fun displayGoalInfo(goalData: Goals?) {
    // Use a default title and deadline if goalData is null
    val title = goalData?.title ?: "No Title"
    val deadline = goalData?.deadline ?: "No Deadline"

    Card(
        modifier = Modifier
            .height(150.dp) // Set a fixed height for the cards
            .fillMaxWidth(0.5f) // Fill half the width of the parent
            .padding(8.dp) // Add padding around the card
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(text = title)
                Text(text = deadline)
            }
        }
    }
}

@Composable
fun displayGoalPieChart(goal: Goals?) {
    /*TODO THE PIE CHART FOR THE GOAL DETAILS PAGE*/
    Card(
        modifier = Modifier
            .height(150.dp) // Set a fixed height for the cards
            .fillMaxWidth() // Fill the remaining width
            .padding(8.dp) // Add padding around the card
    )  {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ){
            Text(text = "This will display the goal's pie chart")
        }
    }
}

@Composable
fun MilestonesBox(milestonesData : List<Milestones>?) {
    Card(
        modifier = Modifier
            .fillMaxSize() // Fill the available space
            .padding(8.dp)// Add padding around the card
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Reserve space for the buttons at the bottom
        ) {
            listOfMilestones(milestonesData)  // This will list the tasks in a LazyColumn
        }
    }
}

@Composable
fun listOfMilestones(milestones: List<Milestones>?) {
    if (milestones.isNullOrEmpty()) {
        Text("No milestones available")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()  // Fill the width of the parent
    ) {
        items(milestones) {milestone ->
            milestoneCard(milestone)
        }
    }
}


@Composable
fun milestoneCard(milestone: Milestones) {
    val checkedState = remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text("${milestone.summary}")
                Text("${milestone.deadline}")
            }

            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }
    }
}

@Composable
fun buttonRow(navController: NavHostController, modifier: Modifier, goal: Goals?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =  modifier
    ) {
        Button(onClick = { navController.navigate(route = AppScreens.createMilestones.name + "/${goal?.id}") }) {
            Icon(Icons.Filled.Add, contentDescription = "Add More Tasks")
            Text(text = "Create New Task")
        }
        Button(onClick = { /* TODO: Define action (implicit intent) for when the user wants to save teh goal deadline to their calendar app. */ }) {
            Icon(Icons.Filled.DateRange, contentDescription = "Add To Calendar")
            Text(text = "Save To Calendar")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun showTasks(){
//    BetterYesterdayTheme {
//        TasksScreen(goalViewModel = , goalIndex = )
//    }
//}