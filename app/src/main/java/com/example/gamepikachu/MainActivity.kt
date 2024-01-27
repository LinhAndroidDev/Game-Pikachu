package com.example.gamepikachu

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamepikachu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val pikachuAdapter = PikachuAdapter()
    private var screen = ScreenPikachu.SCREEN_2
    private var time: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fullScreen()

        initUi()
    }

    private fun initUi() {
        setUpTime()
        createPikachu()

        binding.reset.setOnClickListener {
            resetTimePlay()
            resetPikachu()
        }
    }

    private fun setUpTime() {
        binding.timePlay.max = screen.id / 2
        binding.timePlay.progress = screen.id / 2
        time = object : CountDownTimer(((screen.id * 1000) / 2).toLong(), 1000) {
            override fun onTick(p0: Long) {
                binding.timePlay.progress -= 1
            }

            override fun onFinish() {
                resetPikachu()
                resetTimePlay()
            }

        }.start()
    }

    private fun resetPikachu() {
        pikachuAdapter.index = -1
        pikachuAdapter.isSelect = false
        pikachuAdapter.pikachuSelected = Pikachu(0, 0)
        createPikachu()
    }

    private fun createPikachu() {
        val pikachus: ArrayList<Pikachu> = when (screen) {
            ScreenPikachu.SCREEN_2 -> {
                val grid2 = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
                binding.rcvPikachu.layoutManager = grid2
                val pikachuCovers = listPikachu2()
                pikachuCovers.toList().shuffled() as ArrayList<Pikachu>
            }

            ScreenPikachu.SCREEN_4 -> {
                val grid4 = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
                binding.rcvPikachu.layoutManager = grid4
                val pikachuCovers = listPikachu4()
                pikachuCovers.toList().shuffled() as ArrayList<Pikachu>
            }

            ScreenPikachu.SCREEN_8 -> {
                val grid8 = GridLayoutManager(this, 8, RecyclerView.VERTICAL, false)
                binding.rcvPikachu.layoutManager = grid8
                val pikachuCovers = listPikachu8()
                pikachuCovers.toList().shuffled() as ArrayList<Pikachu>
            }

            ScreenPikachu.SCREEN_12 -> {
                val grid8 = GridLayoutManager(this, 8, RecyclerView.VERTICAL, false)
                binding.rcvPikachu.layoutManager = grid8
                val pikachuCovers = listPikachu12()
                pikachuCovers.toList().shuffled() as ArrayList<Pikachu>
            }

            else -> {
                val grid8 = GridLayoutManager(this, 8, RecyclerView.VERTICAL, false)
                binding.rcvPikachu.layoutManager = grid8
                val pikachuCovers = listPikachu16()
                pikachuCovers.toList().shuffled() as ArrayList<Pikachu>
            }
        }
        pikachuAdapter.pikachus = pikachus
        binding.rcvPikachu.adapter = pikachuAdapter
        pikachuAdapter.isCorrect = {
            val correct = MediaPlayer.create(this, R.raw.soundgame)
            correct.start()
        }
        pikachuAdapter.isFinish = {
            if (it) {
                screen = when (screen) {
                    ScreenPikachu.SCREEN_2 -> {
                        ScreenPikachu.SCREEN_4
                    }

                    ScreenPikachu.SCREEN_4 -> {
                        ScreenPikachu.SCREEN_8
                    }

                    ScreenPikachu.SCREEN_8 -> {
                        ScreenPikachu.SCREEN_12
                    }

                    ScreenPikachu.SCREEN_12 -> {
                        ScreenPikachu.SCREEN_16
                    }

                    else -> {
                        ScreenPikachu.SCREEN_2
                    }
                }
                resetPikachu()
                resetTimePlay()
            }
        }
    }

    private fun resetTimePlay() {
        time?.cancel()
        time = null
        setUpTime()
    }

    private fun fullScreen() {
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }
}