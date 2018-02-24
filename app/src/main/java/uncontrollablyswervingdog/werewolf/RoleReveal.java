package uncontrollablyswervingdog.werewolf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({"ResourceType","SetTextI18n"})
public class RoleReveal extends AppCompatActivity {

    TextView names;
    TextView roles;
    TextView unusedRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_reveal);
        showRoles();
        showUnusedRoles();
        if (MainActivity.smallScreen) {
            scaleForSmallScreen();
        }
    }

    void scaleForSmallScreen() {
        names.setTextSize(25);
        names.setLineSpacing(10f, 1f);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) names.getLayoutParams();
        params.topMargin = 12;
        params.leftMargin = 12;

        roles.setTextSize(25);
        roles.setLineSpacing(10f, 1f);
        params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
        params.topMargin = 12;
        params.rightMargin = 12;

        unusedRoles.setTextSize(20);
    }

    void showRoles() {

        names = new TextView(this);
        names.setId(0);
        names.setTextSize(30);
        names.setGravity(Gravity.START);
        names.setLineSpacing(40f, 1f);
        StringBuilder playerNames = new StringBuilder();
        for (int i=0; i<MainActivity.players.length;i++) {
            playerNames.append(MainActivity.players[i].name).append("\n");
        }
        names.setText(playerNames.toString());
        ((RelativeLayout) findViewById(R.id.roles_scroll)).addView(names);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) names.getLayoutParams();
        params.topMargin = 60;
        params.leftMargin = 60;

        roles = new TextView(this);
        roles.setId(1);
        roles.setTextSize(30);
        roles.setGravity(Gravity.END);
        roles.setLineSpacing(40f, 1f);
        StringBuilder roleNames = new StringBuilder("");
        for (int i=0; i<MainActivity.players.length;i++) {
            roleNames.append(replaceDoppelChar(MainActivity.players[i].finalRole)).append("\n");
        }
        roles.setText(roleNames.toString());
        ((RelativeLayout) findViewById(R.id.roles_scroll)).addView(roles);
        params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = 60;
        params.rightMargin = 60;
    }

    void showUnusedRoles() {
        View view = findViewById(R.id.roleReveal);

        unusedRoles = new TextView(this);
        unusedRoles.setId(10);
        unusedRoles.setTextSize(25);
        unusedRoles.setGravity(Gravity.CENTER);
        StringBuilder unused = new StringBuilder();
        for (String role : CharacterSelect.unusedRoles) {
            unused.append(replaceDoppelChar(role)).append(" ");
        }
        unusedRoles.setText(unused.toString());
        ((RelativeLayout) view).addView(unusedRoles);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unusedRoles.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.restartButton);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }

    public void restart(View v) {
        Intent intent = new Intent(RoleReveal.this, MainActivity.class);
        startActivity(intent);
    }

    static String replaceDoppelChar(String s) {
        if (s.equals("#Doppelganger")) {
            return "Doppelganger";
        }
        return s.replace("#","Doppel-");
    }
}

