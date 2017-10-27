package com.pcos.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notification extends AppCompatActivity {

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
                    Intent intent1 = new Intent(Notification.this, HomePage.class);
                    intent1.putExtra("email", email);
                    intent1.putExtra("password", password);
                    intent1.putExtra("patientID", patientID);
                    intent1.putExtra("mobileCode", verifyCode);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.questionnaire:
                    Intent intent2 = new Intent(Notification.this, Questionnaire.class);
                    intent2.putExtra("email", email);
                    intent2.putExtra("password", password);
                    intent2.putExtra("patientID", patientID);
                    intent2.putExtra("mobileCode", verifyCode);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.notification:

                    return true;
                case R.id.appointment:
                    Intent intent4 = new Intent(Notification.this, Appointment.class);
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
        setContentView(R.layout.activity_notification);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        patientID = getIntent().getStringExtra("patientID");
        verifyCode = getIntent().getStringExtra("mobileCode");

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        String url = "http://10.0.0.2:3000/notification";
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("patientID", patientID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String output = jobj.toString();
        Notification.LoadTask loadTask = new Notification.LoadTask(url, output);
        loadTask.execute((Void) null);
    }

    class LoadTask extends AsyncTask<Void, Void, Boolean> {

        private Exception exception;
        private String url;
        private String output;
        private String result = null;

        private LoadTask (String url, String output) {
            this.url = url;
            this.output = output;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DoPost doPost = new DoPost(url, output);
            result = doPost.sendPost();
            Log.d("notification", result);
            return true;
        }

        protected void onPostExecute(final Boolean success) {
            TextView message = (TextView) findViewById(R.id.message);
            message.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL);
            message.setTextColor(Color.parseColor("#97FFFF"));
            message.setMovementMethod(new ScrollingMovementMethod());
            String printResult = "";
            try {
                JSONObject jobj = new JSONObject(result);
                JSONArray jArray = jobj.getJSONArray("message");
                for(int i = 0; i < jArray.length(); i++){
                    JSONArray ja = (JSONArray) jArray.get(i);
                    printResult = printResult + ((String)ja.get(0) + "\n");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            message.setText(printResult);
        }
    }

}
