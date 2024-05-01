package com.example.betteryesterday.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun GoalScreen(
    goalViewModel: GoalViewModel,
    navController: NavHostController
){
    /*This is the dashboard screen composable
    * TODO : Make the goals screen composable all all of its features*/
    //place holder add show that this is the dashboard screen.
    val context = LocalContext.current;
    val Goals = listOf<Int>(1,2,3,4,5,6,7,8,9,10)
    Column {
        Text(text = "This is the the Goals Page")
        Spacer(modifier = Modifier.height(16.dp))
        ListOfGoals(navController, Goals, context)
        Spacer(modifier = Modifier.height(16.dp))

    }

}

@Composable
fun ListOfGoals(navController: NavHostController, Goals: List<Int>, context: Context){
    //this is where we will be displaying all the goals that the user has.
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
        ) {
        itemsIndexed(Goals) { index, item ->
            GoalCard(navController, item, index, context)
        }
    }
}

@Composable
fun GoalCard(navController: NavHostController, goal: Int, index: Int, context: Context) {
    Card (
        modifier = Modifier
            .height(100.dp) // Set a fixed height for the cards
            .fillMaxWidth() // Fill the remaining width
            .padding(8.dp)// Add padding around the card
    ) {
        Row (
            Modifier.clickable {
                Toast
                    .makeText(context, "Item clicked!", Toast.LENGTH_SHORT)
                    .show()
                    navController.navigate(route = AppScreens.tasks.name + "/$index")
                }
        ){
            //this will be where the pie chart for the goal will be placed.
            Box(contentAlignment = Alignment.Center) {
                Text("Pie chart for $goal")
            }
             Box(contentAlignment = Alignment.Center){
                 Column {
                     Text("<Goal $goal title>")
                     Text("<Goal $goal deadline>")
                 }

             }
        }
    }
}


