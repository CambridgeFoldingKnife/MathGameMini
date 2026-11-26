package com.drojian.mathgamemini
import android.app.Activity
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


//=============================弹窗类
class AnswerDialog(private val activity: Activity) {

    private val title = "失误一次没什么大不了的"
    //第一次答错
    fun showFirstWrong() {
        showFeedback(
            title ,
            message = getFirstWrongMessage(),
            answer = null
        )
    }
/*拼接字符串
里面有安心图片儿，需要拼接一下文字加图片
* */
    private fun getFirstWrongMessage(): CharSequence {
        // 生成第一次做错的弹窗文字，把 [heart] 换成爱心图片
        val message = "必须有 [heart] 才能开始游戏。如果用完了，可以通过练习你的薄弱环节来赚取爱心。"
        val showText = SpannableString(message)
    //找到这个占位符的位置。
    val heartIndex = message.indexOf("[heart]")
    if (heartIndex<0){
        return showText
    }
    //取爱心图片
    val heartImage = ContextCompat.getDrawable(
        activity,
        R.drawable.vector_ic_heart_available
    )?: return showText
    //设置爱心大小
    val heartSize = activity.resources.getDimensionPixelSize(R.dimen.dp_24)
    heartImage.setBounds(0,0,heartSize,heartSize)

    //图片替换占位符
    val heartSpan = ImageSpan(heartImage)
    showText.setSpan(
        heartSpan,
        heartIndex,
        heartIndex+"[heart]".length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return showText

    }

    // 第二次做错：显示正确答案
    fun showCorrectAnswer(answer: Int, onContinue: () -> Unit) {
        showFeedback(
            title = "正确答案",
            answer = answer.toString(),
            message ="",
            onContinue = onContinue
        )
    }


// 两种反馈弹窗共用 有answer显示大号答案
// ========这一关 7 道题第一次错鼓励、第二次错显示正确答案=======
private fun showFeedback(title: String,
                         message: CharSequence,
                         answer: String?,
                         onContinue: () -> Unit={}) {
    val dialog = BottomSheetDialog(activity)

    // 加载自定义布局
    dialog.setContentView(R.layout.dialog_answer_err)

    //遮罩透明度
    dialog.window?.setDimAmount(0.35f)

    //正确答案弹窗  必须点击继续
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)

    val titleText = dialog.findViewById<TextView>(R.id.tv_feedback_title)
    val messageText = dialog.findViewById<TextView>(R.id.tv_feedback_msg)
    val okButton = dialog.findViewById<TextView>(R.id.tv_feedback_ok)
    val answerText = dialog.findViewById<TextView>(R.id.tv_feedback_answer)

    titleText?.text = title
    messageText?.text = message

    if (answer == null) { //第一次做错仅仅鼓励 隐藏答案
        answerText?.visibility = View.GONE
    } else {   //第二次做错 就提示正确答案了
        answerText?.visibility = View.VISIBLE
        answerText?.text = answer
    }
    okButton?.setOnClickListener {
        dialog.dismiss()
        onContinue()
    }
dialog.setOnShowListener {
    val bottomSheet = dialog.findViewById<View>(
        com.google.android.material.R.id.design_bottom_sheet
    )
    bottomSheet?.let { sheet ->
        // 让系统底部弹层透明，使用我们的圆角白底
        sheet.setBackgroundColor(Color.TRANSPARENT)

        // 高度跟随内容，不铺满屏幕
        sheet.layoutParams = sheet.layoutParams.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        val behavior = BottomSheetBehavior.from(sheet)
        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        // 禁止用户把正确答案弹窗滑下去
        behavior.isHideable = false
    }

}
     dialog.show()
}


//     ===== 没有爱心弹窗设置=========================================
fun showNoHeart(onReview:() -> Unit,onClose:()-> Unit){
    val dialog = BottomSheetDialog(activity)
    dialog.setContentView(R.layout.dialog_no_heart)

    dialog.window?.setDimAmount(0.35f)

   // dialog.setCancelable(false)
    //dialog.setCanceledOnTouchOutside(false)

    val titleText = dialog.findViewById<TextView>(R.id.tv_no_heart_title)
    val reviewBoard = dialog.findViewById<View>(R.id.review_board)
    val thankText  = dialog.findViewById<TextView>(R.id.tv_thank)

    titleText?.text = getNoHeartTitle()

    //点击复习按钮跳到 onreview 后续谁调用这个弹窗谁自定义这个函数。
    //但是后续都是跳转到复习页面
    reviewBoard?.setOnClickListener {
        onReview()
    }
    //不了谢谢
    thankText?.setOnClickListener {
        dialog.dismiss()
        onClose()
    }

    dialog.setOnShowListener {
        val bottomSheet = dialog.findViewById< View>(
            com.google.android.material.R.id.design_bottom_sheet
        )
        bottomSheet?.let {  sheet ->
            sheet.setBackgroundColor(Color.TRANSPARENT)
            sheet.layoutParams = sheet.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            val behavior = BottomSheetBehavior.from(sheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isHideable = false

        }
    }
        dialog.show()
}


//处理文字中的爱心图片 加载那个文本框中
private fun getNoHeartTitle(): CharSequence {
    val text = "你没有[heart]了"
    val showText = SpannableString(text)

    val heartPostioin = text.indexOf("[heart]")
    if (heartPostioin < 0) {
        return showText
    }
    val heartimage = ContextCompat.getDrawable(
        activity,
        R.drawable.vector_ic_heart_available
    ) ?: return "你没有爱心了"
     //设置爱心大小
    val heartPx = activity.resources.getDimensionPixelSize(R.dimen.dp_24)
    heartimage.setBounds(0,0,heartPx,heartPx)

    //图片替换掉占位符
    val heartSpan = ImageSpan(heartimage)
    showText.setSpan(
        heartSpan,
        heartPostioin,
        heartPostioin+"[heart]".length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return showText
}

/*
* =========放弃本关卡的弹窗===========
* */
fun showQuit(onGiveUp:()-> Unit,onContinue: () -> Unit,onFeedBack:()-> Unit){
    val dialog = BottomSheetDialog(activity)
    dialog.setContentView(R.layout.dialog_quit_level)

    dialog.window?.setDimAmount(0.35f)
//    dialog.setCancelable(true)
//    dialog.setCanceledOnTouchOutside(true)

    val giveUpButton = dialog.findViewById<TextView>(R.id.btn_give_up)
    val continueButton = dialog.findViewById<TextView>(R.id.btn_coutinue)
    val feedbackButton = dialog.findViewById<View>(R.id.btn_quit_feedback)

    //定义三个按键的跳转逻辑
    giveUpButton?.setOnClickListener {
        dialog.dismiss()
        onGiveUp()
    }

    continueButton?.setOnClickListener {
        dialog.dismiss()
        onContinue()
    }

    feedbackButton?.setOnClickListener {
        dialog.dismiss()
        onFeedBack()
    }
    //todo：？？？？？
    dialog.setOnShowListener {
        val bottomSheet = dialog.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        )

        bottomSheet?.let { sheet ->
            sheet.setBackgroundColor(Color.TRANSPARENT)

            sheet.layoutParams = sheet.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            val behavior = BottomSheetBehavior.from(sheet)
            behavior.skipCollapsed =true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isHideable =false

        }
    }

    dialog.show()

}


}

