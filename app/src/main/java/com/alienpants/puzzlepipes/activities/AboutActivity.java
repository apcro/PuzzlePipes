package com.alienpants.puzzlepipes.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienpants.puzzlepipes.R;
import com.alienpants.puzzlepipes.services.Utilities;

import androidx.core.content.res.ResourcesCompat;


@SuppressWarnings("unchecked")
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Utilities.hideUI(this);

        setContentView(R.layout.activity_about);

        ImageView back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        Typeface typeface = ResourcesCompat.getFont(this, R.font.alba);
        TextView t1 = findViewById(R.id.textView);
        t1.setTypeface(typeface);
        TextView t2 = findViewById(R.id.textView3);
        t2.setTypeface(typeface);
        TextView t3 = findViewById(R.id.textView4);
        t3.setTypeface(typeface);
        TextView t4 = findViewById(R.id.textView5);
        t4.setTypeface(typeface);
    }


}
