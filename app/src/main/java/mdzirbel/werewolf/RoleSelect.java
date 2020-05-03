package mdzirbel.werewolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static mdzirbel.werewolf.Defines.ALPHA_CARD_ROLES;
import static mdzirbel.werewolf.Defines.ROLES;

import static mdzirbel.werewolf.Reference.players;
import static mdzirbel.werewolf.Reference.unusedRoles;
import static mdzirbel.werewolf.Reference.numRounds;
import static mdzirbel.werewolf.Reference.usedRoles;
import static mdzirbel.werewolf.Reference.width;

// TODO alpha wolf requires that you select another card and you must have another werewolf card selected

public class RoleSelect extends AppCompatActivity
{

    // For role amounts, negative numbers indicate that there is no limit, and 0's will not be included in the list
    static final Map<String, Integer> roleMax = new HashMap<>(); // For making and using in the character select view

    int[] amounts; // The active amount of each character selected at any moment. Amount of role = amounts[ArrayUtils.indexOf(ROLES, role)]
    ScrollView scrollView;
    RelativeLayout scrollLayout;
    Button doneButton;
    TextView numLeft;
    int numRolesNeeded;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);

        scrollView = findViewById(R.id.scroll_view);
        scrollLayout = findViewById(R.id.scroll_layout);

        initializeRoleAmounts();

        generatePage();
    }
    static void initializeRoleAmounts() {
        // Negative numbers are unlimited, zero's will not be added to the options, and positive numbers are the cap

        // Set maximums for select roles
        roleMax.put("Doppelganger", 1);
        roleMax.put("Robber", 1);
        roleMax.put("Troublemaker", 1);
        roleMax.put("Drunk", 1);

        // If we wanted, we'd set zeros here to not add those roles

        // Set all others as unlimited
        for (String role : ROLES) {
            if (!roleMax.containsKey(role)) {
                roleMax.put(role, -1);
            }
        }
    }
    void generatePage()
    {
        amounts = new int[ROLES.length];
        for(int i = 0; i < ROLES.length; i++)
        {
            if (roleMax.get(ROLES[i])!= 0) {
                amounts[i] = 0;
                TextView label = new TextView(this);
                label.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                label.setText(ROLES[i]);
                label.setId(i + Defines.LABEL_ID_START);
                if (Reference.smallScreen && ROLES[i].length() > 9) {
                    label.setTextSize(34);
                } else {
                    label.setTextSize(40);
                }
                scrollLayout.addView(label);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) label.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.topMargin = 5;
                if (i == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                } else {
                    params.addRule(RelativeLayout.BELOW, (i - 1) + Defines.LABEL_ID_START);
                }
                params.leftMargin = 10;

                Button tempSubButton = new Button(this);
                tempSubButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 10, 50));
                tempSubButton.setText("-");
                tempSubButton.setId(i + Defines.MINUS_BUTTON_ID_START);

                scrollLayout.addView(tempSubButton);

                params = (RelativeLayout.LayoutParams) tempSubButton.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_TOP, label.getId());
                params.addRule(RelativeLayout.ALIGN_BOTTOM, label.getId());
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                TextView numberLabel = new TextView(this);
                numberLabel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                numberLabel.setText("0");
                numberLabel.setId(i + Defines.NUMBER_LABEL_ID_START);
                numberLabel.setTextSize(40);
                scrollLayout.addView(numberLabel);

                params = (RelativeLayout.LayoutParams) numberLabel.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_TOP, tempSubButton.getId());
                params.addRule(RelativeLayout.ALIGN_BOTTOM, tempSubButton.getId());
                params.addRule(RelativeLayout.LEFT_OF, tempSubButton.getId());
                params.rightMargin = 15;

                Button tempAddButton = new Button(this);
                tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 10, 50));
                tempAddButton.setText("+");
                tempAddButton.setId(i + Defines.PLUS_BUTTON_ID_START);
                tempAddButton.setOnClickListener(new plusMinusClick(numberLabel.getId(), i, 1));
                tempSubButton.setOnClickListener(new plusMinusClick(numberLabel.getId(), i, -1));

                scrollLayout.addView(tempAddButton);//((RelativeLayout) relLayout).addView(tempAddButton);

                params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_TOP, label.getId());
                params.addRule(RelativeLayout.ALIGN_BOTTOM, label.getId());
                params.addRule(RelativeLayout.LEFT_OF, numberLabel.getId());
                params.rightMargin = 15;
            }
        }
        doneButton = new Button(this);
        doneButton.setLayoutParams(new RelativeLayout.LayoutParams(width-30, RelativeLayout.LayoutParams.WRAP_CONTENT));
        doneButton.setText(R.string.done);
        doneButton.setVisibility(View.INVISIBLE);
        doneButton.setOnClickListener(new doneClick());
        scrollLayout.addView(doneButton);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, ROLES.length + 399);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.charSelect);
        params.topMargin = 10;

        numLeft = new TextView(this);
        numLeft.setTextSize(30);
        numLeft.setLayoutParams(new RelativeLayout.LayoutParams(width-30, RelativeLayout.LayoutParams.WRAP_CONTENT));
        String numberOfRolesNeeded = players.length + 3 + "";
        numLeft.setText(numberOfRolesNeeded);
        numLeft.setGravity(Gravity.CENTER);
        scrollLayout.addView(numLeft);//((RelativeLayout) relLayout).addView(numLeft);
        params = (RelativeLayout.LayoutParams) numLeft.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.charSelect);
    }
    class doneClick implements View.OnClickListener
    {
        @Override
        public void onClick(View V)
        {
            Random Rand;
            if (Defines.USE_STATIC_SEED) {
                Rand = new Random(Defines.STATIC_SEED);
            }
            else{
                Rand = new Random();
            }
            // Default player original roles to null so they can be compared to null later
            for (Player player : players) {
                player.originalRole = null;
            }
            // Puts together a list of every unassigned role, including repeats
            ArrayList<String> rolesToAssign = new ArrayList<>(); // All the selected roles and their counts
            for(int i = 0; i < ROLES.length; i++)
            {
                for (int j = 0; j < amounts[i]; j++)
                {
                    rolesToAssign.add(ROLES[i]);
                }
            }
            // Make usedRoles, a copy of all the roles which could appear
            usedRoles = new ArrayList<>();
            usedRoles.addAll(rolesToAssign); // For the background in the discussion timer

            // If we have alpha wolf, pull out a random werewolf card other than alpha wolf. We can assume there is one.
            String alphaWolfRole = null;
            if (rolesToAssign.contains("Alpha Wolf")) {
                ArrayList<String> werewolfRolesForAlpha = new ArrayList<>(); // All the selected roles and their counts
                // Add all the roles that could be required for the Alpha Card into a list
                for (String role : rolesToAssign) {
                    if (Arrays.asList(ALPHA_CARD_ROLES).contains(role)) { //
                        werewolfRolesForAlpha.add(role);
                    }
                }
                // Randomly select one of the werewolf roles and remove it from selection for players
                alphaWolfRole = werewolfRolesForAlpha.get(Rand.nextInt(werewolfRolesForAlpha.size())); // Randomly select a role for the player
                rolesToAssign.remove(alphaWolfRole);
            }

            // Create unusedRoles
            String[] selectedUnusedRoles = new String [Defines.NUM_UNUSED_ROLES];
            for (int i=0; i<Defines.NUM_UNUSED_ROLES; i++)
            {
                int roleSelected = Rand.nextInt(rolesToAssign.size()); // Randomly select a role for the next unused role
                selectedUnusedRoles[i] = rolesToAssign.remove(roleSelected); // Remove it from the list and assign it to the proper place in the array
            }

            // Initialize unusedRoles
            if (rolesToAssign.contains("Alpha Wolf")) {
                unusedRoles.initializeWithAlphaRole(selectedUnusedRoles, alphaWolfRole);
            }
            else {
                unusedRoles.initializeNoAlphaRole(selectedUnusedRoles);
            }

            // Assign players roles randomly
            for (Player player : players)
            {
                int roleSelected = Rand.nextInt(rolesToAssign.size()); // Randomly select a role for the player
                Role role = new Role(rolesToAssign.remove(roleSelected)); // Assign the role to the player and remove it from the list
                player.initialize(role);
            }

            numRounds = checkNumRounds(usedRoles); // Check how many rounds we'll need, given the current roles

            // Print out the players for debugging
            StringBuilder playersString = new StringBuilder();
            for (Player p : players) {
                playersString.append(p.name).append(" ").append(p.originalRole).append(", ");
            }
            Log.d("PLAYERS", "" + playersString);

            // Start the round
            Intent intent = new Intent(RoleSelect.this, Rounds.class);
            startActivity(intent);

        }
    }
    class plusMinusClick implements View.OnClickListener
    {
        int index;
        int idOfText;
        int changeBy;
        plusMinusClick(int id, int index, int changeBy)
        {
            this.changeBy = changeBy;
            this.index = index;
            idOfText = id;
        }
        @Override
        public void onClick(View v)
        {
            int total = 0; // Amount of ROLES selected
            for (int amount : amounts) {
                total += amount;
            }
            TextView label = findViewById(idOfText);
            amounts[index] += changeBy;

            if (amounts[index] < 0) {
                amounts[index] = 0;
                return;
            }
            label.setText(String.valueOf(amounts[index]));
            total += changeBy;
            String numberOfRolesToAdd = players.length + 3 - total + "";
            numLeft.setText(numberOfRolesToAdd);
            if(total==players.length+3)
            {
                doneButton.setVisibility(View.VISIBLE);
                numLeft.setVisibility(View.INVISIBLE);
            }
            else
            {
                doneButton.setVisibility(View.INVISIBLE);
                numLeft.setVisibility(View.VISIBLE);
            }
            adjustPlusButtons(total);
        }
    }
    void adjustPlusButtons(int total) {
        int maxRolesSelected = players.length + Defines.NUM_UNUSED_ROLES;
        maxRolesSelected += amounts[ArrayUtils.indexOf(ROLES, "Alpha Wolf")] >= 1 ? 1 : 0; // If 1 or more alpha wolves are selected, we need one more role to be selected
        if (total == maxRolesSelected) {
            for (int i = 0; i< ROLES.length; i++) {
                (scrollLayout.findViewById(i+Defines.PLUS_BUTTON_ID_START)).setEnabled(false); // Disable plus button
            }
        }
        else {
            for (int i = 0; i < ROLES.length; i++) {
                if (roleMax.get(ROLES[i]) > 0 && roleMax.get(ROLES[i]) == amounts[i]) { // If there is a max and it is reached
                    (scrollLayout.findViewById(i + Defines.PLUS_BUTTON_ID_START)).setEnabled(false); // Disable plus button
                } else {
                    (scrollLayout.findViewById(i + Defines.PLUS_BUTTON_ID_START)).setEnabled(true); // Disable plus button
                }
            }
        }
    }
    static int checkNumRounds(ArrayList<String> roles)
    {
        int numRounds = 1;

        if (roles.contains("Insomniac") || roles.contains("Doppelganger") || roles.contains("Alpha Wolf") // Alpha Wolf moves before Seer goes
                || (roles.contains("Revealer") && roles.contains("Curator"))) // Curator sees what role is revealed
        {
            numRounds++;
        }
        if (roles.contains("Sentinel")) {
            numRounds++;
        }
        //  If you have Alpha Wolf (goes before seer), Sentinel, Doppel-Sentinel, Seer
        //  Sentinel Round, Doppel-Sentinel Round, Alpha Wolf, Seer
        if (roles.contains("Doppelganger") && roles.contains("Sentinel")) {
            numRounds++;
        }
        Log.d("NUMROUNDS", numRounds + "");
        return numRounds;
    }
}
