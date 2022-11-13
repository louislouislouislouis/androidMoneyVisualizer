package com.succiue.myapplication.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
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

val items = listOf(
    AppScreen.Home,
    AppScreen.Statistics,
    AppScreen.Goals,
    AppScreen.Profil
)

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
fun AppScreen(windowSize: WindowSizeClass, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    when(windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            compactWidthApp(navController = navController, modifier)
        }
        WindowWidthSizeClass.Medium -> {
            compactWidthApp(navController = navController, modifier)
        }
        WindowWidthSizeClass.Expanded -> {
            expandedWidthApp(navController = navController, modifier)
        }
        else -> {
            compactWidthApp(navController = navController)
        }
    }
}

@Composable
fun appBody(modifier : Modifier = Modifier,
            innerPadding: PaddingValues = PaddingValues(Dp(10.0f)),
            navController : NavHostController,
            onTitleChanged : (Int) -> Unit,
            onCanNavigateBackChanged : (Boolean) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.route,
        modifier.padding(innerPadding)
    ) {
        goalsListNavigation(
            navController = navController,
            modifier = modifier,
            onTitleChanged = onTitleChanged,
            onCanNavigateBackChange = onCanNavigateBackChanged
        )
        composable(AppScreen.Home.route) { Home(modifier, navController) }
        composable(AppScreen.Statistics.route) { Statistics(modifier, navController) }
        //composable(AppScreen.Goals.route) { Goals(modifier, navController) }
        composable(AppScreen.Profil.route) { Profil(modifier, navController) }

    }
}

@Composable
fun compactWidthApp(navController : NavHostController, modifier: Modifier = Modifier) {
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
        appBody(modifier, innerPadding, navController = navController, onTitleChanged = {}, onCanNavigateBackChanged = { canNavigateBack = it })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun navigationDrawerContent(navController: NavController,
                                    modifier: Modifier = Modifier,
                                    onNavigate: (AppScreen)->Unit){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colors.background)
            .padding(12.dp)
    ) {
        for (screen in items) {
            NavigationDrawerItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                label = {
                    Text(
                        text = stringResource(id = screen.title),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(id = screen.title)
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                    onNavigate(screen)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun expandedWidthApp(navController : NavHostController, modifier: Modifier = Modifier) {
    PermanentNavigationDrawer(drawerContent = {
        navigationDrawerContent(modifier = modifier, navController = navController, onNavigate = {})
    }) {
        appBody(navController = navController, onTitleChanged = {}, onCanNavigateBackChanged = {})
    }
}
