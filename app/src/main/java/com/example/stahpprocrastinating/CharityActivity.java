package com.example.chen.stahpprocrastinating;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;


public class CharityActivity extends ActionBarActivity {

    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goals, menu);

        displayListView();

        checkButtonClick();

        Button goNextButton = (Button) findViewById(R.id.findSelected);

        goNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goNext = new Intent(getBaseContext(), MainActivity.class);
                startActivity(goNext);
            }
        });
        return true;
    }

    private void displayListView() {

        ArrayList<Charity> countryList = new ArrayList<Charity>();
        Charity charity = new Charity("Poverty",false);
        countryList.add(charity);
        charity = new Charity("Children",false);
        countryList.add(charity);
        charity = new Charity("Healthcare",false);
        countryList.add(charity);
        charity = new Charity("Developing Countries",false);
        countryList.add(charity);

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.charity_info, countryList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Charity charity = (Charity) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + charity.getName(),
//                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<Charity> {

        private ArrayList<Charity> charityList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Charity> charityList) {
            super(context, textViewResourceId, charityList);
            this.charityList = new ArrayList<Charity>();
            this.charityList.addAll(charityList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.charity_info, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Charity charity = (Charity) cb.getTag();
                        charity.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Charity charity = charityList.get(position);
            holder.name.setText(charity.getName());
            holder.name.setChecked(charity.isSelected());
            holder.name.setTag(charity);

            return convertView;

        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.findSelected);
        System.out.println("clicked");
//        myButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
//
//                ArrayList<Charity> charityList = dataAdapter.charityList;
//                for(int i=0; i < charityList.size();i++){
//                    Charity charity = charityList.get(i);
//                    if(charity.isSelected()){
//                        responseText.append("\n" + charity.getName());
//                    }
//                }
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
