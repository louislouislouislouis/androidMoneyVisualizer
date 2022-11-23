package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.succiue.myapplication.ui.screens.Screen.Goals.icon
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
                contentPadding = PaddingValues(start = 5.dp, end = 5.dp, bottom = 7.5.dp),
            ) {
                item {
                    GreetingSection(name)
                }
                item {
                    TotalSection(totalAmount, currency)
                }
                item {
                    GraphSection()
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
                            .padding(5.dp)
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
            }
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
            .padding(15.dp)
    ) {
        Surface(){
            Text(
                text = "MoneyApp",
                style = MaterialTheme.typography.headlineMedium
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        ElevatedCard(modifier = Modifier.padding(5.dp), shape = RoundedCornerShape(20.dp)
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
}

@Composable
fun GraphSection(
) {
    ElevatedCard(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .clickable {  },
        shape = RoundedCornerShape(20.dp)
    ){
        Row(modifier = Modifier.padding(10.dp)){
            Surface(Modifier.padding(5.dp)) {
                Image(painter = painterResource(R.drawable.logo_app_mobile),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(percent = 10))
                )
            }
            
            Column(
                modifier = Modifier.padding(15.dp).fillMaxHeight().fillMaxWidth().wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Votre Solde",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Votre Solde",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Votre Solde",
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