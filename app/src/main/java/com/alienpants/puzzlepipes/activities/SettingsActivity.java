package com.alienpants.puzzlepipes.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.alienpants.puzzlepipes.R;
import com.alienpants.puzzlepipes.services.Utilities;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;


@SuppressWarnings("unchecked")
public class SettingsActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Utilities.hideUI(this);

        setContentView(R.layout.activity_settings);

        mContext = this;

        SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
        int boardSize = pref.getInt("difficulty", 4);
        setBoardsize(boardSize);

        ImageView mBack = findViewById(R.id.button_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        ImageView easy = findViewById(R.id.easyText);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardsize(4);
            }
        });

        ImageView normal = findViewById(R.id.normalText);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardsize(5);
            }
        });

        ImageView hard = findViewById(R.id.hardText);
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardsize(6);
            }
        });

        ImageView hard2 = findViewById(R.id.hardText2);
        hard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardsize(7);
            }
        });

        ImageView hard3 = findViewById(R.id.hardText3);
        hard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardsize(8);
            }
        });


        ImageView random = findViewById(R.id.randomText);
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardsize(99);
            }
        });

        ImageView sound = findViewById(R.id.soundText);

        ImageView about = findViewById(R.id.aboutIcon);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutPage();
            }
        });

        ImageView reset = findViewById(R.id.reseticon);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("gamecount", 1);
                editor.putInt("score", 1);
                editor.apply();
                onBackPressed();
            }
        });
    }

    private void showAboutPage() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    private void setBoardsize(int difficulty) {

        SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("difficulty", difficulty);
        editor.apply();

        ImageView easyTick = findViewById(R.id.easyTick);
        ImageView normalTick = findViewById(R.id.normalTick);
        ImageView hardTick = findViewById(R.id.hardTick);
        ImageView hardTick2 = findViewById(R.id.hardTick2);
        ImageView hardTick3 = findViewById(R.id.hardTick3);
        ImageView randomTick = findViewById(R.id.randomTick);

        easyTick.setVisibility(View.INVISIBLE);
        normalTick.setVisibility(View.INVISIBLE);
        hardTick.setVisibility(View.INVISIBLE);
        hardTick2.setVisibility(View.INVISIBLE);
        hardTick3.setVisibility(View.INVISIBLE);
        randomTick.setVisibility(View.INVISIBLE);

        switch (difficulty) {
            case 99:
                randomTick.setVisibility(View.VISIBLE);
                break;
            case 4:
                easyTick.setVisibility(View.VISIBLE);
                break;
            case 5:
                normalTick.setVisibility(View.VISIBLE);
                break;
            case 6:
                hardTick.setVisibility(View.VISIBLE);
                break;
            case 7:
                hardTick2.setVisibility(View.VISIBLE);
                break;
            case 8:
                hardTick3.setVisibility(View.VISIBLE);
                break;


        }


    }

}


