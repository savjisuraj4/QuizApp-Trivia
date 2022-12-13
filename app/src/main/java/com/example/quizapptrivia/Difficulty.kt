package com.example.quizapptrivia

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Difficulty : AppCompatActivity() {
    private lateinit var easy:Button
    private lateinit var medium:Button
    private lateinit var hard:Button
    lateinit var id:String
    private lateinit var categoryName:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty)
        easy=findViewById(R.id.easy)
        medium=findViewById(R.id.medium)
        hard=findViewById(R.id.hard)
        val bundle:Bundle? = intent.extras
        id= bundle?.getString("id").toString()
        categoryName=bundle?.getString("category").toString()
        easy.setOnClickListener { startQuizActivity("easy") }
        medium.setOnClickListener { startQuizActivity("medium") }
        hard.setOnClickListener { startQuizActivity("hard") }
    }

    private fun startQuizActivity(difficultyLevel: String) {
        val intent= Intent(applicationContext,QuizActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("difficulty-level",difficultyLevel)
        startActivity(intent)
    }
}