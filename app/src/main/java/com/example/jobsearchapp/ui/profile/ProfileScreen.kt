package com.example.jobsearchapp.ui.profile

import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: com.example.jobsearchapp.UserProfile,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 0.dp,
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header
            item {
                Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                    ProfileHeader(
                        userProfile = userProfile,
                        onEditClick = onEditClick
                    )
                }
            }

            // About Me Section
            item {
                AboutMeSection(
                    aboutMe = userProfile.aboutMe,
                    onEditClick = { navController.navigate("edit_about_me") }
                )
            }

            // Work Experience Section
            item {
                WorkExperienceSection(
                    workExperiences = userProfile.workExperience,
                    onEditClick = { navController.navigate("change_work_experience") },
                    onAddClick = { navController.navigate("add_work_experience") }
                )
            }

            // Education Section
            item {
                EducationSection(
                    educations = userProfile.education,
                    onEditClick = { /* Handle edit */ },
                    onAddClick = { navController.navigate("add_education") },
                    navController = navController
                )
            }

            // Skills Section
            item {
                SkillsSection(
                    skills = userProfile.skills.map { it.skillName },
                    onEditClick = { navController.navigate("edit_skills") }
                )
            }

            // Languages Section
            item {
                LanguagesSection(
                    languages = userProfile.languages.map { it.languageName },
                    onAddClick = { navController.navigate("add_language") },
                    onEditClick = { languageName ->
                        userProfile.languages.firstOrNull { it.languageName == languageName }?.id?.let { id ->
                            navController.navigate("edit_language/$id")
                        }
                    }
                )
            }

            // Appreciations Section
            item {
                AppreciationSection(
                    appreciations = userProfile.appreciations,
                    onEditClick = onSettingsClick
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    val navController = rememberNavController()
//    ProfileScreen(
//        userProfile = UserProfileWithRelations(
//            profile = UserProfile(
//                id = 1,
//                name = "Sample User",
//                email = "user@example.com",
//                aboutMe = "Sample about me text",
//                resumeFilename = "",
//                appreciations = listOf("Sample appreciation"),
//                location = "Sample Location"
//            ),
//            workExperience = listOf(
//                WorkExperience(
//                    id = 1,
//                    userId = 1,
//                    company = "Sample Company",
//                    position = "Sample Position",
//                    startDate = "2020",
//                    endDate = "2023"
//                )
//            ),
//            education = listOf(
//                Education(
//                    id = 1,
//                    userId = 1,
//                    institution = "Sample University",
//                    degree = "Sample Degree",
//                    graduationDate = "2019"
//                )
//            ),
//            skills = listOf(Skill(1, 1, "Sample Skill")),
//            languages = listOf(Language(1, 1, "English", "Fluent"))
//        ),
//        onEditClick = {},
//        onSettingsClick = {},
//        navController = navController
//    )
//}

@Composable
fun previewUser(
    id: Long = 1,
    name: String = "Mahmoud Hassan",
    email: String = "mahmoud@example.com",
    aboutMe: String = "Android Developer passionate about Jetpack Compose.",
    resumeFilename: String = "resume_mahmoud.pdf",
    workExperience: List<WorkExperience> = listOf(
        WorkExperience(
            userId = 1,
            company = "Tech Corp",
            position = "Android Developer",
            startDate = "2021",
            endDate = "2023"
        )
    ),
    education: List<Education> = listOf(
        Education(
            id = 1,
            userId = 1,
            institution = "Thebes Academy",
            degree = "Bachelor of Computer Science",
            graduationDate = "Expected: 2026"
        )
    ),
    skills: List<Skill> = listOf(
        Skill(id = 1, userId = 1, skillName = "Kotlin"),
        Skill(id = 2, userId = 1, skillName = "Jetpack Compose")
    ),
    languages: List<Language> = listOf(
        Language(id = 1, userId = 1, languageName = "English", languageLevel = "Fluent"),
        Language(id = 2, userId = 1, languageName = "Arabic", languageLevel = "Native")
    ),
    appreciations: List<String> = listOf(
        "Employee of the Month - March 2022",
        "Best App Design Award 2023"
    ),
    location: String = "California, USA"
): UserProfile {
    return UserProfile(
        id = id,
        name = name,
        email = email,
        aboutMe = aboutMe,
        resumeFilename = resumeFilename,
        workExperience = workExperience,
        education = education,
        skills = skills,
        languages = languages,
        appreciations = appreciations,
        location = location
    )
}