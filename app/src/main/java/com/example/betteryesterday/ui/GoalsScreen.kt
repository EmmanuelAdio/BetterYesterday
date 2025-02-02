package com.example.betteryesterday.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.data.Milestones
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel

@Composable
fun GoalScreen(
    goalViewModel: GoalViewModel,
    navController: NavHostController
){
    /* This is the dashboard screen composable */
    //place holder add show that this is the dashboard screen.
    val context = LocalContext.current;

    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        // Indication of long press
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 8.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(4.dp)
        ) {
            Text(
                text = "TIP : Long press on goal card for more options",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        ListOfGoals(navController, goalViewModel, context)
        Spacer(modifier = Modifier.height(16.dp))

    }

}

@Composable
fun ListOfGoals(navController: NavHostController, goalViewModel: GoalViewModel, context: Context){
    //this is where we will be displaying all the goals that the user has.
    val goals = goalViewModel.allGoals.observeAsState(initial = emptyList()).value

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
        ) {
        items(goals) { goal ->
            GoalCard(navController, goal, context, goalViewModel)
        }
    }
}

@Composable
fun GoalCard(
    navController: NavHostController,
    goal: Goals,
    context: Context,
    goalViewModel: GoalViewModel
) {
    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val interactionSource = remember{ MutableInteractionSource() }
    val density = LocalDensity.current

    var showErrorDialog by rememberSaveable { mutableStateOf(false) }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Delete Goal") },
            text = { Text("Are you sure you want to delete the Goal") },
            confirmButton = {
                Button(
                    onClick = {
                        Toast
                            .makeText(context, "Goal Deleted", Toast.LENGTH_SHORT)
                            .show()
                        goalViewModel.deleteGoal(goal)
                        showErrorDialog = false
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

    Card (
        modifier = Modifier
            .height(100.dp) // Set a fixed height for the cards
            .fillMaxWidth() // Fill the remaining width
            .indication(interactionSource, LocalIndication.current)
            .padding(8.dp)// Add padding around the card
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

                        if (!isContextMenuVisible) { // Only trigger if the context menu isn't visible
                            Toast
                                .makeText(context, "Goal clicked!", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(route = AppScreens.goalMilestones.name + "/${goal.id}")
                        }
                    },
                    onLongPress = {
                        isContextMenuVisible = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    }
                )
            }
    ) {
        Row{
            //this will be where the pie chart for the goal will be placed.
            val milestonesViewModel : MilestoneViewModel = viewModel()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            ) {
                var entries = getGoalPieChart(milestonesViewModel,goal).observeAsState().value
                if (entries != null) {
                    PieChart(entries)
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = goal.deadline,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }


        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )

        ){
            DropdownMenuItem(
                text = { Text(text = "Details") },
                onClick = {
                    navController.navigate(route = AppScreens.goalMilestones.name + "/${goal.id}")
                    isContextMenuVisible = false
                })
            DropdownMenuItem(
                text = { Text(text = "Share") },
                onClick = {
                    navController.navigate(route = AppScreens.goalShare.name + "/${goal.id}")
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



