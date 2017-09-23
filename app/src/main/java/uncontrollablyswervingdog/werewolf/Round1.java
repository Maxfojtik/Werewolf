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
import android.widget.ToggleButton;

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

    // Put this back in later, to stop the back button
//    @Override
//    public void onBackPressed() {}

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
    void nextPlayer() {
        int a=2;
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
                generateSeer();
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
        explanationTextView.setText("You see the other werewolves. If you're alone, see an unused role.");
        if(countRoles("Werewolf")>1)//not lone wolf
        {
            showWerewolves(playerNum);
        }
        else//lone wolf
        {
            showUnusedRoleButtons(false);
        }
    }
    void generateSeer() {
        explanationTextView.setText("You see another player's role or two unused roles.");
        showSeerOptions();
//        showUnusedRoleButtons();
    }
    void generateRobber(int playerNum) {
        explanationTextView.setText("Take and view someone else's role and give them your own.");
        showPlayerButtons(playerNum);
    }
    void generateTroublemaker(int playerNum) {
        explanationTextView.setText("Change cards between two other players.");
        showPlayerButtons(playerNum, true);
    }
    void generateMinion() {
        explanationTextView.setText("You see who the werewolves are.");
        showWerewolves(100);
    }
    void generateVillager() {
        explanationTextView.setText("You do nothing at night.");
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 3 * width / 7;

        Button tempAddButton1 = new Button(this);
        tempAddButton1.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, 350));
        tempAddButton1.setText("Player Roles");
        tempAddButton1.setId(0);
        ((RelativeLayout) view).addView(tempAddButton1);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tempAddButton1.getLayoutParams();
        params1.addRule(RelativeLayout.BELOW, R.id.explanation);
        params1.leftMargin = width/21;
        params1.topMargin = 15;

        Button tempAddButton2 = new Button(this);
        tempAddButton2.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, 350));
        tempAddButton2.setText("Unused Roles");
        tempAddButton2.setId(1+0);
        ((RelativeLayout) view).addView(tempAddButton2);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) tempAddButton2.getLayoutParams();
        params2.addRule(RelativeLayout.BELOW, R.id.explanation);
        params2.leftMargin = width/28+width/2;
        params2.topMargin = 15;
    }
    void showUnusedRoleButtons(boolean toggleButtons) {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 2*width/7;
        for (int i=0;i<3;i++){
            Button tempAddButton = new Button(this);
            if (toggleButtons) {
                tempAddButton = new ToggleButton(this);
                ((ToggleButton) tempAddButton).setTextOn(MainActivity.players[i].name + "");
                ((ToggleButton) tempAddButton).setTextOff(MainActivity.players[i].name + "");
            }
            tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, 200));
            tempAddButton.setText("Role "+(i+1));
            tempAddButton.setId(i);
            ((RelativeLayout) view).addView(tempAddButton);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.explanation);
            params.leftMargin = width/14+2*i*width/7;
            params.topMargin = 15;
        }
    }
    void showPlayerButtons(int excludedPlayerIndex) {showPlayerButtons(excludedPlayerIndex,false);} // Optional Parameters
    void showPlayerButtons(int excludedPlayerIndex, boolean toggleButtons)
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
                if (toggleButtons) {
                    tempAddButton = new ToggleButton(this);
                    ((ToggleButton) tempAddButton).setTextOn(MainActivity.players[i].name + "");
                    ((ToggleButton) tempAddButton).setTextOff(MainActivity.players[i].name + "");
                }
                tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, 200));
                tempAddButton.setText(MainActivity.players[i].name + "");
                tempAddButton.setId(i);

                ((RelativeLayout) view).addView(tempAddButton);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();

                if (j == 0) { // Special case for the top ones because they are tied to the explanation
                    params.addRule(RelativeLayout.BELOW, R.id.explanation);
                    params.leftMargin = width/2-buttonWidth-15;
                    params.topMargin = 15;
                }
                else if (j == 1) {
                    params.addRule(RelativeLayout.BELOW, R.id.explanation);
                    params.leftMargin = 15+width/2;
                    params.topMargin = 15;
                }
                else if (j % 2 == 0) {
                    params.addRule(RelativeLayout.BELOW, i - 2);
                    params.leftMargin = width/2-buttonWidth-15;
                    params.topMargin = 15;
                }
                else {
                    params.addRule(RelativeLayout.BELOW, i - 2);
                    params.leftMargin = 15+width/2;
                    params.topMargin = 15;
                }
                j++;
            }

        }
    }
}
