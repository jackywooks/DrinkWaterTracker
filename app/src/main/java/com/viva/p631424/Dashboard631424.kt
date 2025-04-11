package com.viva.p631424

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import com.viva.p631424.data.WaterRecord
import com.viva.p631424.data.WaterRecordViewModel
import com.viva.p631424.ui.theme.VivaProjectTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VivaProjectTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DashboardScreen()
                }
            }
        }
    }

    @Preview
    @Composable
    fun DashboardScreen(viewModel: WaterRecordViewModel = viewModel()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                // For now, we'll insert a default cup (e.g., 1.0)
                val newRecord = WaterRecord(cup = 1.0f, timestamp = LocalDateTime.now())
                viewModel.insert(newRecord)
            }) {
                Text(text = "Add Cup of Water")
            }
            // We'll add the display for today's total later
        }
    }
}