package com.cpen321.ubclocationbroadcaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SortedActivityList extends AppCompatActivity {

    //Spinner that will display the suggest Activity IDs
    private Spinner activitySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorted_list);
        activitySpinner = findViewById(R.id.activitiesSpinner);

        final String [] activities = new String[SortedListClass.aids.length + 1];
        activities[0] = "Suggested Ranked Activities";
        for(int i=0;i<SortedListClass.aids.length;i++){
            activities[i+1] = SortedListClass.aids[i];
            Log.d("SortedList aids","Number: " + i + " : " + SortedListClass.aids[i] + "\n");
        }

        final RequestQueue q = Volley.newRequestQueue(this);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SortedActivityList.this,
                android.R.layout.simple_list_item_1, activities);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(myAdapter);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(position>0){
                    String result = activitySpinner.getSelectedItem().toString();
                    SortedListClass.activity_to_be_displayed = new String();
                    SortedListClass.activity_to_be_displayed = result;
                    Log.d("SortedActivityList","Selected activity aid: " + SortedListClass.activity_to_be_displayed);
                    Intent transition = new Intent(SortedActivityList.this, DisplayActivityDetails.class);
                    startActivity(transition);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}