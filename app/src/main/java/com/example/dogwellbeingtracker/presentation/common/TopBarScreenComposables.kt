package com.example.dogwellbeingtracker.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.presentation.common.DogSexItemData.dogSexItemList

/**
 *  Reusable text field for user input when adding a dog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = keyboardType),
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
    )
}

/**
 * Reusable text field that has a drop down menu on click for selecting a dog's sex.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogSexField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            singleLine = true,
            readOnly = true,
            label = { Text(text = stringResource(R.string.sex)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            isError = isError,
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded }) {
            for (dogSexItem in dogSexItemList) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(dogSexItem.sex))
                    },
                    onClick = {
                        expanded =
                            !expanded; onValueChange(context.getString(dogSexItem.sex))
                    }
                )
            }
        }
    }
}

/**
 * Object containing [DogSexItemData] used in [dogSexItemList] for the "Sex" [DogTextField].
 */
private object DogSexItemData {
    data class DogSexItem(
        @StringRes val sex: Int
    )
    val dogSexItemList = listOf(
        DogSexItem(
            sex = R.string.female
        ),
        DogSexItem(
            sex = R.string.male
        )
    )
}