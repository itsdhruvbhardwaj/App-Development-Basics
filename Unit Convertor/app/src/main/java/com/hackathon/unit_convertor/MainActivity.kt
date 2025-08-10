package com.hackathon.unit_convertor

import android.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hackathon.unit_convertor.ui.theme.Unit_ConvertorTheme
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Unit_ConvertorTheme {
                Scaffold(modifier = Modifier.fillMaxSize())
                { innerPadding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(innerPadding), color = MaterialTheme.colorScheme.background)
                    {
                        UnitConvertor()
                    }
                }
            }
        }
    }
}

@Composable
fun UnitConvertor()
{
    var inputValue by remember { mutableStateOf("") }
    var outputvalue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }
    var iExpand by remember { mutableStateOf(false) }
    var oExpand by remember { mutableStateOf(false) }
    val conversionFactor = remember {mutableStateOf(1.00)}
    val oconversionFactor = remember {mutableStateOf((1.00))}

    fun convertunits()
    {
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result = (inputValueDouble*conversionFactor.value*100.0 / oconversionFactor.value).roundToInt() / 100.0
        outputvalue = result.toString()
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text("Unit Converter", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue=it
                convertunits()
            },
            label = {Text("Enter Value")},
            
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Box{
                Button(onClick = {iExpand = true}) {
                    Text(text = inputUnit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                }
                DropdownMenu(expanded = iExpand, onDismissRequest = {iExpand=false}) {
                    DropdownMenuItem(
                        text = {Text("Centimeters")},
                        onClick = {
                            iExpand = false
                            inputUnit = "centimeters"
                            conversionFactor.value = 0.01
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Feet")},
                        onClick = {
                            iExpand = false
                            inputUnit = "Feet"
                            conversionFactor.value = 0.3048
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Meter")},
                        onClick = {
                            iExpand = false
                            inputUnit = "Meter"
                            conversionFactor.value = 1.0
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Milimeters")},
                        onClick = {
                            iExpand = false
                            inputUnit = "Milimeters"
                            conversionFactor.value = 0.001
                            convertunits()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box{
                Button(onClick = {oExpand = true}) {
                    Text(text = outputUnit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                }
                DropdownMenu(expanded = oExpand, onDismissRequest = {oExpand=false}) {
                    DropdownMenuItem(
                        text = {Text("Centimeters")},
                        onClick = {
                            oExpand = false
                            outputUnit = "centimeters"
                            oconversionFactor.value = 0.01
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Feet")},
                        onClick = {
                            oExpand = false
                            outputUnit = "Feet"
                            oconversionFactor.value = 0.3048
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Meter")},
                        onClick = {
                            oExpand = false
                            outputUnit = "Meter"
                            oconversionFactor.value = 1.0
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Milimeters")},
                        onClick = {
                            oExpand = false
                            outputUnit = "Milimeters"
                            oconversionFactor.value = 0.001
                            convertunits()
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Result: $outputvalue $outputUnit", style = MaterialTheme.typography.headlineMedium)
    }
}


@Preview(showBackground = true)
@Composable
fun UnitConvertorPreview() {
    Unit_ConvertorTheme {
        UnitConvertor()
    }
}