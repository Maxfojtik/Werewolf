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
    Player [] players;

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
            View linearLayout =  findViewById(R.id.mainMenu);
            // Remove the editText and delButton
            ((RelativeLayout) linearLayout).removeView(findViewById((v.getId()-10)));
            ((RelativeLayout) linearLayout).removeView(findViewById((v.getId())));
            // Set the next editText relative to the above one
            RelativeLayout.LayoutParams nextEditTextParams = (RelativeLayout.LayoutParams) findViewById(v.getId()-9).getLayoutParams();
            nextEditTextParams.addRule(RelativeLayout.BELOW, v.getId()-11);
            Log.d("v.getID",v.getId()+"");
            // For the next editTexts, set the alignment so they are under the previous one
            for (int i=v.getId()-9;i<numEditTexts;i++) {
                Log.d("ADJUSTING",i+"");
                EditText editText2 = (EditText) findViewById(i);
                editText2.setHint((i-1)+"test");
                editText2.setId(i-1);
                RelativeLayout.LayoutParams delButtonParams = (RelativeLayout.LayoutParams) findViewById(i).getLayoutParams();
                delButtonParams.addRule(RelativeLayout.BELOW, i+10);
            }
            numEditTexts--;

        }
    };

    public void doneButton(View v) {
        getNames();
        Intent intent = new Intent(MainActivity.this, CharacterSelect.class);
        startActivity(intent);
    }

    public void addNameField() throws Exception {

        numEditTexts++;

        View linearLayout =  findViewById(R.id.mainMenu);

        // Make the new textEdit
        EditText newNameBox = new EditText(this);
        newNameBox.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        newNameBox.setText("");
        newNameBox.setHint(String.valueOf(numEditTexts));
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
        if (numEditTexts >= 4) {
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
        Log.d("GETNAMES",var1.getText()+"");
        players[0] = new Player(var1.getText()+"");
        for (int i=1;i<=players.length;i++) {
            EditText var = (EditText) findViewById(i+1);
            Log.d("GETNAMES",var.getText()+"");
            players[i] = new Player(var.getText()+"");
        }
//        Log.d("PLAYERS",players+"");
    }
}

