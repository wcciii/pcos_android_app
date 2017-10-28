package com.pcos.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class HomePage extends AppCompatActivity {

    private TextView mTextMessage;
    private String email;
    private String password;
    private String patientID;
    private String verifyCode;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.personal_detail:

                    return true;
                case R.id.questionnaire:
                    Intent intent2 = new Intent(HomePage.this, Questionnaire.class);
                    intent2.putExtra("email", email);
                    intent2.putExtra("password", password);
                    intent2.putExtra("patientID", patientID);
                    intent2.putExtra("mobileCode", verifyCode);
                    startActivity(intent2);
                    finish();
                    return true;
                    //break;
                case R.id.notification:
                    Intent intent3 = new Intent(HomePage.this, Notification.class);
                    intent3.putExtra("email", email);
                    intent3.putExtra("password", password);
                    intent3.putExtra("patientID", patientID);
                    intent3.putExtra("mobileCode", verifyCode);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.appointment:
                    Intent intent4 = new Intent(HomePage.this, Appointment.class);
                    intent4.putExtra("email", email);
                    intent4.putExtra("password", password);
                    intent4.putExtra("patientID", patientID);
                    intent4.putExtra("mobileCode", verifyCode);
                    startActivity(intent4);
                    finish();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        verifyCode = getIntent().getStringExtra("mobileCode");


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        String url = "http://10.0.0.2:9000/pcos/api/verify";
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("mobileVerification", verifyCode);
            jobj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String output = jobj.toString();
        LoadTask loadTask = new LoadTask(url, output);
        loadTask.execute((Void) null);
    }

    class LoadTask extends AsyncTask<Void, Void, Boolean> {

        private Exception exception;
        private String url;
        private String output;
        private String result;

        private LoadTask (String url, String output) {
            this.url = url;
            this.output = output;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject jPost = new JSONObject();
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            try {
                jPost.put("email", email);
                jPost.put("deviceToken", refreshedToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DoPost devicePost = new DoPost("http://10.0.0.2:3000/senddevicetoken", jPost.toString());
            devicePost.sendPost();

            DoPost doPost = new DoPost(url, output);
            result = doPost.sendPost();

            return true;
        }

        protected void onPostExecute(final Boolean success) {
            Log.d("result", result);

            JSONObject jobj = null;
            JSONObject profile = null;

            try {
                jobj = new JSONObject(result);
                profile = (JSONObject) jobj.get("profile");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                patientID = Integer.toString((Integer) profile.get("patientId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String textContent = "";

            TextView textEmail = (TextView) findViewById(R.id.email);
            //textEmail.setText("Email: " + email);
            textContent = textContent + "Email: " + email + "\n";
            try {
                textContent = textContent + "\n" + "Center: " + profile.get("centerName") + "\n";
                textContent = textContent + "\n" + "Clinician: " + profile.get("clinicianName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textEmail.setText(textContent);
            textEmail.setTextColor(Color.parseColor("#8E388E"));

            /*TextView textName = (TextView) findViewById(R.id.email);
            textEmail.setText("Email: " + email);
            textEmail.setTextColor(Color.parseColor("#473C8B"));*/

            /*TextView textCenter = (TextView) findViewById(R.id.centerName);
            try {
                textCenter.setText("Center: " + profile.get("centerName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textCenter.setTextColor(Color.parseColor("#5B5B5B"));

            TextView textClinician = (TextView) findViewById(R.id.clinicianName);
            try {
                textClinician.setText("Clinician: " + profile.get("clinicianName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textClinician.setTextColor(Color.parseColor("#5B5B5B"));*/
        }
    }

}
