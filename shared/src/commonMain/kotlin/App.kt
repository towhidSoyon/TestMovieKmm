import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.ExperimentalCoroutinesApi
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import navigation.Navigation
import navigation.NavigationScreen
import navigation.currentRoute
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.FloatingActionBackground
import ui.AppViewModel
import ui.component.AppBarWithArrow
import ui.component.ProgressIndicator
import ui.component.SearchBar
import ui.component.SearchUI
import util.AppString
import util.pagingLoadingState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
internal fun App(viewModel: AppViewModel = AppViewModel()) {
    val navigator = rememberNavigator()
    val isAppBarVisible = remember { mutableStateOf(true) }
    val searchProgressBar = remember { mutableStateOf(false) }

    BackHandler(isAppBarVisible.value.not()) {
        isAppBarVisible.value = true
    }
    MaterialTheme {
        /*var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                greetingText = "Hello, ${getPlatformName()}"
                showImage = !showImage
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(showImage) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
        }*/

        Scaffold(topBar = {
            if (isAppBarVisible.value.not()){
                SearchBar(viewModel){
                    isAppBarVisible.value = true
                }
            } else{
                AppBarWithArrow(
                    AppString.APP_TITLE, isBackEnable = isBackButtonEnable(navigator)
                ) {
                    navigator.goBack()
                }
            }
        }, floatingActionButton = {
            when (currentRoute(navigator)){
                NavigationScreen.Home.route, NavigationScreen.Popular.route, NavigationScreen.TopRated.route, NavigationScreen.Upcoming.route -> {
                    FloatingActionButton(
                        onClick = {
                            isAppBarVisible.value = false
                        }, backgroundColor = FloatingActionBackground
                    ){
                        Icon(Icons.Filled.Search, "", tint = Color.White)
                    }
                }
            }
        }, bottomBar = {
            when (currentRoute(navigator)) {
                NavigationScreen.Home.route, NavigationScreen.Popular.route, NavigationScreen.TopRated.route, NavigationScreen.Upcoming.route -> {
                    BottomNavigationUI(navigator)
                }
            }
        }
        ) {
            Navigation(navigator)
            if (currentRoute(navigator) !== NavigationScreen.MovieDetail.route){
                Column {
                    if (isAppBarVisible.value.not()) {
                        SearchUI(navigator, viewModel.searchData) {
                            isAppBarVisible.value = true
                        }
                        ProgressIndicator(searchProgressBar.value)
                        viewModel.searchData.pagingLoadingState {
                            searchProgressBar.value = it
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationUI(navigator: Navigator) {
    BottomNavigation {
        val items = listOf(
            NavigationScreen.HomeNav,
            NavigationScreen.PopularNav,
            NavigationScreen.TopRatedNav,
            NavigationScreen.UpcomingNav,
        )
        items.forEach {
            BottomNavigationItem(label = { Text(text = it.title) },
                selected = it.route == currentRoute(navigator),
                icon = it.navIcon,
                onClick = {
                    navigator.navigate(
                        it.route,
                        NavOptions(
                            launchSingleTop = true,
                        ),
                    )
                })
        }
    }
}

@Composable
fun isBackButtonEnable(navigator: Navigator): Boolean {
    return when (currentRoute(navigator)) {
        NavigationScreen.Home.route, NavigationScreen.Popular.route, NavigationScreen.TopRated.route, NavigationScreen.Upcoming.route -> {
            false
        }

        else -> {
            true
        }
    }
}

expect fun getPlatformName(): String
