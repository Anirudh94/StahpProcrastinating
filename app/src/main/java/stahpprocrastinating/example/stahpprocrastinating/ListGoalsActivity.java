package stahpprocrastinating.example.stahpprocrastinating;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

// Add this to the header of your file:


public class ListGoalsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_template);
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayList<Goal> goals = new ArrayList<Goal>();

        // dummy data
        Goal dummyGoal = new Goal("goal1", "date1");
        goals.add(dummyGoal);
        dummyGoal = new Goal("goal2", "date2");
        goals.add(dummyGoal);
        dummyGoal = new Goal("goal3", "date3");
        goals.add(dummyGoal);
        dummyGoal = new Goal("goal4", "date4");
        goals.add(dummyGoal);


        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.goals_info, goals);
        listView.setAdapter(customAdapter);

        Button joinButton = (Button) findViewById(R.id.temp_button);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goNext = new Intent(getBaseContext(), NewGoalActivity.class);
                startActivity(goNext);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomAdapter extends ArrayAdapter<Goal> {

        private ArrayList<Goal> goals;

        private class ViewHolder {
            TextView textView1;
            TextView textView2;
        }

        public CustomAdapter(Context context, int textViewSourceId, ArrayList<Goal> goals) {
            super(context, textViewSourceId, goals);
            this.goals = new ArrayList<Goal>();
            this.goals.addAll(goals);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if(convertView == null){
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.goals_info, null);
                holder = new ViewHolder();
                holder.textView1 = (TextView) convertView.findViewById(R.id.goalName);
                holder.textView2 = (TextView) convertView.findViewById(R.id.dueDate);

                for(int i = 0; i < goals.size();i++){
                    Goal goal = goals.get(i);
                    holder.textView1.setText(goal.getGoal());
                    holder.textView2.setText(goal.getDate());
                }

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }
}
