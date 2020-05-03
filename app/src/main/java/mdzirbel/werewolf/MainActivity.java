package mdzirbel.werewolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static mdzirbel.werewolf.Reference.players;

public class MainActivity extends AppCompatActivity {

    Button doneButton;
    ScrollView scrollView;
    RelativeLayout scrollLayout;
    int numNameFields = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the reference variables for width, height, and smallScreen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Reference.width = displayMetrics.widthPixels;
        Reference.height = displayMetrics.heightPixels;
        Reference.smallScreen = Reference.width <= 550;

        // Initialize buttons
        doneButton = findViewById(R.id.done_button);
        scrollView = findViewById(R.id.scroll_view);
        scrollLayout = findViewById(R.id.scroll_layout);

        // If this is the first round, make name fields
        if (Reference.firstTime) {
            for (int i = 0; i<Defines.NUM_STARTING_PLAYER_FIELDS; i++) {
                addPlayer();
            }
            findViewById(Defines.NAME_FIELD_ID_START).requestFocus();
            Reference.firstTime = false;
        }
        // If this is not the first round, fill in the names from last time
        else {
            for (Player player : players) {
                addPlayer(player.name);
            }
            removeUnnamedPlayerText(); // Removes Player X text if it's the same as hints, so that the hint is there instead
        }

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
        Intent intent; // Defining it here so it doesn't get whiny
        switch (item.getItemId()) {
            case R.id.credits:
                intent = new Intent(MainActivity.this, Credits.class);
                startActivity(intent);
                return true;
            case R.id.rules:
                intent = new Intent(MainActivity.this, Rules.class);
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

            int buttonNum = v.getId() - Defines.DEL_BUTTON_ID_START;

            // Remove the nameField, delButton, and addButton
            scrollLayout.removeView(findViewById(buttonNum+Defines.DEL_BUTTON_ID_START));
            scrollLayout.removeView(findViewById(buttonNum+Defines.ADD_BUTTON_ID_START));
            scrollLayout.removeView(findViewById(buttonNum+Defines.NAME_FIELD_ID_START));

            numNameFields--;

            // Adjust the ids down to avoid gaps
            for (int i=buttonNum+1; i<=numNameFields; i++) { // Start past the deleted one and move down

                // Get relevant views and shift their ids down
                findViewById(i+Defines.DEL_BUTTON_ID_START).setId(i+Defines.DEL_BUTTON_ID_START -1);
                findViewById(i+Defines.ADD_BUTTON_ID_START).setId(i+Defines.ADD_BUTTON_ID_START -1);
                findViewById(i+Defines.NAME_FIELD_ID_START).setId(i+Defines.NAME_FIELD_ID_START -1);

            }

            realign(); // Fix formatting, hints, and done button

            if (numNameFields == 1) { // Don't let people remove the last player entry field
                findViewById(Defines.DEL_BUTTON_ID_START).setClickable(false); // Make non-clickable
                findViewById(Defines.DEL_BUTTON_ID_START).setAlpha(.4f); // Gray it out
            }

        }
    };

    // For clicking the plus button
    private OnClickListener onAddClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            int place = v.getId() - Defines.ADD_BUTTON_ID_START + 1;

            addPlayer(place);

            // In case we just went from 1 to 2, make sure you can delete fields again
            findViewById(Defines.DEL_BUTTON_ID_START).setClickable(true);
            findViewById(Defines.DEL_BUTTON_ID_START).setAlpha(1); // Un-Gray it out

        }
    };

    public void doneButton(View v) {
        getNames();
        Intent intent = new Intent(MainActivity.this, RoleSelect.class);
        startActivity(intent);
    }

    public void addPlayer() {addPlayer("", numNameFields);}
    public void addPlayer(int place) {addPlayer("", place);}
    public void addPlayer(String name) {addPlayer(name, numNameFields);}
    public void addPlayer(String name, int place) {

        // Shift later views' ids so there are no conflicts
        shiftIdsUp(place);

        // Add the buttons
        addDeleteButton(place);
        addAddButton(place);
        addNameField(name, place);

        numNameFields++;

        realign(); // Fix formatting, hints, and done button

    }

    public void shiftIdsUp(int place) {
        // Adjust the ids so that there aren't gaps and set the alignment so they stay correctly positioned as they move up
        for (int i=place; i<numNameFields; i++) {

            // Get relevant views
            Button delButton = findViewById(i+Defines.DEL_BUTTON_ID_START);
            Button addButton = findViewById(i+Defines.ADD_BUTTON_ID_START);
            EditText nameField = findViewById(i+Defines.NAME_FIELD_ID_START);

            // Change ids
            delButton.setId(i+Defines.DEL_BUTTON_ID_START +1);
            addButton.setId(i+Defines.ADD_BUTTON_ID_START +1);
            nameField.setId(i+Defines.NAME_FIELD_ID_START +1);

        }

    }

    public void addNameField(String name, int place) {

        // Make the new name field
        EditText newNameField = new EditText(this);
        newNameField.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        newNameField.setText(name);
        newNameField.setMaxLines(1);
        newNameField.setSingleLine(true);
        newNameField.setId(place + Defines.NAME_FIELD_ID_START);
        newNameField.requestFocus();

        // Add the name field to the xml
        scrollLayout.addView(newNameField);

    }

    public void addDeleteButton(int place) {

        // Make the idea of the button
        Button delButton = new Button(this);
        delButton.setLayoutParams(new RelativeLayout.LayoutParams(Reference.width/9, RelativeLayout.LayoutParams.WRAP_CONTENT));
        delButton.setText("-");
        delButton.setId(Defines.DEL_BUTTON_ID_START + place);
        delButton.setOnClickListener(onDeleteClickListener);

        // Add the button to the xml
        scrollLayout.addView(delButton);

    }

    public void addAddButton(int place) {

        // Make the idea of the button
        Button addButton = new Button(this);
        addButton.setLayoutParams(new RelativeLayout.LayoutParams(Reference.width/9, RelativeLayout.LayoutParams.WRAP_CONTENT));
        addButton.setText("+");
        addButton.setId(Defines.ADD_BUTTON_ID_START + place);
        addButton.setOnClickListener(onAddClickListener);

        // Add the button to the xml
        scrollLayout.addView(addButton);

    }

    public void removeUnnamedPlayerText() {
        // Checks all name fields. If they have the same text as their hint; eg. they are default names, removes the text, leaving the hint
        for (int i = 0; i < numNameFields; i++) { // ids index from zero; numNameFields should have already been changed
            TextView nameField = findViewById(i + Defines.NAME_FIELD_ID_START);

            if (nameField.getText().equals(nameField.getHint())) {
                nameField.setText("");
            }
        }
    }

    public void realign() {
        alignPositions();
        alignHints();
        updateDoneButton();
    }

    // Ensures all positions are correct
    public void alignPositions() {

        for (int i = 0; i < numNameFields; i++) { // ids index from zero; numNameFields should have already been changed

            // Get layout params for the relevant views
            RelativeLayout.LayoutParams delButtonParams = (RelativeLayout.LayoutParams) findViewById(i + Defines.DEL_BUTTON_ID_START).getLayoutParams();
            RelativeLayout.LayoutParams addButtonParams = (RelativeLayout.LayoutParams) findViewById(i + Defines.ADD_BUTTON_ID_START).getLayoutParams();
            RelativeLayout.LayoutParams nameFieldParams = (RelativeLayout.LayoutParams) findViewById(i + Defines.NAME_FIELD_ID_START).getLayoutParams();

            // Set the name field below the previous one
            nameFieldParams.addRule(RelativeLayout.BELOW, i + Defines.NAME_FIELD_ID_START - 1);

            // Set the layout of the del and add button so they line up with the name field
            delButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, Defines.NAME_FIELD_ID_START + i);
            delButtonParams.addRule(RelativeLayout.ALIGN_TOP, Defines.NAME_FIELD_ID_START + i);
            addButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, Defines.NAME_FIELD_ID_START + i);
            addButtonParams.addRule(RelativeLayout.ALIGN_TOP, Defines.NAME_FIELD_ID_START + i);

            // Set alignment so everything is correct horizontally
            delButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            addButtonParams.addRule(RelativeLayout.LEFT_OF, i+Defines.DEL_BUTTON_ID_START);
            nameFieldParams.addRule(RelativeLayout.LEFT_OF, i+Defines.ADD_BUTTON_ID_START);
            nameFieldParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            nameFieldParams.leftMargin = 10;

        }

        // Set the next button under the lowest nameField
        RelativeLayout.LayoutParams doneButtonParams = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
        doneButtonParams.addRule(RelativeLayout.BELOW, numNameFields + Defines.NAME_FIELD_ID_START - 1);

    }

    // Set the hints to align with the positions of the names
    public void alignHints() {
        for (int i = 0; i < numNameFields; i++) {
            EditText nameField = findViewById(i + Defines.NAME_FIELD_ID_START);
            String hint = "Player " + (i+1);
            nameField.setHint(hint);

            // If the current text and the hint are the same, remove the text
            String currentText = nameField.getText().toString();
            if (hint.equals(currentText)) {
                nameField.setText("");
            }
        }
    }

    public void updateDoneButton() {
        String doneText = getString(R.string.done) + " (" + numNameFields + " " + getString(R.string.players) + ")";
        doneButton.setText(doneText);
    }

    public void getNames() {
        players = new Player [numNameFields];

        for (int i=0;i<numNameFields;i++) {
            EditText var = findViewById(i + Defines.NAME_FIELD_ID_START);
            if ((var.getText()+"").equals("")) {
                players[i] = new Player(var.getHint()+"");
            }
            else {
                players[i] = new Player(shortenName(var.getText()+""));
            }
        }
    }
    String shortenName(String name) {
        if (name.length()>Defines.MAX_NAME_LENGTH) {
            return name.substring(0,Defines.MAX_NAME_LENGTH);
        }
        return name;
    }
}
