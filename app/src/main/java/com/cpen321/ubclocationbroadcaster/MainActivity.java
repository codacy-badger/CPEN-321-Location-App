package com.cpen321.ubclocationbroadcaster;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button sign_in_btn;
    private Button sign_up_btn;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username_button);
        password = findViewById(R.id.password_button);
        sign_in_btn = findViewById(R.id.sign_in_button);
        sign_up_btn = findViewById(R.id.sign_up_button);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent sign_in_Intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(sign_in_Intent);
                Log.d("sign in button", "sign in button has been clicked");
                //TODO: use mySkeleton if it works for volley request
                //TODO: insert the server url
                String URL = "http://10.0.2.2:3000/users/login";
                String usrname = username.getText().toString();
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
                                Intent sign_in_Intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(sign_in_Intent);
                                try {
                                    boolean successVal = (boolean) response.get("success"); // check if user signed in successfully
                                    String stat = response.get("status").toString(); // get status
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

                requestQueue.add(json_obj);
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