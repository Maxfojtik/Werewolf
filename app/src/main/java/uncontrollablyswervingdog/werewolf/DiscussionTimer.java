package uncontrollablyswervingdog.werewolf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({"ResourceType","SetTextI18n"})
public class DiscussionTimer extends AppCompatActivity {

    int time = 5*60+1;
    TextView timeText;
    Button voteButton;
    boolean votingTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        timeText = findViewById(R.id.timer);
        voteButton = findViewById(R.id.vote);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(updateTimer, 0, 1000);

        TextView roles = new TextView(this);
        roles.setId(1);
        roles.setTextSize(18);
        roles.setGravity(Gravity.LEFT);
        roles.setLineSpacing(40f, 1f);
        roles.setTextColor(Color.rgb(150,150,150));
        StringBuilder roleNames = new StringBuilder("Roles:\n");
        for (Object role : CharacterSelect.usedRoles.keySet().toArray()) {
            roleNames.append(Round1of2.removeDoppelChar((String) role)).append("\n");
        }
        roles.setText(roleNames.toString());
        ((RelativeLayout) findViewById(R.id.discussionTimer)).addView(roles);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.topMargin = 60;
        params.rightMargin = 60;

        if (MainActivity.smallScreen) {
            roles.setTextSize(25);
            roles.setLineSpacing(10f, 1f);
            params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
            params.topMargin = 12;
            params.rightMargin = 12;
        }
    }

    @Override
    public void onBackPressed() {}

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

    public void voteCountdown(View v) {
        View view = findViewById(R.id.discussionTimer);
        ((RelativeLayout) view).removeView(voteButton);
        timeText.setTextSize(140);
        votingTimer=true;
        time = 3;
        timeText.setText(time+"");
    }
}
