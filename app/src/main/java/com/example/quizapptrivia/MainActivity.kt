package com.example.quizapptrivia

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
@Suppress("DEPRECATION")

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var categoryInfo: CategoryInfo
    lateinit var recycleViewAdapter: RecycleviewAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var categoryList=ArrayList<CategoryInfo>()
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            categoryList.clear()
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            swipeRefreshLayout=findViewById(R.id.SwipeRefreshLayout)
            progressDialog=ProgressDialog(this)
            recyclerView = findViewById(R.id.recycleView)
            linearLayoutManager=LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            loadData()
            swipeRefreshLayout.setOnRefreshListener {
                loadData()
                swipeRefreshLayout.isRefreshing=false
            }

        }catch (e:Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }
    }
    private fun loadData() {
            progressDialog.setTitle(R.string.app_name)
        progressDialog.setMessage("Loading..")
        progressDialog.show()
            val url = "https://opentdb.com/api_category.php"
            val queue: RequestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest =
                object : JsonObjectRequest(url, com.android.volley.Response.Listener {
                    val triviaCategories=it.getJSONArray("trivia_categories")
                    for (i in 0..triviaCategories.length().minus(1)){
                        val id=triviaCategories.getJSONObject(i).getString("id")
                        val categoryName=triviaCategories.getJSONObject(i).getString("name")
                        categoryInfo= CategoryInfo(id,categoryName)
                        categoryList.add(categoryInfo)
                    }
                    recycleViewAdapter = RecycleviewAdapter(this, categoryList)
                    recyclerView.adapter = recycleViewAdapter
                    progressDialog.dismiss()
                },
                    com.android.volley.Response.ErrorListener {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }) {

                }
            queue.add(jsonObjectRequest)
    }
    fun levelDisplay(position:Int){
        val intent= Intent(applicationContext,Difficulty::class.java)
        intent.putExtra("id",categoryList[position].id)
        intent.putExtra("category",categoryList[position].category)
        startActivity(intent)
    }
}