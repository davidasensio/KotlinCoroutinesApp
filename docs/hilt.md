# Hilt - Dependency Injection for Android

## What is Hilt?

Hilt is a dependency injection library for Android built on top of Dagger. It reduces boilerplate code and provides a standard way to incorporate Dagger dependency injection into Android applications. Hilt is specifically designed for Android and integrates seamlessly with Android components and Jetpack libraries.

## Official Resources

- **Official Documentation:** https://developer.android.com/training/dependency-injection/hilt-android
- **GitHub Repository:** https://github.com/google/dagger
- **Codelabs:** https://developer.android.com/codelabs/android-hilt

## Configuration

### Dependencies Location
- **Version Catalog:** `gradle/libs.versions.toml`
- **App Module:** `app/build.gradle.kts`
- **Root:** `build.gradle.kts` (plugin)

### Current Setup
```kotlin
// Version Catalog
hilt = "2.51"
ksp = "2.0.21-1.0.25"  // Must match Kotlin version

// Dependencies
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-android-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
```

## Project Structure

### DI Package
```
app/src/main/java/com/handysparksoft/kotlincoroutines/
└── di/
    ├── AppModule.kt           # Application-level dependencies
    ├── NetworkModule.kt       # Network-related dependencies
    ├── DatabaseModule.kt      # Database dependencies
    └── RepositoryModule.kt    # Repository bindings
```

## Core Concepts

### 1. Application Setup

Annotate your Application class with `@HiltAndroidApp`:

```kotlin
@HiltAndroidApp
class CoroutinesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialization code
    }
}
```

### 2. Android Component Integration

Annotate Android components to enable injection:

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Hilt can now inject dependencies here
}

@AndroidEntryPoint
class MyFragment : Fragment() {
    // Hilt can inject here too
}
```

### 3. ViewModel Injection

Use `@HiltViewModel` for ViewModels:

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    // ViewModel implementation
}
```

In Composable:
```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Use viewModel
}
```

### 4. Modules

Define dependencies using modules:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
```

### 5. Bindings

Bind interfaces to implementations:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}
```

## Hilt Components and Scopes

### Component Hierarchy

| Component | Scope | Created At | Destroyed At |
|-----------|-------|------------|--------------|
| SingletonComponent | @Singleton | Application#onCreate() | Application destroyed |
| ActivityRetainedComponent | @ActivityRetainedScoped | Activity#onCreate() | Activity#onDestroy() |
| ViewModelComponent | @ViewModelScoped | ViewModel created | ViewModel cleared |
| ActivityComponent | @ActivityScoped | Activity#onCreate() | Activity#onDestroy() |
| FragmentComponent | @FragmentScoped | Fragment#onAttach() | Fragment#onDestroy() |
| ViewComponent | @ViewScoped | View#super() | View destroyed |
| ServiceComponent | @ServiceScoped | Service#onCreate() | Service#onDestroy() |

### Choosing the Right Scope

```kotlin
// Singleton - Lives for entire app lifecycle
@Singleton
class ApiService @Inject constructor()

// ActivityRetainedScoped - Survives configuration changes
@ActivityRetainedScoped
class UserSession @Inject constructor()

// ViewModelScoped - Lives as long as ViewModel
@ViewModelScoped
class DataCache @Inject constructor()

// ActivityScoped - Recreated on configuration changes
@ActivityScoped
class ThemeManager @Inject constructor()
```

## Common Patterns

### 1. Coroutine Dispatchers

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}

// Qualifiers
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
```

### 2. Retrofit/Network Module

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
```

### 3. Room Database Module

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}
```

### 4. Repository Pattern

```kotlin
// Interface
interface UserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun getUserById(id: String): User?
}

// Implementation
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UserRepository {

    override fun getUsers(): Flow<List<User>> = flow {
        val cachedUsers = userDao.getAllUsers()
        emit(cachedUsers)

        val remoteUsers = apiService.getUsers()
        userDao.insertAll(remoteUsers)
        emit(remoteUsers)
    }.flowOn(dispatcher)

    override suspend fun getUserById(id: String): User? = withContext(dispatcher) {
        userDao.getUserById(id) ?: apiService.getUserById(id)
    }
}

// Module
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}
```

### 5. Use Case with Hilt

```kotlin
class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(userId: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try {
            repository.getUserById(userId)?.let { user ->
                emit(Result.Success(user))
            } ?: emit(Result.Error("User not found"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(dispatcher)
}

// Usage in ViewModel
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<Result<User>>(Result.Loading)
    val userState = _userState.asStateFlow()

    fun loadUser(userId: String) {
        viewModelScope.launch {
            getUserUseCase(userId).collect { result ->
                _userState.value = result
            }
        }
    }
}
```

## Qualifiers

Use qualifiers when you need multiple instances of the same type:

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BasicOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @BasicOkHttpClient
    fun provideBasicOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    @AuthInterceptorOkHttpClient
    fun provideAuthOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }
}

// Usage
class ApiService @Inject constructor(
    @AuthInterceptorOkHttpClient private val client: OkHttpClient
) {
    // Use authenticated client
}
```

## Testing with Hilt

### Unit Tests

For unit tests, don't use Hilt. Create dependencies manually:

```kotlin
class UserViewModelTest {
    private lateinit var viewModel: UserViewModel
    private val mockRepository: UserRepository = mockk()

    @Before
    fun setup() {
        viewModel = UserViewModel(
            getUserUseCase = GetUserUseCase(mockRepository, Dispatchers.Unconfined)
        )
    }

    @Test
    fun `test load user success`() = runTest {
        // Test implementation
    }
}
```

### Instrumented Tests

Use Hilt testing library:

```kotlin
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testSomething() {
        // Test with Hilt injection
    }
}
```

### Replace Bindings in Tests

```kotlin
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {

    @Binds
    abstract fun bindTestUserRepository(
        impl: FakeUserRepository
    ): UserRepository
}
```

## Best Practices

1. **Use Constructor Injection:** Prefer `@Inject constructor` over field injection
2. **Scope Appropriately:** Use the narrowest scope possible
3. **Avoid God Modules:** Split modules by feature or layer
4. **Use Qualifiers:** For multiple instances of same type
5. **Interface Injection:** Inject interfaces, not implementations
6. **Test Without Hilt:** Unit tests shouldn't require Hilt
7. **Document Scopes:** Comment why you chose a particular scope
8. **Lazy Initialization:** Use `Lazy<T>` for expensive dependencies

## Common Issues

### Hilt Cannot Find Generated Code
**Solution:** Clean and rebuild, ensure KSP plugin is applied

### Duplicate Bindings
**Solution:** Check for multiple `@Provides` for same type without qualifiers

### Scope Mismatch
**Solution:** Dependency scope must be equal or wider than dependent

### Missing @AndroidEntryPoint
**Solution:** Add annotation to Activity/Fragment where injection is needed

## Performance Considerations

1. **Singleton Carefully:** Don't overuse @Singleton
2. **Lazy Loading:** Use `Lazy<T>` or `Provider<T>` for expensive objects
3. **Avoid Circular Dependencies:** Refactor code structure
4. **Profile Injection:** Use Android Profiler to check injection overhead

## Migration Tips

### From Manual DI
1. Start with Application class annotation
2. Add modules one by one
3. Replace manual factories with `@Inject`
4. Test incrementally

### From Dagger
1. Replace Dagger components with Hilt components
2. Update scopes to Hilt scopes
3. Use `@InstallIn` instead of component builders
4. Remove custom component implementations

## Related Documentation

- [Navigation Compose](./navigation-compose.md) - Using hiltViewModel() with Navigation
- [CLAUDE.md](../CLAUDE.md) - Project architecture and naming conventions

## Setup Guide

To set up Hilt from scratch, see: [Add Jetpack Navigation 3 and Hilt Prompt](../prompts/03-add-jetpack-navigation-hilt.md)
