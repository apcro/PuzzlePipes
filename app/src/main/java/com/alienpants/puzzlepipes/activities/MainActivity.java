package com.alienpants.puzzlepipes.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alienpants.puzzlepipes.R;
import com.alienpants.puzzlepipes.services.Utilities;

public class MainActivity extends Activity {
	
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Utilities.hideUI(this);
        setContentView(R.layout.activity_main);

		mContext = this;

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		ImageView exitButton = findViewById(R.id.exitButton);
		exitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finishAndRemoveTask();
			}
		});

		ImageView settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, SettingsActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
			}
		});

		ImageView playButton = findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
				SharedPreferences.Editor editor = pref.edit();
				editor.putInt("gamecount", 1);
				editor.apply();
			    Intent intent = new Intent(mContext, GameActivity.class);
			    startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

}
