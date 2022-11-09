package com.succiue.myapplication.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.fragment.CustomButtom
import com.succiue.myapplication.ui.fragment.valueToken
import com.succiue.myapplication.ui.viewmodels.MainViewController


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MoneyVizualizerHome(viewModel: MainViewController, navController: NavController) {
    val ctx = LocalContext.current
    if (viewModel.isLoading.value) {
        Text(text = "LOADING")
    } else {
        if (viewModel.needAnAccess.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Hello" + viewModel.user.displayName)
                Text(text = "U DO NOT HAVE AN ACCOUNT TO THE BANK! FUCK")
                valueToken(
                    text1 = viewModel.linkToken.value,
                    text2 = viewModel.publicToken.value,
                    text3 = viewModel.accessToken.value
                )
                CustomButtom(stringResource(R.string.welcoming_user)) {
                    viewModel.linkAccount(ctx)
                }
                CustomButtom(stringResource(R.string.connectToGoogle)) {
                    viewModel.connectToGoogle(ctx)
                }
            }
        } else {
            Column() {
                Text(text = "U ALREADY HAVE AN ACCOUNT TO THE BANK! AMAZING")
                CustomButtom(stringResource(R.string.connectToGoogle)) {
                    viewModel.connectToGoogle(ctx)
                }
            }

        }
    }
}

@Composable
fun Home(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text("Share my Home")
        }
    }
}
