package uncontrollablyswervingdog.werewolf;

import android.annotation.SuppressLint;
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

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    ScrollView scrollView;
    RelativeLayout scrollLayout;
    Button doneButton;
    static Player [] players;
    static boolean firstTime = true;
    int maxNameLength = 20;
    int numNameFields = 0;
    int numStartingPlayerFields = 4;
    static boolean smallScreen;
    int width;

    // Ids for the various items start at the below numbers, index from 0 from there, and should be the same for the same height
    final int nameFieldIdStart = 100; // The starting point in id for the name fields
    final int delButtonIdStart = 200; // The starting point in id for the delete button
    final int addButtonIdStart = 300; // The starting point in id for the add button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doneButton = findViewById(R.id.done_button);
        scrollView = findViewById(R.id.scroll_view);
        scrollLayout = findViewById(R.id.scroll_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        if (width <= 550) {
            smallScreen = true;
            scaleForSmallScreen();
        }

        // If this is the first round, make name fields
        if (firstTime) {
            for (int i=0; i<numStartingPlayerFields; i++) {
                addPlayer();
            }
            findViewById(nameFieldIdStart).requestFocus();
            firstTime = false;
        }
        // If this is not the first round, fill in the names from last time
        else {
            for (Player player : players) {
                if (player.name.startsWith("Player ")) {
                    addPlayer();
                } else {
                    addPlayer(player.name);
                }
            }
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

            int buttonNum = v.getId() - delButtonIdStart;

            // Remove the nameField, delButton, and addButton
            scrollLayout.removeView(findViewById(buttonNum+delButtonIdStart));
            scrollLayout.removeView(findViewById(buttonNum+addButtonIdStart));
            scrollLayout.removeView(findViewById(buttonNum+nameFieldIdStart));

            numNameFields--;

            // Adjust the ids down to avoid gaps
            for (int i=buttonNum+1; i<=numNameFields; i++) { // Start at below the deleted one and move down

                // Get relevant views
                Button delButton = findViewById(i+delButtonIdStart);
                Button addButton = findViewById(i+addButtonIdStart);
                EditText nameField = findViewById(i+nameFieldIdStart);

                // Change ids
                delButton.setId(i+delButtonIdStart-1);
                addButton.setId(i+addButtonIdStart-1);
                nameField.setId(i+nameFieldIdStart-1);

            }

            realign();

            updateDoneButton();

        }
    };

    // For clicking the plus button
    private OnClickListener onAddClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            int place = v.getId() - addButtonIdStart + 1;

            addPlayer(place);

        }
    };

    public void doneButton(View v) {
        getNames();
        Intent intent = new Intent(MainActivity.this, CharacterSelect.class);
        startActivity(intent);
    }

    public void addPlayer() {addPlayer("", numNameFields);}
    public void addPlayer(int place) {addPlayer("", place);}
    public void addPlayer(String name) {addPlayer(name, numNameFields);}
    public void addPlayer(String name, int place) {

        shiftIdsUp(place);

        // Add the buttons, order is important here
        addDeleteButton(place);
        addAddButton(place);
        addNameField(name, place);

        numNameFields++;

        realign();

        updateDoneButton();

    }

    public void shiftIdsUp(int place) {
        // Adjust the ids so that there aren't gaps and set the alignment so they stay correctly positioned as they move up
        for (int i=place; i<numNameFields; i++) {

            // Get relevant views
            Button delButton = findViewById(i+delButtonIdStart);
            Button addButton = findViewById(i+addButtonIdStart);
            EditText nameField = findViewById(i+nameFieldIdStart);

            // Change ids
            delButton.setId(i+delButtonIdStart+1);
            addButton.setId(i+addButtonIdStart+1);
            nameField.setId(i+nameFieldIdStart+1);

        }

    }

    public void addNameField(String name, int place) {

        // Make the new name field
        EditText newNameField = new EditText(this);
        newNameField.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        newNameField.setHint("Player " + (place + 1));
        newNameField.setText(name);
        newNameField.setMaxLines(1);
        newNameField.setSingleLine(true);
        newNameField.setId(place + nameFieldIdStart);
        newNameField.requestFocus();

        // Add the name field to the xml
        scrollLayout.addView(newNameField);

        // Adjust position for the new textEdit
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newNameField.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.LEFT_OF, place + addButtonIdStart);
        params.leftMargin = 10;

    }

    public void addDeleteButton(int place) {

        // Make the idea of the button
        Button delButton = new Button(this);
        delButton.setLayoutParams(new RelativeLayout.LayoutParams(width/9, RelativeLayout.LayoutParams.WRAP_CONTENT));
        delButton.setText("-");
        delButton.setId((delButtonIdStart + place));
        delButton.setOnClickListener(onDeleteClickListener);

        // Add the button to the xml
        scrollLayout.addView(delButton);

        // Adjust positioning
        RelativeLayout.LayoutParams delButtonParams = (RelativeLayout.LayoutParams) delButton.getLayoutParams();
        delButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    public void addAddButton(int place) {

        // Make the idea of the button
        Button addButton = new Button(this);
        addButton.setLayoutParams(new RelativeLayout.LayoutParams(width/9, RelativeLayout.LayoutParams.WRAP_CONTENT));
        addButton.setText("+");
        addButton.setId((addButtonIdStart + place));

        // Add the button to the xml
        scrollLayout.addView(addButton);
        // Adjust positioning
        RelativeLayout.LayoutParams addButtonParams = (RelativeLayout.LayoutParams) addButton.getLayoutParams();
        addButtonParams.addRule(RelativeLayout.LEFT_OF, delButtonIdStart + place);

        // Make the button add another line
        addButton.setOnClickListener(onAddClickListener);
    }

    public void realign() {
        alignHeights();
        alignHints();
    }

    public void alignHeights() {

        for (int i = 0; i < numNameFields; i++) { // ids index from zero and numNameFields should have already been changed

            // Get relevant views
            Button delButton = findViewById(i + delButtonIdStart);
            Button addButton = findViewById(i + addButtonIdStart);
            EditText nameField = findViewById(i + nameFieldIdStart);

            // Get layout params for the views
            RelativeLayout.LayoutParams delButtonParams = (RelativeLayout.LayoutParams) delButton.getLayoutParams();
            RelativeLayout.LayoutParams addButtonParams = (RelativeLayout.LayoutParams) addButton.getLayoutParams();
            RelativeLayout.LayoutParams nameFieldParams = (RelativeLayout.LayoutParams) nameField.getLayoutParams();

            // Set the layout so they are below
            nameFieldParams.addRule(RelativeLayout.BELOW, i + nameFieldIdStart - 1);

            // Set the layout of the del and add button so they line up with the name field
            delButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, nameFieldIdStart + i);
            delButtonParams.addRule(RelativeLayout.ALIGN_TOP, nameFieldIdStart + i);
            addButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, nameFieldIdStart + i);
            addButtonParams.addRule(RelativeLayout.ALIGN_TOP, nameFieldIdStart + i);

            // Set alignment so everything is correct horizontally
            addButtonParams.addRule(RelativeLayout.LEFT_OF, i+delButtonIdStart);
            nameFieldParams.addRule(RelativeLayout.LEFT_OF, i+addButtonIdStart);

        }

        // Set the next button under the lowest nameField
        RelativeLayout.LayoutParams doneButtonParams = (RelativeLayout.LayoutParams) doneButton.getLayoutParams();
        doneButtonParams.addRule(RelativeLayout.BELOW, numNameFields + nameFieldIdStart - 1);

    }

    // Set the hints to align with the positions of the names
    public void alignHints() {
        for (int i = 0; i < numNameFields; i++) {
            EditText nameField = findViewById(i + nameFieldIdStart);
            nameField.setHint("Player " + (i+1));
        }
    }

    public void updateDoneButton() {
        doneButton.setText("Done ("+numNameFields+" players)");
    }

    public void getNames() {
        players = new Player [numNameFields];

        for (int i=0;i<numNameFields;i++) {
            EditText var = findViewById(i + nameFieldIdStart);
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
