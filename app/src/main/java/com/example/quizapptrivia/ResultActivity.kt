package com.example.quizapptrivia

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class ResultActivity : AppCompatActivity() {
    private var correctAnswer:Int=0
    private var inCorrectAnswer:Int=0
    private lateinit var pieChart: PieChart
    private lateinit var correct: TextView
    private lateinit var incorrect: TextView
    private lateinit var skip: TextView
    private lateinit var homeResult: Button
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_resulty_activty)
            val bundle: Bundle? = intent.extras
            if (bundle != null) {
                correctAnswer = bundle.getInt("CorrectAnswer")
                inCorrectAnswer = bundle.getInt("InCorrectAnswer")
            }

            correct = findViewById(R.id.correct)
            homeResult = findViewById(R.id.homeresult)
            incorrect = findViewById(R.id.incoorect)
            skip = findViewById(R.id.skipped)
            pieChart = findViewById(R.id.piechart)
            correct.text = correctAnswer.toString()
            incorrect.text = inCorrectAnswer.toString()
            val skipNo = correctAnswer + inCorrectAnswer
            skip.text = (10 - skipNo).toString()
            homeResult.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
            pieChart.innerPadding = 50F
            pieChart.addPieSlice(
                PieModel(
                    "Correct", correct.text.toString().toInt().toFloat(),
                    Color.GREEN
                )
            )
            pieChart.addPieSlice(
                PieModel(
                    "Incorrect", incorrect.text.toString().toFloat(),
                    Color.RED
                )
            )
            pieChart.addPieSlice(
                PieModel(
                    "Skip", skip.text.toString().toFloat(),
                    Color.DKGRAY
                )
            )
        }catch (e:Exception){
            Toast.makeText(this,e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

}