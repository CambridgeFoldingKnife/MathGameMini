package com.drojian.mathgamemini

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

//启动页，点击开始后跳转
class LaunchActivity : AppCompatActivity() {
    private lateinit var startButton: TextView //延时给值用lateinit
    private lateinit var dogImage : ImageView
    private lateinit var loadingDots: LinearLayout
    private lateinit var dots: Array<View>
    private val handle = Handler(Looper.getMainLooper())//定义一个延时器
    private var isJumped =false//判断是否已经跳转了
    private var currentDot = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launch)

        startButton = findViewById(R.id.btn_start)
        dogImage = findViewById(R.id.iv_dog)
        loadingDots = findViewById(R.id.loading_dots)
        dots = arrayOf(
            findViewById(R.id.dot_1),
            findViewById(R.id.dot_2),
            findViewById(R.id.dot_3)
        )
        //判断是否是第一次打开游戏，是的话加载 开始按钮
        val sp =getSharedPreferences("math_game", Context.MODE_PRIVATE)
        val openTimes = sp.getInt("open_times",0)//获取open_times这个键存的值，如果这个key没有就值就设为0
        if(openTimes==0){
            startButton.visibility = View.VISIBLE
            loadingDots.visibility = View.GONE
            startBlink()         //🐶一直眨眼
        }else{
           // 非第一次
            startButton.visibility = View.GONE
            loadingDots.visibility = View.VISIBLE //点动画加载1.5s后跳转主界面
            //🐶眨一下就跳转主界面，延时器1.5s执行跳转函数
            startOneEyes()
            startDotRun()
            handle.postDelayed({goToMain()},1500L)
        }
        //user点击也跳转
        startButton.setOnClickListener {
            goToMain()
        }

    }


    //第一次启动页 狗 等两秒开始眨眼 一只眨眼
    private fun startBlink(){
        handle.postDelayed({closeDogEyes()},2000L)
    }
    //非第一次启动-狗只眨一下
    private fun startOneEyes(){
        handle.postDelayed({closeDogEyes()},200L)
    }

    //狗闭眼  切换图片然后200ms睁眼
    private fun closeDogEyes(){
        dogImage.setImageResource(R.drawable.pic_anim_splash_dog_close_eyes)
        handle.postDelayed({openDogEyes()},200L)
    }
    //狗睁眼 然后2s后闭眼
    private fun openDogEyes(){
        dogImage.setImageResource(R.drawable.pic_anim_splash_dog_open_eyes)
        handle.postDelayed({closeDogEyes()},2000L)
    }
    //小白点加载动画
    private fun startDotRun(){
        currentDot = 0
        showDot(currentDot)
        handle.postDelayed({showNextDot()},300L)
    }
    //展示当前点
    private fun showDot(index : Int){
        for (i in 0 until dots.size){
            dots[i].alpha = 0.3f     //其他点 透明度0.3
        }
        dots[index].alpha = 1f  //当前透明度 1
    }
    //展示下一个点  3ms 一次
    private fun showNextDot(){
        currentDot = currentDot+1
        if (currentDot==dots.size)  currentDot=0

        showDot(currentDot)
        handle.postDelayed({showNextDot()},300L)


    }

    //跳转到游戏界面逻辑
    private fun goToMain(){
        if (isJumped){//如果是false就返回  避免按钮页面和定时器同时触发。
            return
        }
        isJumped =true
        val sp = getSharedPreferences("math_game", Context.MODE_PRIVATE)
        val openTimes = sp.getInt("open_times",0)
        sp.edit().putInt("open_times",openTimes+1).apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        handle.removeCallbacksAndMessages(null)
    }
}