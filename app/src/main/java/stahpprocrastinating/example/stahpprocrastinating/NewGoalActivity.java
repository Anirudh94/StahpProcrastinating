package stahpprocrastinating.example.stahpprocrastinating;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText goalText = (EditText) findViewById(R.id.goal);
                final String goal = goalText.getText().toString();
                if (goal.equals("")) {
                        Snackbar.make(findViewById(R.id.mainContent), "Please enter a goal", Snackbar.LENGTH_LONG).show();
                    return;
                }

                EditText dateText = (EditText) findViewById(R.id.date);
                final String date = dateText.getText().toString();
                if (date.equals("")) {
                    Snackbar.make(findViewById(R.id.mainContent), "Please enter a date", Snackbar.LENGTH_LONG).show();
                    return;
                }

                EditText timeText = (EditText) findViewById(R.id.time);
                final String time = timeText.getText().toString();
                if (time.equals("")) {
                    Snackbar.make(findViewById(R.id.mainContent), "Please enter a time", Snackbar.LENGTH_LONG).show();
                    return;
                }

                // TODO: add logic for saving to database here; use strings goal, date, time

                Toast.makeText(getBaseContext(), "Goal Added", Toast.LENGTH_LONG).show();

                Intent goNext = new Intent(getBaseContext(), ListGoalsActivity.class);
                startActivity(goNext);
            }
        });
    }


}
