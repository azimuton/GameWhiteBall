package com.azimuton.gamewhiteball.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.azimuton.gamewhiteball.MAIN
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.database.AppRoomDatabase
import com.azimuton.gamewhiteball.database.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Random
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GameFragment : Fragment() {
    private lateinit var gameView: GameView
    private var time = ""
    private var reportTime = ""
    private var randomColor : Int = 0
    private var score = 0
    private lateinit var musicPlayer: MediaPlayer
    lateinit var database : AppRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameView = GameView(requireActivity())
        return gameView
    }
    @OptIn(ExperimentalStdlibApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppRoomDatabase.getDatabase(requireActivity())
        playMusic()
        // Случайный цвет фона
        randomColor = Color.argb(255, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
        randomColor.toHexString()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MAIN.navController.navigate(R.id.action_gameFragment_to_mainFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }
    inner class GameView(context: Context) : View(context) {
        private val paint = Paint()
        private val blockWidth = 100
        private val blockHeight = 50
        private var blockX1 = 0f
        private var blockY1 = 950f
        private var blockX2 = 0f
        private var blockY2 = 1250f
        private var blockX3 = 0f
        private var blockY3 = 1550f
        private var blockX4 = 0f
        private var blockY4 = 1100f
        private var blockX5 = 0f
        private var blockY5 = 1400f
        private var blockSpeed1 = Random().nextInt(36).toFloat() + 15
        private var blockSpeed2 = Random().nextInt(36).toFloat() + 15
        private var blockSpeed3 = Random().nextInt(36).toFloat() + 15
        private var blockSpeed4 = Random().nextInt(36).toFloat() + 15
        private var blockSpeed5 = Random().nextInt(36).toFloat() + 15
        private val bigCircleRadius = 500f
        private val smallCircleRadius = 300f
        private val ballRadius = 50f
        private var ballX = 0f
        private var ballY = 0f
        private var ballAngle = 0f
        private var isTouching = false
        private var seconds = 0

        @SuppressLint("DrawAllocation", "SimpleDateFormat", "ClickableViewAccessibility")
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            gameView.setBackgroundColor(randomColor)

            paint.color = Color.GRAY
            canvas.drawCircle(width / 2f, height / 2f, bigCircleRadius, paint)
            paint.color = Color.GREEN
            canvas.drawCircle(width / 2f, height / 2f, smallCircleRadius, paint)
            paint.color = Color.BLACK
            canvas.drawRect(blockX1, blockY1, blockX1 + blockWidth, blockY1 + blockHeight, paint)
            paint.color = Color.WHITE
            canvas.drawRect(blockX4, blockY4, blockX4 + blockWidth, blockY4 + blockHeight, paint)
            paint.color = Color.BLACK
            canvas.drawRect(blockX2, blockY2, blockX2 + blockWidth, blockY2 + blockHeight, paint)
            paint.color = Color.WHITE
            canvas.drawRect(blockX5, blockY5, blockX5 + blockWidth, blockY5 + blockHeight, paint)
            paint.color = Color.BLACK
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
            blockX4 += blockSpeed4
            blockX5 += blockSpeed5
            if (blockX1 >= width) {
                blockSpeed1 = Random().nextInt(36).toFloat() + 15
                blockX1 = 0f
            }
            if (blockX2 >= width) {
                blockSpeed2 = Random().nextInt(36).toFloat() + 15
                blockX2 = 0f
            }
            if (blockX3 >= width) {
                blockSpeed3 = Random().nextInt(36).toFloat() + 15
                blockX3 = 0f
            }
            if (blockX4 >= width) {
                blockSpeed4 = Random().nextInt(36).toFloat() + 15
                blockX4 = 0f
            }
            if (blockX5 >= width) {
                blockSpeed5 = Random().nextInt(36).toFloat() + 15
                blockX5 = 0f
            }

            // Проверяем, столкнулся ли шарик с белым блоком
            val dx4 = ballX - blockX4
            val dy4 = ballY - blockY4
            val distance4 = sqrt(dx4 * dx4 + dy4 * dy4)
            val dx5 = ballX - blockX5
            val dy5 = ballY - blockY5
            val distance5 = sqrt(dx5 * dx5 + dy5 * dy5)
            if (distance4 < ballRadius || distance5 < ballRadius) {
                // Если да, то увеличиваем счет
                score += 1
            }

            // Проверяем, столкнулся ли шарик с черным блоком
            val dx1 = ballX - blockX1
            val dy1 = ballY - blockY1
            val distance1 = sqrt(dx1 * dx1 + dy1 * dy1)
            val dx2 = ballX - blockX2
            val dy2 = ballY - blockY2
            val distance2 = sqrt(dx2 * dx2 + dy2 * dy2)
            val dx3 = ballX - blockX3
            val dy3 = ballY - blockY3
            val distance3 = sqrt(dx3 * dx3 + dy3 * dy3)
            if (distance1 < ballRadius || distance2 < ballRadius || distance3 < ballRadius) {
                // Устанавливаем начальное положение шарика
                ballX = width / 2f
                ballY = height / 2f
                score = 0
            }

            // Обновляем значение времени
            val currentTime = System.currentTimeMillis()
            val timeInFormat = SimpleDateFormat("hh:mm:ss").format(currentTime)
            // Рисуем время на экране
            paint.color = Color.BLACK
            paint.textSize = 50f
            canvas.drawText("Время : $timeInFormat", ((width / 2).toFloat() - 200f), 100f, paint)

            // Рисуем секундомер
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                seconds++
                time = String.format(
                    Locale.getDefault(), "%02d:%02d:%02d",
                    seconds / 3600, seconds % 3600 / 60, seconds % 60
                )
            }
            reportTime = "Время игры : $time"
            paint.color = Color.BLACK
            paint.textSize = 50f
            canvas.drawText(reportTime, ((width / 2).toFloat() - 200f), 170f, paint)

            // Рисуем текст счетчика
            paint.color = Color.BLACK
            paint.textSize = 100f
            canvas.drawText("$score", ((width / 2).toFloat()), 300f, paint)

            invalidate()
        }
        @SuppressLint("ClickableViewAccessibility")
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
    override fun onDestroyView() {
        super.onDestroyView()
        stopMusic()
        releaseMusicPlayer()
        val history = History(count = score.toString(), color = randomColor.toString(), time = reportTime)
        database.historyDao().insertHistory(history)
    }
    private fun initMusicPlayer() {
        musicPlayer = MediaPlayer.create(requireActivity(), R.raw.stranger)
        musicPlayer.isLooping = true
    }
    private fun playMusic() {
        if (!::musicPlayer.isInitialized) {
            initMusicPlayer()
        }
        musicPlayer.start()
    }
    private fun stopMusic() {
        musicPlayer.stop()
    }
    private fun releaseMusicPlayer() {
        musicPlayer.stop()
        musicPlayer.release()
    }
}
