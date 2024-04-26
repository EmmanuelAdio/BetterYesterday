package com.example.betteryesterday.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.betteryesterday.ui.theme.BetterYesterdayTheme

@Composable
fun DashboardScreen(modifier: Modifier.Companion = Modifier){
    /*This is the dashboard screen composable
    * TODO : MAke the dashboard screen composable all all of its features*/
    //place holder add show that this is the dashboard screen.
    Text(text = "This is the Dashboard!")
//    Column {
//        /*TODO Set the welcome message on the dashboard to welcome the user's actual USERNAME*/
//        Text(text = "Welcome Back <User>")
//        /*TODO set the text to actually show the date */
//        Text(text = "Today - <Today's date>")
//
//        Row {
//            Card {
//                displayNoOfCompletedTasks()
//            }
//            Card {
//                displayNoOfRemainingTasks()
//            }
//        }
//        
//        
//    }
}

@Composable
fun displayNoOfCompletedTasks(){
    Text(text = "This will display teh number of completed Tasks")
}

@Composable
fun displayNoOfRemainingTasks(){
    Text(text = "This will display the number of remaining Tasks")
}

@Composable
fun displayWeekAnalytics(){
    Text(text = "This will display the number of remaining Tasks")
}

@Composable
fun displayGoalPieCharts(){
    Text(text = "This will display the number of remaining Tasks")
}
//@Preview(showBackground = true)
//@Composable
//fun showDash(){
//    BetterYesterdayTheme {
//        DashboardScreen()
//    }
//}

