package com.example.parallel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class MainPay extends AppCompatActivity {

    public EditText editAmount;
    //originally private
    public Button payBtn;
    public static final int PAYPAL_REQUEST_CODE = 1111;

    //set payment as test/sandbox mode NOT production ready
    private static PayPalConfiguration payPalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalClientConfig.PAYPAL_CLIENT_ID);
    //private String costs;
    //private float costsFloat;

    String amt = " ";

    //@Override
    //Terminate the PayPal service
    //protected void onDestroy(){
    //stopService(new Intent(this, PayPalService.class));
    //super.onDestroy();
    //}


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpay);

        //start paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(intent);

        payBtn = (Button)findViewById(R.id.payBtn);
        editAmount = (EditText)findViewById(R.id.editAmount);

        payBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                paymentProcessing();
                //PaypalPaymentMethod();
            }
        });

/*
        //sent as a float but in as a string
        //this.costs = getIntent().getStringExtra("costs");

        //string to float
        //costsFloat = Float.parseFloat(costs);


        //Casting of button component
        payBtn = findViewById(R.id.payBtn);

        //Kickstart PayPal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(intent);

        payBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PaypalPaymentMethod();
            }

        });
*/
    }

    private void paymentProcessing(){
        amt = editAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amt)), "CAD",
                "TESTING Pay Here", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }
    /*
        private void PaypalPaymentMethod(){

            amt = editAmount.getText().toString();
            PayPalPayment userPayment = new PayPalPayment(new BigDecimal(String.valueOf(amt)), "CAD", "TESTING Pay Here", PayPalPayment.PAYMENT_INTENT_SALE);
            //PayPalPayment thePayment = new PayPalPayment(new BigDecimal(100), "CAD", "IN PAYPALPAYMENTMETHOD(). TESTING...", PayPalPayment.PAYMENT_INTENT_SALE);
            //PayPalPayment thePayment = new PayPalPayment(new BigDecimal(costsFloat), "CAD", "costsFloat", PayPalPayment.PAYMENT_INTENT_SALE);

            //PayPal opens up Parallel app payment option thanks to dependency imported in gradle.
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
            float x = Float.parseFloat(userPayment);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, x);  //Double.parseDouble("5")

            //Starting the activity, requesting the PayPal code
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);

        }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "PAYMENT APPROVED.", Toast.LENGTH_LONG).show();
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirm != null){
                    try{
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amt)
                        );

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "PAYMENT DENIED.", Toast.LENGTH_SHORT).show();
            } else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    //Terminate the PayPal service
    protected void onDestroy(){
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}