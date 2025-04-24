package com.depi.jobsearch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.depi.jobsearch.Education
import com.depi.jobsearch.UserProfile
import com.depi.jobsearch.WorkExperience
import com.depi.jobsearch.Language
import com.depi.jobsearch.Skill


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier,
    userProfile: UserProfile
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
            }
        },
        content = { padding ->
            LazyColumn(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(userProfile.name, style = MaterialTheme.typography.titleLarge)
                    Text(userProfile.email, style = MaterialTheme.typography.bodyLarge)
                }
                item {
                    SectionCard(
                        title = "About Me",
                        itemsList = if (userProfile.aboutMe.isNotEmpty())
                            listOf(userProfile.aboutMe) else emptyList(),
                        icon = Icons.Default.Person
                    )
                }
                item {
                    SectionCard(
                        title = "Work Experience",
                        itemsList = if (userProfile.workExperience.isNotEmpty()) {
                            userProfile.workExperience.map {
                                "${it.position} at ${it.company} (${it.startDate} - ${it.endDate})"}}
                            else listOf("No experience added yet"),
                        icon = Icons.Default.Work
                        )
                }
                item {
                    SectionCard(
                        title = "Education",
                        itemsList = if (userProfile.education.isNotEmpty()) {
                            userProfile.education.map {
                                "${it.degree} from ${it.institution} (${it.graduationDate})"
                            }
                        } else {
                            listOf("No education added yet")
                        },
                        icon = Icons.Default.School
                    )
                }
                item {
                    SectionCard(
                        title = "Skills",
                        itemsList = if (userProfile.skills.isNotEmpty()) {
                            userProfile.skills.map { it.skillName }
                        } else {
                            listOf("No skills added yet")
                        },
                        icon = Icons.Default.Star
                    )
                }
                item {
                    SectionCard(
                        title = "Languages",
                        itemsList = if (userProfile.languages.isNotEmpty()) {
                            userProfile.languages.map { "${it.languageName} (${it.languageLevel})" }
                        } else {
                            listOf("No languages added yet")
                        },
                        icon = Icons.Default.Language
                    )
                }
                item {
                    SectionCard(
                        title = "Appreciation",
                        itemsList = if (userProfile.appreciations.isNotEmpty()) {
                            userProfile.appreciations
                        } else {
                            listOf("No appreciations yet")
                        },
                        icon = Icons.Default.ThumbUp
                    )
                }
                item {
                    SectionCard(
                        title = "Resume",
                        itemsList = if (userProfile.resumeFilename.isNotEmpty()) {
                            listOf(userProfile.resumeFilename)
                        } else {
                            listOf("No resume uploaded yet")
                        },
                        icon = Icons.Default.Add
                    )
                }
            }
        }
    )
}

@Composable
fun previewUser(): UserProfile {
    return UserProfile(
        id = 1,
        name = "Mahmoud Hassan",
        email = "mahmoud@example.com",
        aboutMe = "Android Developer passionate about Jetpack Compose.",
        resumeFilename = "resume_mahmoud.pdf",
        workExperience = listOf(
            WorkExperience(
                userId = 1,
                company = "Tech Corp",
                position = "Android Developer",
                startDate = "2021",
                endDate = "2023"
            )
        ),
        education = listOf(
            Education(
                id = 1,
                userId = 1,
                institution = "Thebes Academy",
                degree = "Bachelor of Computer Science",
                graduationDate = "Expected: 2026"
            )
        ),
        skills = listOf(
            Skill(
                id = 1,
                userId = 1,
                skillName = "Kotlin"
            ),
            Skill(
                id = 2,
                userId = 1,
                skillName = "Jetpack Compose"
            ),
            Skill(
                id = 3,
                userId = 1,
                skillName = "Room Database"
            ),
            Skill(
                id = 4,
                userId = 1,
                skillName = "Firebase"
            )
        ),
        languages = listOf(
            Language(
                id = 1,
                userId = 1,
                languageName = "English",
                languageLevel = "Fluent"
            ),
            Language(
                id = 2,
                userId = 1,
                languageName = "Arabic",
                languageLevel = "Native"
            )
        ),
        appreciations = listOf(
            "Employee of the Month - March 2022",
            "Best App Design Award 2023"
        )
    )
}
