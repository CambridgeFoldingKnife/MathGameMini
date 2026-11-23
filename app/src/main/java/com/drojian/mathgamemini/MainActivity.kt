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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        设置顶部的标题栏和系统状态栏颜色统一
        applySystemBarPadding(
            findViewById<View>(R.id.top_space),
            findViewById<View>(R.id.bottom_space)
        )

        crownText = findViewById(R.id.tv_crown_num)
        heartText = findViewById(R.id.tv_heart_num)

        val home = findViewById<FrameLayout>(R.id.home)
        val achieve = findViewById<FrameLayout>(R.id.achieve)
        val setting = findViewById<FrameLayout>(R.id.setting)

        //1、=========读取本地进度：关卡数、爱心数==========
        //直接读取，没有的话就设置1和5
        val sp = getSharedPreferences("math_game", Context.MODE_PRIVATE)
        val openLevel = sp.getInt("passed_level", 1)
        val hearts = sp.getInt("hearts", 5)


        //设置皇冠数和爱心数  皇冠与通关数有关系：crownnums  = (passed_level-1)*1
        crownText.text= (openLevel-1).toString()
        heartText.text = hearts.toString()

        setupLevelList()

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

    override fun onLevelClick(level: Level) {
        if(level.id > openLevel){
            Toast.makeText(this,R.string.level_locked_tip, Toast.LENGTH_SHORT).show()
            return
        }
        //如果解锁之后跳转关卡页
        val intent = Intent(this, LevelActivity::class.java)
        intent.putExtra("level_id", level.id)
        startActivity(intent)

    }




}