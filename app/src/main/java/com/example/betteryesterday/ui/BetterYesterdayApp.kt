package com.example.betteryesterday.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.betteryesterday.ui.theme.BetterYesterdayTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetterYesterdayApp(){
    val navController: NavHostController = rememberNavController();
    val context = LocalContext.current;

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text("Better Yesterday",
                        maxLines = 1,
                        overflow =  TextOverflow.Ellipsis)//Add a title to the app,
                },
                actions = {
                    IconButton(onClick = {
                        /* Open the user profile screen*/
                        navController.navigate(route = AppScreens.settings.name)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "This is the Settings page")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate(route = AppScreens.dashboard.name)}) {
                        Icon(
                            Icons.Filled.AccountBox,
                            contentDescription = "Dashboard page")
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.goals.name) }) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Goals page",
                        )
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.share.name)}) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Profile",
                        )
                    }
                })
        }){innerpadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.dashboard.name,
            modifier = Modifier.padding(innerpadding)
        ){
            composable(route = AppScreens.dashboard.name){
                DashboardScreen()
            }
            composable(route = AppScreens.settings.name){
                SettingsScreen()
            }
            composable(route = AppScreens.goals.name){
                GoalScreen()
            }
            composable(route = AppScreens.share.name){
                ShareScreen()
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun showApp(){
//    BetterYesterdayTheme {
//        BetterYesterdayApp()
//    }
//}


enum class AppScreens{
    /*TODO : need to create the names of the screens that will be on the webPage*/
    dashboard,//this represents the dashboard page
    goals,//this represents the goals page
    settings,//this represents the settings page
    share//this represents the share page.
}


