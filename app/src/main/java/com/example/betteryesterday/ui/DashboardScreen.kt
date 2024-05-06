package com.example.betteryesterday.ui

import android.security.identity.CredentialDataResult.Entries
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.ui.theme.BetterYesterdayTheme
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel

@Composable
fun DashboardScreen(goalViewModel: GoalViewModel, milestonesViewModel: MilestoneViewModel){
    /*This is the dashboard screen composable
    * TODO : MAke the dashboard screen composable and all of its features*/

    val goals = goalViewModel.allGoals.observeAsState(initial = emptyList()).value //This is how the goals displayed in the lazy row will be stored.
    val noCompletedMilestones = milestonesViewModel.completedMilestones.observeAsState().value
    val noIncompleteMilestones = milestonesViewModel.incompletedMilestones.observeAsState().value
    Column {
        Text("WELCOME BACK", modifier = Modifier.align(Alignment.CenterHorizontally))
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    displayNoOfCompletedTasks(noCompletedMilestones)
                    Spacer(modifier = Modifier.width(16.dp))
                    displayNoOfRemainingTasks(noIncompleteMilestones)
                }
            }

            item{
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                displayGoalPieCharts(goals)
            }
        }
    }

}

@Composable
fun displayNoOfCompletedTasks(noCompletedMilestones: Int?) {
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
                Text(text = "$noCompletedMilestones")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Completed Milestones")
            }
        }
    }
}
@Composable
fun displayNoOfRemainingTasks(noIncompleteMilestones: Int?) {
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
            Column {
                Text(text = "$noIncompleteMilestones")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Remaining milestones")
            }
        }
    }
}

@Composable
fun displayGoalPieCharts(goals: List<Goals>) {
    if (goals == null){
        Text(text = "You Have No Goals")
        Text(text = "Try and add some")
        return
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Spacing between items
        contentPadding = PaddingValues(horizontal = 16.dp)   // Padding on the sides of the LazyRow
    ) {
        // This is how you define individual items within a LazyRow
        items(goals) { item ->
            GoalPie(item)
        }
    }
}

@Composable
fun GoalPie(goal : Goals){
    //this is where we will be making the pie chart for each goal that is displayed on the dashboard
    Card(
        modifier = Modifier
            .height(350.dp)
            .width(350.dp) // Set a width for the card
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("Pie chart for goal:\n ${goal.title}")
        }
    }
}

/*This is where all the code needed to make the pie charts will be*/
data class PieChartEntry(
    val color : Color,
    val percentage : Float
)

fun calculateStartAngles(entries: List<PieChartEntry>) : List<Float>{
    var totalPercentage = 0f
    return entries.map { entry ->
        val startAngle = totalPercentage * 360
        totalPercentage += entry.percentage
        startAngle
    }
}

@Composable
fun PieChart(entries: List<PieChartEntry>) {
    Canvas(modifier = Modifier.size(300.dp)) {
        val startAngles = calculateStartAngles(entries)
        entries.forEachIndexed { index, entry ->
            drawArc(
                color = entry.color,
                startAngle = startAngles[index],
                sweepAngle = entry.percentage * 360f,
                useCenter = true,
                topLeft = Offset.Zero,
                size = this.size
            )
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

