package com.jonathansteele.topcointrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinLoreApp(viewModel: CoinLoreViewModel = viewModel()) {
    val sortedCoins = viewModel.sortedCoins
    val isLoading = viewModel.isLoading
    var sortCriteria by remember { mutableStateOf(SortCriteria.DEFAULT) }
    var expanded by remember { mutableStateOf(false) }

    // Apply sorting
    LaunchedEffect(sortCriteria) {
        viewModel.sortCoins(sortCriteria)
    }

    Scaffold(
        modifier = Modifier.testTag("CoinLoreAppTag"),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Top Cryptocurrencies") },
                actions = {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Sort"
                            )
                        }
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Default") },
                                onClick = {
                                    sortCriteria = SortCriteria.DEFAULT
                                    expanded = false
                                })
                            DropdownMenuItem(
                                text = { Text("Price") },
                                onClick = {
                                    sortCriteria = SortCriteria.PRICE
                                    expanded = false
                                })
                            DropdownMenuItem(
                                text = { Text("24h Change") },
                                onClick = {
                                    sortCriteria = SortCriteria.CHANGE
                                    expanded = false
                                })
                        }
                    }
                })
        }
    ) {
        if (isLoading) {
            // Show loading indicator
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Show the list of coins once data is loaded
            Column(modifier = Modifier.padding(it)) {
                CoinList(coins = sortedCoins)
            }
        }
    }
}

@Composable
fun CoinList(coins: List<Coin>) {
    LazyColumn {
        items(coins) {
            CoinItem(it)
        }
    }
}

@Composable
fun CoinItem(coin: Coin) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${coin.name} (${coin.symbol})",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${coin.priceUsd}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "24h Change:",
                    style = MaterialTheme.typography.bodyMedium
                )
                val changeColor = if (coin.percentChange24h.toFloat() >= 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
                Text(
                    text = "${coin.percentChange24h}%",
                    style = MaterialTheme.typography.bodyMedium.copy(color = changeColor)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
