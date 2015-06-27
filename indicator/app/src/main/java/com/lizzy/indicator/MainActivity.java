package com.lizzy.indicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;


public class MainActivity extends Activity {

    Random rand = new Random();
    int score = 0;
    int wrongCount = 0;
    boolean isLeft = true;
    boolean chosen = false;
    boolean leftChosen = true;
    boolean threadRunning = true;

    AnimationDrawable anim;
    GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main).setBackgroundResource(R.drawable.road_anim);
        anim = (AnimationDrawable) findViewById(R.id.activity_main).getBackground();

        mDetector = new GestureDetector(this, new MyGestureListener());

        (findViewById(R.id.up_ind)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.flat_ind)).setVisibility(View.VISIBLE);
        (findViewById(R.id.down_ind)).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        threadRunning = true;
        anim.start();

        Thread thread = new Thread() {
            public void run(){
                try {
                    while (threadRunning) {
                        chooseDirection();
                        sleep(1000);
                        hideDirection();
                        sleep(2000);
                        checkAnswer();
                        sleep(1000);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void upClick(){
        (findViewById(R.id.indicator_right)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.indicator_left)).setVisibility(View.VISIBLE);
        chosen = true;
        leftChosen = false;

        (findViewById(R.id.up_ind)).setVisibility(View.VISIBLE);
        (findViewById(R.id.flat_ind)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.down_ind)).setVisibility(View.INVISIBLE);
    }
    public void downClick(){
        (findViewById(R.id.indicator_left)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.indicator_right)).setVisibility(View.VISIBLE);
        chosen = true;
        leftChosen = true;
        (findViewById(R.id.up_ind)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.flat_ind)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.down_ind)).setVisibility(View.VISIBLE);
    }

    public void chooseDirection(){

        isLeft = rand.nextBoolean();
        chosen = false;
        runOnUiThread(new Runnable() {
            public void run() {
                if (isLeft) {
                    ((ImageView) findViewById(R.id.direction)).setImageResource(R.drawable.right_turn);
                } else {
                    ((ImageView) findViewById(R.id.direction)).setImageResource(R.drawable.left_turn);
                }
            }
        });
    }

    public void hideDirection(){
        runOnUiThread(new Runnable() {
            public void run() {
                ((ImageView) findViewById(R.id.direction)).setImageDrawable(null);
            }
        });
    }

    public void checkAnswer(){

        Log.w("D", chosen + " " + leftChosen + " " + isLeft);
        runOnUiThread(new Runnable() {
            public void run() {
                if (chosen && leftChosen == isLeft) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    score += 10;
                    ((TextView) findViewById(R.id.score)).setText("Score: \t" + score);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    switch (++wrongCount) {
                        case 3 :
                            ((ImageView) findViewById(R.id.heart1)).setImageResource(R.drawable.cross);
                            endGame();
                            break;
                        case 2 :
                            ((ImageView) findViewById(R.id.heart2)).setImageResource(R.drawable.cross);
                            Toast toast2 = Toast.makeText(getApplicationContext(), "One Life Left", Toast.LENGTH_SHORT);
                            toast2.setGravity(Gravity.CENTER, 0, 0);
                            toast2.show();
                            break;
                        case 1 :
                            ((ImageView) findViewById(R.id.heart3)).setImageResource(R.drawable.cross);
                            break;
                    }
                }
                (findViewById(R.id.indicator_right)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.indicator_left)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.up_ind)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.flat_ind)).setVisibility(View.VISIBLE);
                (findViewById(R.id.down_ind)).setVisibility(View.INVISIBLE);
            }
        });

    }

    public void endGame() {
        threadRunning = false;
        Intent intent = new Intent(this, FailedActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStop() {
        threadRunning = false;
        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if (event1.getY() < event2.getY()) {
                //swipe down
                downClick();
            } else {
                //swipe up
                upClick();
            }
            return true;
        }
    }
}
