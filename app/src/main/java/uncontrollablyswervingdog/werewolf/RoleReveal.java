package uncontrollablyswervingdog.werewolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        View view = findViewById(R.id.roleReveal);

        names = new TextView(this);
        names.setId(0);
        names.setTextSize(35);
        names.setGravity(Gravity.LEFT);
        names.setLineSpacing(40f, 1f);
        String playerNames = "";
        for (int i=0; i<MainActivity.players.length;i++) {
            playerNames += MainActivity.players[i].name+"\n";
        }
        names.setText(playerNames);
        ((RelativeLayout) view).addView(names);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) names.getLayoutParams();
        params.topMargin = 60;
        params.leftMargin = 60;

        roles = new TextView(this);
        roles.setId(1+0);
        roles.setTextSize(35);
        roles.setGravity(Gravity.RIGHT);
        roles.setLineSpacing(40f, 1f);
        String roleNames = "";
        for (int i=0; i<MainActivity.players.length;i++) {
            roleNames += MainActivity.players[i].role+"\n";
        }
        roles.setText(roleNames);
        ((RelativeLayout) view).addView(roles);
        params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = 60;
        params.rightMargin = 60;
    }

    void showUnusedRoles() {
        View view = findViewById(R.id.roleReveal);

        unusedRoles = new TextView(this);
        unusedRoles.setId(10+0);
        unusedRoles.setTextSize(25);
        unusedRoles.setGravity(Gravity.CENTER);
        String unused = "";
        for (String role : CharacterSelect.unusedRoles) {
            unused += role+" ";
        }
        unusedRoles.setText(unused);
        ((RelativeLayout) view).addView(unusedRoles);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unusedRoles.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.restartButton);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }

    void restart(View v) {
        Intent intent = new Intent(RoleReveal.this, MainActivity.class);
        startActivity(intent);
    }
}

