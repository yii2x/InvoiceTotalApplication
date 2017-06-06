package com.basowy.invoicetotalapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements OnEditorActionListener, OnClickListener {

    private EditText subtotalEditText;
    private TextView discountPersentTextView;
    private TextView discountAmountTextView;
    private TextView totalTextView;
    private Button resetButton;

    private String subtotalString = "";

    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subtotalEditText = (EditText) findViewById(R.id.subTotalEditText);
        discountPersentTextView = (TextView)findViewById(R.id.discountPersentTextView);
        discountAmountTextView = (TextView)findViewById(R.id.discountAmountTextView);
        totalTextView = (TextView)findViewById(R.id.totalTextView);
        resetButton = (Button) findViewById(R.id.resetButton);

        //set event listeners
        subtotalEditText.setOnEditorActionListener(this);
        resetButton.setOnClickListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.resetButton:
                subtotalEditText.setText("");
                discountPersentTextView.setText("00%");
                discountAmountTextView.setText("$0.00");
                totalTextView.setText("$0.00");

                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }

        return false;
    }

    private void calculateAndDisplay(){
        subtotalString = subtotalEditText.getText().toString();
        float subtotal;

        if(subtotalString.equals("")){
            subtotal = 0;
        }
        else{
            subtotal = Float.parseFloat(subtotalString);
        }

        float discountPersent = 0;

        if(subtotal >= 200){
            discountPersent = .2f;
        }
        else if (subtotal >= 100){
            discountPersent = .1f;
        }

        float discountAmount = subtotal * discountPersent;
        float total = subtotal - discountAmount;

        //format and display

        NumberFormat persent = NumberFormat.getPercentInstance();
        discountPersentTextView.setText(persent.format(discountPersent));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        discountAmountTextView.setText("(" + currency.format(discountAmount) + ")");

        totalTextView.setText(currency.format(total));
    }

    @Override
    protected void onPause() {

      //  calculateAndDisplay();
//
//        SharedPreferences.Editor editor = savedValues.edit();
//        editor.putString("subtotalString", subtotalString);
//        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

     //   subtotalString = savedValues.getString("subtotalString", "");
    //    subtotalEditText.setText(subtotalString);
        calculateAndDisplay();
    }
}
