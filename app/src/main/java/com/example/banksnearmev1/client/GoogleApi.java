package com.example.banksnearmev1.client;

import com.example.banksnearmev1.models.BanksList;
import com.example.banksnearmev1.services.BanksService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleApi {

    private static Retrofit retrofit = null;


    private static Retrofit getRetrofit(){
        if(retrofit == null){
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);  //level(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();

        }
        return retrofit;
    }

    public static BanksService getService() {
        return getRetrofit().create(BanksService.class);
    }
}
