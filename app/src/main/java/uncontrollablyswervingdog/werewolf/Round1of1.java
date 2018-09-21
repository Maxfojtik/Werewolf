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

import java.util.ArrayList;

import static uncontrollablyswervingdog.werewolf.MainActivity.players;

//TODO make troublemaker display who you switched

@SuppressLint({"ResourceType","SetTextI18n"})
public class Round1of1 extends AppCompatActivity {

    TextView playerNameTextView;
    TextView roleTextView;
    TextView explanationTextView;
    TextView betweenPlayersName;
    int currentPlayer = 0;
    ArrayList<int[]> robberQue = new ArrayList<>();
    ArrayList<int[]> troublemakerQue = new ArrayList<>();
    ArrayList<int[]> drunkQue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the xml file to round_1
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_1);

        // Initialize the text views in the round_1 xml file for editing later
        playerNameTextView = findViewById(R.id.playerName);
        roleTextView = findViewById(R.id.role);
        explanationTextView = findViewById(R.id.explanation);

        setContentView(R.layout.between_players);
        betweenPlayersName = findViewById(R.id.between_players_name); // The text view for between player turns
        betweenPlayersName.setText(players[currentPlayer].name);

        if (MainActivity.smallScreen) {
            scaleForSmallScreen();
        }
    }

    /**
     * Counts the roles of a type
     * @param role the role type to count
     * @return number of roles of the input type
     */
    int countRoles(String role) {
        int total = 0;
        for (int i = 0; i < MainActivity.players.length; i++) {
            if (MainActivity.players[i].originalRole.equals(role)) {
                total++;
            }
        }
        return total;
    }

    /**
     * Changes the values of a couple sizes so it fits on a smaller screen better
     */
    void scaleForSmallScreen() {
        playerNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
        roleTextView.setTextSize(25);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerNameTextView.getLayoutParams();
        params.topMargin = 10;
    }

    /**
     * Button listener for finishing someone's turn. This decides what to do
     */
    class doneClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (currentPlayer+1!=MainActivity.players.length) { // Ensures it's not the last player
                remove();
                currentPlayer++;
                setContentView(R.layout.between_players);
                betweenPlayersName = findViewById(R.id.between_players_name);
                betweenPlayersName.setText(players[currentPlayer].name);
            }
            else {
                doSwitch();
                Intent intent = new Intent(Round1of1.this, DiscussionTimer.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Listener for when the seer decides to pick a player's role or an unused role
     */
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
                showPlayerButtons();
            }
            else {
                showUnusedRoleButtons(true);
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
                                rolesSeen.append("\n").append(CharacterSelect.unusedRoles[i]);
                            }
                        }
                        removeUnusedRoleButtons();
                        showInfo("You saw: " + rolesSeen);
                        generateDoneButton();
                    }
                    break;

                case "Werewolf":
                    String rolesSeen = "";
                    rolesSeen += "\n" + CharacterSelect.unusedRoles[view.getId()];
                    removeUnusedRoleButtons();
                    showInfo("You saw: " + rolesSeen);
                    generateDoneButton();
                    break;

                case "Drunk":
                    queSwitch("Drunk", currentPlayer, view.getId());
                    removeUnusedRoleButtons();
                    showInfo("You switched with role " + ((view.getId()) + 1));
                    generateDoneButton();
                    break;

            }
        }
    }

    /**
     * Listener for selecting a player
     */
    class playerSelection implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int adjust = view.getId() >= currentPlayer ?  1 : 0;
            switch (MainActivity.players[currentPlayer].originalRole) {
                case "Seer":
                    showInfo(MainActivity.players[(view.getId()) + adjust].name + "'s role is: \n" + MainActivity.players[(view.getId()) + adjust].originalRole);

                    removePlayerButtons();
                    generateDoneButton();
                    break;

                case "Robber":
                    showInfo("Your new role is: \n" + MainActivity.players[(view.getId()) + adjust].originalRole);

                    queSwitch("Robber", currentPlayer, (view.getId()) + adjust);

                    removePlayerButtons();

                    // TODO display "you switched x and y"

                    generateDoneButton();
                    break;

                case "Troublemaker":
                    Integer[] tempIntegerArray = new Integer[2];
                    int numSelected = 0;
                    for (int i = 0; i < MainActivity.players.length - 1; i++) {
                        if (((ToggleButton) findViewById(i)).isChecked()) {
                            adjust = view.getId() >= currentPlayer ?  1 : 0;
                            tempIntegerArray[numSelected] = i + adjust;
                            numSelected++;
                        }
                    }
                    if (numSelected == 2) {
                        queSwitch("Troublemaker", tempIntegerArray[0], tempIntegerArray[1]);
                        removePlayerButtons();
                        generateDoneButton();
                    }
                    break;

            }
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

    void queSwitch(String role, int switcher, int switchWith) {
        int[] tempIntArray = new int[]{switcher, switchWith};
        switch (role) {
            case "Robber":
                robberQue.add(tempIntArray);
                break;
            case "Troublemaker":
                troublemakerQue.add(tempIntArray);
                break;
            case "Drunk":
                drunkQue.add(tempIntArray);
                break;
        }
    }
    void doSwitch() {
        for (int[] robberAction : robberQue) {
            MainActivity.players[robberAction[0]].setPostRobber(MainActivity.players[robberAction[1]].postDoppel);
            MainActivity.players[robberAction[1]].setPostRobber(MainActivity.players[robberAction[0]].postDoppel);
        }
        for (int[] troublemakerAction : troublemakerQue) {
            MainActivity.players[troublemakerAction[0]].setPostTrouble(MainActivity.players[troublemakerAction[1]].postRobber);
            MainActivity.players[troublemakerAction[1]].setPostTrouble(MainActivity.players[troublemakerAction[0]].postRobber);
        }
        for (int[] drunkAction : drunkQue) {
            // Order is important here, unlike the robber or troublemaker
            MainActivity.players[drunkAction[0]].setPostDrunk(CharacterSelect.unusedRoles[drunkAction[1]]);
            CharacterSelect.unusedRoles[drunkAction[1]] = MainActivity.players[drunkAction[0]].postTrouble;
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

    /**
     * Makes the text and options for the role
     */
    void generateView()
    {
        playerNameTextView.setText(MainActivity.players[currentPlayer].name);
        roleTextView.setText(MainActivity.players[currentPlayer].originalRole);
        switch (MainActivity.players[currentPlayer].originalRole) {
            case "Werewolf":
                generateWerewolf();
                break;
            case "Seer":
                generateSeer();
                break;
            case "Robber":
                generateRobber();
                break;
            case "Troublemaker":
                generateTroublemaker();
                break;
            case "Minion":
                generateMinion();
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
            case "Drunk":
                generateDrunk();
                break;
            case "Mason":
                generateMason();
                break;
        }
    }

    /**
     * Shows the werewolves, either for a minion or werewolf
     */
    void showWerewolves() {
        StringBuilder otherWerewolves = new StringBuilder();
        for (Player player : MainActivity.players){
            if (player.originalRole.equals("Werewolf")&&player!=MainActivity.players[currentPlayer]) {
                otherWerewolves.append("\n").append(player.name);
            }
        }
        if (MainActivity.players[currentPlayer].originalRole.equals("Minion")) {
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

    /**
     * Generates the view for a werewolf
     */
    void generateWerewolf() // done
    {
        explanationTextView.setText("You see the other werewolves. If you're alone, see an unused role.");
        if(countRoles("Werewolf")>1)//not lone wolf
        {
            showWerewolves();
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
    void generateRobber() {
        explanationTextView.setText("Take and view someone else's role and give them your own.");
        showPlayerButtons();
    }
    void generateTroublemaker() {
        explanationTextView.setText("Change cards between two other players.");
        showPlayerButtons(true);
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
    void generateDrunk() {
        explanationTextView.setText("Choose a card from the center.");
        showUnusedRoleButtons(false);
        generateDoneButton();
    }
    void generateMason() {
        explanationTextView.setText("You see the other mason, if there is one.");
        StringBuilder otherMasons = new StringBuilder();
        for(int i = 0 ; i < MainActivity.players.length; i++)
        {
            if(MainActivity.players[i].originalRole.equals("Mason") && i!=currentPlayer)
            {
                otherMasons.append(MainActivity.players[i].name).append("\n");
            }
        }
        if(!otherMasons.toString().equals(""))
        {
            if(countRoles("Mason") > 2) {
                showInfo("The other masons are " + otherMasons);
            }
            else {
                showInfo("The other mason is " + otherMasons);
            }
        }
        else
        {
            showInfo("You're pretty lonely as the only mason");
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
    void remove() {
        removeInfo();
        removePlayerButtons();
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
    void showUnusedRoleButtons(boolean toggleButtons) {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 2*width/7;
        int height = displayMetrics.heightPixels;
        int buttonHeight = height/6;

        for (int i=0;i<3;i++){
            Button tempAddButton = new Button(this);
            if (toggleButtons) {
                tempAddButton = new ToggleButton(this);
                ((ToggleButton) tempAddButton).setTextOn("Role "+(i+1));
                ((ToggleButton) tempAddButton).setTextOff("Role "+(i+1));
            }
            tempAddButton.setOnClickListener(new unusedRoleClick(MainActivity.players[currentPlayer].originalRole));
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

    /**
     * Show buttons for all players except yourself
     */
    void showPlayerButtons() {showPlayerButtons(false);} // Optional Parameter
    void showPlayerButtons(boolean toggleButtons)
    {
        View view = findViewById(R.id.round_1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int buttonWidth = 2*width/5;
        int height = displayMetrics.heightPixels;
        int buttonHeight = height/10;
        int j=0; //j is used instead of i to fix the times when we skip the player whose turn it is
        for(Player player : MainActivity.players)
        {
            if (MainActivity.players[currentPlayer]!=player) {
                Button tempAddButton = new Button(this);
                if (toggleButtons) {
                    tempAddButton = new ToggleButton(this);
                    ((ToggleButton) tempAddButton).setTextOn(player.name + "");
                    ((ToggleButton) tempAddButton).setTextOff(player.name + "");
                }
                tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, buttonHeight));
                tempAddButton.setText(player.name);
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


    // Stops the back button
    @Override
    public void onBackPressed() {}
}
