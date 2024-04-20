package com.okta.iottest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okta.iottest.ui.theme.Primary70
import com.okta.iottest.ui.theme.PrimaryContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ImageSlider(images: List<Triple<Int, String, String>>) {
    val coroutineScope = rememberCoroutineScope()
    val state = rememberLazyListState()
    var currentImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = currentImageIndex) {
        coroutineScope.launch {
            delay(3000)  // Change image every 5 seconds
            with(state) {
                animateScrollToItem((currentImageIndex + 1) % images.size)
            }
            currentImageIndex = (currentImageIndex + 1) % images.size
        }
    }

    Column {
        LazyRow(state = state) {
            itemsIndexed(images) { index, (imageResId, _, _) ->
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(if (index == images.lastIndex) 400.dp else 360.dp)  // Set the maximum width here
                        .padding(
                            start = 32.dp,
                            end = if (index == images.lastIndex) 32.dp else 0.dp
                        )
                        .fillMaxHeight(0.5f)
                )
            }
        }
        Column(
            modifier = Modifier
                .height(120.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = images[currentImageIndex].second,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 32.dp, end = 32.dp, top = 8.dp)
            )
            Spacer(modifier =  Modifier.padding(bottom = 8.dp))
            Text(
                text = images[currentImageIndex].third,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 32.dp, end = 32.dp,)
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)  // Add space between each dot
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (index == currentImageIndex) Primary70 else PrimaryContainer)
                )
            }
        }

    }
}

