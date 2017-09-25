package uncontrollablyswervingdog.werewolf;

import android.content.Intent;
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

import java.util.LinkedList;

public class Round1 extends AppCompatActivity {

    TextView playerNameTextView;
    TextView roleTextView;
    TextView explanationTextView;
    int currentPlayer = 0;
    LinkedList<Integer[]> robberQue = new LinkedList<>();
    LinkedList<Integer[]> troublemakerQue = new LinkedList<>();

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
        
        generateView(MainActivity.players[currentPlayer].role, currentPlayer);
        setContentView(R.layout.round_1_reveal);
        TextView playerName = (TextView) findViewById(R.id.playerName);
        playerName.setText(MainActivity.players[currentPlayer].name);
    }

    // Put this back in later, to stop the back button
//    @Override
//    public void onBackPressed() {}

    class doneClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (currentPlayer+1!=MainActivity.players.length) {
                remove();
                currentPlayer++;
                generateView(MainActivity.players[currentPlayer].role, currentPlayer);
                setContentView(R.layout.round_1_reveal);
                TextView playerName = (TextView) findViewById(R.id.playerName);
                playerName.setText(MainActivity.players[currentPlayer].name);
            }
            else {
                doSwitch();
                Intent intent = new Intent(Round1.this, DiscussionTimer.class);
                startActivity(intent);
            }
        }
    }
    class seerClick implements View.OnClickListener {
        String selection;
        public seerClick(String selection)
        {
            this.selection = selection;
        }
        @Override
        public void onClick(View view) {
            removeSeerOptions();
            if (selection.equals("player")) {
                showPlayerButtons(currentPlayer);
            }
            else {
                showUnusedRoleButtons(true);
            }
        }
    }
    class unusedRoleClick implements View.OnClickListener {
        boolean toggleButtons;
        public unusedRoleClick(boolean toggleButtons)
        {
            this.toggleButtons = toggleButtons;
        }
        @Override
        public void onClick(View view) {
            int numChecked = 0;
            int notChecked=0;
            if (toggleButtons) {
                for (int i=0; i<3;i++) {
                    if (((ToggleButton) findViewById(i)).isChecked()) {
                        numChecked ++;
                    }
                    else {
                        notChecked = i; // Instead of trying to remember the checked things, just remember the not checked one
                    }
                }
            }
            if (numChecked > 1) {
                String rolesSeen = "";
                for (int i=0; i<3;i++) {
                    if (((ToggleButton) findViewById(i)).isChecked()&&i!=notChecked) {
                        rolesSeen += "\n"+CharacterSelect.unusedRoles[i];
                    }
                }
                removeUnusedRoleButtons();
                showInfo("You saw: "+rolesSeen);
                generateDoneButton();
            }
            else if (!toggleButtons) {
                String rolesSeen = "";
                rolesSeen += "\n"+CharacterSelect.unusedRoles[view.getId()];
                removeUnusedRoleButtons();
                showInfo("You saw: "+rolesSeen);
                generateDoneButton();
            }
        }
    }
    class playerSelection implements View.OnClickListener {
        boolean toggleButtons;
        public playerSelection(boolean toggleButtons) {
            this.toggleButtons = toggleButtons;
        }
        @Override
        public void onClick(View view) {
            if (MainActivity.players[currentPlayer].role.equals("Seer")) {
                if (view.getId()>=currentPlayer) {
                    showInfo(MainActivity.players[view.getId()+1].name+"'s role is: \n"+MainActivity.players[view.getId()].role);
                }
                else {
                    showInfo(MainActivity.players[view.getId()].name+"'s role is: \n"+MainActivity.players[view.getId()].role);
                }
                removePlayerButtons();
                generateDoneButton();
            }
            else if (MainActivity.players[currentPlayer].role.equals("Robber")) {
                if (view.getId()>=currentPlayer) {
                    showInfo("Your new role is: \n"+MainActivity.players[view.getId()+1].role);
                }
                else {
                    showInfo("Your new role is: \n"+MainActivity.players[view.getId()].role);
                }
                if (view.getId()>=currentPlayer) {
                    queSwitch("Robber", currentPlayer, view.getId()+1);
                }
                else {
                    queSwitch("Robber", currentPlayer, view.getId());
                }
                removePlayerButtons();
                generateDoneButton();
            }
            else if (MainActivity.players[currentPlayer].role.equals("Troublemaker")) {
                Integer[] tempIntegerArray = new Integer[2];
                int numSelected = 0;
                for (int i=0;i<MainActivity.players.length-1;i++) {
                    if (((ToggleButton) findViewById(i)).isChecked()){//i!=currentPlayer&&((ToggleButton) findViewById(i)).isChecked()) {
                        if (i>=currentPlayer) {
                            tempIntegerArray[numSelected]=i+1;
                        }
                        else {
                            tempIntegerArray[numSelected]=i;
                        }
                        numSelected++;
                    }
                }
                if (numSelected==2){
                    queSwitch("Troublemaker",tempIntegerArray[0],tempIntegerArray[1]);
                    removePlayerButtons();
                    generateDoneButton();
                }
            }
        }
    }
    void nextPlayer(View v) {
        setContentView(R.layout.round_1);
        playerNameTextView = (TextView) findViewById(R.id.playerName);
        roleTextView = (TextView) findViewById(R.id.role);
        explanationTextView = (TextView) findViewById(R.id.explanation);
        generateView(MainActivity.players[currentPlayer].role, currentPlayer);
    }

    void queSwitch(String role, int switcher, int switchWith) {
        Integer[] tempIntegerArray = new Integer[]{switcher, switchWith};
        if (role.equals("Robber")){
            robberQue.add(tempIntegerArray);
        }
        else if (role.equals("Troublemaker")){
            troublemakerQue.add(tempIntegerArray);
        }
    }
    void doSwitch() {
        for (Integer[] robberAction : robberQue) {
            String tempRole = MainActivity.players[robberAction[0]].role;
            MainActivity.players[robberAction[0]].role = MainActivity.players[robberAction[1]].role;
            MainActivity.players[robberAction[1]].role = tempRole;
        }
        for (Integer[] troublemakerAction : troublemakerQue) {
            String tempRole = MainActivity.players[troublemakerAction[0]].role;
            MainActivity.players[troublemakerAction[0]].role = MainActivity.players[troublemakerAction[1]].role;
            MainActivity.players[troublemakerAction[1]].role = tempRole;
        }
    }

    void generateDoneButton()
    {
        View view = findViewById(R.id.round_1);
        Button doneB = new Button(this);
        doneB.setLayoutParams(new RelativeLayout.LayoutParams(2330, ViewGroup.LayoutParams.WRAP_CONTENT));
        doneB.setText("Done");
        doneB.setId(1+0); //doneButtonID = 1
        doneB.setOnClickListener(new doneClick());

        ((RelativeLayout) view).addView(doneB);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) doneB.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0,0,0,20);
    }
    void remove() {
        removeInfo();
        removePlayerButtons();
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
        if (playerNum==100) { // for the minion
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
    void generateWerewolf(int playerNum) // done
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
        generateDoneButton();
    }
    void generateVillager() {
        explanationTextView.setText("You do nothing at night.");
        generateDoneButton();
    }
    void generalRemove(int to) {
        View view = findViewById(R.id.round_1);
        for (int i=0;i<to;i++) {
            ((RelativeLayout) view).removeView(findViewById(i));
        }
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
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }
    void removeInfo() {
        View view = findViewById(R.id.round_1);
        ((RelativeLayout) view).removeView(findViewById(100+0));
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
        tempAddButton1.setOnClickListener(new seerClick("player"));
        ((RelativeLayout) view).addView(tempAddButton1);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tempAddButton1.getLayoutParams();
        params1.addRule(RelativeLayout.BELOW, R.id.explanation);
        params1.leftMargin = width/21;
        params1.topMargin = 15;

        Button tempAddButton2 = new Button(this);
        tempAddButton2.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, 350));
        tempAddButton2.setText("Unused Roles");
        tempAddButton2.setId(1+0);
        tempAddButton2.setOnClickListener(new seerClick("unused"));
        ((RelativeLayout) view).addView(tempAddButton2);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) tempAddButton2.getLayoutParams();
        params2.addRule(RelativeLayout.BELOW, R.id.explanation);
        params2.leftMargin = width/28+width/2;
        params2.topMargin = 15;
    }
    void removeSeerOptions() {
        View view = findViewById(R.id.round_1);
        ((RelativeLayout) view).removeView(findViewById(0));
        ((RelativeLayout) view).removeView(findViewById(1+0));
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
                ((ToggleButton) tempAddButton).setTextOn("Role "+(i+1));
                ((ToggleButton) tempAddButton).setTextOff("Role "+(i+1));
            }
            tempAddButton.setOnClickListener(new unusedRoleClick(toggleButtons));
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
    void removeUnusedRoleButtons() {
        View view = findViewById(R.id.round_1);
        ((RelativeLayout) view).removeView(findViewById(0));
        ((RelativeLayout) view).removeView(findViewById(1+0));
        ((RelativeLayout) view).removeView(findViewById(2+0));
    }
    void showPlayerButtons(int excludedPlayerIndex) {showPlayerButtons(excludedPlayerIndex,false);} // Optional Parameter
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
                tempAddButton.setId(j);
                tempAddButton.setOnClickListener(new playerSelection(toggleButtons));
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
                    params.addRule(RelativeLayout.BELOW, j - 1);
                    params.leftMargin = width/2-buttonWidth-15;
                    params.topMargin = 15;
                }
                else {
                    params.addRule(RelativeLayout.BELOW, j - 2);
                    params.leftMargin = 15+width/2;
                    params.topMargin = 15;
                }
                j++;
            }

        }
    }
    void removePlayerButtons() {
        View view = findViewById(R.id.round_1);
        for(int i = 0; i < MainActivity.players.length; i++) {
            ((RelativeLayout) view).removeView(findViewById(i));
        }
    }
}
