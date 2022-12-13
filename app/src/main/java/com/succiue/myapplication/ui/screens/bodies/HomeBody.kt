package com.succiue.myapplication.ui.screens.bodies

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.theme.*
import com.succiue.myapplication.ui.viewmodels.TransactionUiState
import com.succiue.myapplication.utils.getMaxValue
import com.succiue.myapplication.utils.getSpendingByMonth
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarPieSection(
    listTransaction: List<TransactionUiState>,
    onNextButtonClicked: () -> Unit
) {
    var data: MutableMap<String, Float> = mutableMapOf(
        stringResource(R.string.January) to 0.0f,
        stringResource(R.string.February) to 0.0f,
        stringResource(R.string.March) to 0.0f,
        stringResource(R.string.April) to 0.0f,
        stringResource(R.string.May) to 0.0f,
        stringResource(R.string.June) to 0.0f,
        stringResource(R.string.July) to 0.0f,
        stringResource(R.string.August) to 0.0f,
        stringResource(R.string.September) to 0.0f,
        stringResource(R.string.October) to 0.0f,
        stringResource(R.string.November) to 0.0f,
        stringResource(R.string.December) to 0.0f,
    )

    getSpendingByMonth(listTransaction, data)
    var max_value = getMaxValue(data.maxOf { it.value })

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
        BarChart(
            data = data,
            max_value = max_value
        )
    }
}

@Composable
fun BarChart(
    data: MutableMap<String, Float>,
    max_value: Int
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                contentAlignment = Alignment.BottomCenter
            ) {

                // scale
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = max_value.toString())
                    Spacer(modifier = Modifier.fillMaxHeight())
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = (max_value / 2).toString())
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(Color.Black)
            )

            // graph
            data.values.forEach {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .width(12.dp)
                        .fillMaxHeight(it / max_value)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    it.toString(),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )

        Row(
            modifier = Modifier
                .padding(start = 64.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            data.keys.forEach {
                Text(
                    modifier = Modifier.width(12.dp),
                    text = it.substring(0, 1),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}