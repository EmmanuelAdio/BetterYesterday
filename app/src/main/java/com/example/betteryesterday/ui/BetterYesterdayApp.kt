package com.example.betteryesterday.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel


enum class AppScreens{
    /*TODO : need to create the names of the screens that will be on the webPage*/
    dashboard,//this represents the dashboard page
    goals,//this represents the goals page
    settings,//this represents the settings page
    goalMilestones,//this represents the page the shows further detail about a goal and all its milestones.
    goalShare,//this represents the share page.
    createGoal,
    createMilestones,
}
data class BottomNavigationItems(
    val title : String,
    val selectedIcon : ImageVector,// this is the icon that we want to display when we are currently on the screen
    val unSelectedIcon : ImageVector, //this is the icon that we want to display when the screen is not being selected.
    val hasNews :  Boolean,// just shows if there is an update on the screen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetterYesterdayApp(){
    val navController: NavHostController = rememberNavController();
    val context = LocalContext.current;

    val goalViewModel : GoalViewModel = viewModel()
    val milestonesViewModel : MilestoneViewModel = viewModel()

    val items = listOf(
        BottomNavigationItems (
            title = AppScreens.dashboard.name,
            selectedIcon = Icons.Filled.AccountBox,
            unSelectedIcon = Icons.Outlined.AccountBox,
            hasNews = false
            ),
        BottomNavigationItems (
            title = AppScreens.goals.name,
            selectedIcon = Icons.Filled.Info,
            unSelectedIcon = Icons.Outlined.Info,
            hasNews = false
        ),
        BottomNavigationItems (
            title = AppScreens.settings.name,
            selectedIcon = Icons.Filled.Settings,
            unSelectedIcon = Icons.Outlined.Settings,
            hasNews = false
        ),

    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                /*This is where i will put the ICON for the Better Yesterday app*/
                title = {
                    Text("Better Yesterday",
                        maxLines = 1,
                        overflow =  TextOverflow.Ellipsis)//Add a title to the app,
                }
            )
        },
        bottomBar = {
           NavigationBar{
               items.forEachIndexed { index, item ->
                   NavigationBarItem(
                       selected = selectedItemIndex == index,
                       onClick = {
                                 selectedItemIndex = index
                           navController.navigate(route = item.title)
                       },
                       label = {
                               Text(text = item.title)
                       },
                       //this makes it so that the label only shows up when teh user is currently on the page
                       //alwaysShowLabel = false,
                       icon = {
                           BadgedBox(
                               badge = {
                                   if(item.hasNews){
                                       Badge()
                                   }
                               }) {
                               Icon(
                                   imageVector = if (index == selectedItemIndex){
                                   item.selectedIcon
                               } else {
                                   item.unSelectedIcon
                               },
                                   contentDescription = item.title)
                           }
                       })
               }
           }
        },
        floatingActionButton = {
            if (currentRoute(navController) == AppScreens.goals.name) {
                FloatingActionButton(onClick = {
                    // Handle FAB click action here
                    navController.navigate(route = AppScreens.createGoal.name)
                },
                ) {
                    // This is what will be displayed inside the FAB
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            } else if (currentRoute(navController) == AppScreens.goalMilestones.name + "/{id}"){
                FloatingActionButton(onClick = {
                    // Handle FAB click action here
                    navController.popBackStack()
                },
                ) {
                    // This is what will be displayed inside the FAB
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        ){innerpadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.dashboard.name,
            modifier = Modifier.padding(innerpadding)
        ){
            composable(route = AppScreens.dashboard.name){
                DashboardScreen(goalViewModel, milestonesViewModel)
            }
            composable(route = AppScreens.settings.name){
                SettingsScreen(navController)
            }
            composable(route = AppScreens.goals.name){
                GoalScreen(goalViewModel, navController)
            }

            composable(route = AppScreens.goalMilestones.name + "/{id}",
                arguments = listOf(
                    navArgument(name = "id") {
                        type = NavType.IntType //extract the argument
                    }
                )
            ) {id ->
                GoalsDetailsScreen(
                    navController,
                    goalViewModel,
                    id.arguments?.getInt("id"),//passing the goalID
                    milestonesViewModel
                )
            }

            composable(route = AppScreens.createMilestones.name + "/{id}",
                arguments = listOf(
                    navArgument(name = "id"){
                        type = NavType.IntType//extract the argument as it is passed through the navigation route
                    }
                )
            ){id ->
                NewMileStone(
                    navController,
                    id.arguments?.getInt("id"),//pass the goal ID to the New Milestone creation page
                    milestonesViewModel
                )
            }

            composable(route = AppScreens.createGoal.name){
                NewGoalScreen(navController,goalViewModel)
            }

            composable(route = AppScreens.goalShare.name){
                ShareScreen()
            }



        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}