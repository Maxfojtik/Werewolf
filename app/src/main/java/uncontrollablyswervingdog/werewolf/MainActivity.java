package uncontrollablyswervingdog.werewolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText name1;
    Button newButton;
    Button doneButton;
    Button delete1;

    int numEditTexts = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name1 = (EditText) findViewById(R.id.name1);
        newButton = (Button) findViewById(R.id.new_button);
        doneButton = (Button) findViewById(R.id.done_button);
        delete1 = (Button) findViewById(R.id.delete_button);

        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    addNameField();
                } catch(Exception e) {

                }
            }
        });

    }

    public void doneButton(View v) {
        Intent intent = new Intent(MainActivity.this, CharacterSelect.class);
        startActivity(intent);
    }

    public void addNameField() throws Exception {
        View linearLayout =  findViewById(R.id.mainMenu);

        // Make the new textEdit
        EditText newNameBox = new EditText(this);
        newNameBox.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        newNameBox.setText("");
        newNameBox.setHint("Name");
        newNameBox.setEms(12);
        newNameBox.setMaxLines(1);
        newNameBox.setSingleLine(true);
        newNameBox.setId(0+numEditTexts); // Not an error, the red lines just note that this isn't the usual way to do things
        newNameBox.requestFocus();
        ((RelativeLayout) linearLayout).addView(newNameBox);
        // Adjust position for the new textEdit
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newNameBox.getLayoutParams();
        if (numEditTexts ==1) {
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

        numEditTexts++;
    }
}

