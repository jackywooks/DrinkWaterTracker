package com.viva.p631424

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viva.p631424.data.DatabaseProvider
import com.viva.p631424.data.WaterRecordViewModel
import com.viva.p631424.data.WaterRecordViewModelFactory
import com.viva.p631424.ui.theme.VivaProjectTheme
import java.time.LocalDate

class History631424 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VivaProjectTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val database = DatabaseProvider.getDatabase(application)
                    val viewModelFactory = WaterRecordViewModelFactory(database)
                    HistoryScreen(viewModelFactory = viewModelFactory)
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(viewModelFactory: WaterRecordViewModelFactory) {
    // access the current context
    val context = LocalContext.current
    val viewModel: WaterRecordViewModel = viewModel(factory = viewModelFactory)
    val records by viewModel.getWaterRecordsByDate(LocalDate.now()).observeAsState(initial = emptyList())

    // Group records by date and calculate the sum of cups for each day
    val dailySummary = mutableListOf<Triple<LocalDate, Float, String>>()
    val groupedRecords = records.groupBy { it.timestamp.toLocalDate() }

    for ((date, dailyRecords) in groupedRecords) {
        var totalCups = 0.0f
        for (record in dailyRecords) {
            totalCups += record.cup
        }

        val status = if (totalCups < 4) {
            "Alert"
        } else if (totalCups < 8) {
            "Drink More"
        } else {
            "Good"
        }

        dailySummary.add(Triple(date, totalCups, status))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "Water Intake History",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Date", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text(text = "Total Cups", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text(text = "Status", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                        Divider()
                    }
                    items(dailySummary) { (date, totalCups, status) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = date.toString(), style = MaterialTheme.typography.bodyLarge)
                            Text(text = "${totalCups}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = status, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
            Button(
                onClick = {
                    val intent = Intent(context, Dashboard631424::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ){
                Text("Dashboard")
            }
        }
    }
}