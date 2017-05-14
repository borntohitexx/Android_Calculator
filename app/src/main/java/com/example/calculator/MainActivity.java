package com.example.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;

    // Variables to hold the operands and operator
    private Double operand1 = null;
    private String pendingOperation = "=";

    private final String SAVED_OPERATION = "SavedOp";
    private final String SAVED_OPERAND1 = "SavedOperand";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Associate references to EditText and TextViews in layout
        result = (EditText) findViewById(R.id.result);
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);

        //Associate references to all buttons on the calculator 0 to 9 and operators
        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);

        Button buttonEquals = (Button) findViewById(R.id.buttonEquals);
        Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
        Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonNeg = (Button) findViewById(R.id.buttonNeg);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                newNumber.append(b.getText().toString()); //Add number + dot to newNumber text widget when tapped
            }
        };
        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);

        //Separate listener for operator buttons
        View.OnClickListener opListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String op = b.getText().toString();
                String value = newNumber.getText().toString();
                try {
                    performOperation(Double.valueOf(value), op);
                } catch (NumberFormatException e) {
                    newNumber.setText("");
                }
                pendingOperation = op;
                displayOperation.setText(pendingOperation);
            }
        };

        //Assign operator buttons with operator listener
        buttonEquals.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);
        buttonEquals.setOnClickListener(opListener);

        //Separate listener for negative button
        View.OnClickListener negListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = newNumber.getText().toString();
                if (value.length() == 0) {
                    newNumber.setText("-");
                } else if (value.equals("-") || value.equals(".")) {
                    newNumber.setText("");
                } else {
                    Double negValue = Double.valueOf(value);
                    negValue = negValue * -1;
                    newNumber.setText(negValue.toString());
                }
            }
        };

        buttonNeg.setOnClickListener(negListener);
    }

    private void performOperation(Double value, String operation) {
        if (operand1 == null) {
            operand1 = value; //If no value has been set for the operand, assign the value in the newNumber view to the operand
        } else {
            if (pendingOperation.equals("=")) {
                pendingOperation = operation;
            }
            switch (pendingOperation) {
                case "=":
                    operand1 = value;
                    break;
                case "/":
                    if (value == 0) {
                        operand1 = 0.0;
                    } else {
                        operand1 /= value;
                    }
                    break;
                case "*":
                    operand1 *= value;
                    break;
                case "-":
                    operand1 -= value;
                    break;
                case "+":
                    operand1 += value;
                    break;

            }
        }

        result.setText(operand1.toString());
        newNumber.setText("");
    }

    //Restore the existing operation and operand if device has been rotated
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(SAVED_OPERATION);
        operand1 = savedInstanceState.getDouble(SAVED_OPERAND1);
        displayOperation.setText(pendingOperation);
    }

    //Store the existing operation and operand when device is rotated
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_OPERATION, pendingOperation);
        if (operand1 != null) { //Only store operand if not null. Otherwise, there is no need to store the operand
            outState.putDouble(SAVED_OPERAND1, operand1);
        }
        super.onSaveInstanceState(outState);
    }
}
