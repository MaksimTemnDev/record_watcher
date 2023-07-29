package com.example.record_watcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.record_watcher.api.JsonManajer
import com.example.record_watcher.api.MailModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = JsonManajer()
        setContent {
            val mailList = remember {
                mutableStateOf(listOf<MailModel>())
            }
            manager.connect(applicationContext, mailList)
            val window = this.window
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.LightGray,
                                Color.DarkGray
                            )
                        )
                    )
            ) {
                menuBuild(items = mailList)
                navbar()
            }
        }
    }
}

@Composable
fun menuBuild(
    items: MutableState<List<MailModel>>,
) {
    Box(modifier = Modifier.fillMaxHeight(0.92f)) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            itemsIndexed(
                items.value
            ) { _, item ->
                menuElement(item)
            }
        }
    }
}


@Composable
fun menuElement(item: MailModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp, top = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Column(modifier = Modifier, verticalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "id:" + item.id.toString(),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            shadow = Shadow(
                                color = Color.LightGray,
                                offset = Offset(1f, 1f),
                                blurRadius = 4f
                            )
                        ),

                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.email,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp,
                            shadow = Shadow(
                                color = Color.LightGray,
                                offset = Offset(1f, 1f),
                                blurRadius = 4f
                            )
                        ),

                        fontWeight = FontWeight.Light
                    )
                }
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.firstName + " " + item.lastName,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp,
                            shadow = Shadow(
                                color = Color.LightGray,
                                offset = Offset(1f, 1f),
                                blurRadius = 4f
                            )
                        ),

                        fontWeight = FontWeight.Light
                    )
                }
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
                    val date = Date(item.dateUpdate.toLong() * 1000)
                    Text(
                        text = sdf.format(date),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp,
                            shadow = Shadow(
                                color = Color.LightGray,
                                offset = Offset(1f, 1f),
                                blurRadius = 4f
                            )
                        ),

                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun navbar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 4.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(vertical = 3.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(vertical = 3.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}