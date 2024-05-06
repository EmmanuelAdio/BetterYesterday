package com.example.betteryesterday.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.ui.viewModels.GoalViewModel

@Composable
fun GoalScreen(
    goalViewModel: GoalViewModel,
    navController: NavHostController
){
    /*This is the dashboard screen composable
    * TODO : Make the goals screen composable all all of its features*/
    //place holder add show that this is the dashboard screen.
    val context = LocalContext.current;
    Column {
        ListOfGoals(navController, goalViewModel, context)
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun ListOfGoals(navController: NavHostController, goalViewModel: GoalViewModel, context: Context){
    //this is where we will be displaying all the goals that the user has.
    val goals = goalViewModel.allGoals.observeAsState(initial = emptyList()).value

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
        ) {
        items(goals) { item ->
            GoalCard(navController, item, context)
        }
    }
}

@Composable
fun GoalCard(navController: NavHostController, goal: Goals, context: Context) {
    Card (
        modifier = Modifier
            .height(100.dp) // Set a fixed height for the cards
            .fillMaxWidth() // Fill the remaining width
            .padding(8.dp)// Add padding around the card
            .clickable {
                Toast
                    .makeText(context, "Goal clicked!", Toast.LENGTH_SHORT)
                    .show()
                navController.navigate(route = AppScreens.goalMilestones.name + "/${goal.id}")
            }
    ) {
        Row{
            //this will be where the pie chart for the goal will be placed.
            Box(contentAlignment = Alignment.Center) {
                Text("Pie chart for\n ${goal.title}")
            }
             Box(contentAlignment = Alignment.Center){
                 Column {
                     Text("${goal.title}")
                     Text("${goal.deadline}")
                 }

             }
        }
    }
}


