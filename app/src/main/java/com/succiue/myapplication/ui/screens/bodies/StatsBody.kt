package com.succiue.myapplication.ui.screens.bodies

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.succiue.myapplication.ui.theme.*
import com.succiue.myapplication.ui.viewmodels.TransactionUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun StatsBody(
    navController: NavHostController,
    listTransaction: List<TransactionUiState>,
    onNextButtonClicked: () -> Unit,
    onNextButtonClicked2: () -> Unit
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        Column() {
            LazyColumn(Modifier.fillMaxSize()) {
                stickyHeader {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        GreetingSection("Mes dépenses")

                        SelectButton(

                            false,
                            onNextButtonClicked,
                            onNextButtonClicked2
                        )
                    }
                }
                item {
                    GraphSection(onNextButtonClicked = {}, listTransaction = listTransaction)
                }
                item {
                    GraphSection(onNextButtonClicked = {}, listTransaction = listTransaction)
                }
                item {
                    GraphSection(onNextButtonClicked = {}, listTransaction = listTransaction)
                }
                item {
                    GraphSection(onNextButtonClicked = {}, listTransaction = listTransaction)
                }
                item {
                    GraphSection(onNextButtonClicked = {}, listTransaction = listTransaction)
                }

            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun SelectButton(
    selected: Boolean,
    onNextButtonClicked: () -> Unit,
    onNextButtonClicked2: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        SubcomposeRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 0.dp, start = 15.dp, end = 15.dp),
            paddingBetween = 20.dp
        ) {
            Button(
                border = BorderStroke(1.dp, if (selected) Color.Gray else Color.Blue),
                onClick = onNextButtonClicked,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Gray,
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            {
                Text(text = "Graphiques")
            }

            Button(
                border = BorderStroke(1.dp, if (!selected) Color.Gray else Color.Blue),
                onClick = onNextButtonClicked2,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Gray,
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Dépenses")
            }

        }
    }
}

//Function to create Button Row
@Composable
private fun SubcomposeRow(
    modifier: Modifier = Modifier,
    paddingBetween: Dp = 0.dp,
    content: @Composable () -> Unit = {},
) {
    val density = LocalDensity.current

    SubcomposeLayout(modifier = modifier) { constraints ->

        var subcomposeIndex = 0

        val spaceBetweenButtons = with(density) {
            paddingBetween.roundToPx()
        }

        var placeables: List<Placeable> = subcompose(subcomposeIndex++, content)
            .map {
                it.measure(constraints)
            }

        var maxWidth = 0
        var maxHeight = 0
        var layoutWidth = 0

        placeables.forEach { placeable: Placeable ->
            maxWidth = placeable.width.coerceAtLeast(maxWidth)
                .coerceAtMost(((constraints.maxWidth - spaceBetweenButtons) / 2))
            maxHeight = placeable.height.coerceAtLeast(maxHeight)
        }


        layoutWidth = maxWidth

        // Remeasure every element using width of longest item using it as min width
        // Our max width is half of the remaining area after we subtract space between buttons
        // and we constraint its maximum width to half width minus space between
        if (placeables.isNotEmpty() && placeables.size > 1) {
            placeables = subcompose(subcomposeIndex, content).map { measurable: Measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = maxWidth,
                        maxWidth = ((constraints.maxWidth - spaceBetweenButtons) / 2)
                            .coerceAtLeast(maxWidth)
                    )
                )
            }

            layoutWidth = (placeables.sumOf { it.width } + spaceBetweenButtons)
                .coerceAtMost(constraints.maxWidth)

            maxHeight = placeables.maxOf { it.height }
        }

        layout(layoutWidth, maxHeight) {
            var xPos = 0
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(xPos, 0)
                xPos += placeable.width + spaceBetweenButtons
            }
        }
    }
}


@Composable
fun MyPieChart(
    listTransaction: List<TransactionUiState>,
    colors: List<Color>
) {

    val byCategory = listTransaction.groupBy { it.category }

    Log.d("Category", "keys = ${byCategory.keys}")
    Log.d("Category", "values = ${byCategory.values}")

    var categoriesAmount = mutableListOf<Float>()

    for (it in byCategory) {
        var categoryAmount = 0.0f;
        for (transaction in it.value) {
            val transactionAmount = transaction.amount.dropLast(1).toFloat()
            if (transactionAmount < 0.0f) {
                categoryAmount += transactionAmount
            }
        }
        categoriesAmount.add(categoryAmount)
    }
    Log.d("Category", "categoriesAmount = $categoriesAmount")

    val total = categoriesAmount.sum()

    val proportions = categoriesAmount.map {
        it * 100 / total
    }

    val sweepAnglePercentage = proportions.map {
        360 * it / 100
    }

    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(Modifier.padding(5.dp)) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White)
            ) {

                var startAngle = 270f

                sweepAnglePercentage.forEachIndexed { index, sweepAngle ->
                    DrawArc(
                        colors[index],
                        startAngle,
                        sweepAngle
                    )

                    startAngle += sweepAngle
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally

        ) {
            byCategory.keys.forEachIndexed { index, category ->
                LegendCard(text = category, color = colors[index])
            }
        }
    }
}

fun DrawScope.DrawArc(
    color: Color,
    startAngle: Float,
    sweepAngle: Float
) {

    val padding = 250f
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(width = size.width - padding, height = size.width - padding),
        style = Stroke(
            width = 130f
        ),
        topLeft = Offset(padding / 2f, padding / 2f)
    )
}

@Preview
@Composable
fun MyPieChartPrev() {
    MyPieChart(
        listTransaction = listOf(
            TransactionUiState(
                amount = "-6.33£",
                merchant = "Uber",
                category = "Travel",
                date = "13/11/2022"
            ), TransactionUiState(
                amount = "-6.33£",
                merchant = "Uber",
                category = "Food",
                date = "13/11/2022"
            ), TransactionUiState(
                amount = "-6.33£",
                merchant = "Uber",
                category = "Food",
                date = "13/11/2022"
            ), TransactionUiState(
                amount = "-6.33£",
                merchant = "Uber",
                category = "Sport",
                date = "13/11/2022"
            )
        ),
        colors = listOf(
            MainColor_1,
            InterColor12_1,
            MainColor_2,
            InterColor23_1,
            MainColor_3
        )
    )
}