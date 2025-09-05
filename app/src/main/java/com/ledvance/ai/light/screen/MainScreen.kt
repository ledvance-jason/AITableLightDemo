package com.ledvance.ai.light.screen

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.ledvance.ai.light.model.MainBottomTab
import com.ledvance.ui.R
import com.ledvance.ui.component.LedvanceBottomItem
import com.ledvance.ui.component.LedvanceBottomNavigation
import com.ledvance.ui.component.LedvanceScreen
import kotlinx.coroutines.launch

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 10:24
 * Describe : MainScreen
 */
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val mainTabList = rememberSaveable {
        listOf(
            LedvanceBottomItem(
                iconResId = R.drawable.ic_home,
                titleResId = R.string.home,
                data = MainBottomTab.Home
            ),
            LedvanceBottomItem(
                iconResId = R.drawable.ic_more,
                titleResId = R.string.more,
                data = MainBottomTab.More
            ),
        )
    }
    val pagerState = rememberPagerState { mainTabList.size }
    LedvanceScreen {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            when (index) {
                0 -> HomeScreen(onItemClick = {})
                1 -> MoreScreen()
            }
        }
        LedvanceBottomNavigation(
            selectedIndex = pagerState.currentPage,
            items = mainTabList,
            onClick = { index, _ ->
                scope.launch { pagerState.animateScrollToPage(index) }
            }
        )
    }
}