package com.lizzy.indicator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class FailedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);

        int score = getIntent().getIntExtra("score", 0);

        ((TextView) findViewById(R.id.textView)).setText("You got it wrong 3 times!! \n But not before you got a score of " + score + "!\nYou can  try again, or go back and re-read the instructions.");
    }

    public void tryAgain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View v){
        finish();
    }
}
