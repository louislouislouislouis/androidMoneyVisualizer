package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalFoundationApi::class)
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
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column() {
            LazyColumn(
                contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 7.5.dp),
            ) {
                item {
                    GreetingSection(name)
                }
                item {
                    TotalSection(totalAmount, currency)
                }
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Text("My last Transactions", modifier = Modifier.padding(16.dp))
                    }

                }
                items(listTransaction.size) { index ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)

                        ) {
                            Text(
                                text = listTransaction[index].amount,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (listTransaction[index].amount.startsWith("-")) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.inversePrimary
                                },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(80.dp)
                            )
                            Text(
                                text = listTransaction[index].merchant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            Text(
                                text = listTransaction[index].date,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            Text(
                                text = listTransaction[index].category,
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
                item {
                    GreetingSection(name)
                }
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