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
    private static final String[] availableWords = new String[]{"Pasta", "Pesto"};
    private static final boolean FIRST = true;
    private static final boolean SECOND = false;
    private Stack<Boolean> mWordStack;
    private boolean currentWord;
    private int totalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWordStack = new Stack<>();
        mNewWord = findViewById(R.id.next_word);
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
        if(checkIfCorrect(FIRST)){
            say(availableWords[0]);
        } else {
            youLose();
        }
        showNextWord();
    }

    public void pestoClicked(View view) {
        if(checkIfCorrect(SECOND)){
            say(availableWords[1]);
        } else {
            youLose();
        }
        showNextWord();

    }

    private void say(String somethingToSay){
        int speechStatus = mTTS.speak(somethingToSay, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e(TTS_TAG, "Error in converting Text to Speech!");
        }
    }

    private void generateNextWords(){
        boolean newWord = Math.round(Math.random()) > 0 ? true : false;
        mWordStack.push(!newWord);
        mWordStack.push(newWord);
    }

    private void showNextWord(){
        if (mWordStack.isEmpty()){
            generateNextWords();
        }
        currentWord = mWordStack.pop();
        mNewWord.setText(availableWords[currentWord ? 0 : 1]);
    }

    private boolean checkIfCorrect(boolean clicked){
        return clicked == currentWord;
    }

    private void youLose(){
        say("YOU LOSE BITCH, MAMA MIA");
        mWordStack = new Stack<>();
    }


}
