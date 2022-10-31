package com.merryblue.fakemessenger.enum

import android.content.Context
import com.merryblue.fakemessenger.R
import org.app.data.model.IntroModel

enum class IntroPage {
    PAGE_0,
    PAGE_1;

    companion object {
        fun allPage(context: Context) : List<IntroModel> {
            return listOf(
                IntroModel(0,
                    R.drawable.boder_oval,
                    context.getString(R.string.txt_option_1),
                    R.drawable.boder_oval,
                    context.getString(R.string.txt_option_2),
                    R.drawable.img_intro_p1_title,
                    R.drawable.img_intro_p1_subtitle
                ),
                IntroModel(1,
                    R.drawable.boder_oval,
                    context.getString(R.string.txt_option_3),
                    R.drawable.iv_done,
                    context.getString(R.string.txt_option_4),
                    R.drawable.img_intro_p2_title,
                    R.drawable.img_intro_p2_subtitle
                ),
            )
        }
    }
}