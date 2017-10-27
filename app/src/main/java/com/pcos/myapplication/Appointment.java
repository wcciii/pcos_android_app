package com.pcos.myapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.PopupMenu;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@TargetApi(Build.VERSION_CODES.N)
public class Appointment extends AppCompatActivity {

    private TextView mTextMessage;
    private String email;
    private String password;
    private String patientID;
    private String verifyCode;
    private String timeSlot = null;

    static final int DATE_DIALOG = 2;

    final Calendar ca = Calendar.getInstance();
    int mYear = ca.get(Calendar.YEAR);
    int mMonth = ca.get(Calendar.MONTH);
    int mDay = ca.get(Calendar.DAY_OF_MONTH);

    EditText consultation_et_birthdate;
    ImageView consultation_iv_birthdate;

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            consultation_et_birthdate.setGravity(Gravity.CENTER_HORIZONTAL);
            consultation_et_birthdate.setText(new
                    StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.personal_detail:
                    Intent intent1 = new Intent(Appointment.this, HomePage.class);
                    intent1.putExtra("email", email);
                    intent1.putExtra("password", password);
                    intent1.putExtra("patientID", patientID);
                    intent1.putExtra("mobileCode", verifyCode);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.questionnaire:
                    Intent intent2 = new Intent(Appointment.this, Questionnaire.class);
                    intent2.putExtra("email", email);
                    intent2.putExtra("password", password);
                    intent2.putExtra("patientID", patientID);
                    intent2.putExtra("mobileCode", verifyCode);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.notification:
                    Intent intent3 = new Intent(Appointment.this, Notification.class);
                    intent3.putExtra("email", email);
                    intent3.putExtra("password", password);
                    intent3.putExtra("patientID", patientID);
                    intent3.putExtra("mobileCode", verifyCode);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.appointment:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        patientID = getIntent().getStringExtra("patientID");
        verifyCode = getIntent().getStringExtra("mobileCode");

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        consultation_et_birthdate = (EditText) findViewById(R.id.consultation_et_birthdate);
        consultation_iv_birthdate = (ImageView) findViewById(R.id.consultation_iv_birthdate);

        consultation_iv_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });

        Button time = (Button) findViewById(R.id.time);

        Button make = (Button) findViewById(R.id.makeApp);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://10.0.0.2:3000/appointment";
                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("patient", patientID);
                    jobj.put("clinId", "4");
                    jobj.put("date", new StringBuffer().append(mYear).append("-")
                                        .append(mMonth + 1).append("-").append(mDay));
                    jobj.put("timeSlot", timeSlot);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Jsonobj", jobj.toString());
                String output = jobj.toString();
                Appointment.LoadTask loadTask = new Appointment.LoadTask(url, output);
                loadTask.execute((Void) null);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    protected void popupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mornFirst:
                        Toast.makeText(getApplicationContext(), "9:00 - 10:30", Toast.LENGTH_LONG).show();
                        timeSlot = "MornFirst";
                        return true;
                    case R.id.mornSecond:
                        Toast.makeText(getApplicationContext(), "10:30 - 12:00", Toast.LENGTH_LONG).show();
                        timeSlot = "MornSecond";
                        return true;
                    case R.id.noonFirst:
                        Toast.makeText(getApplicationContext(), "14:00 - 15:30", Toast.LENGTH_LONG).show();
                        timeSlot = "NoonFirst";
                        return true;
                    case R.id.noonSecond:
                        Toast.makeText(getApplicationContext(), "15:30 - 17:00", Toast.LENGTH_LONG).show();
                        timeSlot = "NoonSecond";
                        return true;
                    default:
                        return false;
                }
            }
        });
        inflater.inflate(R.menu.popummenu, popupMenu.getMenu());
        popupMenu.show();
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
            return true;
        }

        protected void onPostExecute(final Boolean success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Appointment.this);

            JSONObject jResult = null;
            try {
                jResult = new JSONObject(result);
                result = (String) jResult.get("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(result.equals("true"))
                builder.setTitle("Successful")
                        .setMessage("Your appointment is made successfully")
                        .setNeutralButton("OK", null);
            else
                builder.setTitle("Unfortunately")
                        .setMessage("This section is reserved, please choose another time")
                        .setNeutralButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}

