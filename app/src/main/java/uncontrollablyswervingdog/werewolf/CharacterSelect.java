package uncontrollablyswervingdog.werewolf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

@SuppressLint({"ResourceType","SetTextI18n"})
public class CharacterSelect extends AppCompatActivity
{
//    static String[] roles = new String[]{"Werewolf", "Minion", "Mason", "Seer", "Robber", "Troublemaker", "Drunk", "Villager", "Hunter", "Tanner"};
    static String[] roles = new String[]{"#Doppelganger", "Werewolf", "Minion", "Mason", "Seer", "Robber", "Troublemaker", "Drunk", "Insomniac", "Villager", "Hunter", "Tanner"};
    int[] amounts;
    static int numRounds;
    static String[] unusedRoles;
    ScrollView scrollView;
    RelativeLayout scrollLayout;
    static HashMap<String, Integer> usedRoles = new HashMap<>(); // For the background in the discussion timer
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);

        scrollView = findViewById(R.id.scroll_view);
        scrollLayout = findViewById(R.id.scroll_layout);

        generateStuff();
    }
    void generateStuff()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        amounts = new int[roles.length];
        for(int i = 0; i < roles.length; i++)
        {
            amounts[i] = 0;
            TextView label = new TextView(this);
            label.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            label.setText(Round1of2.removeDoppelChar(roles[i]));
            label.setId(i+400);
            if (MainActivity.smallScreen && roles[i].length() > 9) {
                label.setTextSize(34);
            }
            else {
                label.setTextSize(40);
            }
            scrollLayout.addView(label);//((RelativeLayout) relLayout).addView(label);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) label.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.topMargin = 5;
            if (i == 0) {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }else {
                params.addRule(RelativeLayout.BELOW, (i-1)+400);
            }
            params.leftMargin = 10;

            Button tempSubButton = new Button(this);
            tempSubButton.setLayoutParams(new RelativeLayout.LayoutParams(width/10, 50));
            tempSubButton.setText("-");
            tempSubButton.setId(i+200); //add is 100, sub is 200, Number is 300, Label is 400

            scrollLayout.addView(tempSubButton);//((RelativeLayout) relLayout).addView(tempSubButton);

            params = (RelativeLayout.LayoutParams) tempSubButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_TOP, label.getId());
            params.addRule(RelativeLayout.ALIGN_BOTTOM, label.getId());
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView numberLabel = new TextView(this);
            numberLabel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            numberLabel.setText("0");
            numberLabel.setId(i+300);
            numberLabel.setTextSize(40);
            scrollLayout.addView(numberLabel);//((RelativeLayout) relLayout).addView(numberLabel);

            params = (RelativeLayout.LayoutParams) numberLabel.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_TOP, tempSubButton.getId());
            params.addRule(RelativeLayout.ALIGN_BOTTOM, tempSubButton.getId());
            params.addRule(RelativeLayout.LEFT_OF, tempSubButton.getId());
            params.rightMargin = 15;

            Button tempAddButton = new Button(this);
            tempAddButton.setLayoutParams(new RelativeLayout.LayoutParams(width/10, 50));
            tempAddButton.setText("+");
            tempAddButton.setId(i+100);
            tempAddButton.setOnClickListener(new onClick(numberLabel.getId(), i, 1));
            tempSubButton.setOnClickListener(new onClick(numberLabel.getId(), i, -1));

            scrollLayout.addView(tempAddButton);//((RelativeLayout) relLayout).addView(tempAddButton);

            params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_TOP, label.getId());
            params.addRule(RelativeLayout.ALIGN_BOTTOM, label.getId());
            params.addRule(RelativeLayout.LEFT_OF, numberLabel.getId());
            params.rightMargin = 15;
        }
        Button doneButton = new Button(this);
        doneButton.setLayoutParams(new RelativeLayout.LayoutParams(width-30, RelativeLayout.LayoutParams.WRAP_CONTENT));
        doneButton.setText("Done");
        doneButton.setId(500);
        doneButton.setVisibility(View.INVISIBLE);
        doneButton.setOnClickListener(new doneClick());
        scrollLayout.addView(doneButton);//((RelativeLayout) relLayout).addView(doneButton);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
//        params.addRule(RelativeLayout.ALIGN_RIGHT, (roles.length-1)+200);
//        params.addRule(RelativeLayout.ALIGN_LEFT, (roles.length-1)+400);
        params.addRule(RelativeLayout.BELOW, roles.length + 399);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.charSelect);
        params.topMargin = 10;

        TextView numLeft = new TextView(this);
        numLeft.setTextSize(30);
        numLeft.setId(1+500);
        numLeft.setLayoutParams(new RelativeLayout.LayoutParams(width-30, RelativeLayout.LayoutParams.WRAP_CONTENT));
        numLeft.setText(MainActivity.players.length+3+"");
        numLeft.setGravity(Gravity.CENTER);
        scrollLayout.addView(numLeft);//((RelativeLayout) relLayout).addView(numLeft);
        params = (RelativeLayout.LayoutParams) numLeft.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.charSelect);
    }
    Random Rand;
    HashMap<String, Integer> availableRoles = new HashMap<>();
    class doneClick implements View.OnClickListener
    {
        @Override
        public void onClick(View V)
        {
            int numPlayers = MainActivity.players.length;
            int assignedNumber = 0;
            Rand = new Random();
            availableRoles.clear();
            int totalRoles = 0;
            for(int i = 0; i < MainActivity.players.length; i++)
            {
                MainActivity.players[i].originalRole = null;
            }
            for(int i = 0; i < roles.length; i++)
            {
                if(amounts[i]>0)
                {
                    totalRoles += amounts[i];
                    availableRoles.put(roles[i], amounts[i]);
                }
            }
            numRounds = checkNumRounds(availableRoles);
            usedRoles.putAll(availableRoles); // For the background in the discussion timer
            while(assignedNumber!=numPlayers)
            {
                int chosenPlayer = Rand.nextInt(numPlayers);
                if(MainActivity.players[chosenPlayer].originalRole==null)
                {
                    Object[] keys = availableRoles.keySet().toArray(); // returns an array of keys
                    Object[] nums = availableRoles.values().toArray(); // returns an array of values
                    int roleSelected = Rand.nextInt(keys.length);
                    String role = (String) keys[roleSelected];
                    MainActivity.players[chosenPlayer].initialize(role);
                    int number = (int) nums[roleSelected]-1;
                    totalRoles--;
                    if(number==0)
                    {
                        availableRoles.remove(role);
                    }
                    else
                    {
                        availableRoles.put((String) keys[roleSelected], number);
                    }
                    assignedNumber++;
                }
            }
            unusedRoles = new String[totalRoles];
            assignedNumber = 0;
            numPlayers = 3;
            while(assignedNumber!=numPlayers)
            {
                int chosenPlayer = assignedNumber;
                if (unusedRoles[chosenPlayer] == null)
                {
                    Object[] keys = availableRoles.keySet().toArray(); // returns an array of keys
                    Object[] nums = availableRoles.values().toArray(); // returns an array of values
                    int roleSelected = Rand.nextInt(keys.length);
                    String role = (String) keys[roleSelected];
                    unusedRoles[chosenPlayer] = role;
                    int number = (int) nums[roleSelected] - 1;
                    if (number == 0) {
                        availableRoles.remove(role);
                    } else {
                        availableRoles.put((String) keys[roleSelected], number);
                    }
                    assignedNumber++;
                }
            }
            if (numRounds==1) {
                Intent intent = new Intent(CharacterSelect.this, Round1of1.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(CharacterSelect.this, Round1of2.class);
                startActivity(intent);
            }
        }
    }
    class onClick implements View.OnClickListener
    {
        int index = 0;
        int idOfText = 0;
        int changeBy;
        public onClick(int id, int index, int changeBy)
        {
            this.changeBy = changeBy;
            this.index = index;
            idOfText = id;
        }
        @Override
        public void onClick(View V)
        {
            int total = 0;
            for (int amount : amounts) {
                total += amount;
            }
            if(total<MainActivity.players.length+3 || changeBy==-1)
            {
                TextView label = findViewById(idOfText);
                amounts[index] += changeBy;
                if (amounts[index] < 0) {
                    amounts[index] = 0;
                    return;
                }
                label.setText(String.valueOf(amounts[index]));
                total += changeBy;
                ((TextView) findViewById(500+1)).setText((MainActivity.players.length+3-total)+"");
                if(total==MainActivity.players.length+3)
                {
                    findViewById(500).setVisibility(View.VISIBLE);
                    findViewById(501).setVisibility(View.INVISIBLE);
                }
                else
                {
                    findViewById(500).setVisibility(View.INVISIBLE);
                    findViewById(501).setVisibility(View.VISIBLE);
                }
            }
        }
    }
    static int checkNumRounds(HashMap<String, Integer> availableRoles)
    {
        for (Object role : availableRoles.keySet().toArray())
        {
            if(role.equals("Insomniac") || role.equals("#Doppelganger"))
            {
                return 2;
            }
        }
        return 1;
    }
}
