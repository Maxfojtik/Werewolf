package uncontrollablyswervingdog.werewolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;


public class CharacterSelect extends AppCompatActivity
{
    static String[] roles = new String[]{"Werewolf", "Minion", "Villager", "Seer", "Troublemaker", "Robber"};
    int[] amounts;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);
        generateStuff();
    }
    void generateStuff()
    {
        View relLayout = findViewById(R.id.charSelect);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        amounts = new int[roles.length];
        int buttonHeight = 0;
        for(int i = 0; i < roles.length; i++)
        {
            amounts[i] = 0;
            TextView label = new TextView(this);
            label.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            label.setText(roles[i]);
            label.setId(i+400);
            label.setTextSize(40);
            ((RelativeLayout) relLayout).addView(label);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) label.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.topMargin = 10;
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

            ((RelativeLayout) relLayout).addView(tempSubButton);

            params = (RelativeLayout.LayoutParams) tempSubButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_TOP, label.getId());
            params.addRule(RelativeLayout.ALIGN_BOTTOM, label.getId());
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView numberLabel = new TextView(this);
            numberLabel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            numberLabel.setText("0");
            numberLabel.setId(i+300);
            numberLabel.setTextSize(40);
            ((RelativeLayout) relLayout).addView(numberLabel);

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

            ((RelativeLayout) relLayout).addView(tempAddButton);

            params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_TOP, label.getId());
            params.addRule(RelativeLayout.ALIGN_BOTTOM, label.getId());
            params.addRule(RelativeLayout.LEFT_OF, numberLabel.getId());
            params.rightMargin = 15;
        }
        Button doneButton = new Button(this);
        doneButton.setLayoutParams(new RelativeLayout.LayoutParams(width-30, RelativeLayout.LayoutParams.WRAP_CONTENT));
        doneButton.setText("Done");
        doneButton.setId(0+500);
        doneButton.setVisibility(View.INVISIBLE);
        doneButton.setOnClickListener(new doneClick());
        ((RelativeLayout) relLayout).addView(doneButton);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
//        params.addRule(RelativeLayout.ALIGN_RIGHT, (roles.length-1)+200);
//        params.addRule(RelativeLayout.ALIGN_LEFT, (roles.length-1)+400);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.charSelect);
        params.topMargin = 10;

        TextView numLeft = new TextView(this);
        numLeft.setTextSize(30);
        numLeft.setId(1+500);
        numLeft.setLayoutParams(new RelativeLayout.LayoutParams(width-30, RelativeLayout.LayoutParams.WRAP_CONTENT));
        numLeft.setText(MainActivity.players.length+"");
        numLeft.setGravity(Gravity.CENTER);
        ((RelativeLayout) relLayout).addView(numLeft);
        params = (RelativeLayout.LayoutParams) numLeft.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.charSelect);
    }
    Random Rand;
    HashMap<String, Integer> avaliableRoles = new HashMap<String, Integer>();
    class doneClick implements View.OnClickListener
    {
        @Override
        public void onClick(View V)
        {
            int numPlayers = MainActivity.players.length;
            int assignedNumber = 0;
            Rand = new Random();
            avaliableRoles.clear();
            for(int i = 0; i < MainActivity.players.length; i++)
            {
                MainActivity.players[i].role = null;
            }
            for(int i = 0; i < roles.length; i++)
            {
                if(amounts[i]>0)
                {
                    avaliableRoles.put(roles[i], amounts[i]);
                }
            }
            while(assignedNumber!=numPlayers)
            {
                int chosenPlayer = Rand.nextInt(numPlayers);
                if(MainActivity.players[chosenPlayer].role==null)
                {
                    Object[] keys = avaliableRoles.keySet().toArray(); // returns an array of keys
                    Object[] nums = (Object[]) avaliableRoles.values().toArray(); // returns an array of values
                    int roleSelected = Rand.nextInt(keys.length);
                    String role = (String) keys[roleSelected];
                    MainActivity.players[chosenPlayer].role = role;
                    int number = (int) nums[roleSelected]-1;
                    if(number==0)
                    {
                        avaliableRoles.remove(role);
                    }
                    else
                    {
                        avaliableRoles.put((String) keys[roleSelected], number);
                    }
                    assignedNumber++;
                }
            }
            Intent intent = new Intent(CharacterSelect.this, Round1.class);
            startActivity(intent);
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
            for(int i = 0; i < amounts.length; i++)
            {
                total += amounts[i];
            }
            if(total<MainActivity.players.length || changeBy==-1)
            {
                TextView label = (TextView) findViewById(idOfText);
                amounts[index] += changeBy;
                if (amounts[index] < 0) {
                    amounts[index] = 0;
                    return;
                }
                label.setText(String.valueOf(amounts[index]));
                total += changeBy;
                ((TextView) findViewById(500+1)).setText((MainActivity.players.length-total)+"");
                if(total==MainActivity.players.length)
                {
                    findViewById(0+500).setVisibility(View.VISIBLE);
                    findViewById(1+500).setVisibility(View.INVISIBLE);
                }
                else
                {
                    findViewById(0+500).setVisibility(View.INVISIBLE);
                    findViewById(1+500).setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
