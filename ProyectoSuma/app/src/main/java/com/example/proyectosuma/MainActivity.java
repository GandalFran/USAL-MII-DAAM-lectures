package com.example.proyectosuma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText input1;
    private EditText input2;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve inputs
        this.input1 = (EditText) findViewById(R.id.input1);
        this.input2 = (EditText) findViewById(R.id.input2);
        this.result = (TextView) findViewById(R.id.resultView);
    }

    public void suma(View view){

        // retrive numbers
        String input1Value = this.input1.getText().toString();
        String input2Value = this.input2.getText().toString();

         // update values if neccesary
        if(input1Value.isEmpty() ){
            input1Value = "0";
            this.input1.setText("0");
        }

        if(input2Value.isEmpty() ){
            input2Value = "0";
            this.input2.setText("0");
        }

        // transform values
        int number1 = Integer.parseInt(input1Value);
        int number2 = Integer.parseInt(input2Value);
        int numberResult = number1 + number2;

        // update result in field
        this.result.setText(String.valueOf(numberResult));
    }
}