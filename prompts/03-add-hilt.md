# Prompt: Add Hilt Dependency Injection

## Objective
Integrate Hilt for dependency injection into the Android project.

## Instructions

Please add Hilt dependency injection to this Android project with proper configuration and basic setup.

### 1. Update Version Catalog

Add to `gradle/libs.versions.toml`:

**Versions section:**
```toml
[versions]
hilt = "2.51"  # Latest stable version
ksp = "2.0.21-1.0.25"  # Match Kotlin version
```

**Libraries section:**
```toml
[libraries]
# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-android-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
```

**Plugins section:**
```toml
[plugins]
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
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

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                // Your content
            }
        }
    }
}
```

### 6. Create Basic Hilt Modules

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

### 7. Example ViewModel with Hilt

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

### 8. Update AndroidManifest.xml

Ensure the Application class is set:
```xml
<application
    android:name=".CoroutinesApp"
    ...>
```

### 9. Add Proguard Rules (if needed)

Create/update `app/proguard-rules.pro`:
```proguard
# Hilt
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
```

## Expected Outcome

After running this prompt, the project should have:
- ✅ Hilt dependency injection fully integrated
- ✅ Application class annotated with @HiltAndroidApp
- ✅ MainActivity annotated with @AndroidEntryPoint
- ✅ Example Hilt modules for coroutine dispatchers
- ✅ Example ViewModel with Hilt injection
- ✅ KSP annotation processing configured

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
4. **ViewModels injected** - Dependencies are properly injected

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
5. **Constructor Injection:** Prefer constructor injection over field injection

## Related Documentation
- [Hilt Documentation](../docs/hilt.md)

## Additional Resources
- [Hilt Official Docs](https://developer.android.com/training/dependency-injection/hilt-android)
- [Hilt Codelabs](https://developer.android.com/codelabs/android-hilt)
