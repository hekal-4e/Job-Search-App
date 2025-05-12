package com.example.jobsearchapp.ui.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobsearchapp.R
import com.example.jobsearchapp.UserProfile

@Composable
fun ProfileHeader(
    userProfile: UserProfile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = "Cover Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA5A1BD).copy(alpha = 0.2f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pencil_vec),
                        contentDescription = "Edit profile",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edit profile",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .offset(y = 110.dp)
                .clip(CircleShape)
                .border(3.dp, Color.White, CircleShape)
                .background(Color.LightGray)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 215.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = userProfile.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

            Text(
                text = userProfile.location,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



//@Composable
//fun ProfileHeader(
//    userProfile: UserProfile,
//    onEditClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(250.dp)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.img_1),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxSize()
//                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .align(Alignment.TopStart)
//        ) {
//
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_background),
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            Column {
//                Text(
//                    text = userProfile.name,
//                    style = MaterialTheme.typography.titleLarge,
//                    color = Color.White,
//                    fontSize = 18.sp
//                )
//                Text(
//                    text = userProfile.location,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.Gray
//                )
//            }
//
//        }
//
//    }
//}