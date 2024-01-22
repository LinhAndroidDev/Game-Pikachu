package com.example.gamepikachu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamepikachu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val pikachuAdapter = PikachuAdapter()
    private var screen = ScreenPikachu.SCREEN_2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        createPikachu()

        binding.reset.setOnClickListener {
            resetPikachu()
        }
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
                val grid4 = GridLayoutManager(this, 8, RecyclerView.VERTICAL, false)
                binding.rcvPikachu.layoutManager = grid4
                val pikachuCovers = listPikachu8()
                pikachuCovers.toList().shuffled() as ArrayList<Pikachu>
            }

            else -> {
                val grid4 = GridLayoutManager(this, 8, RecyclerView.VERTICAL, false)
                binding.rcvPikachu.layoutManager = grid4
                val pikachuCovers = listPikachu12()
                pikachuCovers.toList().shuffled() as ArrayList<Pikachu>
            }
        }
        pikachuAdapter.pikachus = pikachus
        binding.rcvPikachu.adapter = pikachuAdapter
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

                    else -> {
                        ScreenPikachu.SCREEN_2
                    }
                }
                resetPikachu()
            }
        }
    }
}