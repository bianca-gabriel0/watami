package com.example.myapplication

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DatabaseHelper(
    private val url: String = "jdbc:mysql://127.0.0.1/riddle",
    private val username: String = "root",
    private val password: String = ""
) {

    // Establish a connection to the database
    private fun getConnection(): Connection? {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, username, password)
        } catch (e: SQLException) {
            println("Error connecting to the database: ${e.message}")
            null
        }
    }

    // Retrieve a riddle by its ID
    fun getRiddle(riddleId: Int): Riddle? {
        val query = "SELECT * FROM questions WHERE riddle_id = ?"
        return getConnection()?.use { conn ->
            conn.prepareStatement(query).use { statement ->
                statement.setInt(1, riddleId)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) mapRiddle(resultSet) else null
                }
            }
        }
    }

    // Retrieve all riddles from the database
    fun getAllRiddles(): List<Riddle> {
        val query = "SELECT * FROM questions"
        val riddles = mutableListOf<Riddle>()
        getConnection()?.use { conn ->
            conn.createStatement().use { statement ->
                statement.executeQuery(query).use { resultSet ->
                    while (resultSet.next()) {
                        riddles.add(mapRiddle(resultSet))
                    }
                }
            }
        }
        return riddles
    }

    // Map a ResultSet row to a Riddle object
    private fun mapRiddle(resultSet: ResultSet): Riddle {
        return Riddle(
            id = resultSet.getInt("riddle_id"),
            question = resultSet.getString("riddle_question"),
            choices = parseChoices(resultSet.getString("riddle_choices")),
            answer = resultSet.getString("riddle_answer"),
            points = resultSet.getInt("riddle_points"),
            difficulty = resultSet.getInt("riddle_difficulty")
        )
    }

    // Parse choices from a JSON string
    private fun parseChoices(choicesString: String): List<String> {
        return if (choicesString.isBlank()) {
            emptyList()
        } else {
            Gson().fromJson(choicesString, object : TypeToken<List<String>>() {}.type)
        }
    }

    // Data class to represent a Riddle object
    data class Riddle(
        val id: Int,
        val question: String,
        val choices: List<String>,
        val answer: String,
        val points: Int,
        val difficulty: Int
    )
}

//aa