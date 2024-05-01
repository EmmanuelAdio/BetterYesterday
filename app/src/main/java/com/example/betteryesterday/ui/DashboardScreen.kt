package com.example.betteryesterday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.betteryesterday.ui.theme.BetterYesterdayTheme

@Composable
fun DashboardScreen(goalViewModel: GoalViewModel){
    /*This is the dashboard screen composable
    * TODO : MAke the dashboard screen composable and all of its features*/
    val Goals = listOf<Int>(1,2,3,4,5) //This is how the goals displayed in the lazy row will be stored.
    //place holder add show that this is the dashboard screen.
    Column {
        WelcomeBackMessage()

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            displayNoOfCompletedTasks()
            Spacer(modifier = Modifier.width(16.dp))
            displayNoOfRemainingTasks()
        }

        Spacer(modifier = Modifier.height(16.dp))
        displayWeekAnalytics()

        Spacer(modifier = Modifier.height(16.dp))
        displayGoalPieCharts(Goals)
    }
}

@Composable
fun WelcomeBackMessage(){
    // Placeholder for the actual user's name and date
    /*TODO Set the welcome message on the dashboard to welcome the user's actual USERNAME*/
    val userName = "<User>"
    /*TODO set the text to actually show the date */
    val date = "<Today's date>"

    Text("Welcome Back $userName")
    Spacer(modifier = Modifier.height(8.dp))
    Text("Today - $date")

}

@Composable
fun displayNoOfCompletedTasks(){
    Card(
        modifier = Modifier
            .height(100.dp) // Set a fixed height for the cards
            .fillMaxWidth(0.5f) // Fill half the width of the parent
            .padding(8.dp) // Add padding around the card
    )  {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ){
            Text(text = "This will display the number of completed Tasks")
        }
    }
}
@Composable
fun displayNoOfRemainingTasks(){
    Card(
        modifier = Modifier
            .height(100.dp) // Set a fixed height for the cards
            .fillMaxWidth() // Fill the remaining width
            .padding(8.dp) // Add padding around the card
    )  {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ){
            Text(text = "This will display the number of remaining Tasks")
        }
    }
}

@Composable
fun displayWeekAnalytics(){
    Card {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(200.dp)
                .padding(16.dp)
        ) {
            Text("Graph to show number of tasks completed each day of the week")
        }
    }
}

@Composable
fun displayGoalPieCharts(Goals : List<Int>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Spacing between items
        contentPadding = PaddingValues(horizontal = 16.dp)   // Padding on the sides of the LazyRow
    ) {
        // This is how you define individual items within a LazyRow
        items(Goals) { item ->
            GoalPie(item)
        }
    }
}

@Composable
fun GoalPie(Number: Int){
    //this is where we will be making the pie chart for each goal that is displayed on the dashboard
    Card(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp) // Set a width for the card
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("Pie chart$Number")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showDash(){
    BetterYesterdayTheme {
        //DashboardScreen()
    }
}

