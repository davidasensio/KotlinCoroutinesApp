# Navigation Compose 3 - Type-Safe Navigation

## What is Navigation Compose?

Navigation Compose is the Jetpack Navigation component designed for Compose-based Android apps. Version 3 introduces type-safe navigation using Kotlin serialization, making navigation more robust and compile-time safe.

## Official Resources

- **Official Documentation:** https://developer.android.com/jetpack/compose/navigation
- **Release Notes:** https://developer.android.com/jetpack/androidx/releases/navigation
- **Samples:** https://github.com/android/compose-samples

## Configuration

### Dependencies Location
- **Version Catalog:** `gradle/libs.versions.toml`
- **App Module:** `app/build.gradle.kts`

### Current Setup
```kotlin
// Version Catalog
navigationCompose = "2.8.0"

// Dependencies
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
```

## Project Structure

### Navigation Package
```
app/src/main/java/com/handysparksoft/kotlincoroutines/
└── navigation/
    ├── AppNavigation.kt          # Main NavHost setup
    ├── NavigationRoutes.kt       # Route definitions
    └── NavigationExtensions.kt   # Helper extensions (optional)
```

## Core Concepts

### 1. Navigation Routes

Define routes as sealed classes for type safety:

```kotlin
sealed class NavigationRoutes(val route: String) {
    data object Home : NavigationRoutes("home")

    data object Detail : NavigationRoutes("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }

    data object Profile : NavigationRoutes("profile")
}
```

### 2. NavHost Setup

Main navigation setup with `NavHost`:

```kotlin
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Home.route
    ) {
        composable(NavigationRoutes.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = NavigationRoutes.Detail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            DetailScreen(id = id, navController = navController)
        }
    }
}
```

### 3. Navigation in Screens

Navigate between screens:

```kotlin
@Composable
fun HomeScreen(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(NavigationRoutes.Detail.createRoute("123"))
        }
    ) {
        Text("Go to Detail")
    }
}
```

## Common Navigation Patterns

### Navigate to Screen
```kotlin
navController.navigate(NavigationRoutes.Profile.route)
```

### Navigate with Arguments
```kotlin
navController.navigate(NavigationRoutes.Detail.createRoute(userId))
```

### Navigate and Clear Back Stack
```kotlin
navController.navigate(NavigationRoutes.Home.route) {
    popUpTo(navController.graph.startDestinationId) {
        inclusive = true
    }
}
```

### Navigate Back
```kotlin
navController.popBackStack()
```

### Navigate Back to Specific Destination
```kotlin
navController.popBackStack(NavigationRoutes.Home.route, inclusive = false)
```

## Integration with Hilt

### ViewModel Injection in Screens

Use `hiltViewModel()` to inject ViewModels:

```kotlin
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onNavigate = { destination ->
            navController.navigate(destination)
        }
    )
}
```

### Navigation from ViewModel (Anti-pattern)

**Avoid:** Don't pass NavController to ViewModels.

**Instead:** Use callbacks/events:

```kotlin
// ViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onItemClick(id: String) {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToDetail(id))
        }
    }
}

// Screen
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToDetail -> {
                    navController.navigate(NavigationRoutes.Detail.createRoute(event.id))
                }
            }
        }
    }

    // UI code
}
```

## Advanced Features

### Deep Links

Add deep link support:

```kotlin
composable(
    route = NavigationRoutes.Detail.route,
    deepLinks = listOf(
        navDeepLink { uriPattern = "myapp://detail/{id}" }
    )
) { backStackEntry ->
    // Screen implementation
}
```

### Nested Navigation

Create nested navigation graphs:

```kotlin
fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        startDestination = "login",
        route = "auth"
    ) {
        composable("login") { LoginScreen() }
        composable("register") { RegisterScreen() }
    }
}
```

### Bottom Navigation

Integrate with bottom navigation:

```kotlin
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = { navController.navigate(item.route) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Composables
        }
    }
}
```

## Argument Types

### Supported Types

- String (default)
- Int
- Long
- Float
- Boolean

### Example with Multiple Arguments

```kotlin
sealed class NavigationRoutes(val route: String) {
    data object Search : NavigationRoutes("search/{query}/{category}") {
        fun createRoute(query: String, category: String) = "search/$query/$category"
    }
}

// In NavHost
composable(
    route = NavigationRoutes.Search.route,
    arguments = listOf(
        navArgument("query") { type = NavType.StringType },
        navArgument("category") { type = NavType.StringType }
    )
) { backStackEntry ->
    val query = backStackEntry.arguments?.getString("query")
    val category = backStackEntry.arguments?.getString("category")
    SearchScreen(query = query, category = category)
}
```

### Optional Arguments

```kotlin
sealed class NavigationRoutes(val route: String) {
    data object Filter : NavigationRoutes("filter?sortBy={sortBy}") {
        fun createRoute(sortBy: String? = null) =
            if (sortBy != null) "filter?sortBy=$sortBy" else "filter"
    }
}

// In NavHost
composable(
    route = NavigationRoutes.Filter.route,
    arguments = listOf(
        navArgument("sortBy") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    )
) { backStackEntry ->
    val sortBy = backStackEntry.arguments?.getString("sortBy")
    FilterScreen(sortBy = sortBy)
}
```

## Testing

### Test Navigation

```kotlin
@Test
fun testNavigation() {
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    composeTestRule.setContent {
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        AppNavigation(navController = navController)
    }

    // Verify initial destination
    assertEquals(NavigationRoutes.Home.route, navController.currentBackStackEntry?.destination?.route)

    // Navigate
    navController.navigate(NavigationRoutes.Detail.createRoute("123"))

    // Verify navigation
    assertEquals("detail/{id}", navController.currentBackStackEntry?.destination?.route)
}
```

## Best Practices

1. **Use Sealed Classes:** Define routes as sealed classes for type safety
2. **Separate Concerns:** Keep navigation logic in composables, not ViewModels
3. **Single NavController:** Pass NavController from top-level, don't create multiple
4. **Callback Pattern:** Use callbacks from screens to handle navigation
5. **Route Constants:** Use route constants, not string literals
6. **Test Navigation:** Write tests for navigation flows
7. **Deep Links:** Plan deep link structure early

## Common Issues

### NavController Not Found
**Solution:** Ensure NavController is passed from parent composable

### Back Stack Issues
**Solution:** Use `popUpTo` and `launchSingleTop` correctly

### State Loss on Navigation
**Solution:** Use `rememberSaveable` or ViewModel for state

### Navigation During Composition
**Solution:** Use `LaunchedEffect` or event-based navigation

## Performance Tips

1. **Lazy Loading:** Screens are lazily created
2. **State Hoisting:** Hoist state to survive navigation
3. **ViewModel:** Use ViewModels for data that survives navigation
4. **Arguments:** Prefer simple arguments over complex objects

## Migration from XML Navigation

If migrating from XML-based navigation:
1. Convert navigation graphs to composable functions
2. Replace fragments with composables
3. Update arguments to use NavType
4. Replace SafeArgs with route builders

## Related Documentation

- [Hilt Documentation](./hilt.md) - Dependency injection with ViewModels
- [GitHub Actions](./github-actions.md) - CI/CD for testing navigation

## Setup Guide

To set up Navigation Compose from scratch, see: [Add Jetpack Navigation 3 and Hilt Prompt](../prompts/03-add-jetpack-navigation-hilt.md)
