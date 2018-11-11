package com.sampaio.pastapesto;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech mTTS;
    private static final String TTS_TAG = "TTS";
    private TextView mNewWord;
    private TextView mTotalPoints;
    private static final String[] availableWords = new String[]{"Pasta", "Pesto"};
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private Stack<Integer> mWordStack;
    private int currentWord;
    private int pointCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWordStack = new Stack<>();
        mNewWord = findViewById(R.id.next_word);
        mTotalPoints = findViewById(R.id.total_points);
        showNextWord();
        mTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = mTTS.setLanguage(Locale.ITALIAN);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e(TTS_TAG, "The Language is not supported!");
                    } else {
                        Log.i(TTS_TAG, "Language Supported.");
                    }
                    Log.i(TTS_TAG, "Initialization success.");
                } else {
                    Log.e(TTS_TAG, "TTS initialization failed");
                }
            }
        });
    }

    public void pastaClicked(View view) {
        wordClicked(FIRST);
    }

    public void pestoClicked(View view) {
        wordClicked(SECOND);

    }

    private void say(String somethingToSay){
        int speechStatus = mTTS.speak(somethingToSay, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e(TTS_TAG, "Error in converting Text to Speech!");
        }
    }

    private void generateNextWords(){
        int newWord = (int)Math.round(Math.random());
        mWordStack.push(newWord == FIRST ? SECOND : FIRST);
        mWordStack.push(newWord);
    }

    private void showNextWord(){
        if (mWordStack.isEmpty()){
            generateNextWords();
        }
        currentWord = mWordStack.pop();
        mNewWord.setText(availableWords[currentWord]);
    }

    private boolean checkIfCorrect(int clicked){
        return clicked == currentWord;
    }

    private void youLose(){
        say("YOU LOSE BITCH, MAMA MIA");
        mWordStack = new Stack<>();
        Intent gameOver = new Intent();
        gameOver.putExtra("score_key", pointCount);
        gameOver.setClass(this,GameOver.class);
        pointCount = 0;
        startActivity(gameOver);
    }

    private void wordClicked(int index){
        if(checkIfCorrect(index)){
            say(availableWords[index]);
            pointCount++;
        } else {
            youLose();
        }
        updatePoints(pointCount);
        showNextWord();
    }

    private void updatePoints(int newPoints){
        mTotalPoints.setText(String.valueOf(newPoints));
    }


}
