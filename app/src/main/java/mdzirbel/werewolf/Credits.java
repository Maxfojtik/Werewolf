package mdzirbel.werewolf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Credits extends AppCompatActivity {

    TextView creditsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        creditsTextView = findViewById(R.id.credits_text_view);

        creditsTextView.setText(R.string.developed_by);

        if (Reference.smallScreen) {
            scaleForSmallScreen();
        }
    }
    void scaleForSmallScreen() {
    }
}
