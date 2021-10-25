package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R

val text = """Cześć,
Na początek podaj kilka inforamcji o sobie żebyśmy mogli obliczać twoje postępy"""

@Preview
@Composable
fun PreviewOnboard() {
    Onboard()
}

@Composable
fun Onboard() {
    PaddingCard {
        Text(text = text)
    }
}

@Composable
fun PaddingCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.End

        ) {
            Row() {
                Icon(
                    modifier = Modifier
                        .size(100.dp),
                    painter = painterResource(
                        id = R.drawable.ic_baseline_add_circle_outline_24
                    ),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.padding(top = 4.dp)
                    .weight(1.0f))
                {
                    content()
                }
            }



            TextButton(onClick = { /*TODO*/ }) {
                Text("Konfiguruj")
            }

        }
        Box() {

        }
    }
}


