package com.pcos.myapplication;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

@TargetApi(Build.VERSION_CODES.N)
public class Main2Activity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d("Refreshed token: ", refreshedToken);


        consultation_et_birthdate = (EditText) findViewById(R.id.consultation_et_birthdate);
        consultation_iv_birthdate = (ImageView) findViewById(R.id.consultation_iv_birthdate);

        consultation_iv_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });

        Button time = (Button) findViewById(R.id.time);
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
                        return true;
                    case R.id.mornSecond:
                        Toast.makeText(getApplicationContext(), "10:30 - 12:00", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.noonFirst:
                        Toast.makeText(getApplicationContext(), "14:00 - 15:30", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.noonSecond:
                        Toast.makeText(getApplicationContext(), "15:30 - 17:00", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        inflater.inflate(R.menu.popummenu, popupMenu.getMenu());
        popupMenu.show();
    }
}
