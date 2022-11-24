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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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

            GreetingSection("MoneyApp")

            TotalSection(totalAmount, currency)

            Spacer(modifier = Modifier.height(10.dp))

            GraphSection()

            Spacer(modifier = Modifier.height(10.dp))

            ListSection(listTransaction = listTransaction, 7, "My last transactions", false)
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
    ElevatedCard(modifier = Modifier.clickable {  }
        .padding(top = 0.dp, bottom = 0.dp, start = 10.dp, end = 10.dp),
        shape = RoundedCornerShape(20.dp),
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
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .clickable { }
        .padding(top = 5.dp, bottom = 0.dp, start = 10.dp, end = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ){
        Row(modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){
            Surface(Modifier.padding(5.dp)) {
                Image(painter = painterResource(R.drawable.logo_app_mobile),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListSection(listTransaction: List<TransactionUiState>, indexMax : Int, textTransaction : String, isScrolling : Boolean
) {
    ElevatedCard(modifier = Modifier.clickable {  }
        .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = 5.dp, end = 5.dp, bottom = 7.5.dp),
            modifier = Modifier.scrollEnabled(
                enabled = isScrolling, //provide a mutable state boolean here
            )
        ) {

            stickyHeader {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(textTransaction, modifier = Modifier.padding(10.dp))
                }

            }

            items(listTransaction.size) { index ->
                if(index<indexMax){
                    Card(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
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

                            Spacer(modifier = Modifier.width(10.dp))

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

fun Modifier.scrollEnabled(
    enabled: Boolean,
) = nestedScroll(
    connection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset = if(enabled) Offset.Zero else available
    }
)

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