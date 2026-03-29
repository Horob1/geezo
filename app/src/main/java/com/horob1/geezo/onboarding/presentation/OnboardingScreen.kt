package com.horob1.geezo.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horob1.geezo.R
import com.horob1.geezo.core.presentation.theme.GeezoColor
import com.horob1.geezo.core.presentation.theme.dimens
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingRoute(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    OnboardingScreen(onFinish = {
        onFinish()
        viewModel.finishOnboarding()
    })
}

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { 4 }
    )
    val scope = rememberCoroutineScope()

    fun onNext() {
        if (pagerState.currentPage <= 2)
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> WelcomeScreenOne(
                onNext = {
                    onNext()
                }
            )

            1 -> WelcomeScreenTwo(
                onNext = {
                    onNext()
                }
            )

            2 -> WelcomeScreenThree(
                onNext = {
                    onNext()
                }
            )

            3 -> WelcomeScreenFour(
                onFinish = {
                    onFinish()
                }
            )
        }
    }
}

@Preview
@Composable
fun WelcomeScreenOne(
    onNext: () -> Unit = {}
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                   innerPadding
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.image_ob1),
                contentDescription = stringResource(id = R.string.onboarding_image_1_cd),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        MaterialTheme.dimens.s8
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                WelcomeIndicator(active = 1)

                Column {
                    Text(
                        text = stringResource(id = R.string.onboarding_title_1),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight(700),
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.onboarding_desc_1),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(400),
                        )
                    )
                }

                GetStartedButton {
                    onNext()
                }
            }
        }
    }

}

@Preview
@Composable
fun WelcomeScreenTwo(
    onNext: () -> Unit = {}
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.image_ob2),
                contentDescription = stringResource(id = R.string.onboarding_image_2_cd),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        innerPadding
                    )
                    .padding(
                        MaterialTheme.dimens.s8
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier.weight(1f / 3f)
                )
                Column(
                    modifier = Modifier.weight(2f / 3f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween

                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.onboarding_title_2),
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                            )

                        )
                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.onboarding_desc_2),

                            // Body/2 - 14px Regular ( Mont )
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                            )
                        )
                    }

                    WelcomeIndicator(active = 2)

                    GetStartedButton {
                        onNext()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun WelcomeScreenThree(
    onNext: () -> Unit = {}
) {

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.image_ob3),
                contentDescription = stringResource(id = R.string.onboarding_image_3_cd),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        innerPadding
                    )
                    .padding(
                        MaterialTheme.dimens.s8
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier.weight(1f / 2f)
                )
                Column(
                    modifier = Modifier.weight(1f / 2f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween

                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = stringResource(id = R.string.onboarding_title_3),
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Start,
                            )

                        )
                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.onboarding_desc_3),

                            // Body/2 - 14px Regular ( Mont )
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Start,
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )
                        WelcomeIndicator(active = 3)
                    }



                    GetStartedButton {
                        onNext()
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreenFour(
    onFinish: () -> Unit
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.image_ob4),
                contentDescription = stringResource(id = R.string.onboarding_image_4_cd),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        innerPadding
                    )
                    .padding(
                        MaterialTheme.dimens.s8
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.onboarding_brand),
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.41.sp,
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WelcomeIndicator(active = 4)
                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                    GetStartedButton {
                        onFinish()
                    }
                }
            }
        }
    }
}


@Composable
fun WelcomeIndicator(
    active: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.s3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .size(MaterialTheme.dimens.s3)
                    .background(
                        color = if (index + 1 <= active)
                            GeezoColor.Primary
                        else GeezoColor.Neutral2
                    )
            )
        }
    }
}

@Composable
fun GetStartedButton(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = GeezoColor.Primary,
                shape = RoundedCornerShape(4.dp)
            )
            .clip(
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,

        ) {
        Text(
            text = stringResource(id = R.string.onboarding_get_started),

            // Body/1 - 16px Bold
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight(700),
                color = GeezoColor.Neutral3,
                textAlign = TextAlign.Center,
                letterSpacing = 0.32.sp,
            )
        )
    }
}
