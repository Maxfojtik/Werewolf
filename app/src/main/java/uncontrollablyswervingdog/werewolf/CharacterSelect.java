package uncontrollablyswervingdog.werewolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.DisplayMetrics;
import java.util.LinkedList;


public class CharacterSelect extends AppCompatActivity
{
    String[] roles = new String[]{"Werewolf", "Minion", "Villager", "Seer", "Troublemaker", "Robber"};
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
            tempAddButton.setId(i+100); //add is 100, sub is 200, Number is 300
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
        ((RelativeLayout) relLayout).addView(doneButton);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
//        params.addRule(RelativeLayout.ALIGN_RIGHT, (roles.length-1)+200);
//        params.addRule(RelativeLayout.ALIGN_LEFT, (roles.length-1)+400);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.charSelect);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.charSelect);
        params.topMargin = 10;


    }
    class doneClick implements View.OnClickListener
    {
        @Override
        public void onClick(View V)
        {
            int numPlayers = 10;

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
            TextView label = (TextView)findViewById(idOfText);
            amounts[index] += changeBy;
            if(amounts[index]<0)
            {
                amounts[index] = 0;
            }
            label.setText(String.valueOf(amounts[index]));
        }
    }
}
