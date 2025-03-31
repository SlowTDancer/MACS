package com.ikhut.rps

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val scoreBoard = ScoreBoard(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateScoreBoard()
        setOnClickOnListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun updateScoreBoard() {
        findViewById<TextView>(R.id.player1Score).text = scoreBoard.player1Score.toString()
        findViewById<TextView>(R.id.player2Score).text = scoreBoard.player2Score.toString()
    }

    private fun setOnClickOnListeners() {
        findViewById<Button>(R.id.rockButton).setOnClickListener {
            play(Move.ROCK)
        }
        findViewById<Button>(R.id.paperButton).setOnClickListener {
            play(Move.PAPER)
        }
        findViewById<Button>(R.id.scissorsButton).setOnClickListener {
            play(Move.SCISSORS)
        }
    }

    private fun getRandomMove(): Move {
        return Move.entries[Random.nextInt(Move.entries.size)]
    }

    private fun checkWinner(playerMove: Move, computerMove: Move): Boolean {
        return (playerMove == Move.ROCK && computerMove == Move.SCISSORS)
                || (playerMove == Move.PAPER && computerMove == Move.ROCK)
                || (playerMove == Move.SCISSORS && computerMove == Move.PAPER)
    }

    private fun showMove(move: Move, imageView: ImageView) {
        when (move) {
            Move.ROCK -> {
                imageView.contentDescription = R.string.rock.toString()
                imageView.setImageResource(R.drawable.rock)
            }
            Move.PAPER -> {
                imageView.contentDescription = R.string.paper.toString()
                imageView.setImageResource(R.drawable.paper)
            }
            Move.SCISSORS -> {
                imageView.contentDescription = R.string.scissors.toString()
                imageView.setImageResource(R.drawable.scissors)
            }
        }
    }

    private fun checkVisibility() {
        val initTextView = findViewById<TextView>(R.id.init_text)

        if (findViewById<TextView>(R.id.init_text).visibility == View.VISIBLE) {
            initTextView.visibility = View.GONE
            findViewById<ImageView>(R.id.player1Image).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.player2Image).visibility = View.VISIBLE
        }
    }


    private fun play(playerMove: Move) {
        val computerMove = getRandomMove()

        checkVisibility()

        showMove(playerMove, findViewById(R.id.player2Image))
        showMove(computerMove, findViewById(R.id.player1Image))

        when {
            playerMove == computerMove -> {
                findViewById<TextView>(R.id.player1Score).setTextColor(
                    resources.getColor(R.color.yellow, theme)
                )
                findViewById<TextView>(R.id.player2Score).setTextColor(
                    resources.getColor(R.color.yellow, theme)
                )
            }

            checkWinner(playerMove, computerMove) -> {
                scoreBoard.player2Score++
                findViewById<TextView>(R.id.player2Score).setTextColor(
                    resources.getColor(R.color.green, theme)
                )
                findViewById<TextView>(R.id.player1Score).setTextColor(
                    resources.getColor(R.color.text_dark_gray, theme)
                )
            }

            else -> {
                scoreBoard.player1Score++
                findViewById<TextView>(R.id.player2Score).setTextColor(
                    resources.getColor(R.color.text_dark_gray, theme)
                )
                findViewById<TextView>(R.id.player1Score).setTextColor(
                    resources.getColor(R.color.green, theme)
                )
            }
        }

        updateScoreBoard()
    }
}

enum class Move {
    ROCK, PAPER, SCISSORS
}

data class ScoreBoard(var player1Score: Int, var player2Score: Int)