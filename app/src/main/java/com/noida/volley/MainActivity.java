package com.noida.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.noida.volley.app.Constants;
import com.noida.volley.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText Email,Pass,Name,School ;
    private Button b1;
    private ProgressDialog progressDialog;
    private static final String AUTH = "Basic " + Base64.encodeToString(("user:123456").getBytes(), Base64.NO_WRAP);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = findViewById(R.id.editTextEmail);
        Pass = findViewById(R.id.editTextPassword);
        Name = findViewById(R.id.editTextName);
        School = findViewById(R.id.editTextSchool);
        progressDialog = new ProgressDialog(this);

        b1 = findViewById(R.id.buttonSignUp);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    public void register(){
        final String email = Email.getText().toString().trim();
        final String password =Pass.getText().toString().trim();
        final String name =Name.getText().toString().trim();
        final String gender =School.getText().toString().trim();
        final NameValuePair nameValuePair = new NameValuePair(email,password,name,gender);
        Log.d("email",email);
        progressDialog.setMessage("loding");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                   progressDialog.dismiss();
                        try{
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                })
        {



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                List<NameValuePair> p= new ArrayList<>();
                p.add(nameValuePair);
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("password", password);
                params.put("gender", gender);

                return params;
            }

            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", AUTH);
                return headers;
            }


        };



        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}

