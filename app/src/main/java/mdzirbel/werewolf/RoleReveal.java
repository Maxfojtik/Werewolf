package mdzirbel.werewolf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static mdzirbel.werewolf.Reference.players;
import static mdzirbel.werewolf.Reference.unusedRoles;

@SuppressLint({"ResourceType","SetTextI18n"})
public class RoleReveal extends AppCompatActivity {

    TextView names;
    TextView roles;
    TextView unusedRolesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_reveal);
        showRoles();
        showUnusedRoles();
        tryScaleForSmallScreen();
    }

    void tryScaleForSmallScreen() {
        if (Reference.smallScreen) {
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

        unusedRolesText.setTextSize(20);
    }

    void showRoles() {

        names = new TextView(this);
        names.setId(0);
        names.setTextSize(30);
        names.setGravity(Gravity.START);
        names.setLineSpacing(40f, 1f);
        StringBuilder playerNames = new StringBuilder();
        for (Player player : players) {
            playerNames.append(player.name).append("\n");
        }
        names.setText(playerNames.toString());
        ((RelativeLayout) findViewById(R.id.roles_scroll)).addView(names);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) names.getLayoutParams();
        params.topMargin = 60;
        params.leftMargin = 30;

        roles = new TextView(this);
        roles.setId(1);
        roles.setTextSize(30);
        roles.setGravity(Gravity.END);
        roles.setLineSpacing(40f, 1f);
        StringBuilder roleNames = new StringBuilder();
        for (Player player : players) {
            roleNames.append(player.finalRole.getWholeRoleForPrinting()).append("\n");
        }
        roles.setText(roleNames.toString());
        ((RelativeLayout) findViewById(R.id.roles_scroll)).addView(roles);
        params = (RelativeLayout.LayoutParams) roles.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = 60;
        params.rightMargin = 30;
    }

    void showUnusedRoles() {
        View view = findViewById(R.id.roleReveal);

        unusedRolesText = new TextView(this);
        unusedRolesText.setId(10);
        unusedRolesText.setTextSize(25);
        unusedRolesText.setGravity(Gravity.CENTER);
        StringBuilder unused = new StringBuilder();
        for (Role role : unusedRoles.finalUnusedRoles) {
            unused.append(role.getWholeRoleForPrinting()).append(" ");
        }
        unusedRolesText.setText(unused.toString());
        ((RelativeLayout) view).addView(unusedRolesText);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unusedRolesText.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.restartButton);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }

    public void restart(View v) {
        Intent intent = new Intent(RoleReveal.this, MainActivity.class);
        startActivity(intent);
    }

    // Stops the back button
    @Override
    public void onBackPressed() {}

}

