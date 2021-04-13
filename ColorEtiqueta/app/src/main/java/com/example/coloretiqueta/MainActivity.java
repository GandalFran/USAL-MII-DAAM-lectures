package com.example.coloretiqueta;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ejecutar(View view) {

        // retrieve elements
        TextView message = (TextView) findViewById(R.id.messageView);
        CheckBox visibilityCheckBox =  (CheckBox) findViewById(R.id.visibilityCheckBox);
        RadioGroup colorGroup = (RadioGroup) findViewById(R.id.radioGroupColor);
        RadioGroup  elementTypeGroup = (RadioGroup) findViewById(R.id.radioGroupElement);

        // get status
        int radioButtonId = colorGroup.getCheckedRadioButtonId();
        RadioButton selectedColorButton = (RadioButton) findViewById(radioButtonId);

        int elementTypeGroupId = elementTypeGroup.getCheckedRadioButtonId();
        RadioButton selectedElementTypeButton = (RadioButton) findViewById(elementTypeGroupId);

        boolean isMessageVisible = visibilityCheckBox.isChecked();

        // update message colors
        if(elementTypeGroupId == R.id.backgroundColorButton){
            message.setBackgroundColor(selectedColorButton.getCurrentTextColor());
        }else{
            message.setTextColor(selectedColorButton.getCurrentTextColor());
        }

        // update message visibility
        message.setVisibility( isMessageVisible ? View.VISIBLE : View.INVISIBLE);
    }



}