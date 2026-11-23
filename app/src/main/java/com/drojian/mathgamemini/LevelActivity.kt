package com.drojian.mathgamemini

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

    const val applesMaxNum = 3 //全局常量最多一行 3 个苹果
class LevelActivity : BaseActivity (){
    private lateinit var progressText: TextView    // 顶部进度
    private lateinit var heartText: TextView  // 顶部红心
    private lateinit var leftNumber: TextView//被加数
    private lateinit var rightNumber: TextView//加数
    private lateinit var answerText: TextView//等式右边？
    private lateinit var leftApples: LinearLayout//左边操作数苹果
    private lateinit var rightApples: LinearLayout//右边苹果

    private lateinit var deleteButton: ImageView  //删除按钮
    private lateinit var checkButton: TextView //检查提交按钮
    private lateinit var skipButton: TextView//跳过按钮
    private lateinit var hintButton: ImageView//提示按钮。
    private var answer =  ""  //用户答案
    private var levelId = 1//当前关卡 id
    private var maxAnswerLength = 2  //答案最多几位
    private val maxNumber = 5//当前关卡的最大数字 起始 是5 以内加减法
    private val totalQuestions = 5//最多 5 道题
    private var cunrrentQusetions = 1 //当前的关卡数
    private var leftValue = 0
    private var rightValue = 0
    private var correctAnswer = 0 //当前关卡的正确答案
    private var heartCount = 5//剩余红心
    private var wrongTimes = 0//失败过几次
    private var showingCorrect = false//是否已经显示过正确答案 标记位


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        //处理沉浸式状态栏
        applySystemBarPadding(
            findViewById(R.id.top_space),
            findViewById(R.id.bottom_space)
        )

        levelId  = intent.getIntExtra("level_id",1)
        setAnswerLimit(10)
        progressText = findViewById(R.id.tv_progress)
        heartText = findViewById(R.id.tv_heart)
        leftNumber = findViewById(R.id.tv_left_num)
        rightNumber = findViewById(R.id.tv_right_num)
        answerText = findViewById(R.id.tv_answer)
        leftApples = findViewById(R.id.ll_apple_left)
        rightApples = findViewById(R.id.ll_apple_right)
        deleteButton = findViewById(R.id.iv_delete)
        checkButton = findViewById(R.id.btn_check)
        skipButton = findViewById(R.id.btn_skip)
        hintButton = findViewById(R.id.iv_hint) //提示按钮灯泡

        /*
        * getstring 是 content 包下用来填充 string 中占位符用的
        * content 包
        * */
        heartText.text = getString(R.string.heart_format,heartCount)//初始化爱心 5 个
        newQuestions()


        //提示按钮
        findViewById<TextView>(R.id.tv_close).setOnClickListener {
            finish()
        }
        //关闭按钮
        hintButton.setOnClickListener {
            Toast.makeText(this,"提示功能待开发！", Toast.LENGTH_SHORT).show()
        }
        //给数字键加上对应的数字 0-9
        bindNumber(findViewById(R.id.btn_0), "0")
        bindNumber(findViewById(R.id.btn_1), "1")
        bindNumber(findViewById(R.id.btn_2), "2")
        bindNumber(findViewById(R.id.btn_3), "3")
        bindNumber(findViewById(R.id.btn_4), "4")
        bindNumber(findViewById(R.id.btn_5), "5")
        bindNumber(findViewById(R.id.btn_6), "6")
        bindNumber(findViewById(R.id.btn_7), "7")
        bindNumber(findViewById(R.id.btn_8), "8")
        bindNumber(findViewById(R.id.btn_9), "9")

        //回退数字绑定点击事件
        deleteButton.setOnClickListener {
            if (answer.isNotEmpty()) {//有答案答案不为空
                answer = answer.substring(0, answer.length - 1)
                updateAnswerView()
            }

        }

        // 初始显示 ?，隐藏三个按钮
        updateAnswerView()

        skipButton.setOnClickListener {
            Toast.makeText(this,"跳过", Toast.LENGTH_SHORT).show()
        }

        //检查按钮
        checkButton.setOnClickListener {
            if (showingCorrect){// 正确答案弹窗正在显示 键盘检查按钮不负责继续
                Toast.makeText(this, "请点击弹窗里的继续", Toast.LENGTH_SHORT).show()
            }else if (answer.isEmpty()){
                Toast.makeText(this, "请输入您的答案", Toast.LENGTH_SHORT).show()
            }else{
                val userAnswer = answer.toInt()
                if (userAnswer==correctAnswer){ //用户答案正确
                    goNextQuestion() //是否能走到下一题，需要配合检查爱心数量。
                }else{ //用户答案错误
                    handleWrongAnswer() //判断第几次错，逻辑不一样
                }
            }
        }





    }
/*
* 用来生成最新的题目*/
    private fun newQuestions() {
    leftValue=  Random.nextInt(0,maxNumber+1)
    rightValue =  Random.nextInt(0,maxNumber+1)
    correctAnswer = leftValue+rightValue
    leftNumber.text = leftValue.toString()
    rightNumber.text = rightValue.toString()
    //展示苹果
    showApples(leftValue,rightValue)
    answer = ""
    updateAnswerView()  //更新键盘状态
//重置 错误状态 和 显示正确答案弹窗
    wrongTimes = 0
    showingCorrect =false
    progressText.text = getString(
        R.string.level_progress,
        cunrrentQusetions,
        totalQuestions  )



    }

    //刷新答案和按钮的显示
    private fun updateAnswerView(){
        //如果每填就是？
        if (answer.isEmpty()) {
            answerText.text = "?"
            deleteButton.visibility = View.INVISIBLE
            skipButton.visibility = View.INVISIBLE
            checkButton.visibility = View.INVISIBLE
        } else {
            answerText.text = answer
            deleteButton.visibility = View.VISIBLE
            skipButton.visibility = View.VISIBLE
            checkButton.visibility = View.VISIBLE
        }
    }
    // 根据答案最大值，算出最多输入几位
    private fun setAnswerLimit(maxAnswer: Int){
        maxAnswerLength = maxAnswer.toString().length
    }

    //错误判断:第一次错或者第二次错
    private fun handleWrongAnswer(){
        if(wrongTimes==0){//第一次错
            wrongTimes=1
            if (heartCount >0){
                heartCount -=1
                heartText.text = getString(R.string.heart_format,heartCount)
            }
            Toast.makeText(this,"答错了，请再试一次", Toast.LENGTH_SHORT).show()
        }else{//第二次错
            wrongTimes =2
            showingCorrect =true
            //弹正确答案
            showCorrectAnswerDialog()
        }
    }

/*展示正确答案底部弹窗
* */
    private fun showCorrectAnswerDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("正确答案")
            .setMessage(correctAnswer.toString())
            .setCancelable(false)
            .setPositiveButton("继续") { dialog, which ->
                dialog.dismiss()//先关闭弹窗
                goNextQuestion()//再进入下一题
            }
            .create()

        alertDialog.show()
    }
/*进入下一题
* */
    private fun goNextQuestion() {
        if (cunrrentQusetions>=totalQuestions){
            //showLevelCompleteDialog() // 结果页下一步接 弹窗占位
            val intent = Intent(this, ResultActivity::class.java) // 创建跳转
            intent.putExtra("level_id", levelId) // 把当前关卡传过去
            startActivity(intent) // 打开结果页
            finish() // 关闭答题页
            return // 结束当前方法
        }
       if (heartCount <= 0){
           showNoHeartDialog()
           return
       }
    cunrrentQusetions+=1
    newQuestions()
    }

    /*展示没有红心了的弹窗
 * */
    private fun showNoHeartDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("你没有爱心了")
            .setMessage("红心已经用完了")
            .setCancelable(false)
            .setPositiveButton("返回首页") { dialog, which ->
                finish()

            }
            .create()

        alertDialog.show()
    }

    // 本关完成临时弹窗
    private fun showLevelCompleteDialog() {
        val alertDialog = AlertDialog.Builder(this) // 创建弹窗
            .setTitle("本关完成") // 标题
            .setMessage("点击继续返回首页") // 内容
            .setCancelable(false) // 不能点外面关闭
            .setPositiveButton("继续") { dialog, which -> // 继续按钮
                finish() // 先关闭答题页
            }
            .create() // 创建弹窗对象

        alertDialog.show() // 显示弹窗
    }



    //绑定数字--拼到答案后面
    private fun bindNumber(button: TextView,number: String){
        button.setOnClickListener {
            if(showingCorrect){//已经是第二次出现错误了
                Toast.makeText(this,"请点击弹窗里的继续", Toast.LENGTH_SHORT).show()
            }else if(answer.length < maxAnswerLength){
                //没到打最大位数，按的数字拼接在后面
                answer = answer + number
                updateAnswerView()
            }else{
                Toast.makeText(this,"最多输入：${maxAnswerLength}位数字", Toast.LENGTH_SHORT).show()
            }
        }

    }


    //展示苹果图案。
    private fun showApples(leftCount: Int,rightCount: Int){
        leftApples.removeAllViews()
        rightApples.removeAllViews()
            addAppleRow(leftApples,leftCount)
            addAppleRow(rightApples,rightCount)

    }
    //按照一行一行来添加苹果
    private fun addAppleRow(parent: LinearLayout,count: Int){
        var appleIndex = 0 //当前 苹果数

        while(appleIndex < count){
            var row = LinearLayout(this)
            // 把这一行加进竖向的苹果容器
            parent.addView(row)

            var inRowNum = 0//当前这一行里已经放了几个苹果
            while (inRowNum < applesMaxNum && appleIndex<count){
                addApple(row)
                appleIndex+=1
                inRowNum+=1
            }
        }
    }

//添加苹果图像逻辑 ？？？
 private  fun addApple(parent: LinearLayout){
    val image = ImageView(this)
    image.setImageResource(R.drawable.vector_ic_apple)

    val width = resources.getDimensionPixelSize(R.dimen.dp_30)
    val height = resources.getDimensionPixelSize(R.dimen.dp_34)
    val params = LinearLayout.LayoutParams(width, height)
    params.marginEnd = resources.getDimensionPixelSize(R.dimen.dp_2)
    image.layoutParams = params

    parent.addView(image)

    }



}