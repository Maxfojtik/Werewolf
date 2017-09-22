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

//    CharacterSelect.unusedRoles[0];

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

        generateView(MainActivity.players[0].role, 0);

    }

    void generateDoneButton()
    {
        View view = findViewById(R.id.round_1);
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
    void generateView(String role, int playerNum)
    {
        playerNameTextView.setText(MainActivity.players[playerNum].name);
        roleTextView.setText(MainActivity.players[playerNum].role);
        switch (role) {
            case "Werewolf":
                generateWerewolf(playerNum);
                break;
            case "Seer":
                generateSeer(playerNum);
                break;
            case "Robber":
                generateRobber(playerNum);
                break;
            case "Troublemaker":
                generateTroublemaker(playerNum);
                break;
            case "Minion":
                generateMinion();
                break;
            case "Villager":
                generateVillager();
                break;
        }
    }
    void showWerewolves(int playerNum) {
        String otherWerewolves = "";
        for (int i=0;i<MainActivity.players.length; i++){
            if (MainActivity.players[i].role.equals("Werewolf")&&i!=playerNum) {
                otherWerewolves += "\n" + MainActivity.players[i].name;

            }
        }
        if (playerNum==100) {
            showInfo("The werewolves are:"+otherWerewolves);
        }
        else {
            if (countRoles("Werewolf") > 2) {
                showInfo("The other werewolves are:" + otherWerewolves);
            } else {
                showInfo("The other werewolf is:" + otherWerewolves);
            }
            generateDoneButton();
        }
    }
    void generateWerewolf(int playerNum)
    {
        explanationTextView.setText("You see the other werewolves. If you're alone, see an unused role.\nAlignment: Werewolves");
        if(countRoles("Werewolf")>1)//not lone wolf
        {
            showWerewolves(playerNum);
        }
        else//lone wolf
        {
            showUnusedRoleButtons();
        }
    }
    void generateSeer(int playerNum) {
        explanationTextView.setText("You see another player's role or two unused roles.\nAlignment: Village");
        showPlayerButtons(playerNum);
    }
    void generateRobber(int playerNum) {
        explanationTextView.setText("Take and view someone else's role and give them your own.\nAlignment: New Role");
        showPlayerButtons(playerNum);
    }
    void generateTroublemaker(int playerNum) {
        explanationTextView.setText("Change cards between two other players.\nAlignment: Village");
        showPlayerButtons(playerNum);
    }
    void generateMinion() {
        explanationTextView.setText("You see who the werewolves are.\nAlignment: Werewolves");
        showWerewolves(100);
    }
    void generateVillager() {
        explanationTextView.setText("You do nothing at night.\nAlignment: Village");
        generateDoneButton();
    }
    void showInfo(String info) {
        View view = findViewById(R.id.round_1);
        TextView tempAddTextView = new TextView(this);
        tempAddTextView.setText(info);
        tempAddTextView.setId(100+0);
        tempAddTextView.setTextSize(35);
        tempAddTextView.setGravity(Gravity.CENTER);
        ((RelativeLayout) view).addView(tempAddTextView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tempAddTextView.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.explanation);

    }
    void showSeerOptions() {
        View view = findViewById(R.id.round_1);

    }
    void showUnusedRoleButtons() {
        View view = findViewById(R.id.round_1);
        // will need unused roles first, for which the character selection screen will need to be fixed
    }
    void showPlayerButtons(int excludedPlayerIndex)
    {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 2*width/5;
        int j=0; //j is used instead of i to fix the times when we skip the player whose turn it is
        for(int i = 0; i < MainActivity.players.length; i++)
        {
            if (i!=excludedPlayerIndex) {
                Button tempAddButton = new Button(this);
                tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, 200));
                tempAddButton.setText(MainActivity.players[i].name + "");
                tempAddButton.setId(i);
                //tempAddButton.setOnClickListener(new onClick(numberLabel.getId(), i, 1));

                ((RelativeLayout) view).addView(tempAddButton);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();
                if (j == 0) { // Special case for the top ones because they are tied to the explanation
                    params.addRule(RelativeLayout.BELOW, R.id.explanation);
                    params.addRule(RelativeLayout.ALIGN_RIGHT, RelativeLayout.CENTER_VERTICAL);
                    params.leftMargin = width/2-buttonWidth-15;
                    params.topMargin = 15;
                }
                else if (j == 1) {
                    params.addRule(RelativeLayout.BELOW, R.id.explanation);
                    params.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.CENTER_VERTICAL);
                    params.leftMargin = 15+width/2;
                    params.topMargin = 15;
                }
                else if (j % 2 == 0) {
                    params.addRule(RelativeLayout.BELOW, i - 2);
                    params.addRule(RelativeLayout.ALIGN_RIGHT, RelativeLayout.CENTER_VERTICAL);
                    params.leftMargin = width/2-buttonWidth-15;
                    params.topMargin = 15;
                }
                else {
                    params.addRule(RelativeLayout.BELOW, i - 2);
                    params.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.CENTER_VERTICAL);
                    params.leftMargin = 15+width/2;
                    params.topMargin = 15;
                }
                j++;
            }

        }
    }
}
