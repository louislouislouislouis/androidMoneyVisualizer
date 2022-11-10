package com.succiue.myapplication.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold () {
        Column() {
            Surface1()
            Surface2()
        }

    }
}

@Composable
fun Surface1(){
    val context = LocalContext.current

    Surface (
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp,top = 16.dp, bottom = 0.dp)
    ){
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = CreateMutableInteractionSource(),
                    indication = CreateIndication(color = MaterialTheme.colors.onPrimary),
                    onClick = {
                        Toast
                            .makeText(context, "Go to Dépenses", Toast.LENGTH_SHORT)
                            .show()
                    }

                )
                .padding(15.dp)
        ) {


            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color =  MaterialTheme.colors.onPrimary)
                    ){
                        append("Votre solde")
                    }
                }
            )

            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold, color =  MaterialTheme.colors.onPrimary)
                    ){
                        append("$1287,93")
                    }
                }
            )
        }

    }
}

@Composable
fun Surface2(){
    val context = LocalContext.current

    Surface (
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = CreateMutableInteractionSource(),
                indication = CreateIndication(color = MaterialTheme.colors.onPrimary),
                onClick = {
                    Toast
                        .makeText(context, "Go to Stats", Toast.LENGTH_SHORT)
                        .show()
                }

            )
            .padding(start = 16.dp, end = 16.dp,top = 16.dp, bottom = 0.dp)
    ){
        Row(
            modifier = Modifier
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(painter = painterResource(id = R.drawable.red_house),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.
                padding(start = 10.dp)
                .size(130.dp)
                )
            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            Column(
                modifier = Modifier
                    .padding(15.dp)
            ) {


                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold, color =  MaterialTheme.colors.onPrimary)
                        ){
                            append("Répartition des dépenses")
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color =  MaterialTheme.colors.onPrimary)
                        ){
                            append("On a beaucoup de dépenses là")
                        }
                    }
                )
            }

        }

    }
}

@Composable
fun CreateMutableInteractionSource(): MutableInteractionSource = remember{
    MutableInteractionSource()
}

@Composable
fun CreateIndication(bounded:Boolean = true, color: Color = MaterialTheme.colors.onPrimary) = rememberRipple(bounded = bounded, color = color)