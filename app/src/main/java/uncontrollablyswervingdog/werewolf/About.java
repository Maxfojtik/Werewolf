package uncontrollablyswervingdog.werewolf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {

    TextView devedBy;
    TextView matt;
    TextView and;
    TextView max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        devedBy = (TextView) findViewById(R.id.devedBy);
        matt = (TextView) findViewById(R.id.matt);
        and = (TextView) findViewById(R.id.and);
        max = (TextView) findViewById(R.id.max);

        if (MainActivity.smallScreen) {
            scaleForSmallScreen();
        }
    }
    void scaleForSmallScreen() {
        devedBy.setTextSize(30);
        matt.setTextSize(30);
        and.setTextSize(30);
        max.setTextSize(30);
    }
}
