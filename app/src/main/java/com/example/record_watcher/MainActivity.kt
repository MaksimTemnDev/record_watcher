package com.example.record_watcher

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.record_watcher.api.JsonManajer
import com.example.record_watcher.api.MailModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = JsonManajer()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContent {
            val mailList = remember {
                mutableStateOf(listOf<MailModel>())
            }
            val page = remember {
                mutableStateOf(1)
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
                var pageNum = getPageAmount(mailList)
                pageSearch(pageNum, page)
                menuBuild(items = mailList, page)
                navbar(pageNum, page)
            }
        }
    }
}

@Composable
fun menuBuild(
    items: MutableState<List<MailModel>>,
    page: MutableState<Int>,
) {
    val visible = remember {
        mutableStateOf(items.value)
    }
    Box(modifier = Modifier.fillMaxHeight(0.92f)) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (items.value.size > 10) {
                visible.value = getCurrentList(page.value.toString().toInt(), items.value)
            }
            itemsIndexed(
                visible.value
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


@Composable
fun pageSearch(pageNum: Int, page: MutableState<Int>) {

    var text = remember { mutableStateOf("") }

    val change: (String) -> Unit = { it ->
        if (!it.contains(" ") && it != "" && !it.contains("\n") && !it.contains(".") && !it.contains(
                ","
            ) && !it.contains("-")
        ) {
            if (it.toInt() < 1) {
                text.value = "1"
            } else if (it.toInt() > pageNum) {
                text.value = pageNum.toString()
            } else {
                text.value = it
            }
            page.value = text.value.toInt()
        } else {
            text.value = ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.24f)
            .fillMaxHeight(0.07f)
    ) {
        TextField(
            value = text.value,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = change,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_find_in_page_24),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        )
    }
}

@Composable
fun navbar(num: Int, pageNum: MutableState<Int>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp, top = 4.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(vertical = 3.dp)
                    .clickable {
                        if (pageNum.value > 1) {
                            pageNum.value--
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                for (i in 1..num) {
                    Box(modifier = Modifier
                        .padding(top = 6.dp)
                        .clickable {
                            pageNum.value = i
                        }) {
                        Text(
                            text = i.toString(),
                            fontSize = 17.sp,
                            color = if (pageNum.value == i) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )
                    }
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(vertical = 3.dp)
                    .clickable {
                        if (pageNum.value < num) {
                            pageNum.value++
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_24),
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

fun getCurrentList(num: Int, mailList: List<MailModel>): List<MailModel> {
    val bufferList = mutableListOf<MailModel>()
    if (num == 0) {
        for (i in 0..(num * 10 - 1)) {
            bufferList.add(mailList[i])
        }
    } else {
        for (i in (num - 1) * 10..num * 10 - 1) {
            bufferList.add(mailList[i])
        }
    }
    return bufferList
}

fun getPageAmount(mailList: MutableState<List<MailModel>>): Int {
    val size = mailList.value.size
    var pageAmount = size / 10
    if (size % 10 != 0) {
        pageAmount += 1
    }
    return pageAmount
}