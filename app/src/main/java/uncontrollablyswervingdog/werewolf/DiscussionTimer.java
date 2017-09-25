package uncontrollablyswervingdog.werewolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class DiscussionTimer extends AppCompatActivity {

    int time = 5*60+1;
    TextView timeText;
    Button voteButton;
    boolean votingTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        timeText = (TextView) findViewById(R.id.timer);
        voteButton = (Button) findViewById(R.id.vote);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(updateTimer, 0, 1000);
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(changeTimer, 0, 1, SECONDS);
    }
    TimerTask updateTimer = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = findViewById(R.id.discussionTimer);
                    time--;
                    String minutes =  time/60+"";
                    String seconds = time%60+"";
                    Log.d("SECONDS",seconds.length()+"");
                    if (seconds.length()==1 && !votingTimer) {
                        seconds="0"+seconds;
                    }

                    if (!votingTimer) {
                        timeText.setText(minutes + ":" + seconds);
                    }
                    else if (seconds.length()==1){ // length becomes 2 when time is negative
                        timeText.setText(seconds);
                    }

                    if (time == 0 && !votingTimer) {
                        voteCountdown(view);
                    }
                    else if (time == 0 && votingTimer) {
                        timeText.setText("Vote");
                    }
                    else if (time == -3) {
                        updateTimer.cancel();
                        Intent intent = new Intent(DiscussionTimer.this, RoleReveal.class);
                        startActivity(intent);
                    }

                }
            });

        }
    };

    void voteCountdown(View v) {
        View view = findViewById(R.id.discussionTimer);
        ((RelativeLayout) view).removeView(voteButton);
        timeText.setTextSize(140);
        votingTimer=true;
        time = 3;
        timeText.setText(time+"");
    }
}
