package com.example.parallel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView textId;
    TextView textAmt;
    TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        textId = findViewById(R.id.textId);
        textStatus = findViewById(R.id.textStatus);
        textAmt = findViewById(R.id.textAmt);

        //Get intent
        Intent intent = getIntent();

        try{
            JSONObject jsonObj = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObj.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount){
        try {
            //textAmt.setText(response.getString(String.format("$%s", paymentAmount)));
            textAmt.setText(response.getString("$" + paymentAmount));
            textStatus.setText(response.getString("status"));
            textId.setText(response.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
