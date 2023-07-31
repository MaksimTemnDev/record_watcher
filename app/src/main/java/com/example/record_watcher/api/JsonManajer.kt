package com.example.record_watcher.api

import android.content.Context
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class JsonManajer {
    public fun connect(context: Context, mailList: MutableState<List<MailModel>>){
        val url = "https://run.mocky.io/v3/956d7c43-b513-4698-aa7d-6a9bfd4f1bec"
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            parseData(response, mailList)
        },
            {
                println("Error!")
            })
        queue.add(stringRequest)
    }

    private fun parseData(result: String, mailList: MutableState<List<MailModel>>){
        val mainObject = JSONObject(result)
        val list = parseEntities(mainObject)
        mailList.value = list
    }

    private fun parseEntities(mainObject: JSONObject): List<MailModel>{
        val list = ArrayList<MailModel>()
        val values = mainObject.getJSONArray("result")
        for (i in 0 until values.length()){
            val direct = values[i] as JSONObject
            val item = MailModel(
                direct.get("id") as Int,
                direct.get("email") as String,
                direct.getString("firstName"),
                direct.getString("lastName"),
                direct.getString("dateUpdate")
            )
            list.add(item)
        }
        return list
    }
}