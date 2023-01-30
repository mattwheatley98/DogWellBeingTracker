package com.example.dogwellbeingtracker.presentation.top_bar_screens.dog_information

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.domain.model.Dog
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.home.EmptyListAnimation
import com.example.dogwellbeingtracker.presentation.common.DogSexField
import com.example.dogwellbeingtracker.presentation.common.DogTextField
import kotlinx.coroutines.launch

/**
 * Screen for the dog information feature.
 */
@Composable
fun DogInformationScreen(
    addDog: () -> Unit,
    viewModel: DogInformationViewModel = hiltViewModel(),
    ) {
    val coroutineScope = rememberCoroutineScope()
    val dogInformationUiState by viewModel.dogInformationUiState.collectAsState()

    DogInformationBody(
        dogList = dogInformationUiState.dogList,
        deleteDog = { coroutineScope.launch { viewModel.deleteDog(it) } },
        onEditDogValueChange = viewModel::updateEditDogUiState,
        submitChanges = { coroutineScope.launch { viewModel.updateDog(); viewModel.selectDog(it.toDog()) } },
        editDogUiState = viewModel.editDogUiState,
        convertDogToDogDetails = { viewModel.convertDogToEditDogDetails(it) },
        expandEditTextField = { coroutineScope.launch { viewModel.expandEditTextField(it) } },
        resetEditFieldExpansion = { coroutineScope.launch { viewModel.resetEditFieldExpansion() } },
        addDog = addDog
    )
}

/**
 * Body for [DogInformationScreen].
 * Checks if the list of dogs is empty, and if it is, [EmptyListAnimation] displays and prompts the user to add a dog.
 */
@Composable
private fun DogInformationBody(
    dogList: List<Dog>,
    deleteDog: (Dog) -> Unit,
    onEditDogValueChange: (EditDogDetails) -> Unit,
    submitChanges: (EditDogDetails) -> Unit,
    convertDogToDogDetails: (Dog) -> Unit,
    expandEditTextField: (Dog) -> Unit,
    resetEditFieldExpansion: () -> Unit,
    editDogUiState: EditDogUiState,
    addDog: () -> Unit
) {
    if (dogList.isEmpty()) { //Already initialized... don't need the loading state
        EmptyListAnimation(addDog = { addDog() })
    } else {
        DogInformationList(
            dogList = dogList,
            deleteDog = deleteDog,
            onValueChange = onEditDogValueChange,
            submitChanges = submitChanges,
            convertDogToDogDetails = convertDogToDogDetails,
            expandEditTextField = expandEditTextField,
            resetEditFieldExpansion = resetEditFieldExpansion,
            editDogDetails = editDogUiState.editDogDetails,
            )
    }
}

/**
 * Composable with a lazy column that displays [DogInformationEntry].
 */
@Composable
private fun DogInformationList(
    dogList: List<Dog>,
    deleteDog: (Dog) -> Unit,
    onValueChange: (EditDogDetails) -> Unit,
    submitChanges: (EditDogDetails) -> Unit,
    convertDogToDogDetails: (Dog) -> Unit,
    expandEditTextField: (Dog) -> Unit,
    resetEditFieldExpansion: () -> Unit,
    editDogDetails: EditDogDetails,
    modifier: Modifier = Modifier,
    ) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        items(items = dogList, key = { dog -> dog.id }) { dog ->
            DogInformationEntry(
                dog = dog,
                deleteDog = deleteDog,
                onValueChange = onValueChange,
                submitChanges = submitChanges,
                convertDogToDogDetails = convertDogToDogDetails,
                expandEditTextField = expandEditTextField,
                resetEditFieldExpansion = resetEditFieldExpansion,
                editDogDetails = editDogDetails,
                )
        }
        if (editDogDetails.sex == "") {
            resetEditFieldExpansion()
        }
    }
}

/**
 * Composable that gives an overview of a single [Dog].
 * When clicked, an edit button will appear, and if that is clicked, text fields and buttons for editing the dog's information will appear.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DogInformationEntry(
    dog: Dog,
    deleteDog: (Dog) -> Unit,
    onValueChange: (EditDogDetails) -> Unit,
    submitChanges: (EditDogDetails) -> Unit,
    convertDogToDogDetails: (Dog) -> Unit,
    expandEditTextField: (Dog) -> Unit,
    resetEditFieldExpansion: () -> Unit,
    editDogDetails: EditDogDetails,
    modifier: Modifier = Modifier,
    ) {
    var expandedButtons by remember { mutableStateOf(false) }
    val visibleState =
        remember {
            MutableTransitionState(initialState = false).apply {
                targetState = true
            }
        }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)),
        exit = fadeOut()
    ) {
        Card(
            modifier = Modifier
                .padding()
                .fillMaxWidth()
                .animateEnterExit(
                    enter = slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessVeryLow
                        )
                    )
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
                .clickable {
                    expandedButtons = !expandedButtons; resetEditFieldExpansion()
                }
        ) {
            Row {
                AsyncImage(
                    model = dog.picture,
                    contentDescription = dog.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(0.dp)
                        .weight(.3F)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(100.dp)
                        .padding(4.dp)
                        .weight(.4F)
                ) {
                    Text(text = "${stringResource(id = R.string.name_colon)} ${dog.name}")
                    Text(text = "${stringResource(id = R.string.age_colon)} ${dog.age}")
                    Text(text = "${stringResource(id = R.string.breed_colon)} ${dog.breed}")
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(100.dp)
                        .padding(4.dp)
                        .weight(.4F)
                ) {
                    Text(text = "${stringResource(id = R.string.weight_colon)} ${dog.weight}")
                    Text(text = "${stringResource(id = R.string.calories_colon)} ${dog.dailyMaxCalories}")
                    Text(text = "${stringResource(id = R.string.sex_colon)} ${dog.sex}")
                }
            }
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
            if (expandedButtons) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = {
                            resetEditFieldExpansion(); expandEditTextField(dog); convertDogToDogDetails(
                            dog
                        )
                        },
                        modifier = modifier
                            .padding(start = 32.dp, top = 4.dp, end = 32.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "${stringResource(id = R.string.edit)} ${dog.name} ${stringResource(id = R.string.s_information)}")
                    }
                }
                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            deleteDog(dog)
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },

                        )
                }
            }
            if (dog.isEditFieldExpanded) {
                EditDogTextFields(
                    dog = dog,
                    onValueChange = onValueChange,
                    submitChanges = submitChanges,
                    editDogDetails = editDogDetails,
                    deleteDog = { deleteConfirmationRequired = true },
                )
            }
        }
    }
}

/**
 * Delete confirmation dialog that prompts users to delete their saved dog.
 */
@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(text = stringResource(R.string.attention)) },
        text = { Text(text = stringResource(R.string.are_you_sure_you_want)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}

/**
 * Composable that contains the edit dog input fields.
 * [DogTextField] and [DogSexField] are reusable input fields from "DogFieldComposables" with parameters following the their according formats.
 */
@Composable
private fun EditDogTextFields(
    dog: Dog,
    onValueChange: (EditDogDetails) -> Unit,
    submitChanges: (EditDogDetails) -> Unit,
    editDogDetails: EditDogDetails,
    deleteDog: (Dog) -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)) {
        Row {
            DogTextField(
                label = stringResource(id = R.string.name),
                value = editDogDetails.name,
                onValueChange = { onValueChange(editDogDetails.copy(name = it)) },
                isError = editDogDetails.name == "",
                modifier = modifier.weight(.5F)
            )
            Spacer(modifier = modifier.padding(4.dp))
            DogTextField(
                label = stringResource(id = R.string.age),
                value = editDogDetails.age,
                onValueChange = { onValueChange(editDogDetails.copy(age = it)) },
                keyboardType = KeyboardType.Number,
                isError = editDogDetails.age == "",
                modifier = modifier.weight(.5F)
            )
        }
        Row {
            DogTextField(
                label = stringResource(id = R.string.breed),
                value = editDogDetails.breed,
                onValueChange = { onValueChange(editDogDetails.copy(breed = it)) },
                isError = editDogDetails.breed == "",
                modifier = modifier.weight(.5F)
            )
            Spacer(modifier = modifier.padding(4.dp))
            DogTextField(
                label = stringResource(id = R.string.weight),
                value = editDogDetails.weight,
                onValueChange = { onValueChange(editDogDetails.copy(weight = it)) },
                keyboardType = KeyboardType.Number,
                isError = editDogDetails.weight == "",
                modifier = modifier.weight(.5F)
            )
        }
        Row {
            DogTextField(
                label = stringResource(id = R.string.daily_calories),
                value = editDogDetails.dailyMaxCalories,
                onValueChange = { onValueChange(editDogDetails.copy(dailyMaxCalories = it)) },
                keyboardType = KeyboardType.Number,
                isError = editDogDetails.dailyMaxCalories == "",
                modifier = modifier.weight(.5F)
            )
            Spacer(modifier = modifier.padding(4.dp))
            DogSexField(
                value = editDogDetails.sex,
                onValueChange = { onValueChange(editDogDetails.copy(sex = it)) },
                isError = editDogDetails.sex == "",
                modifier = modifier.weight(.5F)
            )
        }
        EditDogButtons(
            onValueChange = onValueChange,
            submitChanges = submitChanges,
            editDogDetails = editDogDetails,
            deleteDog = deleteDog,
            dog = dog,
        )
    }
}

/**
 * Composable that lets users change their dog's photo, submit any changes to their dog based on [EditDogDetails], or delete their dog.
 */
@Composable
private fun EditDogButtons(
    onValueChange: (EditDogDetails) -> Unit,
    submitChanges: (EditDogDetails) -> Unit,
    editDogDetails: EditDogDetails,
    deleteDog: (Dog) -> Unit,
    dog: Dog,
    modifier: Modifier = Modifier,
    ) {
    Row(modifier = modifier.padding(top = 6.dp)) {
        val context = LocalContext.current
        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    val resolver = context.contentResolver
                    resolver.takePersistableUriPermission(uri, flags)
                    onValueChange(editDogDetails.copy(picture = uri.toString()))
                    Toast.makeText(context, R.string.picture_selected, Toast.LENGTH_SHORT).show()
                } else (Toast.makeText(context, R.string.picture_not_selected, Toast.LENGTH_SHORT)
                    .show())
            }
        )
        Button(onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }, modifier = modifier.weight(.33F)) {
            Text(text = stringResource(R.string.change_photo), textAlign = TextAlign.Center)
        }
        Spacer(modifier = modifier.padding(start = 4.dp, end = 4.dp))
        Button(
            onClick = {
                if (editDogDetails.name == dog.name && editDogDetails.age == dog.age && editDogDetails.breed == dog.breed && editDogDetails.weight == dog.weight && editDogDetails.dailyMaxCalories == dog.dailyMaxCalories && editDogDetails.sex == dog.sex && editDogDetails.picture == dog.picture) {
                    Toast.makeText(context, R.string.no_changes_made, Toast.LENGTH_SHORT).show()
                } else {
                    submitChanges(editDogDetails)
                    Toast.makeText(context, R.string.changes_submitted, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = modifier.weight(.33F)
        ) {
            Text(text = stringResource(R.string.submit_changes), textAlign = TextAlign.Center)
        }
        Spacer(modifier = modifier.padding(start = 4.dp, end = 4.dp))
        Button(
            onClick = { deleteDog(dog) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = modifier.weight(.33F)
        ) {
            Text(text = "${stringResource(id = R.string.delete)} ${dog.name}", textAlign = TextAlign.Center)
        }
    }
}