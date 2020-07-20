package com.greencross.gctemperlib.push;

import com.greencross.gctemperlib.RCApplication;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by MobileDoctor on 2017-02-08.
 */

public class MyFirebaseService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh(){
        RCApplication.deviceToken = FirebaseInstanceId.getInstance().getToken();
    }
}
