package com.succiue.myapplication.ui.screens.routes

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.screens.Screen
import com.succiue.myapplication.ui.screens.bodies.CreateGoalsBody
import com.succiue.myapplication.ui.screens.bodies.GoalsBody

enum class AddObjectiveRoutes(@StringRes val title: Int) {
    StartGoal(R.string.goalScreen),
    CreateObjective(R.string.createGoal),
}

fun NavGraphBuilder.createObjectiveNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    onTitleChanged: (Int) -> Unit,
    onCanNavigateBackChange: (Boolean) -> Unit,
) {
    navigation(startDestination = AddObjectiveRoutes.StartGoal.name, route = Screen.Goals.route) {
        composable(route = AddObjectiveRoutes.StartGoal.name) {
            onCanNavigateBackChange(false)
            GoalsBody(
                modifier,
                onNextButtonClicked = {
                    navController.navigate(AddObjectiveRoutes.CreateObjective.name)
                },
            )
        }
        composable(route = AddObjectiveRoutes.CreateObjective.name) {
            onCanNavigateBackChange(true)
            CreateGoalsBody(
                modifier,
                onNextButtonClicked = {
                    navController.navigate(AddObjectiveRoutes.StartGoal.name)
                },
            )
        }
    }
}