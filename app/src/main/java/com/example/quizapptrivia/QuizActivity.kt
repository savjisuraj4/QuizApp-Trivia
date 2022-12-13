package com.example.quizapptrivia

import androidx.appcompat.app.AppCompatActivity

import android.annotation.SuppressLint
import android.app.AlertDialog

import android.app.ProgressDialog

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.*

import android.view.View

import android.widget.Button

import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


@Suppress("DEPRECATION")

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var id: String
    private lateinit var difficultyLevel: String
    private lateinit var question: TextView
    var questionAnswer = ArrayList<QuestionInfo>()
    lateinit var questionInfo: QuestionInfo
    private var mSelectedOption: String=""
    private var incurrectquestion:Int=0
    private var currentquestion: Int = 0
    private var mCorrectAnswers:Int=0
    private var option1: Button? = null
    private var option2: Button? = null
    private var option3: Button? = null
    private var option4: Button? = null
    private var questionNo: TextView? = null
    lateinit var progressdiaglog: ProgressDialog
    private lateinit var progressbartextview: TextView
    private var previous: Button? = null
    private lateinit var progrssBar: ProgressBar
    private var submitb: Button? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val bundle: Bundle? = intent.extras
        id = bundle?.getString("id").toString()
        difficultyLevel = bundle?.getString("difficulty-level").toString()

        question = findViewById(R.id.tv_question)
        option1 = findViewById(R.id.tv_option_one)
        option2 = findViewById(R.id.tv_option_two)
        option3 = findViewById(R.id.tv_option_three)
        option4 = findViewById(R.id.tv_option_four)
        previous = findViewById(R.id.previous)
        progrssBar = findViewById(R.id.progressBar)
        progressbartextview = findViewById(R.id.tv_progress_textview)
        submitb = findViewById(R.id.next)
        questionNo = findViewById(R.id.questionNo)

        option1?.setOnClickListener(this)
        option2?.setOnClickListener(this)
        option3?.setOnClickListener(this)
        option4?.setOnClickListener(this)
        previous?.setOnClickListener(this)
        submitb?.setOnClickListener(this)
        previous?.setOnClickListener(this)


        progressdiaglog = ProgressDialog(this)
        progressdiaglog.setCancelable(false)
        progressdiaglog.onBackPressed()
        progressdiaglog.setMessage("Loading...")

        fetchData()
        progressdiaglog.show()
    }

    private fun fetchData() {

        val url =
            "https://opentdb.com/api.php?amount=10&category=$id&difficulty=$difficultyLevel&type=multiple"
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest =
            object : JsonObjectRequest(url, com.android.volley.Response.Listener {
                try {
                    val result = it.getJSONArray("results")
                    val answerList = arrayOf("", "", "", "")
                    for (i in 0..9) {

                        val random = (0..3).shuffled().take(4)
                        val question: String =
                            result.getJSONObject(i).getString("question").toString()
                        val option1: String =
                            result.getJSONObject(i).getJSONArray("incorrect_answers").getString(0)
                                .toString()
                        val option2: String =
                            result.getJSONObject(i).getJSONArray("incorrect_answers").getString(1)
                                .toString()
                        val option3: String =
                            result.getJSONObject(i).getJSONArray("incorrect_answers").getString(2)
                                .toString()
                        val correctAnswer: String =
                            result.getJSONObject(i).getString("correct_answer").toString()

                        answerList[0] = option1
                        answerList[1] = option2
                        answerList[2] = option3
                        answerList[3] = correctAnswer

                        questionInfo = QuestionInfo(
                            (i + 1).toString(),
                            question,
                            answerList[random[0]],
                            answerList[random[1]],
                            answerList[random[2]],
                            answerList[random[3]],
                            correctAnswer,
                            false,
                            ""
                        )
                        questionAnswer.add(questionInfo)
                    }
                    setQuestion()
                    progressdiaglog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                }
            },
                com.android.volley.Response.ErrorListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }) {

            }
        queue.add(jsonObjectRequest)
    }

    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()
        option1?.let {
            options.add(0, it)
        }
        option2?.let {
            options.add(1, it)
        }
        option3?.let {
            options.add(2, it)
        }
        option4?.let {
            options.add(3, it)
        }

        for (option in options) {
            option.setTextColor(Color.BLACK)
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.button
            )
        }
    }

    private fun selectedOption(tv: Button, selectedOption: String) {

        defaultOptionsView()

        mSelectedOption = selectedOption

        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                option1?.let {
                    selectedOption(it, it.text.toString())
                }
            }
            R.id.tv_option_two -> {
                option2?.let {
                    selectedOption(it, it.text.toString())
                }
            }
            R.id.tv_option_three -> {
                option3?.let {
                    selectedOption(it, it.text.toString())
                }
            }
            R.id.tv_option_four -> {
                option4?.let {
                    selectedOption(it, it.text.toString())
                }
            }
            R.id.next -> {
                submitb?.let {
                    if (mSelectedOption.isEmpty() && currentquestion<questionAnswer.size.minus(1)) {
                        skipQuestion()
                    }
                    else if(currentquestion<questionAnswer.size && mSelectedOption.isNotEmpty()){
                        checkAnswer()
                    }
                    else {
                        resultActivity()
                    }
                }
            }
            R.id.previous->{
                previous?.let {
                    try {
                        currentquestion--
                        val question = questionAnswer[currentquestion]
                        setQuestion()
                        if (question.attempted) {
                            answerView(
                                question.selectedOption,
                                R.drawable.wrong_option_border_bg
                            )
                            answerView(
                                question.answer,
                                R.drawable.correct_option_border_bg
                            )
                        }
                    }catch (e1:ArrayIndexOutOfBoundsException){
                        Toast.makeText(this,"No Questions Before",Toast.LENGTH_LONG).show()

                    } catch (e: RuntimeException) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                }
            }
        }


    private fun checkAnswer() {
        val  question=questionAnswer[currentquestion]
        if(question.answer!=mSelectedOption){
            answerView(
                mSelectedOption,
                R.drawable.wrong_option_border_bg
            )
            incurrectquestion++

        }
        else{
            mCorrectAnswers++
        }
        answerView(
            question.answer,
            R.drawable.correct_option_border_bg
        )
        question.attempted=true
        question.selectedOption=mSelectedOption
        mSelectedOption=""

    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        try {
            option1?.isEnabled=true
            option2?.isEnabled=true
            option3?.isEnabled=true
            option4?.isEnabled=true
            progressbartextview.text=(currentquestion+1).toString()+"/10"
            val question1 =
                questionAnswer[currentquestion] // Getting the question from the list with the help of current position.
            defaultOptionsView()
            progrssBar.progress = currentquestion+1
            if ((currentquestion) == (questionAnswer.size + 1)) {
                submitb?.text = "FINISH"
            } else {
                submitb?.text = "NEXT"
            }

            questionNo?.text="Q.\t"+(currentquestion+1).toString()
            question.text = question1.question
            option1?.text = question1.option1
            option2?.text = question1.option2
            option3?.text = question1.option3
            option4?.text = question1.option4

        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun skipQuestion() {
        try{
        currentquestion++
        val question = questionAnswer[currentquestion]
            val size=questionAnswer.size.minus(1)
        when {
            (currentquestion <= size && !question.attempted) -> {
                setQuestion()

            }
            (currentquestion <= size) -> {
                setQuestion()
                answerView(question.selectedOption,R.drawable.wrong_option_border_bg)
                answerView(question.answer,R.drawable.correct_option_border_bg)
            }
        }
        }catch (e:Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    private fun answerView(answer: String, drawableView: Int) {
        option1?.isEnabled = false
        option2?.isEnabled = false
        option3?.isEnabled = false
        option4?.isEnabled = false

        when (answer) {

            option1?.text.toString() -> {
                option1?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            option2?.text.toString() -> {
                option2?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            option3?.text.toString() -> {
                option3?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            option4?.text.toString() -> {
                option4?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }

    }

    private fun resultActivity(){
        try {

            Toast.makeText(
                this,
                "You have successfully completed the quiz. Your Score is : $mCorrectAnswers",
                Toast.LENGTH_SHORT
            ).show()
            val intent1 = Intent(this, ResultActivity::class.java)
            intent1.putExtra("CorrectAnswer",mCorrectAnswers)
            intent1.putExtra("InCorrectAnswer",incurrectquestion)

            startActivity(intent1)
        }catch (e:Exception){
            Toast.makeText(applicationContext,e.toString(),Toast.LENGTH_LONG).show()
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_baseline_error_24)
            .setTitle("Exit")
            .setMessage("Are You Sure Want to Exit")
            .setPositiveButton("Yes") { dialog, which -> resultActivity() }
            .setNegativeButton("No"){dialog, which->}
            .create().show()
    }
}