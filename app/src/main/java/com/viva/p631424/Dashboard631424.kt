package com.viva.p631424

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viva.p631424.data.DatabaseProvider
import com.viva.p631424.data.WaterRecordViewModel
import com.viva.p631424.data.WaterRecordViewModelFactory
import com.viva.p631424.ui.theme.VivaProjectTheme
import java.time.LocalDate
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.viva.p631424.data.WaterRecord
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class Dashboard631424 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VivaProjectTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Initiate the database
                    val database = DatabaseProvider.getDatabase(application)
                    val viewModelFactory = WaterRecordViewModelFactory(database)
                    DashboardScreen(viewModelFactory = viewModelFactory)
                }
            }
        }
        // Insert dummy records
        insertDummyRecords()
    }

    @Composable
    fun DashboardScreen(viewModelFactory: WaterRecordViewModelFactory) {
        val viewModel: WaterRecordViewModel = viewModel(factory = viewModelFactory)
        val totalToday by viewModel.getTotalCupsByDate(LocalDate.now()).observeAsState(initial = 0.0f)
        var showAlert by remember { mutableStateOf(false) }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Invalid Operation") },
                text = { Text("You cannot drink negative cups of water.") }
            )
        }

        fun handleWaterRecordChange(cups: Float) {
            if (totalToday + cups < 0) {
                showAlert = true
            } else {
                viewModel.addWaterRecord(cups)
            }
        }

        // access the current context
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Daily Water Intake",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Circle with text
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$totalToday Cups",
                        fontSize  = 30.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Buttons in corners
                Button(
                    onClick = { handleWaterRecordChange(0.5f) },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(y = (-80).dp)
                ) {
                    Text("+0.5")
                }

                Button(
                    onClick = { handleWaterRecordChange(1.0f) },
                    modifier = Modifier.align(Alignment.CenterEnd).offset(y = (-80).dp)
                ) {
                    Text("+1")
                }

                Button(
                    onClick = { handleWaterRecordChange(-0.25f) },
                    modifier = Modifier.align(Alignment.CenterStart).offset(y = 80.dp)
                ) {
                    Text("-0.25")
                }

                Button(
                    onClick = { handleWaterRecordChange(-0.5f) },
                    modifier = Modifier.align(Alignment.CenterEnd).offset(y = 80.dp)
                ) {
                    Text("-0.5")
                }

                Button(
                    onClick = {
                        val intent = Intent(context, History631424::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text("History")
                }
            }
        }
    }

    private fun insertDummyRecords() {
        val database = DatabaseProvider.getDatabase(application)
        val viewModel = WaterRecordViewModel(database)

        // Define some dummy records
        val dummyRecords = listOf(
            WaterRecord(cup = 2.0f, timestamp = LocalDateTime.now().minusDays(1)),
            WaterRecord(cup = 3.5f, timestamp = LocalDateTime.now().minusDays(2)),
            WaterRecord(cup = 1.0f, timestamp = LocalDateTime.now().minusDays(3)),
            WaterRecord(cup = 4.0f, timestamp = LocalDateTime.now().minusDays(4))
        )

        // Insert records in a coroutine
        viewModel.viewModelScope.launch {
            dummyRecords.forEach { record ->
                viewModel.insert(record)
            }
        }
    }
}
