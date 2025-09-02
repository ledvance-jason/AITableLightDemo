package com.ledvance.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ledvance.ui.extensions.toDp
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/2 16:43
 * Describe : LedvanceInputText
 */
@Composable
fun LedvanceInputText(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    inputTextType: InputTextType = InputTextType.Text,
    unit: String? = null,
    valueOutOfBoundsValidation: Boolean = false,
    min: Number = 0,
    max: Number = 100,
    onValueOutOfBounds: (value: String, min: Number, max: Number) -> Unit = { _, _, _ -> },
    onDone: (String) -> Unit = {},
    onValueChange: (String) -> Unit
) {
    val text by rememberUpdatedState(value)
    var isFocused by remember { mutableStateOf(false) }

    val decimalRegex = if (inputTextType is InputTextType.Decimal) {
        remember(inputTextType.scale) {
            Regex("^-?\\d*(\\.\\d{0,${inputTextType.scale}})?$")
        }
    } else null

    val validationValueOutOfBoundsLogic: (String) -> Boolean = validation@{ newValue: String ->
        when (inputTextType) {
            is InputTextType.Decimal -> {
                val doubleValue = newValue.toDoubleOrNull() ?: return@validation run {
                    onValueOutOfBounds.invoke(newValue, min, max)
                    val minValue = min.toString()
                    onValueChange.invoke(minValue)
                    onDone.invoke(minValue)
                    false
                }
                val minDouble = min.toDouble()
                val maxDouble = max.toDouble()
                if (doubleValue < minDouble || doubleValue > maxDouble) {
                    onValueOutOfBounds.invoke(newValue, min, max)
                    val minOrMax = if (doubleValue < minDouble) min.toString() else max.toString()
                    onValueChange.invoke(minOrMax)
                    onDone.invoke(minOrMax)
                    return@validation false
                }
                return@validation true
            }

            InputTextType.Integer -> {
                val intValue = newValue.toIntOrNull() ?: return@validation run {
                    onValueOutOfBounds.invoke(newValue, min, max)
                    val minValue = min.toString()
                    onValueChange.invoke(minValue)
                    onDone.invoke(minValue)
                    false
                }
                if (intValue < min.toInt() || intValue > max.toInt()) {
                    onValueOutOfBounds.invoke(newValue, min, max)
                    val minOrMax = if (intValue < min.toInt()) min.toString() else max.toString()
                    onValueChange.invoke(minOrMax)
                    onDone.invoke(minOrMax)
                    return@validation false

                }
                return@validation true
            }

            else -> true
        }
    }

    val validationValueChangeLogic: (String) -> Unit = { newValue: String ->
        when (inputTextType) {
            is InputTextType.Decimal -> {
                newValue.takeIf { decimalRegex != null && it.matches(decimalRegex) }?.also {
                    onValueChange.invoke(newValue)
                }
            }

            InputTextType.Integer -> {
                newValue.takeIf { newValue.isEmpty() || newValue == "-" || newValue.toIntOrNull() != null }
                    ?.also {
                        onValueChange.invoke(newValue)
                    }
            }

            InputTextType.Text -> {
                onValueChange.invoke(newValue)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AppTheme.typography.bodySmall,
            color = AppTheme.colors.title,
            modifier = Modifier
                .weight(1f)
                .padding(end = 40.dp)
        )
        Row(
            modifier = Modifier
                .width(155.dp)
                .height(30.dp)
                .then(
                    if (isFocused) Modifier.border(
                        width = 1.dp,
                        brush = AppTheme.colors.buttonBorderBrush,
                        shape = RoundedCornerShape(4.dp)
                    )
                    else Modifier.border(
                        width = 1.toDp(),
                        color = AppTheme.colors.textFieldBorder,
                        shape = RoundedCornerShape(4.dp)
                    )
                )
                .background(AppTheme.colors.textFieldBackground),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            BasicTextField(
                value = text,
                onValueChange = validationValueChangeLogic,
                textStyle = AppTheme.typography.bodySmall.copy(color = AppTheme.colors.textFieldContent),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = inputTextType.keyboardType,
                    imeAction = ImeAction.Done
                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .weight(1f)
                    .onFocusChanged {
                        if (isFocused && !it.isFocused) {
                            val shouldInvokeOnDone = if (valueOutOfBoundsValidation) {
                                validationValueOutOfBoundsLogic.invoke(text)
                            } else true
                            if (shouldInvokeOnDone) {
                                onDone.invoke(text)
                            }
                        }
                        isFocused = it.isFocused
                    }
            )
            if (!unit.isNullOrEmpty()) {
                Text(
                    text = unit,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.textFieldUnit,
                    modifier = Modifier.padding(end = 15.dp)
                )
            }
        }
    }
}

sealed class InputTextType(val keyboardType: KeyboardType) {
    data object Text : InputTextType(KeyboardType.Text)
    data object Integer : InputTextType(KeyboardType.Number)
    data class Decimal(val scale: Int = 1) : InputTextType(KeyboardType.Decimal)
}