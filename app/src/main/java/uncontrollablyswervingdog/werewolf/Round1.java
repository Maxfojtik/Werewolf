package uncontrollablyswervingdog.werewolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Round1 extends AppCompatActivity {
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
        generateView(findViewById(R.id.round_1), "Werewolf");
    }

    void generateDoneButton(View view)
    {
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
    void generateView(View layout, String role)
    {
        if (role.equals("Werewolf"))
        {
            generateWerewolf(layout);
        }
    }
    void generateWerewolf(View layout)
    {
        if(countRoles("Werewolf")>1)//not lone wolf
        {
            
        }
        else//lone wolf
        {

        }
        generateDoneButton(layout);
    }
}