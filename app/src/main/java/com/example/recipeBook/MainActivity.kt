package com.example.recipeBook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.DestinationMainContent
import com.example.core.navigation.MainScreenContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
        }
    }
}

@Composable
fun AppScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        content = { contentPadding ->
            MainContent(
                contentPadding = contentPadding,
                navController = navController,
                startDestination = DestinationMainContent.FIND_ROUTE
            )
        },
        bottomBar = { BottomNavigationBar(navController, navBackStackEntry) }
    ) /*{ innerPadding ->
        NavigationHost(navController, Modifier.padding(innerPadding))
    }*/
}

@Composable
private fun MainContent(
    contentPadding: PaddingValues,
    navController: NavHostController,
    startDestination: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        MainContentNavGraph(
            navController = navController,
            startDestination = startDestination
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController,
                        navBackStackEntry: NavBackStackEntry?) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        bottomNavigationItems.forEach { item ->
            val currentDestination = navBackStackEntry?.destination
            NavigationBarItem(
                selected = currentDestination?.route == item.mainScreenContent.route,
                onClick = {
                    conditionSwitchScreen(
                        navController,
                        currentDestination?.route,
                        item.mainScreenContent.route
                    )
                },
                label = {
                    Text(
                        text = item.mainScreenContent.nameScreen,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (currentDestination?.route == item.mainScreenContent.route) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.mainScreenContent.nameScreen
                    )
                }
            )
        }
        /*val items = listOf(
            Screen.Home,
            Screen.Search,
            Screen.Profile
        )
        items.forEach { screen ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) }
            )
        }*/
    }
}

private val bottomNavigationItems = listOf(
    BottomNavigationItem(
        mainScreenContent = MainScreenContent.Create,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    ),
    BottomNavigationItem(
        mainScreenContent = MainScreenContent.Find,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    ),
    BottomNavigationItem(
        mainScreenContent = MainScreenContent.Book,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)


/*@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}*/

/*@Composable
fun HomeScreen() {
    CenteredText("Home Screen")
}*/

/*@Composable
fun SearchScreen() {
    CenteredText("Search Screen")
}*/

/*@Composable
fun ProfileScreen() {
    CenteredText("Profile Screen")
}*/

@Composable
fun CenteredText(text: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Search : Screen("search", "Search", Icons.Filled.Search)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}