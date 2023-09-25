package ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberAsyncImagePainter
import data.model.moviedetail.MovieDetail
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import navigation.NavigationScreen
import theme.DefaultBackgroundColor
import theme.FontColor
import ui.component.ProgressIndicator
import ui.component.shimmerBackground
import ui.component.text.SubtitlePrimary
import ui.component.text.SubtitleSecondary
import util.AppConstant
import util.AppString
import util.hourMinutes
import util.network.DataState
import util.roundTo

@Composable
fun MovieDetail(
    navigator: Navigator,
    movieId:Int
){
    val movieDetailViewModel = koinViewModel(MovieDetailViewModel::class)
    LaunchedEffect(1){
        movieDetailViewModel.movieDetail(movieId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                DefaultBackgroundColor
            )
    ){
        movieDetailViewModel.movieDetail.collectAsState().value?.let{
            when (it){
                is DataState.Loading -> {
                    ProgressIndicator()
                }

                is DataState.Success<MovieDetail> -> {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                AppConstant.IMAGE_URL.plus(
                                    it.data.poster_path
                                )
                            ),
                            contentDescription = it.data.poster_path,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp).shimmerBackground(
                                    RoundedCornerShape(5.dp)
                                ),
                            contentScale = ContentScale.Crop,
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp, end = 10.dp)
                        ) {
                            Text(
                                text = it.data.title,
                                modifier = Modifier.padding(top = 10.dp),
                                color = FontColor,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.W700,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp, top = 10.dp)
                            ) {

                                Column(Modifier.weight(1f)) {
                                    SubtitlePrimary(
                                        text = it.data.original_language,
                                    )
                                    SubtitleSecondary(
                                        text = AppString.LANGUAGE
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    SubtitlePrimary(
                                        text = it.data.vote_average.roundTo(1).toString(),
                                    )
                                    SubtitleSecondary(
                                        text = AppString.RATING
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    SubtitlePrimary(
                                        text = it.data.runtime.hourMinutes()
                                    )
                                    SubtitleSecondary(
                                        text = AppString.DURATION
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    SubtitlePrimary(
                                        text = it.data.release_date
                                    )
                                    SubtitleSecondary(
                                        text = AppString.RELEASE_DATE
                                    )
                                }
                            }
                            Text(
                                text = AppString.DESCRIPTION,
                                color = FontColor,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(text = it.data.overview)
                        }
                    }
                }

                is DataState.Error -> {
                    Text("Error :${it.exception}")
                    println(it.exception)
                }
            }
        }
    }
}