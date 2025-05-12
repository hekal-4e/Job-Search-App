package com.depi.jobsearch.ui.screens.editscreens


import OrangeTheme.Purple80
import androidx.activity.compose.BackHandler
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.jobsearchapp.ui.profile.DateFieldWithPicker
import com.example.jobsearchapp.ui.profile.FormField
import com.example.jobsearchapp.ui.profile.RemoveWorkExperienceBottomSheet
import com.example.jobsearchapp.ui.profile.UndoWorkChangesBottomSheet
import com.example.jobsearchapp.ui.profile.WorkExperience
import com.example.jobsearchapp.ui.profile.DatePickerDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkExperienceScreen(
    onBack: () -> Unit,
    onSave: (WorkExperience) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var jobTitle by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCurrentPosition by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var showUndoChangesDialog by remember { mutableStateOf(false) }
    val undoChangesSheetState = rememberModalBottomSheetState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun hasUnsavedChanges(): Boolean {
        return (
                jobTitle.isNotEmpty() ||
                        company.isNotEmpty() ||
                        startDate.isNotEmpty() ||
                        endDate.isNotEmpty() ||
                        description.isNotEmpty()
                )
    }

    val onBackPressed = {
        if (hasUnsavedChanges()) {
            showUndoChangesDialog = true
        } else {
            onBack()
        }
        Unit
    }

    BackHandler(onBack = onBackPressed)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF2A0F66)
                )
            }
            Text(
                text = "Add work experience",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2A0F66),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FormField(
            label = "Job title",
            value = jobTitle,
            onValueChange = { jobTitle = it },
            placeholder = "Enter your position here"
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormField(
            label = "Company",
            value = company,
            onValueChange = { company = it },
            placeholder = "Enter company name"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateFieldWithPicker(
                label = "Start date",
                value = startDate,
                onPickerClick = { showStartDatePicker = true },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            DateFieldWithPicker(
                label = "End date",
                value = endDate,
                onPickerClick = { showEndDatePicker = true },
                enabled = !isCurrentPosition,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isCurrentPosition,
                onCheckedChange = { isCurrentPosition = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF2A0F66),
                    uncheckedColor = Color(0xFF757575)
                )
            )
            Text(
                text = "This is my position now",
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormField(
            label = "Description",
            value = description,
            onValueChange = { description = it },
            placeholder = "Briefly describe your role and responsibilities",
            singleLine = false,
            maxLines = 5,
            minHeight = 120.dp
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val finalEndDate = if (isCurrentPosition) "Present" else endDate
                val newWorkExperience = WorkExperience(
                    userId = 1,
                    company = company,
                    position = jobTitle,
                    startDate = startDate,
                    endDate = finalEndDate
                )
                onSave(newWorkExperience)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E0F5C),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "SAVE",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            title = "Start Date",
            onDismissRequest = { showStartDatePicker = false },
            onDateSelected = { month, year ->
                startDate = "$month $year"
                showStartDatePicker = false
            }
        )
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            title = "End Date",
            onDismissRequest = { showEndDatePicker = false },
            onDateSelected = { month, year ->
                endDate = "$month $year"
                showEndDatePicker = false
            }
        )
    }

    if (showUndoChangesDialog) {
        UndoWorkChangesBottomSheet(
            onDismissRequest = { showUndoChangesDialog = false },
            onConfirm = {
                showUndoChangesDialog = false
                onBack()
            },
            onUndoChanges = {
                showUndoChangesDialog = false
            },
            sheetState = undoChangesSheetState
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeWorkExperienceScreen(
    workExperiences: List<com.example.jobsearchapp.WorkExperience>,
    onWorkExperienceUpdated: (List<com.example.jobsearchapp.WorkExperience>) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val currentExperience = workExperiences.firstOrNull() ?: com.example.jobsearchapp.WorkExperience(
        userId = 1,
        company = "",
        position = "",
        startDate = "",
        endDate = "",
        description = "",
        id = 1
    )

    val originalJobTitle = remember { mutableStateOf(currentExperience.position) }
    val originalCompany = remember { mutableStateOf(currentExperience.company) }
    val originalStartDate = remember { mutableStateOf(currentExperience.startDate) }
    val originalEndDate = remember { mutableStateOf(currentExperience.endDate) }

    var jobTitle by remember { mutableStateOf(currentExperience.position) }
    var company by remember { mutableStateOf(currentExperience.company) }
    var startDate by remember { mutableStateOf(currentExperience.startDate) }
    var endDate by remember { mutableStateOf(currentExperience.endDate) }
    var description by remember { mutableStateOf("") }
    var isCurrentPosition by remember { mutableStateOf(currentExperience.endDate == "Present") }

    val scrollState = rememberScrollState()
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showUndoChangesBottomSheet by remember { mutableStateOf(false) }
    val undoChangesSheetState = rememberModalBottomSheetState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun hasUnsavedChanges(): Boolean {
        return (
                jobTitle != originalJobTitle.value ||
                        company != originalCompany.value ||
                        startDate != originalStartDate.value ||
                        endDate != originalEndDate.value ||
                        (isCurrentPosition && originalEndDate.value != "Present") ||
                        (!isCurrentPosition && originalEndDate.value == "Present")
                )
    }

    val onBackPressed = {
        if (hasUnsavedChanges()) {
            showUndoChangesBottomSheet = true
        } else {
            navController.popBackStack()
        }
        Unit
    }

    BackHandler(onBack = onBackPressed)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF2A0F66)
                )
            }
            Text(
                text = "Change work experience",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2A0F66),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FormField(
            label = "Job title",
            value = jobTitle,
            onValueChange = { jobTitle = it },
            placeholder = "Enter your position here"
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormField(
            label = "Company",
            value = company,
            onValueChange = { company = it },
            placeholder = "Enter company name"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateFieldWithPicker(
                label = "Start date",
                value = startDate,
                onPickerClick = { showStartDatePicker = true },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            DateFieldWithPicker(
                label = "End date",
                value = if (isCurrentPosition) "Present" else endDate,
                onPickerClick = { showEndDatePicker = true },
                enabled = !isCurrentPosition,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isCurrentPosition,
                onCheckedChange = {
                    isCurrentPosition = it
                    if (it) {
                        endDate = "Present"
                    } else if (endDate == "Present") {
                        endDate = ""
                    }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF2A0F66),
                    uncheckedColor = Color(0xFF757575)
                )
            )
            Text(
                text = "This is my position now",
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormField(
            label = "Description",
            value = description,
            onValueChange = { description = it },
            placeholder = "Briefly describe your role and responsibilities",
            singleLine = false,
            maxLines = 5,
            minHeight = 120.dp
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { showRemoveDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    text = "REMOVE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = {
                    val finalEndDate = if (isCurrentPosition) "Present" else endDate
                    val updatedExperience = com.example.jobsearchapp.WorkExperience(
                        userId = currentExperience.userId,
                        company = company,
                        position = jobTitle,
                        startDate = startDate,
                        endDate = finalEndDate,
                        id = currentExperience.id,
                        description = currentExperience.description
                    )

                    val updatedList = if (workExperiences.isEmpty()) {
                        listOf(updatedExperience)
                    } else {
                        workExperiences.map {
                            if (it == currentExperience) updatedExperience else it
                        }
                    }

                    onWorkExperienceUpdated(updatedList)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E0F5C),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    text = "SAVE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            title = "Start Date",
            onDismissRequest = { showStartDatePicker = false },
            onDateSelected = { month, year ->
                startDate = "$month $year"
                showStartDatePicker = false
            }
        )
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            title = "End Date",
            onDismissRequest = { showEndDatePicker = false },
            onDateSelected = { month, year ->
                endDate = "$month $year"
                showEndDatePicker = false
            }
        )
    }

    if (showUndoChangesBottomSheet) {
        UndoWorkChangesBottomSheet(
            sheetState = undoChangesSheetState,
            onDismissRequest = { showUndoChangesBottomSheet = false },
            onConfirm = {
                showUndoChangesBottomSheet = false
                navController.popBackStack()
            },
            onUndoChanges = {
                showUndoChangesBottomSheet = false
                jobTitle = originalJobTitle.value
                company = originalCompany.value
                startDate = originalStartDate.value
                endDate = originalEndDate.value
                isCurrentPosition = originalEndDate.value == "Present"
                description = ""
            }
        )
    }

    if (showRemoveDialog) {
        RemoveWorkExperienceBottomSheet(
            onConfirm = { showRemoveDialog = false },
            onDismissRequest = { showRemoveDialog = false }
        )
    }
}