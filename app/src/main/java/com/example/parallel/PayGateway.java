package com.example.parallel;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PayGateway extends AppCompatActivity {

    LocalTime now = LocalTime.now();
    int hourNow = now.getHour();
    int minNow = now.getMinute();
    String amt = " ";
    Context context = this;
    public static final int PAYPAL_REQUEST_CODE = 1111;
    TextView tvTime;
    TextView tvPay;
    EditText editAmount;
    Button timeBtn;
    Button payBtn;


    //set payment as test/sandbox mode NOT production ready
    private static PayPalConfiguration payPalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalClientConfig.PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_gateway);

        tvTime = (TextView)findViewById(R.id.timeTextView);
        //tvPay = (TextView)findViewById(R.id.payTextView);
        tvPay = (TextView)findViewById(R.id.editAmount);

        Calendar cldr = Calendar.getInstance();

        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minute = cldr.get(Calendar.MINUTE);

        timeBtn = (Button)findViewById(R.id.timeBtn);
        timeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvTime.setText(hourOfDay + ":" + minute);
                        float parkingRate = 0.25f;
                        float parkingCost = ((minute - minNow) < 0) ? ((minute - minNow + 60) + ((hourOfDay - hourNow - 1) * 60)) * parkingRate : ((minute - minNow) + ((hourOfDay - hourNow) * 60)) * parkingRate;
                        //tvPay.setText(String.format("$ " + "%.2f", parkingCost));
                        tvPay.setText(String.valueOf(parkingCost));

                    }
                },hour, minute,android.text.format.DateFormat.is24HourFormat(context));

                timePickerDialog.show();

                //float parkingRate = 0.25f;
                //float parkingCost = ((minute - minNow) < 0) ? ((minute - minNow + 60) + ((hour - hourNow - 1) * 60)) * parkingRate : ((minute - minNow) + ((hour - hourNow) * 60)) * parkingRate;

                //tvPay.setText(String.format("$ " + "%.2f", parkingCost));

                //timePickerDialog.show();

            }
        });

        //start paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(intent);

        payBtn = (Button)findViewById(R.id.payBtn);
        //editTextNumberDecimal = (EditText)findViewById(R.id.editText);
        editAmount = (EditText)findViewById(R.id.editAmount);
        //textView = (TextView)findViewById(R.id.textView);

        payBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                paymentProcessing();
                //PaypalPaymentMethod();
            }
        });
    }

    private void paymentProcessing(){
        amt = editAmount.getText().toString();
        //amt = textView.getText().toString();
        //amt = Float.toString(cost);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amt)), "CAD",
                "TESTING Pay Here", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                //code to trigger notification
               /* AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
                long timeAtButtonClick = System.currentTimeMillis();
                long time10 = 10 *1000;
                alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtonClick+time10,pendingIntent); */
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