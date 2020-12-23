package com.example.grybas.velykom_geras;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingsScreen extends AppCompatActivity {

    int maxRange;

    public static Intent makeIntent(Context context){
        return new Intent(context, SettingsScreen.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        final EditText skaicius = (EditText) findViewById(R.id.skaicius);
        Button next = (Button) findViewById(R.id.next);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxRange = Integer.valueOf(skaicius.getText().toString());

                saveNum(maxRange);
                Intent intent2 = new Intent(SettingsScreen.this, MainActivity.class);
                startActivity(intent2);
            }
        });

        int savedValue  = getNum(this);
        Toast.makeText(this,"Saved: " + savedValue,Toast.LENGTH_SHORT).show();
    }

    private void saveNum(int maxRange) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Reiksme",maxRange);
        editor.apply();
    }

    static public int getNum(Context context){
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs",MODE_PRIVATE);
        return prefs.getInt("Reiksme",48);
    }

}
