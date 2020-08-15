package com.alienpants.puzzlepipes.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alienpants.puzzlepipes.R;
import com.alienpants.puzzlepipes.datamodels.GameStatus;
import com.alienpants.puzzlepipes.services.Utilities;

import androidx.core.content.res.ResourcesCompat;

public class GameActivity extends Activity {

    private TextView mHighScoreView, mLevelCountView;

	private Context mContext;

	private int mCount, mLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Utilities.hideUI(this);

        setContentView(R.layout.activity_game);

        mContext = this;

        mHighScoreView = findViewById(R.id.highScoreText);
        mLevelCountView = findViewById(R.id.levelNumberText);

        ImageView exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RelativeLayout surfaceHolder = findViewById(R.id.surfaceLayout);

        MainView mMainView = new MainView(this);
		mMainView.gameStatus = GameStatus.playing;
		surfaceHolder.addView(mMainView);

		updateScore();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.alba);
        mHighScoreView.setTypeface(typeface);
        mLevelCountView.setTypeface(typeface);

        final Handler handler = new Handler();
        final int delay = 2000;

        handler.postDelayed(new Runnable() {
            public void run() {
                updateScore();
                handler.postDelayed(this, delay);
            }
        }, delay);

	}

	@Override
	protected void onResume() {
        getLevel();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

    private void getLevel() {
        SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
        mCount = pref.getInt("gamecount", 1);
    }

    private void getHighScore() {
        SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
        mLevel = pref.getInt("score", 1);
    }

    public void updateScore() {
        getHighScore();
        getLevel();
        mHighScoreView.setText("High: " + String.valueOf(mLevel));
        mLevelCountView.setText("Level: " + String.valueOf(mCount));
    }

}
