package com.example.puffpoof

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dbHelper = DBHelper(this)

        fetchDollData()
    }

    private fun fetchDollData() {
        val url = "https://api.npoint.io/9d7f4f02be5d5631a664"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val dolls = response.getJSONArray("dolls")
                for (i in 0 until dolls.length()) {
                    val dollJson = dolls.getJSONObject(i)
                    val doll = Doll(
                        dollJson.getInt("id"),
                        dollJson.getString("name"),
                        dollJson.getString("size"),
                        dollJson.getDouble("rating").toFloat(),
                        dollJson.getInt("price"),
                        dollJson.getString("image"),
                        dollJson.getString("description")
                    )
                    dbHelper.insertDoll(doll)
                }
                displayDollData()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun displayDollData() {
        val dolls = dbHelper.getAllDolls()
        val adapter = DollAdapter(dolls) { doll ->
            val intent = Intent(this, DollDetail::class.java)
            intent.putExtra("doll", doll)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
