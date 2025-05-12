package com.example.jobsearchapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.depi.jobsearch.ui.screens.editscreens.AddLanguageScreen
import com.depi.jobsearch.ui.screens.editscreens.AddWorkExperienceScreen
import com.depi.jobsearch.ui.screens.editscreens.ChangeWorkExperienceScreen
import com.depi.jobsearch.ui.screens.editscreens.EditLanguageScreen
import com.depi.jobsearch.ui.screens.editscreens.LanguageScreen
import com.example.jobsearchapp.ui.*
import com.example.jobsearchapp.ui.proposalscreen
import com.example.jobsearchapp.ui.profile.Edit.*
import com.example.jobsearchapp.ui.profile.ProfileScreen
import com.example.jobsearchapp.ui.profile.SettingsScreen
import com.example.jobsearchapp.ui.theme.JobSearchAppTheme
import com.example.jobsearchapp.ui.theme.ThemeViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    companion object {
        const val PREFS_NAME = "JobSearchAppPrefs"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        if (!isLoggedIn && FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().signOut()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val appViewModel: AppViewModel = viewModel()
            val isPurpleTheme = themeViewModel.isPurpleTheme.collectAsState(initial = false).value

            JobSearchAppTheme(isPurpleTheme = isPurpleTheme) {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route
                val authViewModel: AuthViewModel = viewModel()

                // Animation state for transitions
                val animationState = remember { mutableStateOf(AnimationState.IDLE) }
                val transitionProgress = remember { Animatable(0f) }

                // Track previous and current routes for animation
                val previousRoute = remember { mutableStateOf<String?>(null) }
                val currentRouteState = remember { mutableStateOf<String?>(null) }

                // Update route states for transition
                LaunchedEffect(currentRoute) {
                    if (currentRoute != currentRouteState.value) {
                        previousRoute.value = currentRouteState.value
                        currentRouteState.value = currentRoute
                        // Trigger animation
                        animationState.value = AnimationState.ANIMATING
                        transitionProgress.snapTo(0f)
                        transitionProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(2000, easing = FastOutSlowInEasing)
                        )
                        animationState.value = AnimationState.IDLE
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { paddingValues ->
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                        ) {
                            // Main content with animated NavHost
                            AnimatedNavHost(
                                navController = navController,
                                startDestination = "welcome",
                                transitionProgress = transitionProgress.value,
                                animationState = animationState.value
                            ) {
                                composable("welcome") {
                                    WelcomeScreen(navController = navController)
                                }
                                composable("login") {
                                    LoginScreen(navController = navController, viewModel = authViewModel)
                                }
                                composable("signup") {
                                    SignUpScreen(navController = navController, viewModel = authViewModel)
                                }
                                composable("verifyEmail") {
                                    EmailVerificationScreen(navController = navController, viewModel = authViewModel)
                                }
                                composable("home") {
                                    val isUserLoggedIn = remember {
                                        mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
                                    }
                                    // Check if user is actually logged in
                                    LaunchedEffect(Unit) {
                                        if (!isUserLoggedIn.value) {
                                            navController.navigate("welcome") {
                                                popUpTo(navController.graph.id) { inclusive = true }
                                            }
                                        }
                                    }
                                    HomeScreen(
                                        navController = navController,
                                        viewModel = appViewModel,
                                        onThemeChange = { themeViewModel.toggleTheme() },
                                        onLogout = {
                                            // Perform logout
                                            authViewModel.signout()
                                            // Navigate to welcome screen
                                            navController.navigate("welcome") {
                                                popUpTo(navController.graph.id) { inclusive = true }
                                            }
                                        }
                                    )
                                }
                                composable(
                                    route = "jobDetails/{jobId}",
                                    arguments = listOf(navArgument("jobId") { type = NavType.StringType })
                                ) { backStackEntry ->
                                    val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                                    JobDetailsScreen(
                                        navController = navController,
                                        viewModel = appViewModel,
                                        jobId = jobId
                                    )
                                }
                                composable(
                                    route = "proposal/{jobId}",
                                    arguments = listOf(navArgument("jobId") { type = NavType.StringType })
                                ) { backStackEntry ->
                                    val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                                    val job = appViewModel.jobs.find { it.id == jobId }
                                    if (job != null) {
                                        proposalscreen(
                                            navController = navController,
                                            viewModel = appViewModel,
                                            job = job,
                                        )
                                    } else {
                                        Text(
                                            text = "Job not found",
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                }
                                // Inside your NavHost builder in MainActivity.kt
                                composable("applicationSuccess") {
                                    // Get the job title and company from the navigation arguments
                                    val jobTitle = navController.previousBackStackEntry
                                        ?.savedStateHandle?.get<String>("jobTitle") ?: "the job"
                                    val companyName = navController.previousBackStackEntry
                                        ?.savedStateHandle?.get<String>("companyName") ?: "the company"
                                    ApplicationSuccessScreen(
                                        navController = navController,
                                        jobTitle = jobTitle,
                                        companyName = companyName
                                    )
                                }
                                composable("savedJobs") {
                                    SavedJobsScreen(
                                        navController = navController,
                                        savedJobs = appViewModel.savedJobs
                                    )
                                }
                                composable("myApplications") {
                                    UserApplicationsScreen(
                                        navController = navController,
                                        viewModel = appViewModel
                                    )
                                }
                                composable(
                                    route = "recruiterProposals/{jobId}",
                                    arguments = listOf(navArgument("jobId") { type = NavType.StringType })
                                ) { backStackEntry ->
                                    val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                                    RecruiterProposalsScreen(
                                        navController = navController,
                                        viewModel = appViewModel,
                                        jobId = jobId
                                    )
                                }
                                composable("poster") {
                                    PosterScreen(
                                        navController = navController,
                                        viewModel = appViewModel
                                    )
                                }
                                composable("addJob") {
                                    AddJobDialog(
                                        navController = navController,
                                        viewModel = appViewModel
                                    )
                                }
                                composable("notifications") {
                                    NotificationsScreen(
                                        navController = navController,
                                        viewModel = appViewModel
                                    )
                                }
                                composable("chats") {
                                    ChatsListScreen(
                                        navController = navController,
                                        viewModel = appViewModel
                                    )
                                }
                                composable(
                                    route = "chat/{chatId}",
                                    arguments = listOf(navArgument("chatId") { type = NavType.StringType })
                                ) { backStackEntry ->
                                    val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                                    ChatScreen(
                                        navController = navController,
                                        viewModel = appViewModel,
                                        chatId = chatId
                                    )
                                }

                                // Add profile-related routes from the second AppNavigation
                                composable("profile") {
                                    val initialUserProfile = previewUser() // You'll need to implement this function
                                    ProfileScreen(
                                        userProfile = initialUserProfile,
                                        onEditClick = { navController.navigate("edit_about_me") },
                                        onSettingsClick = { navController.navigate("settings") },
                                        navController = navController
                                    )
                                }
                                composable("settings") {
                                    SettingsScreen(onBack = { navController.popBackStack() })
                                }
                                composable("edit_about_me") {
                                    val initialUserProfile = previewUser() // You'll need to implement this function
                                    EditAboutScreen(
                                        initialAboutText = initialUserProfile.aboutMe,
                                        onBackClick = { navController.popBackStack() },
                                        onSaveClick = { newAboutText ->
                                            // Update user profile logic here
                                            navController.popBackStack()
                                        }
                                    )
                                }
                                composable("change_work_experience") {
                                    val initialUserProfile: UserProfile = previewUser() // You'll need to implement this function
                                    ChangeWorkExperienceScreen(
                                        workExperiences = initialUserProfile.workExperience,
                                        onWorkExperienceUpdated = { updatedList ->
                                            // Update user profile logic here
                                            navController.popBackStack()
                                        },
                                        navController = navController
                                    )
                                }
                                composable("add_work_experience") {
                                    AddWorkExperienceScreen(
                                        onBack = { navController.popBackStack() },
                                        onSave = { newWorkExperience ->
                                            // Update user profile logic here
                                            navController.popBackStack()
                                        },
                                        navController = navController
                                    )
                                }
                                composable("add_education") {
                                    AddEducationScreen(
                                        onBackClick = { navController.popBackStack() },
                                        onSaveClick = { institution, degree, graduationDate ->
                                            // Update user profile logic here
                                            navController.popBackStack()
                                        }
                                    )
                                }
                                composable(
                                    "edit_education/{educationId}",
                                    arguments = listOf(navArgument("educationId") { type = NavType.LongType })
                                ) { backStackEntry ->
                                    val educationId = backStackEntry.arguments?.getLong("educationId") ?: 0L
                                    val initialUserProfile = previewUser() // You'll need to implement this function
                                    val education = initialUserProfile.education.firstOrNull { it.id == educationId }
                                        ?: return@composable

                                    ChangeEducationScreen(
                                        education = education,
                                        onCloseClick = { navController.popBackStack() },
                                        onSaveClick = { institution, degree, graduationDate ->
                                            // Update user profile logic here
                                            navController.popBackStack()
                                        },
                                        onRemoveClick = {
                                            // Remove education logic here
                                            navController.popBackStack()
                                        }
                                    )
                                }
                                composable("edit_skills") {
                                    val initialUserProfile = previewUser() // You'll need to implement this function
                                    SkillScreen(
                                        skills = initialUserProfile.skills,
                                        onEditClick = { navController.navigate("add_skills") },
                                        onBackClick = { navController.popBackStack() },
                                        onRemoveSkill = { skill ->
                                            // Remove skill logic here
                                        }
                                    )
                                }
                                composable("add_skills") {
                                    val initialUserProfile = previewUser() // You'll need to implement this
                                    AddSkillScreen(
                                        availableSkills = listOf(
                                            "Kotlin", "Java", "Android", "iOS", "Swift", "Flutter",
                                            "React Native", "JavaScript", "TypeScript", "HTML", "CSS",
                                            "SQL", "Firebase", "AWS", "Git", "Scrum", "Agile"
                                        ).map { skillName ->
                                            Skill(
                                                id = 0,
                                                userId = initialUserProfile.id,
                                                skillName = skillName
                                            )
                                        },
                                        userSkills = initialUserProfile.skills,
                                        onSkillAdded = { newSkill ->
                                            // Add skill logic here
                                        },
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                                composable("edit_languages") {
                                    val initialUserProfile = previewUser() // You'll need to implement this
                                    LanguageScreen(
                                        languages = initialUserProfile.languages,
                                        onAddLanguageClick = { navController.navigate("add_language") },
                                        onEditLanguageClick = { language ->
                                            navController.navigate("edit_language/${language.id}")
                                        },
                                        onDeleteLanguage = { language ->
                                            // Delete language logic here
                                        },
                                        onSaveClick = { navController.popBackStack() }
                                    )
                                }
                                composable("add_language") {
                                    val initialUserProfile = previewUser() // You'll need to implement this
                                    AddLanguageScreen(
                                        availableLanguages = listOf(
                                            "English", "Indonesian", "Malaysian", "French",
                                            "German", "Hindi", "Italian", "Japanese"
                                        ),
                                        onLanguageSelected = { selectedLanguage ->
                                            val newLanguage = Language(
                                                id = System.currentTimeMillis(),
                                                userId = initialUserProfile.id,
                                                languageName = selectedLanguage,
                                                languageLevel = "0,0",
                                            )
                                            // Add language logic here
                                            navController.navigate("edit_language/${newLanguage.id}")
                                        },
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                                composable(
                                    route = "edit_language/{languageId}",
                                    arguments = listOf(navArgument("languageId") { type = NavType.LongType })
                                ) { backStackEntry ->
                                    val languageId = backStackEntry.arguments?.getLong("languageId") ?: 0L
                                    val initialUserProfile = previewUser() // You'll need to implement this
                                    val language = initialUserProfile.languages.find { it.id == languageId } ?: return@composable

                                    EditLanguageScreen(
                                        language = language,
                                        onBackClick = { navController.popBackStack() },
                                        onSaveClick = { oral, written ->
                                            // Update language logic here
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }

                            // Geometric transition overlay
                            if (animationState.value == AnimationState.ANIMATING) {
                                GeometricTransitionOverlay(
                                    progress = transitionProgress.value,
                                    currentRoute = currentRouteState.value,
                                    previousRoute = previousRoute.value
                                )
                            }
                        }
                    },
                    bottomBar = {
                        if (currentRoute in listOf("home", "savedJobs", "myApplications", "poster", "chats", "profile")) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                )
            }
        }
    }

    // Add this function to provide a preview user for testing
    private fun previewUser(): UserProfile {
        return UserProfile(
            id = 1L,
            name = "John Doe",
            email = "john.doe@example.com",
            phone = "+1234567890",
            aboutMe = "Experienced software developer with a passion for mobile app development.",
            workExperience = listOf(
                WorkExperience(
                    id = 1L,
                    userId = 1L,
                    company = "Tech Solutions Inc.",
                    position = "Senior Android Developer",
                    startDate = "Jan 2020",
                    endDate = "Present",
                    description = "Leading the development of Android applications."
                )
            ),
            education = listOf(
                Education(
                    id = 1L,
                    userId = 1L,
                    institution = "University of Technology",
                    degree = "Bachelor of Computer Science",
                    graduationDate = "May 2018"
                )
            ),
            skills = listOf(
                Skill(
                    id = 1L,
                    userId = 1L,
                    skillName = "Kotlin"
                ),
                Skill(
                    id = 2L,
                    userId = 1L,
                    skillName = "Android Development"
                )
            ),
            languages = listOf(
                Language(
                    id = 1L,
                    userId = 1L,
                    languageName = "English",
                    languageLevel = "5,5"
                ),
                Language(
                    id = 2L,
                    userId = 1L,
                    languageName = "Spanish",
                    languageLevel = "3,2"
                )
            ),
            appreciations = TODO()
        )
    }
}

// Animation state enum
enum class AnimationState {
    IDLE, ANIMATING
}

// Custom animated NavHost
@Composable
fun AnimatedNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    transitionProgress: Float = 0f,
    animationState: AnimationState = AnimationState.IDLE,
    builder: NavGraphBuilder.() -> Unit,
) {
    // Screen animation properties
    val screenScale by animateFloatAsState(
        targetValue = if (animationState == AnimationState.ANIMATING) 0.9f else 1f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "screenScale"
    )
    val screenAlpha by animateFloatAsState(
        targetValue = if (animationState == AnimationState.ANIMATING) 0.6f else 1f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "screenAlpha"
    )

    Box(modifier = modifier) {
        // Apply animations to the NavHost
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = screenScale
                    scaleY = screenScale
                    alpha = screenAlpha
                },
            builder = builder
        )
    }
}

// Geometric transition overlay with shapes
@Composable
fun GeometricTransitionOverlay(
    progress: Float,
    currentRoute: String?,
    previousRoute: String?
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    // Determine transition color based on routes
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    // Create geometric shapes
    val shapes = remember {
        List(30) {
            GeometricShape(
                centerX = Random.nextFloat() * screenWidthPx,
                centerY = Random.nextFloat() * screenHeightPx,
                size = Random.nextFloat() * 60f + 20f,
                rotation = Random.nextFloat() * 360f,
                shapeType = ShapeType.values()[Random.nextInt(ShapeType.values().size)],
                color = when (Random.nextInt(3)) {
                    0 -> primaryColor
                    1 -> secondaryColor
                    else -> tertiaryColor
                }
            )
        }
    }

    // Animation effects based on transition progress
    val animatedProgress = remember(progress) { Animatable(0f) }
    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Draw geometric shapes
        Canvas(modifier = Modifier.fillMaxSize()) {
            shapes.forEach { shape ->
                // Calculate current properties based on animation progress
                val currentAlpha = if (progress < 0.5f) {
                    progress * 2
                } else {
                    (1 - progress) * 2
                }
                val currentSize = shape.size * (0.5f + currentAlpha)
                val currentX = shape.centerX
                val currentY = shape.centerY

                // Draw different shapes
                when (shape.shapeType) {
                    ShapeType.CIRCLE -> {
                        drawCircle(
                            color = shape.color.copy(alpha = currentAlpha),
                            radius = currentSize / 2,
                            center = Offset(currentX, currentY)
                        )
                    }
                    ShapeType.SQUARE -> {
                        rotate(shape.rotation + progress * 180, Offset(currentX, currentY)) {
                            drawRect(
                                color = shape.color.copy(alpha = currentAlpha),
                                topLeft = Offset(currentX - currentSize / 2, currentY - currentSize / 2),
                                size = Size(currentSize, currentSize)
                            )
                        }
                    }
                    ShapeType.TRIANGLE -> {
                        val path = Path()
                        rotate(shape.rotation + progress * 180, Offset(currentX, currentY)) {
                            // Create triangle path
                            path.moveTo(currentX, currentY - currentSize / 2)
                            path.lineTo(currentX + currentSize / 2, currentY + currentSize / 2)
                            path.lineTo(currentX - currentSize / 2, currentY + currentSize / 2)
                            path.close()
                            drawPath(
                                path = path,
                                color = shape.color.copy(alpha = currentAlpha)
                            )
                        }
                    }
                    ShapeType.PENTAGON -> {
                        val path = Path()
                        rotate(shape.rotation + progress * 180, Offset(currentX, currentY)) {
                            // Create pentagon path
                            val radius = currentSize / 2
                            val sides = 5
                            for (i in 0 until sides) {
                                val angle = (i * 2 * Math.PI / sides).toFloat()
                                val x = currentX + radius * cos(angle)
                                val y = currentY + radius * sin(angle)
                                if (i == 0) {
                                    path.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                }
                            }
                            path.close()
                            drawPath(
                                path = path,
                                color = shape.color.copy(alpha = currentAlpha)
                            )
                        }
                    }
                    ShapeType.HEXAGON -> {
                        val path = Path()
                        rotate(shape.rotation + progress * 180, Offset(currentX, currentY)) {
                            // Create hexagon path
                            val radius = currentSize / 2
                            val sides = 6
                            for (i in 0 until sides) {
                                val angle = (i * 2 * Math.PI / sides).toFloat()
                                val x = currentX + radius * cos(angle)
                                val y = currentY + radius * sin(angle)
                                if (i == 0) {
                                    path.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                }
                            }
                            path.close()
                            drawPath(
                                path = path,
                                color = shape.color.copy(alpha = currentAlpha)
                            )
                        }
                    }
                    ShapeType.STAR -> {
                        val path = Path()
                        rotate(shape.rotation + progress * 180, Offset(currentX, currentY)) {
                            // Create star path
                            val outerRadius = currentSize / 2
                            val innerRadius = outerRadius * 0.4f
                            val points = 5
                            for (i in 0 until points * 2) {
                                val radius = if (i % 2 == 0) outerRadius else innerRadius
                                val angle = (i * Math.PI / points).toFloat()
                                val x = currentX + radius * cos(angle)
                                val y = currentY + radius * sin(angle)
                                if (i == 0) {
                                    path.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                }
                            }
                            path.close()
                            drawPath(
                                path = path,
                                color = shape.color.copy(alpha = currentAlpha)
                            )
                        }
                    }
                    ShapeType.LINE -> {
                        rotate(shape.rotation + progress * 180, Offset(currentX, currentY)) {
                            drawLine(
                                color = shape.color.copy(alpha = currentAlpha),
                                start = Offset(currentX - currentSize / 2, currentY),
                                end = Offset(currentX + currentSize / 2, currentY),
                                strokeWidth = 4f,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }

            // Draw a central geometric pattern
            val centerX = size.width / 2
            val centerY = size.height / 2
            val maxSize = minOf(size.width, size.height) * 0.4f

            // Draw concentric shapes
            for (i in 0 until 3) {
                val shapeSize = maxSize * (1 - i * 0.25f)
                val shapeAlpha = if (progress < 0.5f) {
                    progress * 2 * (1 - i * 0.2f)
                } else {
                    (1 - progress) * 2 * (1 - i * 0.2f)
                }

                // Rotate based on progress
                rotate(progress * 180 * (i + 1), Offset(centerX, centerY)) {
                    when (i % 3) {
                        0 -> { // Hexagon
                            val path = Path()
                            val sides = 6
                            for (j in 0 until sides) {
                                val angle = (j * 2 * Math.PI / sides).toFloat()
                                val x = centerX + shapeSize * cos(angle)
                                val y = centerY + shapeSize * sin(angle)
                                if (j == 0) {
                                    path.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                }
                            }
                            path.close()
                            drawPath(
                                path = path,
                                color = primaryColor.copy(alpha = shapeAlpha),
                                style = Stroke(width = 3f)
                            )
                        }
                        1 -> { // Square
                            drawRect(
                                color = secondaryColor.copy(alpha = shapeAlpha),
                                topLeft = Offset(centerX - shapeSize / 2, centerY - shapeSize / 2),
                                size = Size(shapeSize, shapeSize),
                                style = Stroke(width = 3f)
                            )
                        }
                        2 -> { // Circle
                            drawCircle(
                                color = tertiaryColor.copy(alpha = shapeAlpha),
                                radius = shapeSize / 2,
                                center = Offset(centerX, centerY),
                                style = Stroke(width = 3f)
                            )
                        }
                    }
                }
            }
        }

        // Central pulse effect
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val pulseSize by animateFloatAsState(
                targetValue = if (progress < 0.5f) progress * 2 else (1 - progress) * 2,
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                label = "pulseSize"
            )
            Box(
                modifier = Modifier
                    .size(200.dp * pulseSize)
                    .alpha(0.3f * (1 - progress * progress))
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
            )
        }
    }
}

// Geometric shape data class
data class GeometricShape(
    val centerX: Float,
    val centerY: Float,
    val size: Float,
    val rotation: Float,
    val shapeType: ShapeType,
    val color: Color
)

// Shape types enum
enum class ShapeType {
    CIRCLE, SQUARE, TRIANGLE, PENTAGON, HEXAGON, STAR, LINE
}

// Model classes needed for the profile section
data class UserProfile(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val aboutMe: String,
    val workExperience: List<WorkExperience>,
    val education: List<Education>,
    val skills: List<Skill>,
    val languages: List<Language>,
    val appreciations: List<String>,
    val location: String = ""
    )

data class WorkExperience(
    val id: Long,
    val userId: Long,
    val company: String,
    val position: String,
    val startDate: String,
    val endDate: String,
    val description: String
)

data class Education(
    val id: Long,
    val userId: Long,
    val institution: String,
    val degree: String,
    val graduationDate: String
)

data class Skill(
    val id: Long,
    val userId: Long,
    val skillName: String
)

data class Language(
    val id: Long,
    val userId: Long,
    val languageName: String,
    val languageLevel: String // Format: "oral,written" where each is a number from 0-5
)
