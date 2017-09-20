package uncontrollablyswervingdog.werewolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;


public class Timer extends AppCompatActivity {

    int time = 5*60;
    TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        timeText = (TextView) findViewById(R.id.timer);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(changeTimer, 0, 1, SECONDS);
    }

    Runnable changeTimer = new Runnable() {
        public void run() {
            time--;
            timeText.setText(time+"");
        }
    };
}
