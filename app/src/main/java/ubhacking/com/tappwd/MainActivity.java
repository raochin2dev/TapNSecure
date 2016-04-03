package ubhacking.com.tappwd;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private int touchCount = 0, i, cnt, sum, currentState = 0;
    private long[] currentTime;
    private List<Long> touchCountList;
    private List<Long> touchTimeList = new ArrayList<>();
    private long interVal;
    private Integer[] numbers = new Integer[4];
    private TextToSpeech engine;
    private float x1, x2, y1, y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        engine = new TextToSpeech(this, this);
        engine.setLanguage(Locale.US);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 0 = App Started , 1 = Creating Pin
        if (currentState == 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    y1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    y2 = event.getY();
                    float deltaY = y2 - y1;

                    if (Math.abs(deltaY) > 350) {
                        // Top to Bottom
                        if (y2 > y1) {
                            engine.speak(" Top to bottom ", TextToSpeech.QUEUE_FLUSH, null);
                            while (engine.isSpeaking()) ;
                        }

                        // Bottom to Top
                        else {
                            engine.speak(" Start Tapping ", TextToSpeech.QUEUE_FLUSH, null);
                            while (engine.isSpeaking()) ;
                            cnt = 0;
                            i = 0;
                            touchCount = 0;
                            touchTimeList.clear();
                            currentState = 1;
                        }

                    }
                    break;
            }
        } else {
            if (cnt < 4) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    System.out.println("Cnt:" + cnt);
                    touchTimeList.add(System.currentTimeMillis());
                    if (i > 0) {
                        interVal = touchTimeList.get(i) - touchTimeList.get(i - 1);
                    } else {
                        interVal = 0;
                    }

                    System.out.println("Inteval:" + interVal);
                    if (interVal < 1500) {
                        touchCount++;
                        numbers[cnt] = touchCount;

                    } else {
                        cnt++;
                        if (cnt > 3) {
                            getNumbers();
                        } else {
                            numbers[cnt] = 1;
                            touchCount = 1;
                        }

                    }
                    i++;
                }

            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > 350) {
                            // Left to Right swipe action
                            if (x2 > x1) {
//                            System.out.println("L TO R");
                                engine.speak(" Pattern Saved ", TextToSpeech.QUEUE_FLUSH, null);
                                while (engine.isSpeaking()) ;
                                cnt = 0;
                                i = 0;
                                touchTimeList.clear();
                                touchCount = 0;
                                currentState = 0;

                            }

                            // Right to left swipe action
                            else {
//                            System.out.println("R TO L");
                                engine.speak(" Please enter new pin ", TextToSpeech.QUEUE_FLUSH, null);
                                while (engine.isSpeaking()) ;
                                cnt = 0;
                                i = 0;
                                touchCount = 0;
                                touchTimeList.clear();
                                currentState = 1;

                            }

                        } else {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
            }
        }
//               System.out.println("Numbers:"+numbers[cnt]);

        return super.onTouchEvent(event);
    }

    public void getNumbers() {

//        TextToSpeech ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//            }
//        }
//        );


        int cnt = numbers.length;
        engine.speak(" Your pin is : ", TextToSpeech.QUEUE_FLUSH, null);
        while (engine.isSpeaking()) ;

        for (int i = 0; i < cnt; i++) {
            System.out.println("Numbers :" + numbers[i]);
//            TextToSpeech tts = new TextToSpeech(this, this);

            engine.speak("" + numbers[i], TextToSpeech.QUEUE_FLUSH, null);
            while (engine.isSpeaking()) ;
        }

        engine.speak(" Swipe Right to confirm ", TextToSpeech.QUEUE_FLUSH, null);
        while (engine.isSpeaking()) ;

        engine.playSilence(300, TextToSpeech.QUEUE_FLUSH, null);
        while (engine.isSpeaking()) ;

        engine.speak(" Swipe Left to reset ", TextToSpeech.QUEUE_FLUSH, null);
        while (engine.isSpeaking()) ;

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            engine.speak("Hello UB Hacking", TextToSpeech.QUEUE_FLUSH, null);
            while (engine.isSpeaking()) ;

            engine.playSilence(300, TextToSpeech.QUEUE_FLUSH, null);
            while (engine.isSpeaking()) ;

            engine.speak("To create a pin Swipe Up", TextToSpeech.QUEUE_FLUSH, null);
            while (engine.isSpeaking()) ;

        }
    }
}
