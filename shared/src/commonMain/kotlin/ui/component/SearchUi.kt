package ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberAsyncImagePainter
import data.model.BaseModelV2
import moe.tlaster.precompose.navigation.Navigator
import navigation.NavigationScreen
import theme.DefaultBackgroundColor
import theme.FontColor
import theme.SecondaryFontColor
import theme.cornerRadius
import util.AppConstant
import util.AppString
import util.network.DataState
import util.roundTo

@Composable
fun SearchUI(
    navController: Navigator,
    searchData: MutableState<DataState<BaseModelV2>?>,
    itemClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, 550.dp) // define max height
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(color = DefaultBackgroundColor)
            .padding(top = 8.dp)

    ) {
        searchData.value?.let {
            if (it is DataState.Success<BaseModelV2>) {
                items(items = it.data.results, itemContent = { item ->
                    Row(modifier = Modifier
                        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                        .clickable {
                            itemClick.invoke()
                            navController.navigate(
                                NavigationScreen.MovieDetail.route.plus(
                                    "/${item.id}"
                                )
                            )
                        }) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                AppConstant.IMAGE_URL.plus(
                                    item.backdrop_path
                                )
                            ),
                            contentDescription = item.backdrop_path,
                            modifier = Modifier
                                .height(100.dp)
                                .width(80.dp).cornerRadius(8).shimmerBackground(RoundedCornerShape(5.dp)),
                            contentScale = ContentScale.Crop,
                        )
                        Column {
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    top = 4.dp
                                ),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = item.release_date,
                                color = FontColor,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                text = "${AppString.RATING_SEARCH} ${
                                    item.vote_average.roundTo(
                                        1
                                    )
                                }",
                                color = SecondaryFontColor,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                })
            }
        }
    }
}