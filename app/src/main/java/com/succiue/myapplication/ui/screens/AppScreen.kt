package com.succiue.myapplication.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.succiue.myapplication.R

sealed class AppScreen(val route: String, @StringRes val title: Int, val icon: Int) {
    object Home : AppScreen("home", R.string.home, R.drawable.home)
    object Statistics : AppScreen("statistics", R.string.statistics, R.drawable.data_management)
    object Goals : AppScreen("goals", R.string.goals, R.drawable.dollar)
    object Profil : AppScreen("profil", R.string.profil, R.drawable.user)
}

@Composable
fun TopBar(@StringRes title: Int,
           canNavigateBack: Boolean,
           modifier: Modifier = Modifier,
           navigateBack: ()->Unit) {
    TopAppBar(
        title = { Text(stringResource(id = title)) },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun BottomBar(modifier: Modifier = Modifier,
              navController: NavController,
              onMainScreenChanged: (AppScreen) -> Unit
            ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        AppScreen.Home,
        AppScreen.Statistics,
        AppScreen.Goals,
        AppScreen.Profil
    )

    BottomAppBar(modifier, backgroundColor = MaterialTheme.colors.primary) {
        items.forEach { screen ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colors.primaryVariant,
                unselectedContentColor = MaterialTheme.colors.secondary,
                icon = { Icon(painter= painterResource(id =screen.icon), contentDescription = null, modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    onMainScreenChanged(screen)
                }
            )
        }
    }
}

@Composable
fun AppScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = AppScreen.Home

    var currentTitle by remember {
        mutableStateOf(startDestination.title)
    }

    var canNavigateBack by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopBar(title = currentTitle, canNavigateBack = canNavigateBack) {

            }
        },
        bottomBar = {
            BottomBar(navController = navController) {
                currentTitle = it.title
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier.padding(innerPadding)
        ) {
            goalsListNavigation(navController = navController, modifier = Modifier, onTitleChanged = {}, onCanNavigateBackChange = { canNavigateBack = it })
            composable(AppScreen.Home.route) { Home(modifier, navController) }
            composable(AppScreen.Statistics.route) { Statistics(modifier, navController) }
            //composable(AppScreen.Goals.route) { Goals(modifier, navController) }
            composable(AppScreen.Profil.route) { Profil(modifier, navController) }

        }
    }
}