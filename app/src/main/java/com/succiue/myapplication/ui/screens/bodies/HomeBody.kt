package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.theme.MyApplicationTheme

@Composable
fun HomeBody(
    navController: NavHostController,
    name: String,
    totalAmount: String,
    currency: String,
    getBalanceAction: () -> Unit,
    getTransactionsAction: () -> Unit

) {
    val list = (1..10).map { it.toString() }
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {

        Column() {
            GreetingSection(name)
            TotalSection(totalAmount, currency)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(screenHeight / 3),
                contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(list.size) { index ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = list[index],
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            Text("I am the Home Body")
            Button(onClick = { getBalanceAction() }) {
                Text(text = "GET BALANCE (IN LOGCAT)")
            }
            Button(onClick = { getTransactionsAction() }) {
                Text(text = "GET TRANSACTIONS (IN LOGCAT)")
            }
        }
    }
}


@Composable
fun GreetingSection(
    name: String = "Philipp"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hi, $name",
                style = MaterialTheme.typography.headlineMedium

            )
            Text(
                text = "We wish you have a good day!",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.logo_app_mobile),
            contentDescription = null,
            modifier = Modifier
                .size(51.dp),
            tint = Color.Unspecified
        )
    }
}


@Composable
fun TotalSection(
    amount: String,
    currency: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Card(modifier = Modifier.padding(15.dp)) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$amount $currency",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Seems quite rich today !",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        

    }
}

@Preview(showSystemUi = true)
@Composable
fun ComposablePreview() {
    MyApplicationTheme() {
        HomeBody(
            rememberNavController(),
            name = "Louis LOMBARD",
            totalAmount = "1234.50",
            currency = "$",
            {},
            {})

    }
}