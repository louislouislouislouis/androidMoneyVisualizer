package com.succiue.myapplication.ui.screens

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.succiue.myapplication.MainActivity
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.screens.bodies.*
import com.succiue.myapplication.ui.screens.routes.createObjectiveNavigation
import com.succiue.myapplication.ui.screens.routes.editProfileNavigation
import com.succiue.myapplication.ui.viewmodels.LoginViewModel
import com.succiue.myapplication.ui.viewmodels.MainViewModel
import com.succiue.myapplication.ui.viewmodels.ObjectifViewModel

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: Int,
) {
    object Home : Screen("home", R.string.home, R.drawable.home)
    object Stats : Screen("stats", R.string.stats, R.drawable.data_management)
    object Goals : Screen("goals", R.string.goals, R.drawable.dollar)
    object Profile : Screen("profile", R.string.profile, R.drawable.user)
    object Depenses : Screen("depenses", R.string.stats, R.drawable.data_management)
}

val MainScreens = listOf(
    Screen.Home,
    Screen.Stats,
    Screen.Goals,
    Screen.Profile,
)


fun Context.findActivity(): MainActivity? = when (this) {
    is MainActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyVisualizerHome(
    viewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    objectifViewModel: ObjectifViewModel,
    windowsSize: WindowSizeClass,
    modifier: Modifier = Modifier,
) {

    // State Variable
    var uiState = viewModel.uiState
    val navController = rememberNavController()

    // Loading Screen
    if (uiState.loading) {
        Text(text = "Loading")
    } else {
        if (uiState.needAnAccess) {
            NoAccountScreen(viewModel)
        } else {
            /*
               * Show Normal Screen Only if no loading or don't need Connect bank
               *
             */
            when (windowsSize.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    AppCompactWidthNavigation(
                        modifier,
                        navController = navController,
                        viewModel = viewModel,
                        objectifViewModel = objectifViewModel,
                        loginViewModel = loginViewModel
                    )
                }
                WindowWidthSizeClass.Medium -> {
                    AppCompactWidthNavigation(
                        modifier,
                        navController = navController,
                        viewModel = viewModel,
                        objectifViewModel = objectifViewModel,
                        loginViewModel = loginViewModel
                    )
                }
                WindowWidthSizeClass.Expanded -> {
                    AppExpandedWidthNavigation(
                        modifier,
                        navController = navController,
                        viewModel = viewModel,
                        objectifViewModel = objectifViewModel,
                        loginViewModel = loginViewModel
                    )
                }
                else -> {
                    AppCompactWidthNavigation(
                        modifier,
                        navController = navController,
                        viewModel = viewModel,
                        objectifViewModel = objectifViewModel,
                        loginViewModel = loginViewModel
                    )
                }
            }
        }
    }
}


// Big Screen Screen
@ExperimentalMaterial3Api
@Composable
fun AppExpandedWidthNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    objectifViewModel: ObjectifViewModel,
    viewModel: MainViewModel,
    loginViewModel: LoginViewModel
) {
    val navigationDrawerContentDescription = "TEST"
    PermanentNavigationDrawer(
        modifier = Modifier.testTag(navigationDrawerContentDescription),
        drawerContent = {
            NavigationDrawerContent(
                modifier = modifier,
                navController = navController,
                onNavigate = {
                })
        }
    ) {
        AppBody(
            navController = navController,
            modifier = modifier,
            onTitleChanged = {
            },
            onCanNavigateBackChanged = {
            },
            objectifViewModel = objectifViewModel,
            viewModel = viewModel,
            loginViewModel = loginViewModel
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationDrawerContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        for (screen in MainScreens) {
            NavigationDrawerItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                label = {
                    Text(
                        text = stringResource(id = screen.resourceId),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp)
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
                        // reelecting the same item
                        launchSingleTop = true
                        // Restore state when reelecting a previously selected item
                        restoreState = true
                    }
                    onNavigate(screen)
                }
            )
        }
    }
}

/*
 * The normal Screen for normal user
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCompactWidthNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    objectifViewModel: ObjectifViewModel,
    viewModel: MainViewModel,
    loginViewModel: LoginViewModel,
) {

    /**
     * State Var
     */
    var currentScreenTitle by remember {
        mutableStateOf(Screen.Home.resourceId)
    }
    var canNavigateBack by remember {
        mutableStateOf(false)
    }



    Scaffold(
        /*
        topBar = {
            TopBar(
                title = currentScreenTitle,
                canNavigateBack = canNavigateBack,
                modifier,

            ) {
                navController.navigateUp()
            }

        },
         */

        bottomBar = {
            BottomBar(navController, modifier) {
                currentScreenTitle = it.resourceId
            }
        }) { innerPadding ->
        AppBody(
            innerPadding = innerPadding,
            navController = navController,
            modifier = modifier,
            onTitleChanged = {
                currentScreenTitle = it
            },
            onCanNavigateBackChanged = {
                canNavigateBack = it
            },
            viewModel = viewModel,
            loginViewModel = loginViewModel,
            objectifViewModel = objectifViewModel
        )

    }
}

@Composable
fun AppBody(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(Dp(10.0f)),
    navController: NavHostController,
    onTitleChanged: (Int) -> Unit,
    onCanNavigateBackChanged: (Boolean) -> Unit,
    viewModel: MainViewModel,
    objectifViewModel: ObjectifViewModel,
    loginViewModel: LoginViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            HomeBody(
                navController,
                listTransaction = viewModel.uiState.transactionList,
                name = viewModel.uiState.name,
                totalAmount = viewModel.uiState.totalAmount,
                currency = viewModel.uiState.currency,
                onNextClick1 = {
                    navController.navigate(Screen.Depenses.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNextClick2 = {
                    navController.navigate(Screen.Stats.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Screen.Stats.route) {
            StatsBody(
                navController,
                listTransaction = viewModel.uiState.transactionList,
                onNextButtonClicked = {},
                onNextButtonClicked2 = {
                    navController.navigate(Screen.Depenses.route) {
                        popUpTo(Screen.Stats.route) { inclusive = true }
                    }
                },
            )
        }
        createObjectiveNavigation(
            objectifViewModel = objectifViewModel,
            navController = navController,
            modifier = modifier,
            onTitleChanged = onTitleChanged,
            onCanNavigateBackChange = onCanNavigateBackChanged
        )
        composable(Screen.Depenses.route) {
            DepensesBody(
                navController,
                listTransaction = viewModel.uiState.transactionList,
                onNextButtonClicked = {
                    navController.navigate(Screen.Stats.route) {
                        popUpTo(Screen.Depenses.route) { inclusive = true }
                    }
                },
                onNextButtonClicked2 = {}
            )
        }
        editProfileNavigation(
            navController = navController,
            modifier,
            onTitleChanged = onTitleChanged,
            onCanNavigateBackChange = onCanNavigateBackChanged,
            loginViewModel, name = viewModel.uiState.name
        )
    }
}


@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit
) {
    BottomNavigation(
        modifier,
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        MainScreens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp)
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.secondary,
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reelecting the same item
                        launchSingleTop = true
                        // Restore state when reelecting a previously selected item
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
fun TopBar(
    @StringRes title: Int,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val contextForToast = LocalContext.current.applicationContext

    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }
        },
        actions = {
            TopAppBarActionButton(
                imageVector = Icons.Outlined.Search,
                description = "Search"
            ) {
                Toast.makeText(contextForToast, "Search Click", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
}

@Composable
fun TopAppBarActionButton(
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.logo_app_mobile),
            contentDescription = null,
            modifier = Modifier
                .size(51.dp),
            tint = Color.Unspecified
        )
    }
}