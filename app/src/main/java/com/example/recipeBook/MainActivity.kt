package com.example.recipeBook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
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
import com.example.recipeBook.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppScreen()
            }
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
    )
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
    }
}

private val bottomNavigationItems = listOf(
    BottomNavigationItem(
        mainScreenContent = MainScreenContent.Create,
        selectedIcon = Icons.Filled.Create,
        unselectedIcon = Icons.Outlined.Create
    ),
    BottomNavigationItem(
        mainScreenContent = MainScreenContent.Find,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),
    BottomNavigationItem(
        mainScreenContent = MainScreenContent.Book,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star
    )
)


sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Search : Screen("search", "Search", Icons.Filled.Search)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}