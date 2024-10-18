package com.example.myapplication

import DatabaseHelper
import Riddle
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class Activity_Game : AppCompatActivity() {

    private lateinit var settings2: ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var screen: View
    private lateinit var brownbar: View
    private lateinit var submitanswer: Button
    private lateinit var ResetButton: ImageButton
    private lateinit var tv_question: TextView
    private lateinit var tv_optionOne: TextView
    private lateinit var tv_optionTwo: TextView
    private lateinit var tv_optionThree: TextView
    private lateinit var ScoreOutput: TextView
    private lateinit var Score: TextView
    private lateinit var DatabaseHelper: DatabaseHelper
    private var currentRiddle: Riddle? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tv_question = findViewById(R.id.tv_question)
        tv_optionOne = findViewById(R.id.tv_optionOne)
        tv_optionTwo = findViewById(R.id.tv_optionTwo)
        tv_optionThree = findViewById(R.id.tv_optionThree)

        DatabaseHelper = DatabaseHelper()

        // Load a riddle (e.g., with ID 1)
        loadRiddle(1)

        // Initialize sound pool
        val soundPool = SoundPool.Builder().build()
        val soundId = soundPool.load(this, R.raw.mouse_click, 1)

        settings2 = findViewById(R.id.settings2)
        settings2.setOnClickListener {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            val intent2 = Intent(this, Activity_Game_to_Settings::class.java)
            startActivity(intent2)
        }

        ResetButton = findViewById(R.id.ResetButton)
        ResetButton.setOnClickListener {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            // Restart the game or reset logic can be implemented here
        }

        sharedPreferences = getSharedPreferences("dark_mode_pref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val isDarkModeEnabled = sharedPreferences.getBoolean("is_dark_mode_enabled", false)
        updateUI(isDarkModeEnabled)

        screen = findViewById(R.id.screen)
        brownbar = findViewById(R.id.brownbar)
        submitanswer = findViewById(R.id.submitanswer)
        ScoreOutput = findViewById(R.id.ScoreOutput)
        Score = findViewById(R.id.Score)

        // Dark mode UI setup
        if (isDarkModeEnabled) {
            updateDarkModeUI()
        } else {
            updateLightModeUI()
        }


    }

    private fun loadRiddle(riddleId: Int) {
        currentRiddle = DatabaseHelper.getRiddle(riddleId)

        currentRiddle?.let { riddle ->
            tv_question.text = riddle.question
            tv_optionOne.text = riddle.choices.getOrNull(0) ?: ""
            tv_optionTwo.text = riddle.choices.getOrNull(1) ?: ""
            tv_optionThree.text = riddle.choices.getOrNull(2) ?: ""

            // Set click listeners for answer options
            tv_optionOne.setOnClickListener { checkAnswer(riddle, riddle.choices[0]) }
            tv_optionTwo.setOnClickListener { checkAnswer(riddle, riddle.choices[1]) }
            tv_optionThree.setOnClickListener { checkAnswer(riddle, riddle.choices[2]) }
        } ?: run {
            // Handle case where no riddle is found
            tv_question.text = "No riddle found."
            tv_optionOne.text = ""
            tv_optionTwo.text = ""
            tv_optionThree.text = ""
        }
    }


    private fun checkAnswer(riddle: Riddle, selectedChoice: String) {
        if (selectedChoice == riddle.answer) {
            // Correct answer logic
            ScoreOutput.text = "Correct! You earned ${riddle.points} points."
            // Optionally, load the next riddle or reset the game
        } else {
            // Incorrect answer logic
            ScoreOutput.text = "Wrong! The correct answer was: ${riddle.answer}."
        }
    }

    private fun updateUI(isDarkModeEnabled: Boolean) {
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun updateDarkModeUI() {
        screen.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_screen))
        brownbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_mode_bar))
        submitanswer.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_mode_buttons))
        submitanswer.setTextColor(ContextCompat.getColor(this, R.color.dark_mode_buttons_text))
        Score.setTextColor(ContextCompat.getColor(this, R.color.dark_mode_text))
        ScoreOutput.setTextColor(ContextCompat.getColor(this, R.color.dark_mode_text))
        settings2.setImageResource(R.drawable.dark_mode_settingsbutton)
        ResetButton.setImageResource(R.drawable.dark_mode_resetbutton)
    }

    private fun updateLightModeUI() {
        screen.setBackgroundColor(ContextCompat.getColor(this, R.color.screen))
        brownbar.setBackgroundColor(ContextCompat.getColor(this, R.color.light_mode_bar))
        submitanswer.setBackgroundColor(ContextCompat.getColor(this, R.color.light_mode_submit))
        submitanswer.setTextColor(ContextCompat.getColor(this, R.color.light_mode_submit_text))
        Score.setTextColor(ContextCompat.getColor(this, R.color.light_mode_score))
        ScoreOutput.setTextColor(ContextCompat.getColor(this, R.color.light_mode_score))
        settings2.setImageResource(R.drawable.settingsbutton)
        ResetButton.setImageResource(R.drawable.resetbutton)
    }
}