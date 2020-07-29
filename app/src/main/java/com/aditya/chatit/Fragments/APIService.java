package com.aditya.chatit.Fragments;

import com.aditya.chatit.Notifications.MyResponse;
import com.aditya.chatit.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
            "Authorization:key=AAAA96il5fg:APA91bExxpVClnGsA0Hys2cA2UmygERAAEuGmamG8AM_rkQP8pnfAKQDt16AXBnWwHw9wKSniDfXAlGK__lD-dy6JsRdAojxt7THAy1oeJuqivhXmTv-QyIQdz8OJUK6E37-PFjlkuck"
                    }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
