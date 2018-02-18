package uncontrollablyswervingdog.werewolf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

@SuppressLint({"ResourceType","SetTextI18n"})
public class Round1of2 extends AppCompatActivity {

    TextView playerNameTextView;
    TextView roleTextView;
    TextView explanationTextView;
    int currentPlayer = 0;
    static ArrayList<int[]> troublemakerQue = new ArrayList<>();
    static ArrayList<int[]> robberQue = new ArrayList<>();
    static ArrayList<int[]> drunkQue = new ArrayList<>();
    static String[] postDoppelUnusedRoles; // Like unused roles but changes for Doppel-Drunk
    static String doppelCharacter = "#"; // To change this there are 2 places in CharacterSelect you need to change too

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_1);

        // Starts these as blank for multiple rounds
        troublemakerQue = new ArrayList<>();
        robberQue = new ArrayList<>();
        drunkQue = new ArrayList<>();

        postDoppelUnusedRoles = CharacterSelect.unusedRoles;
        playerNameTextView = findViewById(R.id.playerName);
        roleTextView = findViewById(R.id.role);
        explanationTextView = findViewById(R.id.explanation);

//        generateView();
        setContentView(R.layout.round_1_reveal);
        playerNameTextView.setText(MainActivity.players[currentPlayer].name);

        if (MainActivity.smallScreen) {
            scaleForSmallScreen();
        }
    }

    /**
     * Makes the text and options for the role
     */
    void generateView()
    {
        String role = removeDoppelChar(MainActivity.players[currentPlayer].preDoppel);
        playerNameTextView.setText(MainActivity.players[currentPlayer].name);
        roleTextView.setText(role);
        switch (role) {
            case "Doppelganger":
                generateDoppelganger();
                break;
            case "Robber":
                generateRobberChoice(); // Need robber to select now and see later
                break;
            case "Troublemaker":
                generateTroublemaker();
                break;
            case "Drunk":
                generateDrunk();
                break;
            default:
                explanationTextView.setText("You will do your action next round");
                generateDoneButton();
                break;
        }
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
            if (currentPlayer+1!=MainActivity.players.length) {
                remove();
                currentPlayer++;
//                generateView();
                setContentView(R.layout.round_1_reveal);
                TextView playerName = findViewById(R.id.playerName);
                playerName.setText(MainActivity.players[currentPlayer].name);
            }
            else {
                doRound1Switch();
                startActivity(new Intent(Round1of2.this, Round2of2.class));
            }
        }
    }

    /**
     * Listener for when you click on an unused role button for any reason
     */
    class unusedRoleClick implements View.OnClickListener {
        @Override
        public void onClick(View view)
        {
            switch (MainActivity.players[currentPlayer].postDoppel) {
                case "Seer":
                    // First checks whether, if the buttons are togglebuttons, one or two have been clicked
                    int numChecked = 0;
                    int notChecked=0;
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
                                rolesSeen.append("\n").append(postDoppelUnusedRoles[i]);
                            }
                        }
                        removeUnusedRoleButtons();
                        showInfo("You saw: " + rolesSeen);
                        generateDoneButton();
                    }
                    break;
                case "Drunk":
                    // Doppel-Drunk
                    if (removeDoppelChar(MainActivity.players[currentPlayer].originalRole).equals("Doppelganger")) {
                        String newRole = CharacterSelect.unusedRoles[view.getId()]; // your new role
                        CharacterSelect.unusedRoles[view.getId()] = MainActivity.players[currentPlayer].originalRole; // Put your role in the middle
                        postDoppelUnusedRoles[view.getId()] = MainActivity.players[currentPlayer].originalRole; // Also do so for the seer
                        MainActivity.players[view.getId()].setPostDoppel(newRole); // Set your postDoppel roles to the center role
                    }
                    // Regular Drunk
                    else {
                        queSwitch("Drunk",currentPlayer,view.getId());
                    }
                    removeUnusedRoleButtons();
                    showInfo("You switched with role " + (view.getId() + 1));
                    generateDoneButton();
                    break;
                case "Werewolf":
                    String roleSeen = "";
                    roleSeen += "\n" + postDoppelUnusedRoles[view.getId()];
                    removeUnusedRoleButtons();
                    showInfo("You saw: " + roleSeen);
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
            // adjust ensures there aren't problems with the button indexes from when they skip your player
            // Inline if for setting value used for this (Type varName = condition ? if true : if false;)
            int adjust = view.getId() >= currentPlayer ?  1 : 0;
            // For Robber
            if (removeDoppelChar(MainActivity.players[currentPlayer].preDoppel).equals("Robber")) {

                // If Doppel-Robber
                if (removeDoppelChar(MainActivity.players[currentPlayer].originalRole).equals("Doppelganger")) {
                    String tempRole = MainActivity.players[currentPlayer].preDoppel; // Temp variable to hold doppel's role
                    MainActivity.players[currentPlayer].setPostDoppel(MainActivity.players[view.getId()+adjust].preDoppel); // Sets doppel's roles to their role
                    MainActivity.players[view.getId()+adjust].setPostDoppel(tempRole); // Sets their roles to your role (using temp var)
                }
                // Regular Robber
                else {
                    queSwitch("Robber",currentPlayer,view.getId()+adjust);
                }

                // Shows the robber that they will see their role next round
                showInfo("You will see what you stole in the next round"); // Has to go now for insom, has to not see bc. dopp-troublemaker
                removePlayerButtons();
                generateDoneButton();
            }
            // For doppelgangers who haven't done anything yet
            else if (removeDoppelChar(MainActivity.players[currentPlayer].preDoppel).equals("Doppelganger")) {
                MainActivity.players[currentPlayer].setPreDoppel(doppelCharacter+MainActivity.players[view.getId()+adjust].originalRole);
                removePlayerButtons();
                generateView();
            }
            // Troublemaker
            else if (removeDoppelChar(MainActivity.players[currentPlayer].preDoppel).equals("Troublemaker")) {

                // Both troublemaker sections first:
                // Checks how many buttons are selected, and puts them in a list
                int[] tempIntArray = new int[2];
                int numSelected = 0;
                for (int i = 0; i < MainActivity.players.length - 1; i++) {
                    adjust = i >= currentPlayer ?  1 : 0;
                    if (((ToggleButton) findViewById(i)).isChecked()) {
                        tempIntArray[numSelected] = i + adjust;
                        numSelected++;
                    }
                }

                // Nothing happens until two buttons are selected
                if (numSelected == 2) {
                    // Doppel-Troublemaker
                    if (removeDoppelChar(MainActivity.players[currentPlayer].originalRole).equals("Doppelganger")) {
                        MainActivity.players[tempIntArray[0]].setPostDoppel(MainActivity.players[tempIntArray[1]].originalRole);
                        MainActivity.players[tempIntArray[1]].setPostDoppel(MainActivity.players[tempIntArray[0]].originalRole);
                    }
                    // Regular Troublemaker
                    else {
                        queSwitch("Troublemaker",tempIntArray[0],tempIntArray[1]);
                    }

                    removePlayerButtons();
                    generateDoneButton();
                }
            }
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

    void doRound1Switch() {
        for (int[] robberAction : robberQue) {
            MainActivity.players[robberAction[0]].setPostRobber(MainActivity.players[robberAction[1]].postDoppel);
            MainActivity.players[robberAction[1]].setPostRobber(MainActivity.players[robberAction[0]].postDoppel);
        }
        for (int[] troublemakerAction : troublemakerQue) {
            MainActivity.players[troublemakerAction[0]].setPostTrouble(MainActivity.players[troublemakerAction[1]].postRobber);
            MainActivity.players[troublemakerAction[1]].setPostTrouble(MainActivity.players[troublemakerAction[0]].postRobber);
        }
        for (int[] drunkAction : drunkQue) {
            CharacterSelect.unusedRoles[drunkAction[0]] = MainActivity.players[drunkAction[1]].postTrouble;
            MainActivity.players[drunkAction[1]].setPostDrunk(CharacterSelect.unusedRoles[drunkAction[0]]);
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
    void generateDoppelganger() {
        explanationTextView.setText("Select a character to copy");
        showPlayerButtons();
    }
    void generateRobberChoice() {
        explanationTextView.setText("Take and view someone else's role and give them your own.");
        showPlayerButtons();
    }
    void generateTroublemaker() {
        explanationTextView.setText("Change cards between two other players.");
        showPlayerButtons();
    }
    void generateDrunk() { // Need doppel-drunk in case seer looks in middle
        explanationTextView.setText("Choose a card from the center.");
        showUnusedRoleButtons();
        generateDoneButton();
    }
    void showInfo(String info) {
        View view = findViewById(R.id.round_1);
        TextView tempAddTextView = new TextView(this);
        tempAddTextView.setText(info);
        tempAddTextView.setId(100);
        if (MainActivity.smallScreen) {
            tempAddTextView.setTextSize(25);
        }
        else {
            tempAddTextView.setTextSize(35);
        }
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
            tempAddButton.setOnClickListener(new unusedRoleClick());
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
    void showPlayerButtons()
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
            if (player!=MainActivity.players[currentPlayer]) {
                Button tempAddButton = new Button(this);
                if (removeDoppelChar(MainActivity.players[currentPlayer].postDoppel).equals("Troublemaker")) {
                    tempAddButton = new ToggleButton(this);
                    ((ToggleButton) tempAddButton).setTextOn(player.name + "");
                    ((ToggleButton) tempAddButton).setTextOff(player.name + "");
                }
                tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(buttonWidth, buttonHeight));
                tempAddButton.setText(player.name + "");
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
    static String removeDoppelChar(String s) {
        return s.replace(doppelCharacter,"");
    }
    static String useDoppelChar(String s) {
        if (s.contains(Round1of2.doppelCharacter)) {
            return "Doppelganger";
        }
        return s;
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

    // Stops the back button
    @Override
    public void onBackPressed() {}

    String logReturn(String print)
    {
        Log.e("LOG",print);
        return print;
    }
}
