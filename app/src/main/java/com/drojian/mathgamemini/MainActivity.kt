package com.drojian.mathgamemini

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : BaseActivity(), LevelAdapter.OnLevelClick {
    private lateinit var crownText: TextView
    private lateinit var heartText: TextView

    private  var openLevel = 1

    private var heartCount = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applySystemBarPadding(
            findViewById<View>(R.id.top_space),
            findViewById<View>(R.id.bottom_space)
        )

        crownText = findViewById(R.id.tv_crown_num)
        heartText = findViewById(R.id.tv_heart_num)

        val home = findViewById<FrameLayout>(R.id.home)
        val achieve = findViewById<FrameLayout>(R.id.achieve)
        val setting = findViewById<FrameLayout>(R.id.setting)


        //======底部按钮栏目跳转 其他界面=======
        //TODO(跳转其他页面)
        home.setOnClickListener {
            Toast.makeText(this,R.string.tab_home, Toast.LENGTH_SHORT).show()
        }

        achieve.setOnClickListener {
            Toast.makeText(this,R.string.tab_achieve_todo,Toast.LENGTH_SHORT).show()
        }

        setting.setOnClickListener {
            Toast.makeText(this,R.string.tab_setting_todo, Toast.LENGTH_SHORT).show()
        }

    }
/*

* */
    private fun setupLevelList(){
        val levelList = ArrayList<Level>()
        levelList.add(Level(1,getString(R.string.level_add_1)))
        levelList.add(Level(2,getString(R.string.level_add_2)))
        levelList.add(Level(3,getString(R.string.level_add_3)))
        levelList.add(Level(4,getString(R.string.level_equal_5_1)))
        levelList.add(Level(5,getString(R.string.level_equal_5_2)))

        val recycler = findViewById<RecyclerView>(R.id.rv_level_list)
        recycler.layoutManager = GridLayoutManager(this,2)
        recycler.adapter = LevelAdapter(levelList,openLevel,this)
    }


    //点击打开关卡
    override fun onLevelClick(level: Level) {
        if(level.id > openLevel){
            Toast.makeText(this,R.string.level_locked_tip, Toast.LENGTH_SHORT).show()
            return
        }
        if (heartCount<=0){
            showNoHeartDialog()
            return //
        }
        //如果是已经解锁的 跳转关卡页
        val intent = Intent(this, LevelActivity::class.java)
        intent.putExtra("level_id", level.id)
        startActivity(intent)
    }

/*
* 没有爱心不让进下一关
 */
    private fun showNoHeartDialog() {
        AnswerDialog(this).showNoHeart(
            onReview = {
                Toast.makeText(this,"复习页还没做，占个位置", Toast.LENGTH_SHORT).show()
            },
            onClose = {

            }
        )
    }


    /*每次回到首页就执行
    * */
    override fun onResume() {
        super.onResume()
        refreshHomeProgress()

    }
    //读取进度刷新首页
    /*设计皇冠数、记录通关数、
    * */
    private fun  refreshHomeProgress(){
        val sp = getSharedPreferences("math_game", Context.MODE_PRIVATE)
        openLevel = sp.getInt("passed_level",1)
        heartCount = sp.getInt("hearts",5)

        crownText.text = (openLevel-1).toString()
        heartText.text = heartCount.toString()
         //刷新一下关卡页面的 状态，变绿
        setupLevelList()

    }




}