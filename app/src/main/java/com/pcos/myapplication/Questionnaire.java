package com.pcos.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Questionnaire extends AppCompatActivity {

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
                    Intent intent1 = new Intent(Questionnaire.this, HomePage.class);
                    intent1.putExtra("email", email);
                    intent1.putExtra("password", password);
                    intent1.putExtra("patientID", patientID);
                    intent1.putExtra("mobileCode", verifyCode);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.questionnaire:

                    return true;
                case R.id.notification:
                    Intent intent3 = new Intent(Questionnaire.this, Notification.class);
                    intent3.putExtra("email", email);
                    intent3.putExtra("password", password);
                    intent3.putExtra("patientID", patientID);
                    intent3.putExtra("mobileCode", verifyCode);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.appointment:
                    Intent intent4 = new Intent(Questionnaire.this, Appointment.class);
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
        setContentView(R.layout.activity_questionnaire);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        patientID = getIntent().getStringExtra("patientID");
        verifyCode = getIntent().getStringExtra("mobileCode");

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        Button findQues = (Button) findViewById(R.id.find);
        findQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Questionnaire.this, FindQuestionnaire.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("patientID", patientID);
                startActivity(intent);
            }
        });

        Button addQues = (Button) findViewById(R.id.add);
        addQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Questionnaire.this, AddQuestionnaire.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("patientID", patientID);
                startActivity(intent);
            }
        });

    }

}
