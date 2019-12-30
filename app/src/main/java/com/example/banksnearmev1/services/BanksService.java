package com.example.banksnearmev1.services;

import com.example.banksnearmev1.models.BanksList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BanksService {

    @GET("maps/api/place/nearbysearch/json")
    Call<BanksList> getBanks(
            @Query("location") String location,
            @Query("key") String key,
            @Query("radius") String radius,
            @Query("types") String types);

}
