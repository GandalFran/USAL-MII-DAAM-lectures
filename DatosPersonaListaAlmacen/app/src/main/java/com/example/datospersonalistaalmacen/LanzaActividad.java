package com.example.datospersonalistaalmacen;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datospersonalistaalmacen.bean.UnaPersona;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class LanzaActividad extends AppCompatActivity {

    public static final String LANZA_ACTIVIDAD_AGE_STATUS = "LANZA_ACTIVIDAD_AGE_STATUS";
    public static final String LANZA_ACTIVIDAD_NAME_STATUS = "LANZA_ACTIVIDAD_NAME_STATUS";
    public static final String LANZA_ACTIVIDAD_DATE_STATUS = "LANZA_ACTIVIDAD_DATE_STATUS";
    public static final String LANZA_ACTIVIDAD_PHONE_STATUS = "LANZA_ACTIVIDAD_PHONE_STATUS";
    public static final String LANZA_ACTIVIDAD_SURNAME_STATUS = "LANZA_ACTIVIDAD_SURNAME_STATUS";
    public static final String LANZA_ACTIVIDAD_ENGLISH_LEVEL_STATUS = "LANZA_ACTIVIDAD_ENGLISH_LEVEL_STATUS";
    public static final String LANZA_ACTIVIDAD_DRIVING_LICENSE_STATUS = "LANZA_ACTIVIDAD_DRIVING_LICENSE_STATUS";

    public static final String PERSON_KEY = "person";
    public static final String POSITION_KEY = "position";
    public static final String IS_MODIFY = "isModification";

    private boolean isModify;

    private TextView dateInput;
    private EditText nameInput;
    private EditText ageInput;
    private EditText phoneInput;
    private EditText surnameInput;
    private CheckBox drivingLicenseInput;
    private RadioGroup englishLevelInput;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_lanza_actividad);

        // retrieve inputs
        this.ageInput = findViewById(R.id.ageInput);
        this.dateInput = findViewById(R.id.dateInput);
        this.nameInput = findViewById(R.id.nameInput);
        this.phoneInput = findViewById(R.id.phoneInput);
        this.surnameInput = findViewById(R.id.surnameInput);
        this.englishLevelInput = findViewById(R.id.englishlevelInput);
        this.drivingLicenseInput = findViewById(R.id.drivingLicesnseInput);

        // initialize date
        this.dateInput.setText(Utils.getDateString(Calendar.getInstance().getTime()));

        // if is a modification activity, update
        Bundle params = getIntent().getExtras();
        this.isModify = params.getBoolean(IS_MODIFY);
        if (this.isModify) {
            UnaPersona p = (UnaPersona) getIntent().getSerializableExtra(PERSON_KEY);
            this.populateInputs(p);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        this.nameInput.setText(savedState.getString(LANZA_ACTIVIDAD_NAME_STATUS));
        this.dateInput.setText(savedState.getString(LANZA_ACTIVIDAD_DATE_STATUS));
        this.ageInput.setText(savedState.getString(LANZA_ACTIVIDAD_AGE_STATUS));
        this.phoneInput.setText(savedState.getString(LANZA_ACTIVIDAD_PHONE_STATUS));
        this.surnameInput.setText(savedState.getString(LANZA_ACTIVIDAD_SURNAME_STATUS));
        this.englishLevelInput.check(savedState.getInt(LANZA_ACTIVIDAD_ENGLISH_LEVEL_STATUS));
        this.drivingLicenseInput.setChecked(savedState.getBoolean(LANZA_ACTIVIDAD_DRIVING_LICENSE_STATUS));
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        int selectedEnglishLevelButtonId = this.englishLevelInput.getCheckedRadioButtonId();
        RadioButton englishLevel =  findViewById(selectedEnglishLevelButtonId);

        currentState.putString(LANZA_ACTIVIDAD_AGE_STATUS, this.ageInput.getText().toString());
        currentState.putString(LANZA_ACTIVIDAD_NAME_STATUS, this.nameInput.getText().toString());
        currentState.putString(LANZA_ACTIVIDAD_DATE_STATUS, this.dateInput.getText().toString());
        currentState.putString(LANZA_ACTIVIDAD_PHONE_STATUS, this.phoneInput.getText().toString());
        currentState.putString(LANZA_ACTIVIDAD_ENGLISH_LEVEL_STATUS, englishLevel.getText().toString());
        currentState.putString(LANZA_ACTIVIDAD_SURNAME_STATUS, this.surnameInput.getText().toString());
        currentState.putBoolean(LANZA_ACTIVIDAD_DRIVING_LICENSE_STATUS, this.drivingLicenseInput.isChecked());

        super.onSaveInstanceState(currentState);
    }

    public void populateInputs(UnaPersona p) {
        this.ageInput.setText(p.getAge());
        this.nameInput.setText(p.getName());
        this.dateInput.setText(Utils.getDateString(p.getDate()));
        this.phoneInput.setText(p.getPhone());
        this.surnameInput.setText(p.getSurname());
        this.drivingLicenseInput.setChecked(p.isDrivingLicense());

        for (int i=0; i<this.englishLevelInput.getChildCount(); i++) {
            RadioButton b = (RadioButton) this.englishLevelInput.getChildAt(i);
            if (b.getText().equals( this.getEnglishLevelString(p.getEnglishLevel()) )) {
                this.englishLevelInput.check(b.getId());
                break;
            }
        }
    }

    public void changeDate(View view) {

        // get current date in calendar format
        Calendar c = Calendar.getInstance();

        // display and add event listener to update date in user. Also set the stored date for user as default
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Date date = new GregorianCalendar(year, month, day).getTime();
                String dateString = Utils.getDateString(date);
                dateInput.setText(dateString);
            }
        }, c.get(Calendar.YEAR),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        dpd.show();
    }

    public void addUser(View view) {

        // retrieve user data
        String age = this.ageInput.getText().toString();
        String name = this.nameInput.getText().toString();
        String dateString = this.dateInput.getText().toString();
        String phone = this.phoneInput.getText().toString();
        String surname = this.surnameInput.getText().toString();
        boolean hasDrivingLicense = this.drivingLicenseInput.isChecked();
        int selectedEnglishLevelButtonId = this.englishLevelInput.getCheckedRadioButtonId();
        String englishLevelString = ((RadioButton) findViewById(selectedEnglishLevelButtonId)).getText().toString();
        UnaPersona.EnglishLevel englishLevel = this.getEnglishLevelFromString(englishLevelString);

        // check
        if(name.isEmpty()){
            name = getResources().getString(R.string.unknown);
        }
        if(surname.isEmpty()){
            surname = getResources().getString(R.string.unknown);
        }
        if (age.isEmpty()) {
            age = getResources().getString(R.string.unknown);
        }
        if (phone.isEmpty()) {
            phone = getResources().getString(R.string.unknown);
        }

        Date date = null;
        if (dateString.isEmpty()) {
            date = Calendar.getInstance().getTime();
        }else{
            date = Utils.fromDateString(dateString);
        }

        UnaPersona p = new UnaPersona(name,  surname,  age,  date,  phone,  englishLevel,  hasDrivingLicense);

        // return data to main activity
        // NOTE: it also clears activity stack with FLAG_ACTIVITY_CLEAR_TOP
        Intent intentResult = new Intent(this, MainActivity.class);
        intentResult.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentResult.putExtra(PERSON_KEY, p);
        if(this.isModify){
            int position = this.getIntent().getIntExtra(POSITION_KEY, 0);
            intentResult.putExtra(POSITION_KEY,position);
        }
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }

    public void reset(View view) {
        this.nameInput.getText().clear();
        this.ageInput.getText().clear();
        this.phoneInput.getText().clear();
        this.englishLevelInput.clearCheck();
        this.surnameInput.getText().clear();
        this.dateInput.setText(Utils.getDateString(Calendar.getInstance().getTime()));
        this.englishLevelInput.check(R.id.lowLevelEnglish);
        if (this.drivingLicenseInput.isChecked()){
            this.drivingLicenseInput.toggle();
        }
    }

    public void cancel(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private UnaPersona.EnglishLevel getEnglishLevelFromString(String level){
        if(level.equals(getResources().getString(R.string.english_level_low))){
            return UnaPersona.EnglishLevel.LOW;
        }else if(level.equals(getResources().getString(R.string.english_level_medium))){
            return UnaPersona.EnglishLevel.MEDIUM;
        }else{
            return UnaPersona.EnglishLevel.HIGH;
        }
    }

    private String getEnglishLevelString(UnaPersona.EnglishLevel level){
        switch (level){
            case LOW: return getResources().getString(R.string.english_level_low);
            case MEDIUM: return getResources().getString(R.string.english_level_medium);
            case HIGH: return getResources().getString(R.string.english_level_high);
            default: return null;
        }
    }
}
