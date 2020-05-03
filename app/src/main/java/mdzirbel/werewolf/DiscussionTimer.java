package mdzirbel.werewolf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class DiscussionTimer extends AppCompatActivity {

    int timeRemaining = Load.getTime();
    TextView timeText;
    Button voteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        timeText = findViewById(R.id.timer);
        voteButton = findViewById(R.id.vote);

        // Format for roles list
        TextView roles = new TextView(this);
        roles.setTextSize(18);
        roles.setGravity(Gravity.START);
        roles.setLineSpacing(40f, 1f);
        roles.setTextColor(Color.rgb(150,150,150));

        // Put the ROLES that are in the game on the side
        StringBuilder roleNames = new StringBuilder("Roles:\n");
        for (String role : Reference.usedRoles) {
            roleNames.append(role).append("\n");
        }
        roles.setText(roleNames.toString());

        // Positioning for roles list
        ((RelativeLayout) findViewById(R.id.discussionTimer)).addView(roles);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.topMargin = 60;
        params.rightMargin = 60;

        // If there is a small screen, adjust text size, spacing, and margins
        if (Reference.smallScreen) {
            roles.setTextSize(25);
            roles.setLineSpacing(10f, 1f);
            params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
            params.topMargin = 12;
            params.rightMargin = 12;
        }

        // Start updating the timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(discussionTimerUpdate, 0, 1000);
    }

    // Updates for countdown during discussion phase
    TimerTask discussionTimerUpdate = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int minutes =  timeRemaining / 60;
                    int seconds = timeRemaining % 60;

                    // The inline if adds a 0 if seconds is a single digit number
                    String timerText = minutes + ":" + (seconds<10 ? "0"+seconds : seconds);
                    timeText.setText(timerText);

                    if (timeRemaining == 0) {
                        startVoteCountdown(); // Ends the discussion timer, starts the vote countdown
                    }

                    timeRemaining--;
                }
            });

        }
    };

    public void voteNowButton(View v) { // When you click vote now button
        startVoteCountdown();
    }
    public void startVoteCountdown() {
        discussionTimerUpdate.cancel(); // Cancel the old timer
        ((RelativeLayout) findViewById(R.id.discussionTimer)).removeView(voteButton); // Remove vote button
        timeText.setTextSize(100); // Timer is bigger

        timeRemaining = 3; // 3s countdown to vote

        String vote_in_time = getString(R.string.vote_in) + "" + timeRemaining;
        timeText.setText(vote_in_time);

        // Start countdown to vote
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(voteCountdown, 0, 1000); // TODO check that delay works
    }

    TimerTask voteCountdown = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (0 < timeRemaining && timeRemaining <= 3 ) {
                        String vote_in_time = getString(R.string.vote_in) + "" + timeRemaining;
                        timeText.setText(vote_in_time);
                    }

                    if (timeRemaining == 0) {
                        voteNow();
                    }
                    timeRemaining--;

                }
            });
        }
    };

    public void voteNow() {
        voteCountdown.cancel(); // Cancel the vote countdown timer

        timeText.setText(R.string.vote_now);

        // Move on to role reveal after VOTE_NOW_TEXT_WAIT_TIME
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DiscussionTimer.this, RoleReveal.class);
                startActivity(intent);
            }
        }, Defines.VOTE_NOW_TEXT_WAIT_TIME);

    }

    // Cancel back button
    @Override
    public void onBackPressed() {}

}
