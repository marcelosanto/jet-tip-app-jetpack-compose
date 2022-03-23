package xyz.spacermarcelo.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.spacermarcelo.jettipapp.components.InputField
import xyz.spacermarcelo.jettipapp.ui.theme.JetTipAppTheme
import xyz.spacermarcelo.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        Surface {
            content()
        }
    }
}


@Composable
fun TopHeader(totalPerPerson: Double = 132.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFF8BC34A),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "R$ $total",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.h4
            )
        }

    }
}

@Preview
@Composable
fun MainContent() {
    BillForm { billAmt ->
        Log.d("BILL", "MainContent: $billAmt")

    }

}


@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
            TopHeader()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopHeader()

        Surface(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray),
            color = Color(0xFFFFFFFF),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(6.dp),
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
                        onValueChange(totalBillState.value.trim())

                        keyboardController?.hide()
                    }
                )

                if (validState) {
                    Row(
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(120.dp))
                        Row(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                onClick = { /*TODO*/ })

                            Text(
                                text = "5",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp)
                            )

                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                onClick = { /*TODO*/ })
                        }
                    }

                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "Tip",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(200.dp))

                        Text(
                            "$33.00",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("33%")

                        Spacer(modifier = Modifier.height(14.dp))

                        //Slider
                        Slider(
                            value = sliderPositionState.value,
                            onValueChange = { newVal ->
                                sliderPositionState.value = newVal
                                Log.d("SLIDER", "BillForm: $newVal")
                            },
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            steps = 5,
                            onValueChangeFinished = {
                                Log.d("SLIDER", "BillForm: Finished...")
                            }
                        )
                    }

                } else {
                    Box {}
                }

            }
        }
    }
}