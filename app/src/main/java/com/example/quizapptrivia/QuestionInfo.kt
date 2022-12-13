package com.example.quizapptrivia

data class QuestionInfo (
    var questionNo: String,
    var question: String,
    var option1: String,
    var option2: String,
    var option3: String,
    var option4: String,
    var answer: String,
    var attempted:Boolean,
    var selectedOption:String
    )