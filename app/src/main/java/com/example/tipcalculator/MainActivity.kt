package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.components.InputField
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            MyApp { MainContent() }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipCalculatorTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            //.clip(shape = RoundedCornerShape(corner = CornerSize(15.dp)))
            .clip(shape = CircleShape.copy(all = CornerSize(15.dp))),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val totalFormatted = "%.2f".format(totalPerPerson)
            Text(text = "Total per Person", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "$$totalFormatted",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold
            )

        }
    }
}


@Preview
@Composable
fun MainContent() {
    val totalBillState = remember{ mutableStateOf("")}
    val validState = remember(totalBillState.value) {totalBillState.value.trim().isNotEmpty()}
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(15.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column() {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    keyboardController?.hide()
                }
            )

        }
    }
}

//@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculatorTheme {
        MyApp { Text(text = "Hello!") }
    }
}