package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.theme.*
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
    onNextClick1: () -> Unit,
    onNextClick2: () -> Unit,

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
                    TotalSection(totalAmount, currency, onNextClick1)
                }
                item {
                    GraphSection(onNextClick2, listTransaction = listTransaction)
                }
                item {
                    ListSection(
                        listTransaction = listTransaction,
                        maxIndex = 7,
                        onNextClick1
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalSection(
    amount: String,
    currency: String,
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onNextButtonClicked
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
                style = MaterialTheme.typography.headlineLarge,
                color = MainColor_2
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphSection(
    onNextButtonClicked: () -> Unit,
    listTransaction: List<TransactionUiState>
) {
    val context = LocalContext.current
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onNextButtonClicked
    ) {
        MyPieChart(
            listTransaction = listTransaction,
            colors = listOf(
                MainColor_1,
                InterColor23_1,
                MainColor_2,
                InterColor31_1,
                MainColor_3,
                InterColor12_2,
                InterColor23_2,
                InterColor12_1,
                InterColor31_2
            )
        )
    }
}

@Composable
fun LegendCard(text: String, color: Color) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(15.dp)
                .padding(end = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = color
            )
        ) {

        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSection(
    listTransaction: List<TransactionUiState>,
    maxIndex: Int,
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    var index = 0
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onNextButtonClicked
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

                if (index < maxIndex) {
                    Card(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Column() {
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
                                        MainColor_3
                                    } else {
                                        MainColor_2
                                    },
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(80.dp)
                                )
                                Text(
                                    text = transaction.merchant,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(start = 10.dp)
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
                index += 1
            }
        }
    }
}