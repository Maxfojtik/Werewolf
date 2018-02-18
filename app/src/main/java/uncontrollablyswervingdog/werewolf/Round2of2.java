package uncontrollablyswervingdog.werewolf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressLint({"ResourceType","SetTextI18n"})
public class Round2of2 extends AppCompatActivity {

    TextView playerNameTextView;
    TextView roleTextView;
    TextView explanationTextView;
    int currentPlayer = 0;

    int countRoles(String role) {
        int total = 0;
        for (int i = 0; i < MainActivity.players.length; i++) {
            if (Round1of2.removeDoppelChar(MainActivity.players[i].preDoppel).equals(role)) {
                total++;
            }
        }
        return total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_1);

        playerNameTextView = findViewById(R.id.playerName);
        roleTextView = findViewById(R.id.role);
        explanationTextView = findViewById(R.id.explanation);

        generateView();
        setContentView(R.layout.round_1_reveal);
        playerNameTextView.setText(MainActivity.players[currentPlayer].name);

        if (MainActivity.smallScreen) {
            scaleForSmallScreen();
        }

    }

    void scaleForSmallScreen() {
        playerNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
        roleTextView.setTextSize(25);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerNameTextView.getLayoutParams();
        params.topMargin = 10;
    }

    // Stops the back button
    @Override
    public void onBackPressed() {}

    class doneClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (currentPlayer+1!=MainActivity.players.length) {
                remove();
                currentPlayer++;
                generateView();
                setContentView(R.layout.round_1_reveal);
                TextView playerName = findViewById(R.id.playerName);
                playerName.setText(MainActivity.players[currentPlayer].name);
            }
            else {
                Intent intent = new Intent(Round2of2.this, DiscussionTimer.class);
                startActivity(intent);
            }
        }
    }
    class seerClick implements View.OnClickListener {
        String selection;
        seerClick(String selection)
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
                showUnusedRoleButtons();
            }
        }
    }
    /**
     * Listener for when you click on an unused role button for any reason
     */
    class unusedRoleClick implements View.OnClickListener {
        String role;
        unusedRoleClick(String role)
        {
            this.role = role;
        }
        @Override
        public void onClick(View view) {
            switch (role) {
                case "Seer":
                    // First checks whether, if the buttons are togglebuttons, one or two have been clicked
                    int numChecked = 0;
                    int notChecked = 0;
                    for (int i = 0; i < 3; i++) {
                        if (((ToggleButton) findViewById(i)).isChecked()) {
                            numChecked++;
                        } else {
                            notChecked = i; // Instead of trying to remember the checked things, just remember the not checked one
                        }
                    }
                    if (numChecked > 1) {
                        StringBuilder rolesSeen = new StringBuilder();
                        for (int i = 0; i < 3; i++) {
                            if (((ToggleButton) findViewById(i)).isChecked() && i != notChecked) {
                                rolesSeen.append("\n").append(Round1of2.removeDoppelChar(Round1of2.postDoppelUnusedRoles[i]));
                            }
                        }
                        removeUnusedRoleButtons();
                        showInfo("You saw: " + rolesSeen);
                        generateDoneButton();
                    }
                    break;

                case "Werewolf":
                    String rolesSeen = "";
                    rolesSeen += "\n" + Round1of2.removeDoppelChar(Round1of2.postDoppelUnusedRoles[view.getId()]);
                    removeUnusedRoleButtons();
                    showInfo("You saw: " + rolesSeen);
                    generateDoneButton();
                    break;
            }
        }
    }

    /**
     * Player selection for round 2, the only reason for this is seer/doppelSeer, so seer is assumed
     */
    class playerSelection implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (Round1of2.removeDoppelChar(MainActivity.players[currentPlayer].originalRole).equals("Seer")) {
                int adjust = view.getId() >= currentPlayer ?  1 : 0;

                showInfo(MainActivity.players[view.getId()+adjust].name+"'s role is: \n"+ Round1of2.useDoppelChar(MainActivity.players[view.getId()+adjust].postDoppel));

                removePlayerButtons();
                generateDoneButton();
            }
        }
    }

    void generateDoneButton()
    {
        View view = findViewById(R.id.round_1);
        Button doneB = new Button(this);
        doneB.setLayoutParams(new RelativeLayout.LayoutParams(2330, ViewGroup.LayoutParams.WRAP_CONTENT));
        doneB.setText("Done");
        doneB.setId(1); //doneButtonID = 1
        doneB.setOnClickListener(new doneClick());

        ((RelativeLayout) view).addView(doneB);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) doneB.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(15,0,15,15);
    }
    void remove() {
        removeInfo();
        removePlayerButtons();
    }

    void generateView()
    {
        String role = Round1of2.removeDoppelChar(MainActivity.players[currentPlayer].preDoppel);
        playerNameTextView.setText(MainActivity.players[currentPlayer].name);
        roleTextView.setText(RoleReveal.replaceDoppelChar(MainActivity.players[currentPlayer].preDoppel));
        switch (role) {
            case "Werewolf":
                generateWerewolf();
                break;
            case "Minion":
                generateMinion();
                break;
            case "Mason":
                generateMason();
                break;
            case "Seer":
                generateSeer();
                break;
            case "Robber":
                generateRound2Robber();
                break;
            case "Insomniac":
                generateInsomniac();
                break;
            case "Villager":
                generateVillager();
                break;
            case "Hunter":
                generateHunter();
                break;
            case "Tanner":
                generateTanner();
                break;
            default:
                generateAlreadyDone();
                break;
        }
    }
    void showWerewolves() {
        StringBuilder otherWerewolves = new StringBuilder();
        for (Player player : MainActivity.players){
            if (Round1of2.removeDoppelChar(player.preDoppel).equals("Werewolf")&&player!=MainActivity.players[currentPlayer]) {
                otherWerewolves.append("\n").append(player.name);
            }
        }
        if (Round1of2.removeDoppelChar(MainActivity.players[currentPlayer].postDoppel).equals("Minion")) { // for the minion
            if (countRoles("Werewolf") >= 2) {
                showInfo("The werewolves are:" + otherWerewolves);
            } else {
                showInfo("The werewolf is:" + otherWerewolves);
            }
        }
        else {
            if (countRoles("Werewolf") >= 3) {
                showInfo("The other werewolves are:" + otherWerewolves);
            } else {
                showInfo("The other werewolf is:" + otherWerewolves);
            }
        }
        generateDoneButton();
    }
    void generateWerewolf()
    {
        explanationTextView.setText("You see the other werewolves. If you're alone, see an unused role.");
        if(countRoles("Werewolf")>1) //not lone wolf
        {
            showWerewolves();
        }
        else //lone wolf
        {
            showUnusedRoleButtons();
        }
    }
    void generateSeer() {
        explanationTextView.setText("You see another player's role or two unused roles.");
        showSeerOptions();
    }
    void generateRound2Robber() {
        explanationTextView.setText("You take and view someone else's role and give them your own.");
        if (Round1of2.removeDoppelChar(MainActivity.players[currentPlayer].originalRole).equals("Doppelganger")) {
            showInfo("You stole "+ Round1of2.useDoppelChar(MainActivity.players[currentPlayer].postDoppel));
        }
        else {
            showInfo("You stole " + Round1of2.useDoppelChar(MainActivity.players[currentPlayer].postRobber));
        }
        generateDoneButton();
    }
    void generateMinion() {
        explanationTextView.setText("You see who the werewolves are. If there are no werewolves, you become one.");
        if(countRoles("Werewolf")>0) {
            showWerewolves();
        }
        else
        {
            showInfo("There are no werewolves. You are now one, unless one shows up later.");
        }
        generateDoneButton();
    }
    void generateVillager() {
        explanationTextView.setText("You do nothing at night.");
        generateDoneButton();
    }
    void generateHunter() {
        explanationTextView.setText("The person you vote for also dies if you are killed.");
        generateDoneButton();
    }
    void generateTanner() {
        explanationTextView.setText("You hate your job and your life. You only win if you die.");
        generateDoneButton();
    }
    void generateInsomniac() {
        explanationTextView.setText("Your role at the end of the night is "+ MainActivity.players[currentPlayer].finalRole);
        generateDoneButton();
    }
    void generateAlreadyDone() {
        explanationTextView.setText("You already did your role");
        generateDoneButton();
    }
    void generateMason() {
        explanationTextView.setText("You see the other mason, if there is one.");
        StringBuilder otherMasons = new StringBuilder();
        for(Player player : MainActivity.players)
        {
            if(Round1of2.removeDoppelChar(player.preDoppel).equals("Mason") && player!=MainActivity.players[currentPlayer])
            {
                otherMasons.append(player.name).append("\n");
            }
        }
        if(!otherMasons.toString().equals(""))
        {
            if(countRoles("Mason") > 3) {
                showInfo("The other masons are " + otherMasons);
            }
            else {
                showInfo("The other mason is " + otherMasons);
            }
        }
        else
        {
            showInfo("You are the only mason :(");
        }
        generateDoneButton();
    }
    void showInfo(String info) {
        View view = findViewById(R.id.round_1);
        TextView tempAddTextView = new TextView(this);
        tempAddTextView.setText(info);
        tempAddTextView.setId(100);
        tempAddTextView.setTextSize(35);
        tempAddTextView.setGravity(Gravity.CENTER);
        ((RelativeLayout) view).addView(tempAddTextView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tempAddTextView.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.explanation);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }
    void removeInfo() {
        View view = findViewById(R.id.round_1);
        ((RelativeLayout) view).removeView(findViewById(100));
    }

    void showSeerOptions() {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 3 * width / 7;
        int height = displayMetrics.heightPixels;
        int buttonHeight = height/6;

        Button tempAddButton1 = new Button(this);
        tempAddButton1.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, buttonHeight));
        tempAddButton1.setText("Player Roles");
        tempAddButton1.setId(0);
        tempAddButton1.setOnClickListener(new seerClick("player"));
        ((RelativeLayout) view).addView(tempAddButton1);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tempAddButton1.getLayoutParams();
        params1.addRule(RelativeLayout.BELOW, R.id.explanation);
        params1.leftMargin = width/21;
        params1.topMargin = 15;

        Button tempAddButton2 = new Button(this);
        tempAddButton2.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, buttonHeight));
        tempAddButton2.setText("Unused Roles");
        tempAddButton2.setId(1);
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
        ((RelativeLayout) view).removeView(findViewById(1));
    }
    void showUnusedRoleButtons() {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 2*width/7;
        int height = displayMetrics.heightPixels;
        int buttonHeight = height/6;

        for (int i=0;i<3;i++){
            Button tempAddButton = new Button(this);
            if (Round1of2.removeDoppelChar(MainActivity.players[currentPlayer].originalRole).equals("Seer")) {
                tempAddButton = new ToggleButton(this);
                ((ToggleButton) tempAddButton).setTextOn("Role "+(i+1));
                ((ToggleButton) tempAddButton).setTextOff("Role "+(i+1));
            }
            tempAddButton.setOnClickListener(new unusedRoleClick(Round1of2.removeDoppelChar(MainActivity.players[currentPlayer].postDoppel)));
            tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, buttonHeight));
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
        ((RelativeLayout) view).removeView(findViewById(1));
        ((RelativeLayout) view).removeView(findViewById(2));
    }
    void showPlayerButtons(int excludedPlayerIndex)
    {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 2*width/5;
        int height = displayMetrics.heightPixels;
        int buttonHeight = height/10;
        int j=0; //j is used instead of i to fix the times when we skip the player whose turn it is
        for(int i = 0; i < MainActivity.players.length; i++)
        {
            if (i!=excludedPlayerIndex) {
                Button tempAddButton = new Button(this);
                tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, buttonHeight));
                tempAddButton.setText(MainActivity.players[i].name);
                tempAddButton.setId(j);
                tempAddButton.setOnClickListener(new playerSelection());
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

    public void nextPlayer(View v) {
        setContentView(R.layout.round_1);
        playerNameTextView = findViewById(R.id.playerName);
        roleTextView = findViewById(R.id.role);
        explanationTextView = findViewById(R.id.explanation);
        generateView();
        if (MainActivity.smallScreen) {
            scaleForSmallScreen();
        }
    }

}
