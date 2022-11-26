package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.succiue.myapplication.ui.viewmodels.TransactionUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun StatsBody(navController: NavHostController, listTransaction: List<TransactionUiState>) {
    val transactionVision = remember { mutableStateOf(false) }
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
                        SelectButton(transactionVision)
                    }
                }
                if (!transactionVision.value) {
                    item {
                        GraphSection(onNextButtonClicked = {})
                    }
                    item {
                        GraphSection(onNextButtonClicked = {})
                    }
                    item {
                        GraphSection(onNextButtonClicked = {})
                    }
                    item {
                        GraphSection(onNextButtonClicked = {})
                    }
                    item {
                        GraphSection(onNextButtonClicked = {})
                    }
                } else {
                    item {
                        ListSection(
                            listTransaction = listTransaction,
                            maxIndex = listTransaction.size,
                            onNextButtonClicked = {}
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}


@Composable
fun SelectButton(selected: MutableState<Boolean>) {
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
                border = BorderStroke(1.dp, if (selected.value) Color.Gray else Color.Blue),
                onClick = { selected.value = false },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Gray,
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            {
                Text(text = "Graphiques")
            }

            Button(
                border = BorderStroke(1.dp, if (!selected.value) Color.Gray else Color.Blue),
                onClick = { selected.value = true },
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