package com.vuthaihung.fancydialouge.ui.splash;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vuthaihung.fancydialouge.R;


public class TextGradient extends ConstraintLayout {

    public TextGradient(Context context) {
        super(context);
        initView();
    }

    public TextGradient(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.gradient_text, this);
        TextView txt_gradient = findViewById(R.id.txt_gradient);
        TextPaint paint = txt_gradient.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, txt_gradient.getTextSize(),
                new int[]{
                        Color.parseColor("#0192FF"),
                        Color.parseColor("#AE38EF"),
                        Color.parseColor("#FD6E64"),
                }, null, Shader.TileMode.CLAMP);
        txt_gradient.getPaint().setShader(textShader);
    }
}
