package com.example.betteryesterday.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.ui.theme.BetterYesterdayTheme
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel
import kotlin.math.cos
import kotlin.math.sin

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
                displayGoalPieCharts(goals, milestonesViewModel)
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
fun displayGoalPieCharts(goals: List<Goals>, milestonesViewModel: MilestoneViewModel) {
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
            GoalPie(item, milestonesViewModel)
        }
    }
}

@Composable
fun GoalPie(goal: Goals, milestonesViewModel: MilestoneViewModel){
    //this is where we will be making the pie chart for each goal that is displayed on the dashboard
    var entries = getGoalPieChart(milestonesViewModel,goal).observeAsState().value
    Card(
        modifier = Modifier
            .height(380.dp)
            .width(350.dp) // Set a width for the card
            .padding(8.dp)
    ) {
        Column (modifier = Modifier.fillMaxSize()) {
            Text("${goal.title}", modifier = Modifier.align(Alignment.CenterHorizontally))
            if (entries != null) {
                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Legend(entries)
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(25.dp)) {
                if (entries != null) {
                    PieChart(entries, false)
                }
            }
        }
    }
}

/*This is where all the code needed to make the pie charts will be*/
data class PieChartEntry(
    val label : String,
    val color : Color,
    val percentage : Float
)

fun getGoalPieChart(milestonesViewModel: MilestoneViewModel, goal: Goals) : LiveData<List<PieChartEntry>>{
    //this mediator Live data allows for multiple live sources to be observed at once and updates whenever one source changes
    val result = MediatorLiveData<List<PieChartEntry>>()

    val totalLiveData = milestonesViewModel.numOfGoalMilestone(goal.id)
    val completeLiveData = milestonesViewModel.numOfGoalComplete(goal.id)
    val incompleteLiveData = milestonesViewModel.numOfGoalIncomplete(goal.id)

    result.addSource(totalLiveData) { total ->
        if (total != null) {
            val complete = completeLiveData.value ?: 0
            val incomplete = incompleteLiveData.value ?: 0
            if (total > 0) { // Ensure there is no division by zero
                val entries = listOf(
                    PieChartEntry(
                        label = "Complete",
                        color = Color.Green,
                        percentage = (complete.toFloat() / total.toFloat())
                    ),
                    PieChartEntry(
                        label = "Incomplete",
                        color = Color.Red,
                        percentage = (incomplete.toFloat() / total.toFloat())
                    )
                )
                result.value = entries
            }
        }
    }

    result.addSource(completeLiveData) { complete ->
        val total = totalLiveData.value ?: 0
        val incomplete = incompleteLiveData.value ?: 0
        if (total > 0) {
            val entries = listOf(
                PieChartEntry(
                    label = "Complete",
                    color = Color.Green,
                    percentage = (complete.toFloat() / total.toFloat())
                ),
                PieChartEntry(
                    label = "Incomplete",
                    color = Color.Red,
                    percentage = (incomplete.toFloat() / total.toFloat())
                )
            )
            result.value = entries
        }
    }

    result.addSource(incompleteLiveData) { incomplete ->
        val total = totalLiveData.value ?: 0
        val complete = completeLiveData.value ?: 0
        if (total > 0) {
            val entries = listOf(
                PieChartEntry(
                    label = "Complete",
                    color = Color.Green,
                    percentage = (complete.toFloat() / total.toFloat())
                ),
                PieChartEntry(
                    label = "Incomplete",
                    color = Color.Red,
                    percentage = (incomplete.toFloat() / total.toFloat())
                )
            )
            result.value = entries
        }
    }

    return result
}

fun calculateStartAngles(entries: List<PieChartEntry>) : List<Float>{
    var totalPercentage = 0f
    return entries.map { entry ->
        val startAngle = totalPercentage * 360
        totalPercentage += entry.percentage
        startAngle
    }
}

@Composable
fun PieChart(entries: List<PieChartEntry>, full: Boolean) {
    Canvas(modifier = Modifier.fillMaxSize()
        .padding(4.dp)) {
        val startAngles = calculateStartAngles(entries)
        val gap = 0.5f // Gap in degrees
        entries.forEachIndexed { index, entry ->
            drawArc(
                color = entry.color,
                startAngle = startAngles[index]+ gap /2,
                sweepAngle = entry.percentage * 360f,
                useCenter = true,
                topLeft = Offset.Zero,
                size = this.size
            )
            // Draw text labels
            if ((entry.percentage > 0) && (full)){
                val textRadius = size.minDimension / 2 * 0.5f // Adjust radius for label positioning
                val textAngle = Math.toRadians((startAngles[index] + (entry.percentage * 360f) / 2).toDouble()).toFloat()
                val textX = center.x + textRadius * cos(textAngle)
                val textY = center.y + textRadius * sin(textAngle)
                drawContext.canvas.nativeCanvas.drawText(
                    entry.label,
                    textX,
                    textY,
                    Paint().apply {
                        textSize = 28.sp.toPx() // Set text size
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

@Composable
fun Legend(entries: List<PieChartEntry>) {
    Row {
        entries.forEach { entry ->
            if (entry.percentage > 0){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(color = entry.color, shape = CircleShape)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = entry.label)
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

