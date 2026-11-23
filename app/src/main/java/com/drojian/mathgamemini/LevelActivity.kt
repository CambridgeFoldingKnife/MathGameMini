package com.drojian.mathgamemini

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
    private var worryTimes = 0//失败过几次
    private var showingCorrec = false//是否已经显示过正确答案 标记位


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
            if (answer.isEmpty()) {
                Toast.makeText(this, "请输入您的答案", Toast.LENGTH_SHORT).show()
            } else {
                val userAnswer = answer.toInt()
                if (userAnswer == correctAnswer) {//做对了
                    if (cunrrentQusetions < totalQuestions) {//当前不是最后一关
                        cunrrentQusetions += 1
                        Toast.makeText(this, "Good！", Toast.LENGTH_SHORT).show()
                        newQuestions()
                    } else {//是最后一关
                        Toast.makeText(this, "本关完成", Toast.LENGTH_SHORT).show()
                    }
                }else{//做错了 扣一个心心
                    if (heartCount > 0){
                        heartCount-=1
                        heartText.text =getString(R.string.heart_format,heartCount)
                        if (heartCount==0){
                            Toast.makeText(this,"已经没有爱心了", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(this,"答错了，加油哦", Toast.LENGTH_SHORT).show()
                        answer = ""
                        updateAnswerView()
                    }else{
                        Toast.makeText(this,"已经没有爱心了", Toast.LENGTH_SHORT).show()
                    }
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

    showApples(leftValue,rightValue)
    answer = ""
    updateAnswerView()



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


    //绑定数字--拼到答案后面
    private fun bindNumber(button: TextView,number: String){
        button.setOnClickListener {
            if(answer.length< maxAnswerLength){
                //没到打最大位数，按的数字拼接在后面
                answer = answer + number
                updateAnswerView()
            } else{
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