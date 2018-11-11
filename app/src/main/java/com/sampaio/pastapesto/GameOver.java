package com.sampaio.pastapesto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private TextView mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        mScore = findViewById(R.id.final_score);
        int finalScore = getScoreFromIntent();
        setScore(finalScore);

    }

    @Override
    protected void onResume(){
        super.onResume();
        int finalScore = getScoreFromIntent();
        setScore(finalScore);
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    public void continuePlaying(View view) {
        Intent gameStart = new Intent();
        gameStart.setClass(this,MainActivity.class);
        startActivity(gameStart);
    }

    public void shareScore(View view) {
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setText(getShareScoreText())
                .setChooserTitle("Choose the cutest icon")
                .startChooser();
    }

    private int getScoreFromIntent(){
        return getIntent().getIntExtra("score_key", -1);
    }

    private void setScore(int score){
        if (score >= 0){
            mScore.setText(String.valueOf(score));
        } else {
            mScore.setText("Error occured");
        }

    }

    private String getShareScoreText(){
        return "I JUST PASTA PESTOED " + getScoreFromIntent() + " TIMES\nCAN YOU BEAT THIS?";
    }
}
