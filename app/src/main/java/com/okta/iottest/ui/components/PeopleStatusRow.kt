package com.okta.iottest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.okta.iottest.R
import com.okta.iottest.ui.theme.ErrorContainer
import com.okta.iottest.ui.theme.SemanticBrown10

@Composable
fun PeopleStatusRow(
    profilePicture : Int,
    name: String,
    distance: String,
    updatedTime: String,
    status: String?,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (status == "fall") ErrorContainer else if (status == "help") SemanticBrown10 else Color.White)
                .padding(8.dp)
                .clickable(onClick = { onClick(status ?: "") })  // Make the row clickable,
        ){
            Image(
                painter = painterResource(profilePicture),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(60.dp)
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
                    text = distance,
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = updatedTime,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically)
            ) {
                if (status == "fall") {
                    Image(
                        painter = painterResource(R.drawable.fall_status),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                    )
                } else if (status == "help") {
                    Image(
                        painter = painterResource(R.drawable.help_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                    )
                } else {
                    Box(modifier = Modifier.size(50.dp)) {
                        // No image is shown
                    }
                }

            }
        }
    }
}
