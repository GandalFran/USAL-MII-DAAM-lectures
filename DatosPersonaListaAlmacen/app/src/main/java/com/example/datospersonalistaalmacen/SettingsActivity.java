package com.example.datospersonalistaalmacen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    public final static String SETTINGS_KEY = "SETTINGS_KEY";
    public static final String URL_KEY = "URL_KEY";
    public static final String FORMAT_KEY = "FORMAT_KEY";
    public static final String STORAGE_TYPE_KEY = "STORAGE_TYPE_KEY";
    public static final String COMM_TYPE_KEY = "COMM_TYPE_KEY";

    private SharedPreferences settings = null;

    private TextView urlText;
    private RadioGroup outputFormatRadioGroup;
    private RadioGroup storageTypeRadioGroup;
    private RadioGroup commRadioGroup;

    @Override
    protected void onCreate(Bundle state) {

        super.onCreate(state);
        setContentView(R.layout.activity_settings);

        // load elements
        this.urlText = (TextView) findViewById(R.id.urlText);
        this.outputFormatRadioGroup = (RadioGroup) findViewById(R.id.outputFormatRadioGroup);
        this.storageTypeRadioGroup = (RadioGroup) findViewById(R.id.storageTypeRadioGroup);
        this.commRadioGroup = (RadioGroup) findViewById(R.id.commTypeRadioGroup);

        this.settings = SettingsActivity.this.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE);

        // load ui
        this.loadSettings();
    }

    public void loadSettings(){
        this.urlText.setText(this.settings.getString(URL_KEY, ""));
        this.outputFormatRadioGroup.check(this.settings.getInt(FORMAT_KEY, R.id.xmlFormatRadioButton));
        this.storageTypeRadioGroup.check(this.settings.getInt(STORAGE_TYPE_KEY, R.id.externalMemoryRadioButton));
        this.commRadioGroup.check(this.settings.getInt(COMM_TYPE_KEY, R.id.asyncTasksRadioButton));
    }

    public void updateSettings(View view){
        SharedPreferences.Editor e = this.settings.edit();
        e.putString(URL_KEY, this.urlText.getText().toString());
        e.putInt(FORMAT_KEY, this.outputFormatRadioGroup.getCheckedRadioButtonId());
        e.putInt(STORAGE_TYPE_KEY, this.storageTypeRadioGroup.getCheckedRadioButtonId());
        e.putInt(COMM_TYPE_KEY, this.commRadioGroup.getCheckedRadioButtonId());
        e.apply();
    }

    public void cancel(View view) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}