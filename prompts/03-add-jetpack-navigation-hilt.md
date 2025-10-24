# Prompt: Add Jetpack Navigation 3 and Hilt

## Objective
Integrate Navigation Compose 3 for navigation and Hilt for dependency injection into the Android project.

## Instructions

Please add Jetpack Navigation 3 and Hilt dependency injection to this Android project with proper configuration and basic setup.

### 1. Update Version Catalog

Add to `gradle/libs.versions.toml`:

**Versions section:**
```toml
[versions]
hilt = "2.51"  # Latest stable version
hiltNavigationCompose = "1.2.0"
navigationCompose = "2.8.0"  # Navigation Compose 3
```

**Libraries section:**
```toml
[libraries]
# Navigation
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-android-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
```

**Plugins section:**
```toml
[plugins]
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version = "2.0.21-1.0.25" }  # Match Kotlin version
```

### 2. Configure Root build.gradle.kts

Add plugins (but don't apply):
```kotlin
plugins {
    // ... existing plugins
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
```

### 3. Configure App Module build.gradle.kts

**Apply plugins:**
```kotlin
plugins {
    // ... existing plugins
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}
```

**Add dependencies:**
```kotlin
dependencies {
    // ... existing dependencies

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
}
```

**Configure KSP (if needed for generated code visibility):**
```kotlin
android {
    // ... existing config

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}
```

### 4. Setup Application Class for Hilt

Modify `CoroutinesApp.kt`:
```kotlin
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CoroutinesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
```

### 5. Setup MainActivity for Hilt

Modify `MainActivity.kt`:
```kotlin
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }
}
```

### 6. Create Navigation Setup

**Create navigation package structure:**
```
app/src/main/java/com/handysparksoft/kotlincoroutines/
└── navigation/
    ├── AppNavigation.kt
    ├── NavigationRoutes.kt
    └── NavigationExtensions.kt (optional)
```

**NavigationRoutes.kt:**
```kotlin
package com.handysparksoft.kotlincoroutines.navigation

sealed class NavigationRoutes(val route: String) {
    data object Home : NavigationRoutes("home")
    data object Detail : NavigationRoutes("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
    // Add more routes as needed
}
```

**AppNavigation.kt:**
```kotlin
package com.handysparksoft.kotlincoroutines.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Home.route
    ) {
        composable(NavigationRoutes.Home.route) {
            // HomeScreen(navController = navController)
            // Placeholder - implement your home screen
        }

        composable(
            route = NavigationRoutes.Detail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            // DetailScreen(id = id, navController = navController)
            // Placeholder - implement your detail screen
        }
    }
}
```

### 7. Create Basic Hilt Modules

**Create di package structure:**
```
app/src/main/java/com/handysparksoft/kotlincoroutines/
└── di/
    ├── AppModule.kt
    └── NetworkModule.kt (example)
```

**AppModule.kt:**
```kotlin
package com.handysparksoft.kotlincoroutines.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @IoDispatcher
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    @Singleton
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DefaultDispatcher
    @Provides
    @Singleton
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
```

### 8. Example ViewModel with Hilt

**Example structure:**
```kotlin
package com.handysparksoft.kotlincoroutines.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handysparksoft.kotlincoroutines.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(ioDispatcher) {
            // Load data
            _uiState.value = HomeUiState.Success("Data loaded")
        }
    }
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val data: String) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
```

### 9. Example Screen with ViewModel Injection

**Example structure:**
```kotlin
package com.handysparksoft.kotlincoroutines.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onNavigateToDetail = { id ->
            navController.navigate(NavigationRoutes.Detail.createRoute(id))
        }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onNavigateToDetail: (String) -> Unit
) {
    // UI implementation
}
```

### 10. Update AndroidManifest.xml

Ensure the Application class is set:
```xml
<application
    android:name=".CoroutinesApp"
    ...>
```

### 11. Add Proguard Rules (if needed)

Create/update `app/proguard-rules.pro`:
```proguard
# Hilt
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# Navigation
-keepnames class androidx.navigation.fragment.NavHostFragment
```

## Expected Outcome

After running this prompt, the project should have:
- ✅ Navigation Compose 3 configured and ready to use
- ✅ Hilt dependency injection fully integrated
- ✅ Application class annotated with @HiltAndroidApp
- ✅ MainActivity annotated with @AndroidEntryPoint
- ✅ Basic navigation setup with routes
- ✅ Example Hilt modules for coroutine dispatchers
- ✅ Example ViewModel with Hilt injection
- ✅ Navigation and Hilt working together with hiltViewModel()

## Verification Commands

```bash
# Build the project to verify Hilt code generation
./gradlew app:assembleDebug

# Run tests to ensure everything works
./gradlew app:testDebugUnitTest

# Verify code style still passes
./gradlew verifyStyle
```

## Verification Steps

1. **Build succeeds** - Hilt annotation processors generate code
2. **No compilation errors** - All Hilt annotations resolved
3. **Application starts** - Hilt initializes without crashes
4. **Navigation works** - Can navigate between screens
5. **ViewModels injected** - hiltViewModel() provides instances

## Common Issues

### KSP Version Mismatch
Ensure KSP version matches Kotlin version:
- Kotlin 2.0.21 → KSP 2.0.21-1.0.25

### Build Error: "Unresolved reference: hilt"
- Clean and rebuild: `./gradlew clean build`
- Invalidate caches in Android Studio

### Hilt Cannot Find Generated Code
- Ensure KSP plugin is applied before Hilt plugin
- Check generated code in `build/generated/ksp/`

### Navigation Not Working
- Verify NavHost startDestination matches a route
- Check route strings are consistent

## Architecture Integration

### Clean Architecture Layers with Hilt

**Data Layer Modules:**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}
```

**Domain Layer (Use Cases):**
```kotlin
class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<Result<User>> = flow {
        // Implementation
    }.flowOn(dispatcher)
}
```

**Presentation Layer (ViewModels):**
Already covered in example above with @HiltViewModel.

## Best Practices

1. **Scoping:** Use appropriate component scopes (@Singleton, @ActivityScoped, etc.)
2. **Qualifiers:** Use custom qualifiers for multiple bindings of same type
3. **Module Organization:** Separate modules by feature or layer
4. **Testing:** Use Hilt testing library for instrumented tests
5. **Navigation:** Use type-safe navigation arguments when possible

## Related Documentation
- [Navigation Compose Documentation](../docs/navigation-compose.md) (will be created)
- [Hilt Documentation](../docs/hilt.md) (will be created)

## Additional Resources
- [Navigation Compose Official Docs](https://developer.android.com/jetpack/compose/navigation)
- [Hilt Official Docs](https://developer.android.com/training/dependency-injection/hilt-android)
- [Navigation Compose 3 Release Notes](https://developer.android.com/jetpack/androidx/releases/navigation)
