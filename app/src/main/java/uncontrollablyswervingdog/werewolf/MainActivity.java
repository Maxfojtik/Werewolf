package uncontrollablyswervingdog.werewolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    ScrollView scrollView;
    RelativeLayout scrollLayout;
    Button newButton;
    Button doneButton;
    static Player [] players;
    static boolean firstTime = true;
    int maxNameLength = 15;
    int numEditTexts = 0;
    static boolean smallScreen;
    int maxPlayers=10;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newButton = (Button) findViewById(R.id.new_button);
        doneButton = (Button) findViewById(R.id.done_button);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollLayout = (RelativeLayout) findViewById(R.id.scroll_layout);

        // If this is the first round, make more name fields
        if (firstTime) {
            addNameField();
            addNameField();
            addNameField();
            addNameField();
            firstTime = false;
        }
        // If this is not the first time playing, fill in names
        else {
            for (int i=0;i<players.length;i++) {
                if (players[i].name.startsWith("Player ")) {
                    addNameField("");
                } else {
                    addNameField(players[i].name);
                }
            }
        }

        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNameField();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        if (width <= 550) {
            smallScreen = true;
            scaleForSmallScreen();
        }

    }

    void scaleForSmallScreen() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gamemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // For clicking the delete button
    private OnClickListener onDeleteClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            newButton.setVisibility(View.VISIBLE);

            View linearLayout =  findViewById(R.id.mainMenu);
            // Remove the editText and delButton
            scrollLayout.removeView(findViewById((v.getId()-10)));
            scrollLayout.removeView(findViewById((v.getId())));
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
                    if (i==2) {
                        lowerEditTextParams.addRule(RelativeLayout.BELOW, i-1);
                    }
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

    public void addNameField() {addNameField("");}
    public void addNameField(String name) {

        numEditTexts++;

        // Make the new textEdit
        EditText newEditText = new EditText(this);
        newEditText.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        newEditText.setHint("Player " + numEditTexts);
        newEditText.setText("");
        if (!name.equals("")) {
            newEditText.setText(name);
        }

        newEditText.setMaxLines(1);
        newEditText.setSingleLine(true);
        newEditText.setId(0+numEditTexts);
        Log.d("SETTING ID TO", numEditTexts+"");
        newEditText.requestFocus();
        if (smallScreen) {
            newEditText.setEms(2);
            newEditText.setMaxEms(2);
        }
        else {
            newEditText.setEms(3);
            newEditText.setMaxEms(3);
        }
        scrollLayout.addView(newEditText);
        // Adjust position for the new textEdit
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newEditText.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, numEditTexts-1);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.leftMargin = 10;

        // Makes the delete button
        Button delButton = new Button(this);
        delButton.setText("-");
        delButton.setId((10+numEditTexts));
        delButton.setOnClickListener(onDeleteClickListener);
        delButton.setWidth(10);

        scrollLayout.addView(delButton);
        RelativeLayout.LayoutParams delButtonParams = (RelativeLayout.LayoutParams) delButton.getLayoutParams();
        delButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, numEditTexts);
        delButtonParams.addRule(RelativeLayout.ALIGN_TOP, numEditTexts);
        delButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        // Adjust position for the done button
        RelativeLayout.LayoutParams doneButtonParams = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
        doneButtonParams.addRule(RelativeLayout.BELOW, numEditTexts);

        // Adjust position for the new button
        RelativeLayout.LayoutParams newButtonParams = (RelativeLayout.LayoutParams) newButton.getLayoutParams();
        newButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, numEditTexts);
        newButtonParams.addRule(RelativeLayout.LEFT_OF, 1);

        // Caps number of players
        if (numEditTexts == maxPlayers) {
            newButton.setVisibility(View.INVISIBLE);
        }

        // Makes the first delete button not visible
        (findViewById(11+0)).setVisibility(View.GONE);
    }

    public void getNames() {
        players = new Player [numEditTexts];
//        EditText var1 = (EditText) findViewById(R.id.name1);
//
//        the = var1.getText()+"";
//        if ((var1.getText()).equals("")) {
//            players[0] = new Player("Player 1");
//        }
//        else {
//            players[0] = new Player(shortenName(var1.getText()+""));
//        }
        for (int i=0;i<players.length;i++) {
            Log.d("TESTING",i+"");
            EditText var = (EditText) findViewById(i+1);
            if ((var.getText()+"").equals("")) {
                players[i] = new Player(var.getHint()+"");
            }
            else {
                players[i] = new Player(shortenName(var.getText()+""));
            }
        }
    }
    String shortenName(String name) {
        if (name.length()>maxNameLength) {
            return name.substring(0,maxNameLength);
        }
        return name;
    }
}
