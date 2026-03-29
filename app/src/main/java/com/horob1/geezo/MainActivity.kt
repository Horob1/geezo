package com.horob1.geezo

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.horob1.geezo.core.domain.repository.NetworkLogRepository
import com.horob1.geezo.core.presentation.theme.GeezoColor
import com.horob1.geezo.core.presentation.theme.GeezoTheme
import com.horob1.geezo.core.presentation.theme.dimens
import com.horob1.geezo.navigation.Routes
import com.horob1.geezo.navigation.graph.GeezoNavHost
import com.horob1.geezo.onboarding.domain.repository.AppLaunchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

class MainActivity : ComponentActivity(), KoinComponent {

    private val networkLogRepository: NetworkLogRepository by inject()
    private val appLaunchRepository: AppLaunchRepository by inject()
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // animation splash screen
        installSplashScreen().apply {
            setKeepOnScreenCondition { !isReady }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        enableEdgeToEdge()
        setContent {
            GeezoTheme {
                GeezoComposeApp(
                    networkLogRepository = networkLogRepository,
                    appLaunchRepository = appLaunchRepository,
                    onReady = { isReady = true }
                )
            }
        }
    }
}

@Composable
fun GeezoComposeApp(
    networkLogRepository: NetworkLogRepository,
    appLaunchRepository: AppLaunchRepository,
    onReady: () -> Unit
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    var startDestination by remember { mutableStateOf<Routes?>(null) }

    LaunchedEffect(Unit) {
        val isFirstRun = runCatching { appLaunchRepository.isFirstRun().first() }
            .getOrDefault(false)
        startDestination = if (isFirstRun) Routes.OnBoarding else Routes.Main
        onReady()
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val isInApiDebugModule = backStackEntry?.destination
        ?.hierarchy
        ?.any { it.hasRoute<Routes.ApiDebug>() } == true

    Box {
        startDestination?.let { destination ->
            GeezoNavHost(
                navController = navController,
                startDestination = destination,
                onOnboardingCompleted = {
                    scope.launch { appLaunchRepository.setFirstRunCompleted() }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        DebugGestureOverlay(
            onOpenDebug = {
                if (isInApiDebugModule) return@DebugGestureOverlay
                navController.navigate(Routes.ApiDebug) {
                    launchSingleTop = true
                }
            },
            onLongPressClear = {
                scope.launch { networkLogRepository.deleteAllLogs() }
            }
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DebugGestureOverlay(
    onOpenDebug: () -> Unit,
    onLongPressClear: () -> Unit
) {
    if (!BuildConfig.DEBUG) return

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(999f)
    ) {
        DebugFloatingButton(
            initialOffset = Offset(screenWidthPx - 250f, screenHeightPx - 250f),
            screenWidthPx = screenWidthPx,
            screenHeightPx = screenHeightPx,
            onClick = onOpenDebug,
            onLongClick = onLongPressClear
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DebugFloatingButton(
    initialOffset: Offset,
    screenWidthPx: Float,
    screenHeightPx: Float,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(initialOffset.x) }
    var offsetY by remember { mutableFloatStateOf(initialOffset.y) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(MaterialTheme.dimens.buttonLG)
            .background(GeezoColor.Primary, CircleShape)
            .border(
                width = 1.dp,
                color = GeezoColor.OnPrimary,
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX = (offsetX + dragAmount.x).coerceIn(0f, screenWidthPx - size.width)
                    offsetY = (offsetY + dragAmount.y).coerceIn(0f, screenHeightPx - size.height)
                }
            }
            .clip(
                CircleShape
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.s1)
        ) {
            Icon(Icons.Default.Api, tint = GeezoColor.OnPrimary, contentDescription = "Debug")
        }
    }
}