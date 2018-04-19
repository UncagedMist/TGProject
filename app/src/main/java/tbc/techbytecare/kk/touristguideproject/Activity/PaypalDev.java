package tbc.techbytecare.kk.touristguideproject.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import tbc.techbytecare.kk.touristguideproject.Config.Config;
import tbc.techbytecare.kk.touristguideproject.R;

public class PaypalDev extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    EditText edtAmount;

    String amount = "";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_dev);

        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnPayNow = findViewById(R.id.btnPayNow);
        edtAmount = findViewById(R.id.edtAmount);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        amount = edtAmount.getText().toString();

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
                "Tourist App Payment",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE)  {

            if (resultCode == RESULT_OK)    {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null)   {

                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

//                        startActivity(new Intent(this,PaymentDetailsActivity.class)
//                                .putExtra("PaymentDetails",paymentDetails)
//                                .putExtra("PaymentAmount",amount)
//                        );
                        Toast.makeText(this, "Payment Done..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaypalDev.this,HomeActivity.class));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)   {
            Toast.makeText(this, "invalid...", Toast.LENGTH_SHORT).show();
        }
    }
}
