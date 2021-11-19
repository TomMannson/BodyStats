package com.tommannson.bodystats.feature.previewstats.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

data class PreviewScreenState
@OptIn(ExperimentalPagerApi::class) constructor(
    val pageState: PagerState,
    var tabIndex: Int,
) {

    @OptIn(ExperimentalPagerApi::class)
    suspend fun selectIndex(indexToSelect: Int) {
        tabIndex = indexToSelect
        pageState.animateScrollToPage(indexToSelect)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPreviewScreenState(initialPage: Int) = remember {
    PreviewScreenState(
        PagerState(initialPage),
        initialPage,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagableTabLayout(
    count: Int,
    tabPagerState: PreviewScreenState,
    titleFactory: (Int) -> String,
    unPageableContent: @Composable () -> Unit = {},
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    Column() {
        val coroutineScope = rememberCoroutineScope()
        ScrollableTabRow(selectedTabIndex = tabPagerState.pageState.currentPage) {
            for (i in 0 until count) {
                Tab(
                    selected = i == tabPagerState.tabIndex,
                    text = { Text(titleFactory(i)) },
                    onClick = {
                        coroutineScope.launch {
                            tabPagerState.selectIndex(i)
                        }
                    }
                )
            }
        }
        unPageableContent()
        HorizontalPager(
            state = tabPagerState.pageState,
            count = count,
            content = content
        )
    }
}