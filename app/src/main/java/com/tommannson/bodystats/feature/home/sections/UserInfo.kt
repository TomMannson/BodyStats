package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.home.WeightInfo
import com.tommannson.bodystats.infrastructure.configuration.ApplicationUser
import com.tommannson.bodystats.infrastructure.configuration.Gender
import com.tommannson.bodystats.utils.fmt

@Composable
fun UserInfo(navController: NavController, currentUser: ApplicationUser, weightInfo: WeightInfo) {
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
                            Icon(
                                painterResource(id = R.drawable.baseline_sentiment_satisfied_alt_24),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(60.dp).align(Alignment.Center)
                            )
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
                                    append("${currentUser.height fmt "#.#"} cm")
                                },
                                style = MaterialTheme.typography.caption, color = Color.Gray,
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.Black, fontSize = 12.sp)) {
                                        append("Płeć: ")
                                    }
                                    append(if(currentUser.sex == Gender.FEMALE) "Kobieta" else "Mężczyzna")
                                },
                                style = MaterialTheme.typography.caption, color = Color.Gray,
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(onClick = {
                            navController.navigate(Screen.ConfigurationScreen.route)


                        }) {
                            Text("Ustaw swoje dane".uppercase())
                        }
                    }
                }
                Box(Modifier.padding(16.dp)) {

                    val min = currentUser.weight
                    val max = currentUser.dreamWeight
                    val current = 75f

                    MyProgress(min, max, weightInfo.weight)
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewUserInfo() {
    UserInfo(
        navController = rememberNavController(),
        currentUser = ApplicationUser("asd", 1.0f, 1.0f, 1f, Gender.FEMALE),
        WeightInfo(1f, "")
    )
}