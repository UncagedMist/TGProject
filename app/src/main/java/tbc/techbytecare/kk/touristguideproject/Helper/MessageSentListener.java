package tbc.techbytecare.kk.touristguideproject.Helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tbc.techbytecare.kk.touristguideproject.Activity.MoneyOrderActivity;

public class MessageSentListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int resultCode = this.getResultCode();
        boolean successfullySent = resultCode == Activity.RESULT_OK;

        MoneyOrderActivity.getAppContext().unregisterReceiver(this);
    }
}