package uncontrollablyswervingdog.werewolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Round1 extends AppCompatActivity {

    TextView playerNameTextView;
    TextView roleTextView;
    TextView explanationTextView;

    int countRoles(String role) {
        int total = 0;
        for (int i = 0; i < MainActivity.players.length; i++) {
            if (MainActivity.players[i].role.equals(role)) {
                total++;
            }
        }
        return total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.round_1_reveal);
        setContentView(R.layout.round_1);

        playerNameTextView = (TextView) findViewById(R.id.playerName);
        Log.d("PLAYERNAME",playerNameTextView+" "+R.id.playerName);
        roleTextView = (TextView) findViewById(R.id.role);
        explanationTextView = (TextView) findViewById(R.id.explanation);

        generateView(findViewById(R.id.round_1), "Werewolf", 0);

    }

    void generateDoneButton(View view)
    {
        Button doneB = new Button(this);
        doneB.setLayoutParams(new RelativeLayout.LayoutParams(2330, ViewGroup.LayoutParams.WRAP_CONTENT));
        doneB.setText("Done");
        doneB.setId(1+0); //doneButtonID = 1

        ((RelativeLayout) view).addView(doneB);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) doneB.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0,0,0,20);
    }
    void generateView(View layout, String role, int playerNum)
    {
        playerNameTextView.setText(MainActivity.players[playerNum].name);
        roleTextView.setText(MainActivity.players[playerNum].role);
        if (role.equals("Werewolf"))
        {
            generateWerewolf(layout, playerNum);
        }
        else if (role.equals("Seer")) {
            generateSeer(layout, playerNum);
        }
    }
    void showWerewolves(View view, int playerNum) {
        String otherWerewolves = "";
        for (int i=0;i<MainActivity.players.length; i++){
            if (MainActivity.players[i].role.equals("Werewolf")&&i!=playerNum) {
                otherWerewolves += "\n" + MainActivity.players[i].name;

            }
        }
        if (countRoles("Werewolf")>2) {
            showInfo(view, "The other werewolves are:"+otherWerewolves);
        }
        else {
            showInfo(view, "The other werewolf is:"+otherWerewolves);
        }
        generateDoneButton(view);
    }
    void generateWerewolf(View view, int playerNum)
    {
        explanationTextView.setText("You see the other werewolves. If you're alone, see an unused role.\nAlignment: Werewolves");
        if(countRoles("Werewolf")>1)//not lone wolf
        {
            showWerewolves(view, playerNum);
        }
        else//lone wolf
        {
            showUnusedButtons();
        }
    }
    void generateSeer(View layout, int playerNum) {
        explanationTextView.setText("You see another player's role or two unused roles.\nAlignment: Village");
        showPlayerButtons(playerNum);
    }
    void showInfo(View view, String info) {
        TextView tempAddTextView = new TextView(this);
        tempAddTextView.setText(info);
        tempAddTextView.setId(100+0);
        tempAddTextView.setTextSize(35);
        tempAddTextView.setGravity(Gravity.CENTER);
        ((RelativeLayout) view).addView(tempAddTextView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tempAddTextView.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.explanation);

    }
    void showUnusedButtons() {
        View view = findViewById(R.id.round_1);
        // will need center cards first
    }
    void showPlayerButtons(int excludedPlayerIndex)
    {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int j=0; //j is used instead of i to fix the times when we skip the player whose turn it is
        for(int i = 0; i < MainActivity.players.length; i++)
        {
            if (i!=excludedPlayerIndex) {
                Button tempAddButton = new Button(this);
                tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(width - 20, 500));
                tempAddButton.setText(MainActivity.players[i] + "");
                tempAddButton.setId(j);
                //tempAddButton.setOnClickListener(new onClick(numberLabel.getId(), i, 1));

                ((RelativeLayout) view).addView(tempAddButton);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();
                if (j%2==0) {
//                    params.addRule(RelativeLayout.BELOW, label.getId());
//                    params.addRule(RelativeLayout.LEFT_OF, numberLabel.getId());
                    params.rightMargin = 15;
                }
                else {
//                    params.addRule(RelativeLayout.BELOW, label.getId());
//                    params.addRule(RelativeLayout.RIGHT_OF, numberLabel.getId());
                    params.leftMargin = 15;
                }
            }
            j++;
        }
    }
}
