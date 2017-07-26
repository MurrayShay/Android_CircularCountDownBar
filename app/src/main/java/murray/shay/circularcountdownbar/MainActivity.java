package murray.shay.circularcountdownbar;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import murray.shay.countdownbar.CircularCountDownBar;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button mStartCountDownBtn;
    private CircularCountDownBar mCountDownBar1;
    private CircularCountDownBar mCountDownBar2;
    private CircularCountDownBar mCountDownBar3;
    private CircularCountDownBar mCountDownBar4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        mCountDownBar1 = new CircularCountDownBar.Builder(this)
                .setMaxProgress(60)
                .setProgressColor(Color.CYAN)
                .setTextColor(Color.WHITE)
                .build();

        mCountDownBar2 = new CircularCountDownBar.Builder(this)
                .setMaxProgress(30)
                .setProgressColor(Color.GREEN)
                .setRoundedCorners(false)
                .build();

        mCountDownBar3 = new CircularCountDownBar.Builder(this)
                .setMaxProgress(10)
                .setProgressColor(Color.BLACK)
                .setDrawText(false)
                .build();

        mCountDownBar4 = new CircularCountDownBar.Builder(this)
                .setMaxProgress(20)
                .setProgressColor(Color.BLUE)
                .setStrokeWidth(30)
                .setTextColor(Color.RED)
                .setPadding(15,15,15,15)
                .setRoundedCorners(false)
                .setDrawText(true)
                .build();

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(350,350);
        params1.gravity = Gravity.CENTER;
        params1.topMargin = 30;
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(200,200);
        params2.gravity = Gravity.CENTER;
        params2.topMargin = 30;
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(120,120);
        params3.gravity = Gravity.CENTER;
        params3.topMargin = 30;
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(300,300);
        params4.gravity = Gravity.CENTER;
        params4.topMargin = 30;

        linearLayout.addView(mCountDownBar1,params1);
        linearLayout.addView(mCountDownBar2,params2);
        linearLayout.addView(mCountDownBar3,params3);
        linearLayout.addView(mCountDownBar4,params4);


    }

    private void findView() {
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        mStartCountDownBtn = (Button) findViewById(R.id.countDownBtn);
        mStartCountDownBtn.setOnClickListener(startOnClick);
    }

    OnClickListener startOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startCountDownTimertask(mCountDownBar1,60);
            startCountDownTimertask(mCountDownBar2,30);
            startCountDownTimertask(mCountDownBar3,10);
            startCountDownTimertask(mCountDownBar4,20);
        }
    };

    private void startCountDownTimertask(final CircularCountDownBar countDownBar, int cd){
        int timeOffset = 10;
        new CountDownTimer((cd *= 1000) + timeOffset, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countDownBar.setProgress((int)(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}
