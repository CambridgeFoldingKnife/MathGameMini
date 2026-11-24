package com.drojian.mathgamemini

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class ResultActivity : BaseActivity(){
    private var levelId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        applySystemBarPadding(
            findViewById(R.id.top_space),
            findViewById(R.id.bottom_space)
        )

        levelId = intent.getIntExtra("level_id",1)
        var nextButton = findViewById<TextView>(R.id.btn_next_level)
        var okButton = findViewById<TextView>(R.id.btn_ok)
     /*   if(levelId>=5){
            nextButton.setOnClickListener {
                Toast.makeText(this,"下一关按钮！", Toast.LENGTH_SHORT).show()
            }*/

        /*点击之后开启下一关  存一下当前关卡
        * */
        nextButton.setOnClickListener {
            var intent = Intent(this, LevelActivity::class.java)
            intent.putExtra("level_id",levelId+1)
            startActivity(intent)
            finish()
        }
        /*ok 按钮点击之后关闭结果页
        * */
        okButton.setOnClickListener {
            finish()
        }


        }


    }

