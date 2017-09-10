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
    LinkedList<Button> addButtons = new LinkedList<Button>();
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
        for(int i = 0; i < roles.length; i++)
        {
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
            Log.v("MAX", tempSubButton.getText().toString());
            tempSubButton.setId(i+200); //add is 100, sub is 200, Number is 300, Label is 400
            tempSubButton.setOnClickListener(new onClickSub(roles[i]));

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
            tempAddButton.setOnClickListener(new onClickAdd(roles[i]));

            ((RelativeLayout) relLayout).addView(tempAddButton);

            params = (RelativeLayout.LayoutParams) tempAddButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_TOP, label.getId());
            params.addRule(RelativeLayout.ALIGN_BOTTOM, label.getId());
            params.addRule(RelativeLayout.LEFT_OF, numberLabel.getId());
            params.rightMargin = 15;

        }
    }
    class onClickAdd implements View.OnClickListener
    {
        String role = "";
        public onClickAdd(String role)
        {
            this.role = role;
        }
        @Override
        public void onClick(View V)
        {

        }
    }
    class onClickSub implements View.OnClickListener
    {
        String role = "";
        public onClickSub(String role)
        {
            this.role = role;
        }
        @Override
        public void onClick(View V)
        {

        }
    }
}
