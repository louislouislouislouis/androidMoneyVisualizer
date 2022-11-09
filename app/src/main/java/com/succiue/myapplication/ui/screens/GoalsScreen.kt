package com.succiue.myapplication.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.viewmodels.GoalViewModel


enum class AddGoalRoutes(@StringRes val title: Int) {
    Start(R.string.create_goal),
    SelectCategory(R.string.goal_category),
    Summary(R.string.summary)
}

fun NavGraphBuilder.goalsListNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GoalViewModel = GoalViewModel(),
    onTitleChanged : (Int) -> Unit,
    onCanNavigateBackChange: (Boolean) -> Unit
){
    navigation(startDestination = AddGoalRoutes.Start.name, route = AppScreen.Goals.route){
        composable(route = AddGoalRoutes.Start.name) {
            onCanNavigateBackChange(false)
            StartCreateGoalScreen(modifier, onNextButtonClicked = {
                navController.navigate(AddGoalRoutes.SelectCategory.name)
            })
        }
        composable(route = AddGoalRoutes.SelectCategory.name) {
            onCanNavigateBackChange(false)
            SelectCategoriesLanScreen(modifier, onNextButtonClicked = {
                navController.navigate(AddGoalRoutes.Summary.name)
            })
        }
        composable(route = AddGoalRoutes.Summary.name) {
            onCanNavigateBackChange(false)
            SummaryCreateGoalScreen(modifier, onNextButtonClicked = {
                navController.popBackStack(AddGoalRoutes.Start.name, inclusive = false)
            })
        }
    }
}

@Composable
fun Goals(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text("Share my Goals")
        }
    }
}