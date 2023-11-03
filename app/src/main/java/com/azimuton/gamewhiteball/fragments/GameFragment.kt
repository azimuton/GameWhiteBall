package com.azimuton.gamewhiteball.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.azimuton.gamewhiteball.MAIN
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.databinding.FragmentGameBinding
import java.lang.Math.random
import java.util.Random
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {
    private lateinit var gameView: GameView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameView = GameView(requireActivity())
        return gameView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // gameView.invalidate()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MAIN.navController.navigate(R.id.action_gameFragment_to_mainFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    inner class GameView(context: Context) : View(context) {
        private val paint = Paint()
        private val blockWidth = 200
        private val blockHeight = 50
        private var blockX1 = 0f
        private var blockY1 = 1000f
        private var blockX2 = 0f
        private var blockY2 = 1250f
        private var blockX3 = 0f
        private var blockY3 = 1550f
        private var blockSpeed1 = Random().nextInt(16).toFloat() +5
        private var blockSpeed2 = Random().nextInt(16).toFloat() +5
        private var blockSpeed3 = Random().nextInt(16).toFloat() +5

        private val bigCircleRadius = 400f
        private val smallCircleRadius = 200f
        private val ballRadius = 50f
        private var ballX = 0f
        private var ballY = 0f
        private var ballAngle = 0f
        private var isTouching = false


        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            paint.color = Color.GRAY
            canvas.drawCircle(width/2f, height/2f, bigCircleRadius, paint)
            paint.color = Color.WHITE
            canvas.drawCircle(width/2f, height/2f, smallCircleRadius, paint)
            paint.color = Color.BLACK
            canvas.drawRect(blockX1, blockY1, blockX1 + blockWidth, blockY1 + blockHeight, paint)
            canvas.drawRect(blockX2, blockY2, blockX2 + blockWidth, blockY2 + blockHeight, paint)
            canvas.drawRect(blockX3, blockY3, blockX3 + blockWidth, blockY3 + blockHeight, paint)

            // Рассчитываем позицию шарика
            ballX = width / 2 + (bigCircleRadius - 100f) * cos(ballAngle)
            ballY = height / 2 + (bigCircleRadius - 100f) * sin(ballAngle)

            // Рисуем шарик
            paint.color = Color.WHITE
            canvas.drawCircle(ballX, ballY, ballRadius, paint)


            blockX1 += blockSpeed1
            blockX2 += blockSpeed2
            blockX3 += blockSpeed3
            if (blockX1 >= width) {
                blockSpeed1 = Random().nextInt(16).toFloat() +5
                blockX1 = 0f
            }
            if (blockX2 >= width) {
                blockSpeed2 = Random().nextInt(16).toFloat() + 5
                blockX2 = 0f
            }
            if (blockX3 >= width) {
                blockSpeed3 = Random().nextInt(16).toFloat() + 5
                blockX3 = 0f
            }

            invalidate()
        }
        override fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                     //Проверяем, нажат ли палец на шарик
                    val x = event.x
                    val y = event.y
                    if (ballX - ballRadius <= x && x <= ballX + ballRadius && ballY - ballRadius <= y && y <= ballY + ballRadius) {
                        isTouching = true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    // Если палец на шарике, то перемещаем шарик в соответствии с положением пальца
                    if (isTouching) {
                        // Перемещаем шарик в направлении движения пальца
                        val dx = event.x - ballX
                        val dy = event.y - ballY
                        val direction = if (dx > 0) 1 else -1
                        ballX += dx * direction
                        ballY += dy * direction

                        // Изменяем угол поворота шарика
                        val isScrollingRight = dx > 0
                        val isScrollingLeft = dy > 0
                        ballAngle += if (isScrollingRight && isScrollingLeft) 0.1f else -0.1f
                        if (ballAngle >= 2 * Math.PI) {
                            ballAngle = 0f
                        } else if (ballAngle < 0) {
                            ballAngle = (2 * Math.PI).toFloat()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // Если палец отпущен, то сбрасываем флаг нажатия
                    isTouching = false
                }
            }
            return true
        }

    }
}
