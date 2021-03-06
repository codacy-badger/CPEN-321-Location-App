package com.cpen321.ubclocationbroadcaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {

    private Button sign_in_btn;
    private Button sign_up_btn;
    private EditText username;
    private EditText password;
    //final SharedPreferences userSettings = getSharedPreferences("UserPreferences", MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            return;
                        }

                        String token = task.getResult().getToken();
                        String msg = getString(R.string.fcm_token, token);
                        Log.d("MainActivity", msg);
                    }
                });

        username = findViewById(R.id.username_button);
        password = findViewById(R.id.password_button);
        sign_in_btn = findViewById(R.id.sign_in_button);
        sign_up_btn = findViewById(R.id.sign_up_button);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final RequestQueue q = Volley.newRequestQueue(this);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent sign_in_Intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(sign_in_Intent);
                Log.d("sign in button", "sign in button has been clicked");

                //String URL = "http://40.122.147.73:3030/users/login";
                String URL = "http://10.0.2.2:3000/users/login";
                final String usrname = username.getText().toString();
                String passwrd = password.getText().toString();

                //format request
                JSONObject jsnRequest = new JSONObject();
                try {
                    jsnRequest.put("name", usrname);
                    jsnRequest.put("password", passwrd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest json_obj = new JsonObjectRequest(Request.Method.POST, URL, jsnRequest,
                        new Response.Listener<JSONObject> (){
                            @Override
                            public void onResponse(JSONObject response){
                                try {
                                    boolean successVal = (boolean) response.get("success"); // check if user signed in successfully
                                    String stat = response.get("status").toString(); // get status
                                    if(successVal) {

                                        final JsonArrayRequest userDetails = new JsonArrayRequest("http://10.0.2.2:3000/profiles/all",
                                                new Response.Listener<JSONArray> (){
                                                    @Override
                                                    public void onResponse(JSONArray response){
                                                        try {
                                                            //pos is the index of our user in userDB
                                                            int pos = -1;
                                                            for(int i=0; i<response.length();i++){
                                                                JSONObject jsonObject = response.getJSONObject(i);
                                                                if(jsonObject.getString("username").toString().equals(usrname)){
                                                                    pos = i;
                                                                    Log.d("poschange", "onResponse: " + pos);
                                                                    break;
                                                                }
                                                            }
                                                            //jsonObject is the user object
                                                            JSONObject jsonObject = response.getJSONObject(pos);
                                                            int numOfCourses = jsonObject.getJSONArray("CourseRegistered").length();
                                                            UserDetails.name = jsonObject.getString("name");
                                                            UserDetails.phone = jsonObject.getString("phone");
                                                            UserDetails.school = jsonObject.getString("school");
                                                            UserDetails.major = jsonObject.getString("major");
                                                            UserDetails.privatePublic = jsonObject.getString("private");
                                                            UserDetails.inactivity = jsonObject.getString("inActivity");
                                                            UserDetails.activityID = jsonObject.getString("activityID");
                                                            UserDetails.courseRegistered = new String[numOfCourses];
                                                            for(int i=0;i<numOfCourses;i++){
                                                                UserDetails.courseRegistered[i] = jsonObject.getJSONArray("CourseRegistered").getString(i);
                                                            }

                                                        } catch (JSONException e) {
                                                            Toast.makeText(MainActivity.this, "Unable to send the sign in data to the server!", Toast.LENGTH_SHORT).show();
                                                            Log.d("error2", "---------------------------------------");
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                            }
                                        });
                                        q.add(userDetails);

                                        Intent sign_in_Intent = new Intent(MainActivity.this, MenuActivity.class);
                                        sign_in_Intent.putExtra("USERNAME", usrname);
                                        startActivity(sign_in_Intent);
                                        UserDetails.username = usrname;
                                    } else {
                                        Toast.makeText(MainActivity.this, "ERROR: " + stat, Toast.LENGTH_SHORT).show();
                                    }
                                    Log.d("SignInActivity", stat);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(MainActivity.this, "Unable to send the sign in data to the server!", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

                MySingleton.getInstance(MainActivity.this).addToRequestQueue(json_obj);
            }
        });


        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_up_Intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(sign_up_Intent);
                Log.d("sign up button", "sign up button has been clicked");
            }
        });
    }
}