package com.example.parallel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class MainPay extends AppCompatActivity {

    private Button paymentBtn;
    private int PAYPAL_REQ_CODE = 12;
    private static PayPalConfiguration payPalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalClientConfig.PAYPAL_CLIENT_ID);
    private String costs;
    private float costsFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sent as a float but in as a string
        this.costs = getIntent().getStringExtra("costs");

        //string to float
        costsFloat = Float.parseFloat(costs);


        //Cast component
        paymentBtn = findViewById(R.id.paymentBtn);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(intent);

        paymentBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PaypalPaymentMethod();
            }
        });
    }

    private void PaypalPaymentMethod(){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(costsFloat), "CAD", "costsFloat", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Payment Made Successfully.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "PAYMENT DENIED.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy(){
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}