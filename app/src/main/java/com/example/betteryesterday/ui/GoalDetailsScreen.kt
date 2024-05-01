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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

@Composable
fun GoalsDetailsScreen(
    goalViewModel: GoalViewModel,
    goalIndex: Int?
    ){
    Column {
        Text(text = "This is the Task Page for Goal $goalIndex ")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            displayGoalInfo()
            Spacer(modifier = Modifier.width(16.dp))
            displayGoalPieChart()
        }
        taskBox()
    }
}

@Composable
fun displayGoalInfo(){
    Card(
        modifier = Modifier
            .height(150.dp) // Set a fixed height for the cards
            .fillMaxWidth(0.5f) // Fill half the width of the parent
            .padding(8.dp) // Add padding around the card
    )  {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ){
            Column {
                Text(text = "<Goal title>")
                Text(text = "<Goal Deadline>")
            }
        }
    }
}
@Composable
fun displayGoalPieChart(){
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
fun taskBox() {
    Card(
        modifier = Modifier
            .fillMaxSize() // Fill the available space
            .padding(8.dp) // Add padding around the card
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Fill the available space inside the card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)  // Reserve space for the buttons at the bottom
            ) {
                listOfTasks()  // This will list the tasks in a LazyColumn
            }
            buttonRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)  // Align the button row at the bottom of the Box
                    .fillMaxWidth()  // Take up full width at the bottom
            )
        }
    }
}

@Composable
fun listOfTasks() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()  // Fill the width of the parent
    ) {
        items(100) { index -> // Dynamically loading example items
            Text(text = "Task $index")
        }
    }
}

@Composable
fun buttonRow(modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { /* TODO: Define action */ }) {
            Icon(Icons.Filled.Add, contentDescription = "Add More Tasks")
            Text(text = "Create New Task")
        }
        Button(onClick = { /* TODO: Define action */ }) {
            Icon(Icons.Filled.DateRange, contentDescription = "Add To Calendar")
            Text(text = "Save To Calendar")
        }
    }
}

@Composable
fun getTasks(){
    /*TODO this is the function that will allow us to pass all of the tasks for the goal
    *  into the program as a list.*/
}

//@Preview(showBackground = true)
//@Composable
//fun showTasks(){
//    BetterYesterdayTheme {
//        TasksScreen(goalViewModel = , goalIndex = )
//    }
//}