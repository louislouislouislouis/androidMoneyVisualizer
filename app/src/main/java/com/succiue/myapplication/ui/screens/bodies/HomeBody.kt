package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.succiue.myapplication.ui.viewmodels.TransactionUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeBody(
    navController: NavHostController,
    listTransaction: List<TransactionUiState>,
    name: String,
    totalAmount: String,
    currency: String,
    getBalanceAction: () -> Unit,
    getTransactionsAction: () -> Unit

) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(15) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        itemCount += 5
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
    Column() {
        Box(Modifier.pullRefresh(state)) {
            LazyColumn(Modifier.fillMaxSize()) {
                stickyHeader {
                    GreetingSection(name)
                }
                item {
                    TotalSection(totalAmount, currency)
                }
                item {
                    GraphSection()
                }
                item {
                    ListSection(
                        listTransaction = listTransaction,
                        maxIndex = 8
                    )
                }
            }
            PullRefreshIndicator(
                refreshing,
                state,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }

}


@Composable
fun GreetingSection(
    name: String = ""
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Surface() {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.logo_app_mobile),
            contentDescription = null,
            modifier = Modifier
                .size(34.dp),
            tint = Color.Unspecified
        )
    }
}


@Composable
fun TotalSection(
    amount: String,
    currency: String
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Votre Solde",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$amount $currency",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun GraphSection(
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            .clickable { },

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(Modifier.padding(5.dp)) {
                Image(
                    painter = painterResource(R.drawable.logo_app_mobile),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(200.dp)
                        .background(MaterialTheme.colorScheme.onPrimary),

                    )
            }

            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                LegendCard(text = "Votre solde")
                LegendCard(text = "Votre solde")
                LegendCard(text = "Votre solde")
                LegendCard(text = "Votre solde")
                LegendCard(text = "Votre solde")
                LegendCard(text = "Votre solde")
                LegendCard(text = "Votre solde")
            }
        }
    }
}

@Composable
fun LegendCard(text: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {

        }

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ListSection(
    listTransaction: List<TransactionUiState>, maxIndex: Int
) {
    var index = 0
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column() {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Last transactions", modifier = Modifier.padding(10.dp))
            }

            listTransaction.forEach() { transaction ->
                index += 1
                if (index < maxIndex) {
                    Card(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 10.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = transaction.amount,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (transaction.amount.startsWith("-")) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.inversePrimary
                                },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(80.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = transaction.merchant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            Text(
                                text = transaction.date,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            Text(
                                text = transaction.category,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }


                }
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
            listTransaction = listOf(
                TransactionUiState(
                    amount = "-6.33£",
                    merchant = "Uber",
                    category = "Travel",
                    date = "13/11/2022"
                ), TransactionUiState(
                    amount = "6.33£",
                    merchant = "Uber",
                    category = "Travel",
                    date = "13/11/2022"
                ), TransactionUiState(
                    amount = "-6.33£",
                    merchant = "Uber",
                    category = "Travel",
                    date = "13/11/2022"
                ), TransactionUiState(
                    amount = "-6.33£",
                    merchant = "Uber",
                    category = "Travel",
                    date = "13/11/2022"
                )
            ),
            getTransactionsAction = {},
            getBalanceAction = {}
        )

    }
}