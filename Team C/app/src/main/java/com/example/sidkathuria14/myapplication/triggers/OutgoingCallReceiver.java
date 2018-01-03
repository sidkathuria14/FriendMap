package com.example.sidkathuria14.myapplication.triggers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sidkathuria14 on 2/1/18.
 */

public class OutgoingCallReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        if (phoneNumber != null
                && phoneNumber.equals(PhoneNumberTrigger.PHONE_NUMBER_TRIGGER)) {
            PhoneNumberTrigger.launchPanicIntent(context);
        }

    }
}
