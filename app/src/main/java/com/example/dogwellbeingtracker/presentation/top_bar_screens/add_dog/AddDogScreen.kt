package com.example.dogwellbeingtracker.presentation.top_bar_screens.add_dog

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.presentation.common.DogSexField
import com.example.dogwellbeingtracker.presentation.common.DogTextField
import kotlinx.coroutines.launch

/**
 * Screen for the add dog feature.
 */
@Composable
fun AddDogScreen(
    onToHomeClick: () -> Unit,
    addDog: () -> Unit,
    viewModel: AddDogViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    AddDogBody(
        addDogUiState = viewModel.addDogUiState,
        onDogValueChange = viewModel::updateUiState,
        onUploadPictureClick = viewModel::updateUiState,
        onAddDogClick = { coroutineScope.launch { viewModel.addDog() }; addDog() },
        onToHomeClick = onToHomeClick
    )
}

/**
 * Body for [AddDogScreen].
 */
@Composable
private fun AddDogBody(
    addDogUiState: AddDogUiState,
    onDogValueChange: (DogDetails) -> Unit,
    onUploadPictureClick: (DogDetails) -> Unit,
    onAddDogClick: () -> Unit,
    onToHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(16.dp)
            ) {
                AddDogTextFields(
                    dogDetails = addDogUiState.dogDetails,
                    onValueChange = onDogValueChange
                )
            }
        }
        AddDogButtons(
            onUploadPictureClick = onUploadPictureClick,
            onAddDogClick = onAddDogClick,
            onToHomeClick = onToHomeClick,
            dogDetails = addDogUiState.dogDetails,
        )
        UploadedDogPicture(dogDetails = addDogUiState.dogDetails)
    }
}

/**
 * Composable that contains the add dog input fields.
 * [DogTextField] and [DogSexField] are reusable input fields from "DogFieldComposables" with parameters following the their according formats.
 */
@Composable
private fun AddDogTextFields(
    modifier: Modifier = Modifier,
    onValueChange: (DogDetails) -> Unit,
    dogDetails: DogDetails,
) {
    Text(
        text = stringResource(R.string.enter_in_your_dogs_information),
        style = MaterialTheme.typography.titleMedium
    )
    Row {
        DogTextField(
            label = stringResource(R.string.name),
            value = dogDetails.name,
            onValueChange = { onValueChange(dogDetails.copy(name = it)) },
            isError = dogDetails.name == "",
            modifier = modifier.weight(.5F)
        )
        Spacer(modifier = modifier.padding(4.dp))
        DogTextField(
            label = stringResource(R.string.age),
            value = dogDetails.age,
            onValueChange = { onValueChange(dogDetails.copy(age = it)) },
            keyboardType = KeyboardType.Number,
            isError = dogDetails.age == "",
            modifier = modifier.weight(.5F)
        )
    }
    Row {
        DogTextField(
            label = stringResource(R.string.breed),
            value = dogDetails.breed,
            onValueChange = { onValueChange(dogDetails.copy(breed = it)) },
            isError = dogDetails.breed == "",
            modifier = modifier.weight(.5F)
        )
        Spacer(modifier = modifier.padding(4.dp))
        DogTextField(
            label = stringResource(R.string.weight),
            value = dogDetails.weight,
            onValueChange = { onValueChange(dogDetails.copy(weight = it)) },
            keyboardType = KeyboardType.Number,
            isError = dogDetails.weight == "",
            modifier = modifier.weight(.5F)
        )
    }
    Row {
        DogTextField(
            label = stringResource(R.string.daily_calories),
            value = dogDetails.dailyMaxCalories,
            onValueChange = { onValueChange(dogDetails.copy(dailyMaxCalories = it)) },
            keyboardType = KeyboardType.Number,
            isError = dogDetails.dailyMaxCalories == "",
            modifier = modifier.weight(.5F)
        )
        Spacer(modifier = modifier.padding(4.dp))
        DogSexField(
            value = dogDetails.sex,
            onValueChange = { onValueChange(dogDetails.copy(sex = it)) },
            isError = dogDetails.sex == "",
            modifier = modifier.weight(.5F)
        )
    }
}

/**
 * Composable that contains buttons for uploading a dog (validates user input based on [DogDetails]), adding a dog to the database, and navigating to the home screen.
 */
@Composable
private fun AddDogButtons(
    onUploadPictureClick: (DogDetails) -> Unit,
    onAddDogClick: () -> Unit,
    onToHomeClick: () -> Unit,
    dogDetails: DogDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val resolver = context.contentResolver
                resolver.takePersistableUriPermission(uri, flags)
                onUploadPictureClick(dogDetails.copy(picture = uri.toString()))
            } else (Toast.makeText(context, R.string.picture_not_selected, Toast.LENGTH_SHORT)
                .show())
        }
    )
    Column {
        Button(
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp))
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(text = stringResource(R.string.upload_a_picture_of_your))
        }
        Row {
            Button(
                onClick = {
                    if (dogDetails.name == "" || dogDetails.age == "" || dogDetails.breed == "" || dogDetails.weight == "" || dogDetails.dailyMaxCalories == "" || dogDetails.sex == "" || dogDetails.picture == "") {
                        Toast.makeText(
                            context,
                            R.string.one_or_more_fields_are,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onAddDogClick()
                        Toast.makeText(
                            context,
                            "${context.getString(R.string.successfully_added_dog_name)} ${dogDetails.name}!",
                            Toast.LENGTH_SHORT
                        ).show()
                        focusManager.clearFocus()
                    }
                }, modifier = modifier
                    .weight(.5F)
                    .clip(RoundedCornerShape(50.dp))
                    .padding(start = 16.dp, end = 8.dp)
            ) {
                Text(text = stringResource(R.string.add_dog))
            }
            Button(
                onClick = { onToHomeClick() }, modifier = modifier
                    .weight(.5F)
                    .clip(RoundedCornerShape(50.dp))
                    .padding(start = 8.dp, end = 16.dp)
            ) {
                Text(text = stringResource(R.string.to_home))
            }
        }
    }
}

/**
 * Displays the picture that the user uploaded with the "Upload a picture of your dog!" button under [AddDogButtons].
 */
@Composable
private fun UploadedDogPicture(
    dogDetails: DogDetails,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Card(
            modifier = modifier
                .size(175.dp)
                .padding(start = 10.dp)
        ) {
            if (dogDetails.picture == "") {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier
                        .height(175.dp)
                        .border(
                            2.dp,
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.your_dogs_picture_will),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                AsyncImage(
                    model = Uri.parse(Uri.decode(dogDetails.picture)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}