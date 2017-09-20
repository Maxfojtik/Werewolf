package uncontrollablyswervingdog.werewolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.content.Intent;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    EditText name1;
    Button newButton;
    Button doneButton;
    Button delete1;
    static Player [] players;

    int numEditTexts = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name1 = (EditText) findViewById(R.id.name1);
        newButton = (Button) findViewById(R.id.new_button);
        doneButton = (Button) findViewById(R.id.done_button);
        delete1 = (Button) findViewById(R.id.delete_button);

        try {
            addNameField();addNameField();addNameField();
        } catch (Exception e){}

        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            try {
                addNameField();
            } catch(Exception e) {}
            }
        });
    }

    // For clicking the delete button
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            newButton.setVisibility(View.VISIBLE);

            View linearLayout =  findViewById(R.id.mainMenu);
            // Remove the editText and delButton
            ((RelativeLayout) linearLayout).removeView(findViewById((v.getId()-10)));
            ((RelativeLayout) linearLayout).removeView(findViewById((v.getId())));
            numEditTexts--;
            // Set the next editText relative to the above one
            if (findViewById(v.getId()-9)!=null) {
                RelativeLayout.LayoutParams nextEditTextParams = (RelativeLayout.LayoutParams) findViewById(v.getId() - 9).getLayoutParams();
                nextEditTextParams.addRule(RelativeLayout.BELOW, v.getId() - 11);
            }
            // For the next editTexts, set the alignment so they are under the previous one
            for (int i=v.getId()-9;i<numEditTexts+2;i++) {
                EditText editText2 = (EditText) findViewById(i);
                Button delButton2 = (Button) findViewById(i+10);
                editText2.setHint("Player "+(i-1));
                editText2.setId(i-1);
                delButton2.setId(i+9);
                if (findViewById(i+1)!=null) {
                    RelativeLayout.LayoutParams lowerEditTextParams = (RelativeLayout.LayoutParams) findViewById(i+1).getLayoutParams();
                    lowerEditTextParams.addRule(RelativeLayout.BELOW, i-1);
                }
            }
            for (int i=v.getId();i<=numEditTexts+10;i++) {
                RelativeLayout.LayoutParams lowerEditTextParams = (RelativeLayout.LayoutParams) findViewById(i).getLayoutParams();
                lowerEditTextParams.addRule(RelativeLayout.ALIGN_BOTTOM, i-10);
            }
            RelativeLayout.LayoutParams lowerEditTextParams = (RelativeLayout.LayoutParams) newButton.getLayoutParams();
            lowerEditTextParams.addRule(RelativeLayout.ALIGN_BOTTOM, numEditTexts);

            RelativeLayout.LayoutParams doneButtonParams = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
            doneButtonParams.addRule(RelativeLayout.BELOW, numEditTexts);
        }
    };

    public void doneButton(View v) {
        getNames();
        Intent intent = new Intent(MainActivity.this, CharacterSelect.class);
        startActivity(intent);
    }

    public void timerButton(View v) {
        Intent intent = new Intent(MainActivity.this, Timer.class);
        startActivity(intent);
    }

    public void addNameField() throws Exception {

        numEditTexts++;

        View linearLayout =  findViewById(R.id.mainMenu);

        // Make the new textEdit
        EditText newNameBox = new EditText(this);
        newNameBox.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        newNameBox.setText("");
        newNameBox.setHint("Player "+numEditTexts);
        newNameBox.setEms(12);
        newNameBox.setMaxLines(1);
        newNameBox.setSingleLine(true);
        newNameBox.setId(0+numEditTexts);
        newNameBox.requestFocus();
        ((RelativeLayout) linearLayout).addView(newNameBox);
        // Adjust position for the new textEdit
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newNameBox.getLayoutParams();
        if (numEditTexts ==2) {
            params.addRule(RelativeLayout.BELOW, name1.getId());
        }else {
            params.addRule(RelativeLayout.BELOW, numEditTexts-1);
        }
        params.addRule(RelativeLayout.ALIGN_LEFT, name1.getId());
        params.addRule(RelativeLayout.ALIGN_RIGHT, name1.getId());

        // Make the new del button if needed
        if (numEditTexts > 4) {
            Button delButton = new Button(this);
            delButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            delButton.setText("-");
            delButton.setId((10+numEditTexts));
            delButton.setOnClickListener(onClickListener);

            RelativeLayout.LayoutParams delButtonParams = (RelativeLayout.LayoutParams) delButton.getLayoutParams();
            delButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, numEditTexts);
            delButtonParams.addRule(RelativeLayout.ALIGN_LEFT, delete1.getId());
            delButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, delete1.getId());
            ((RelativeLayout) linearLayout).addView(delButton);
        }

        // Adjust position for the done button
        RelativeLayout.LayoutParams doneButtonParams = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
        doneButtonParams.addRule(RelativeLayout.BELOW, numEditTexts);

        // Adjust position for the new button
        RelativeLayout.LayoutParams newButtonParams = (RelativeLayout.LayoutParams) newButton.getLayoutParams();
        newButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, numEditTexts);

        // Caps number of players at 10
        if (numEditTexts == 9) {
            newButton.setVisibility(View.INVISIBLE);
        }

    }

    public void getNames() {
        players = new Player [numEditTexts];
        EditText var1 = (EditText) findViewById(R.id.name1);
        if ((var1.getText()+"").equals("")) {
            players[0] = new Player("Player 1");
        }
        else {
            players[0] = new Player(var1.getText()+"");
        }
        for (int i=2;i<players.length+1;i++) {
            EditText var = (EditText) findViewById(i+0);
            if ((var.getText()+"").equals("")) {
                players[i-1] = new Player(var.getHint()+"");
            }
            else {
                players[i-1] = new Player(var.getText()+"");
            }
        }
    }
}
