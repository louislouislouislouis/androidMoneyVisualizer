package com.succiue.myapplication.ui.screens.routes

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.screens.Screen
import com.succiue.myapplication.ui.screens.bodies.EditProfileBody
import com.succiue.myapplication.ui.screens.bodies.ProfileBody
import com.succiue.myapplication.ui.viewmodels.LoginViewModel

enum class AddProfileRoutes(@StringRes val title: Int) {
    StartProfile(R.string.profileScreen),
    EditProfile(R.string.editProfile),
}

fun NavGraphBuilder.editProfileNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    onTitleChanged: (Int) -> Unit,
    onCanNavigateBackChange: (Boolean) -> Unit,
    viewModel: LoginViewModel, name: String,
) {
    navigation(
        startDestination = AddProfileRoutes.StartProfile.name,
        route = Screen.Profile.route
    ) {
        composable(route = AddProfileRoutes.StartProfile.name) {
            onCanNavigateBackChange(false)
            ProfileBody(
                modifier, onNextButtonClicked = {
                    navController.navigate(AddProfileRoutes.EditProfile.name)
                },
                viewModel = viewModel,
                name = name
            )
        }
        composable(route = AddProfileRoutes.EditProfile.name) {
            onCanNavigateBackChange(true)
            EditProfileBody(
                modifier,
                onNextButtonClicked = {
                    navController.navigate(AddProfileRoutes.StartProfile.name)
                },
                viewModel = viewModel,
                name = name
            )
        }
    }
}