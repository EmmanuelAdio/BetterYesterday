package com.example.betteryesterday.ui

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset

import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.data.Milestones
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun GoalsDetailsScreen(
    navController: NavHostController,
    goalViewModel: GoalViewModel,
    goalIndex: Int?,
    milestonesViewModel: MilestoneViewModel
){
    val goal : LiveData<Goals>? = goalIndex?.let { goalViewModel.getGoal(it) }//this is good practice so if the goal index in the route is null the program handles ir
    val goalState = goal?.observeAsState() // This handles observing and gets the current state
    val goalData = goalState?.value // Extract the current goal, may be null

    val milestonesList : LiveData<List<Milestones>>? = goalIndex?.let { milestonesViewModel.getGoalMilestones(it) }
    val milestoneData = milestonesList?.observeAsState(initial = emptyList())?.value

    val context = LocalContext.current;
    Column {
        LazyColumn {
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    displayGoalInfo(goalData)
                    Spacer(modifier = Modifier.width(16.dp))
                    displayGoalPieChart(milestonesViewModel,goalData)
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
        MilestonesBox(milestoneData, milestonesViewModel, context)
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
fun displayGoalPieChart(milestonesViewModel: MilestoneViewModel, goal: Goals?) {
    Card(
        modifier = Modifier
            .height(150.dp) // Set a fixed height for the cards
            .fillMaxWidth() // Fill the remaining width
            .padding(8.dp) // Add padding around the card
    )  {
        Box(
            contentAlignment = Alignment.Center,
        ){
            var entries = goal?.let { getGoalPieChart(milestonesViewModel, it).observeAsState().value }
            if (entries != null) {
                PieChart(entries)
            }
        }
    }
}

@Composable
fun buttonRow(navController: NavHostController, modifier: Modifier, goal: Goals?) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =  modifier
    ) {
        Button(onClick = { navController.navigate(route = AppScreens.createMilestones.name + "/${goal?.id}") }) {
            Icon(Icons.Filled.Add, contentDescription = "Add More Tasks")
            Text(text = "New Milestone")
        }
        Button(onClick = {
            /* Define action (implicit intent) for when the user wants to save teh goal deadline to their calendar app.*/
            Log.v("INTENT CHECK", "start")

            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, "Deadline for Goal :${goal?.title}")
                putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    goal?.deadline?.let { convertDateToEpochMillis(it) })
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                    goal?.deadline?.let { convertDateToEpochMillis(it) })
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }

            Log.v("INTENT CHECK", "end")
        }) {
            Icon(Icons.Filled.DateRange, contentDescription = "Add To Calendar")
            Text(text = "Save To Calendar")
        }
    }
}

fun convertDateToEpochMillis(dateStr: String): Long {
    /*This function changes date into EpochMillis so it can be passed in the implicit intent to the user's calander app.*/
    // Adjust the pattern here to match "d/M/yyyy"
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    val date = LocalDate.parse(dateStr, formatter)
    return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}


@Composable
fun MilestonesBox(
    milestonesData: List<Milestones>?,
    milestonesViewModel: MilestoneViewModel,
    context: Context
) {
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
            listOfMilestones(milestonesData, milestonesViewModel, context)  // This will list the tasks in a LazyColumn
        }
    }
}

@Composable
fun listOfMilestones(
    milestones: List<Milestones>?,
    milestonesViewModel: MilestoneViewModel,
    context: Context
) {
    if (milestones.isNullOrEmpty()) {
        Text("No milestones available")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()  // Fill the width of the parent
            .padding(bottom = 40.dp)
    ) {
        items(milestones) {milestone ->
            milestoneCard(milestone, milestonesViewModel, context)
        }
    }
}


@Composable
fun milestoneCard(
    milestone: Milestones,
    milestonesViewModel: MilestoneViewModel,
    context: Context

) {
    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val interactionSource = remember{ MutableInteractionSource() }
    val density = LocalDensity.current

    val checkedState = remember { mutableStateOf(milestone.complete) }

    var showErrorDialog by rememberSaveable { mutableStateOf(false) }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Delete Milestone") },
            text = { Text("Are you sure you want to delete the Goal") },
            confirmButton = {
                Button(
                    onClick = {
                        Toast
                            .makeText(context, "Milestone deleted", Toast.LENGTH_SHORT)
                            .show()
                        milestonesViewModel.deleteMilestone(milestone)
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) { Text("Cancel") }
            }
        )
    }

    Card(
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth()
            .indication(interactionSource, LocalIndication.current)
            .padding(8.dp)
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onPress = { // This handles tap without the need for clickable
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)// this interaction source stuff makes it possible for the interaction animation to appear when the user presses the goal card.
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    },
                    onLongPress = {
                        isContextMenuVisible = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    }
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(milestone.summary, style = MaterialTheme.typography.titleMedium)
                Text("Due: ${milestone.deadline}", style = MaterialTheme.typography.labelMedium)
            }
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    if (it) {
                        milestone.complete = true
                        milestonesViewModel.updateMilestone(milestone)
                        Toast.makeText(context, "Marked Complete!", Toast.LENGTH_SHORT).show()
                    } else {
                        milestone.complete = false
                        milestonesViewModel.updateMilestone(milestone)
                        Toast.makeText(context, "Marked Incomplete!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )

        ){
            DropdownMenuItem(
                text = { Text(text = "Mark As Incomplete") },
                onClick = {
                    checkedState.value = false
                    milestone.complete = false
                    milestonesViewModel.updateMilestone(milestone)
                    Toast.makeText(context, "Marked Incomplete!", Toast.LENGTH_SHORT).show()

                    isContextMenuVisible = false
                })
            DropdownMenuItem(
                text = { Text(text = "Mark As Complete") },
                onClick = {
                    milestone.complete = true
                    milestonesViewModel.updateMilestone(milestone)
                    Toast.makeText(context, "Marked Complete!", Toast.LENGTH_SHORT).show()
                    checkedState.value = true

                    isContextMenuVisible = false
                })
            DropdownMenuItem(
                text = { Text(text = "Delete") },
                onClick = {
                    showErrorDialog = true

                    isContextMenuVisible = false
                })
        }
    }
}