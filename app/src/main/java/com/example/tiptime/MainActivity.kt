package com.example.tiptime

import android.icu.text.NumberFormat
import android.icu.text.NumberFormat.getCurrencyInstance
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TipTimeScreen() {
    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var tipInput by remember { mutableStateOf("") }
    val tipprecent = tipInput.toDoubleOrNull() ?: 0.0
    var roundUp by remember {
        mutableStateOf(false)
    }
    val tip = calculateTip(amount, tipprecent, roundUp)
    Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = stringResource(id = R.string.calculate_tip),
        fontSize = 24.sp,
        modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(label = R.string.bill_amount,
            value = amountInput,
            onValueChange = {amountInput = it}
        )
        EditTipField(label = R.string.how_was_the_service,
            value = tipInput,
            onValueChange = {tipInput = it})
        Spacer(modifier = Modifier.height(24.dp))
        RoundTheTipRow(roundUp = roundUp, onRoundChange = {roundUp = it})
        Text(
            text = stringResource(id = R.string.tip_amount, tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun EditNumberField(@StringRes label: Int, value: String,
                    onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier)
{
    TextField(value = value,
        onValueChange = onValueChange,
        label = {
                Text(text = stringResource(id = label),
                modifier = Modifier.fillMaxWidth())

        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),

        singleLine = true
    )
}
@Composable
fun EditTipField(@StringRes label: Int, value: String,
                    onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier
) {
    TextField(value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(id = label),
                modifier = Modifier.fillMaxWidth())

        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),

        singleLine = true
    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .size(48.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(checked = roundUp,
            onCheckedChange = onRoundChange,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
private fun calculateTip (amount: Double, tipprecent: Double = 15.0,roundUp: Boolean): String {
    var tip = tipprecent / 100 * amount
    if (roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return getCurrencyInstance().format(tip)

}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}