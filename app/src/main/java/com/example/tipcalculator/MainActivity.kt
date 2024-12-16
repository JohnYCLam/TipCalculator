package com.example.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.tipcalculator.util.calculateTotalPerPerson
import com.example.tipcalculator.util.calculateTotalTip
import com.example.tipcalculator.widgets.RoundIconButton

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
            .clip(shape = CircleShape.copy(all = CornerSize(20.dp)))
            .padding(5.dp),
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
    val totalBillState = remember { mutableStateOf("") }
    val sliderPositionState = remember { mutableStateOf(0f) }
    val tipPercentageState = remember { mutableStateOf(0) }
    val tipAmountState = remember { mutableStateOf(0.0) }
    val splitByState = remember { mutableStateOf(1) }
    val splitByRange = IntRange(start = 1, endInclusive = 1000)
    val totalPerPersonState = remember { mutableStateOf(0.0) }
    Column {
        TopHeader(totalPerPersonState.value)
        BillForm(totalBillState = totalBillState,
            sliderPositionState =  sliderPositionState,
            tipPercentageState =  tipPercentageState,
            tipAmountState =  tipAmountState,
            splitByState =  splitByState,
            splitByRange =  splitByRange,
            totalPerPersonState =  totalPerPersonState) { billAmt ->
            Log.d("AMT", "Amount: $billAmt")
        }
    }
}

@Composable
fun BillForm(modifier: Modifier = Modifier,
             totalBillState: MutableState<String>,
             sliderPositionState: MutableState<Float>,
             tipPercentageState: MutableState<Int>,
             tipAmountState: MutableState<Double>,
             splitByState: MutableState<Int>,
             splitByRange: IntRange = 1..1000,
             totalPerPersonState: MutableState<Double>,
             onValChange: (String) -> Unit = {}) {

    val validState = remember(totalBillState.value) { totalBillState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current
    //val tipPercentage = (sliderPositionState.value * 100).toInt()

    Surface(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(15.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )

            if (validState) {
                Row(modifier = Modifier.padding(5.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    Text("Split", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(150.dp))
                    Row(modifier = Modifier.padding(5.dp), horizontalArrangement = Arrangement.End) {
                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                            splitByState.value =
                                if (splitByState.value > 1) splitByState.value - 1
                                else 1

                            totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value, splitByState.value, tipPercentageState.value)
                        })
                        Text("${splitByState.value}", modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp, end = 10.dp))
                        RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                            if (splitByState.value < splitByRange.last) {
                                splitByState.value += 1

                                totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value, splitByState.value, tipPercentageState.value)
                            }
                        })

                    }
                }

                //Tip Row
                Row(modifier = Modifier.padding(5.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    Text("Tip", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(260.dp))
                    Text("$ ${tipAmountState.value}", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                }

                //Slider
                Column(modifier = Modifier.padding(5.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${tipPercentageState.value} %")
                    Spacer(modifier = Modifier.height(20.dp))
                    Slider(value = sliderPositionState.value, onValueChange = {newVal ->
                        sliderPositionState.value = newVal
                        tipPercentageState.value = (sliderPositionState.value * 100).toInt()
                        tipAmountState.value = calculateTotalTip(totalBillState.value, tipPercentageState.value)
                        totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value, splitByState.value, tipPercentageState.value)

                        Log.d("Slider", "Bill Form: $newVal")},
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                        steps = 5)
                }
            } else {
                Box() {}
            }

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