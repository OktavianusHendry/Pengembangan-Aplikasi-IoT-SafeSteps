package com.okta.iottest.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.model.LatLng

@Composable
fun DeviceRow(
    profilePicture: Any, // Change this to Any
    name: String,
    distance: String,
    updatedTime: String,
    status: String?,
    location: LatLng,
    modifier: Modifier = Modifier,
    onClick: (Any, String, String, String, String, LatLng) -> Unit = { _, _, _, _, _, _ -> }
) {
    // Add content here
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp )
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {  }
                .padding(8.dp)
//                    .clickable(onClick = { onClick(profilePicture, name, distance, updatedTime, status ?: "", location) })  // Make the row clickable,
        ){
            when (profilePicture) {
                is Int -> {
                    // Load drawable resource
                    Image(
                        painter = painterResource(id = profilePicture),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(65.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(start = 16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "Last online on $updatedTime",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                is Uri -> {
                    // Load image from Uri
                    Image(
                        painter = rememberImagePainter(data = profilePicture),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(65.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(start = 16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "Last online Now",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.Top)
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "More",
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
