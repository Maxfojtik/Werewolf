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

    int numEditTexts = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name1 = (EditText) findViewById(R.id.name1);
        newButton = (Button) findViewById(R.id.new_button);
        doneButton = (Button) findViewById(R.id.done_button);

        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    addNameField();
                } catch(Exception e) {

                }
            }
        });

//        doneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, CharacterSelect.class);
//                startActivity(intent);
//            }
//        });
    }

    public void doneButton(View v) {
        Intent intent = new Intent(MainActivity.this, CharacterSelect.class);
        startActivity(intent);
    }

    public void addNameField() throws Exception {
        View linearLayout =  findViewById(R.id.mainMenu);
        EditText newNameBox = new EditText(this);newNameBox.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        newNameBox.setText("");
        newNameBox.setHint("Name");
        newNameBox.setEms(10);
        newNameBox.setMaxLines(1);
        newNameBox.setId(numEditTexts); // Not an error, the red lines just note that this isn't the usual way to do things
        newNameBox.requestFocus();

        ((RelativeLayout) linearLayout).addView(newNameBox);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newNameBox.getLayoutParams();
        if (numEditTexts ==1) {
            params.addRule(RelativeLayout.BELOW, name1.getId());
        }else {
            params.addRule(RelativeLayout.BELOW, numEditTexts-1);
        }
        params.addRule(RelativeLayout.ALIGN_LEFT, name1.getId());
        params.addRule(RelativeLayout.ALIGN_RIGHT, name1.getId());
        RelativeLayout.LayoutParams buttonParams = (RelativeLayout.LayoutParams) newButton.getLayoutParams();
        buttonParams.addRule(RelativeLayout.ALIGN_TOP, numEditTexts);
        if (numEditTexts == 9) {
            newButton.setVisibility(View.INVISIBLE);
        }

        numEditTexts++;
    }
}

