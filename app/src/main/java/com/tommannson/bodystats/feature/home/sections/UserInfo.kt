package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.infrastructure.configuration.ApplicationUser
import com.tommannson.bodystats.infrastructure.configuration.Gender

@Composable
fun UserInfo(navController: NavController, currentUser: ApplicationUser) {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Card(
            elevation = 5.dp
        ) {
            Column {
                Row(Modifier.padding(16.dp)) {
                    Box(
                        Modifier
                            .weight(1.0f)
                    ) {
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .size(60.dp)
                                .background(MaterialTheme.colors.primary)
                        ) {

                        }
                    }
                    Column(Modifier.weight(2.0f)) {
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(currentUser.name)

                            Text(
                                buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.Black, fontSize = 12.sp)) {
                                        append("Wzrost: ")
                                    }
                                    append("${currentUser.height} cm")
                                },
                                style = MaterialTheme.typography.caption, color = Color.Gray,
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.Black, fontSize = 12.sp)) {
                                        append("Płeć: ")
                                    }
                                    append("Kobieta")
                                },
                                style = MaterialTheme.typography.caption, color = Color.Gray,
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(onClick = {
                            navController.navigate(Screen.ConfigurationScreen.route)


                        }) {
                            Text("Edit Your info".uppercase())
                        }
                    }
                }
                Box(Modifier.padding(16.dp)) {

                    val min = currentUser.weight
                    val max = currentUser.dreamWeight
                    val current = 75f

                    MyProgress(min, max, current)
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewUserInfo() {
    UserInfo(navController = rememberNavController(), currentUser = ApplicationUser("asd", 1.0f, 1.0f, 1f, Gender.FEMALE ))
}