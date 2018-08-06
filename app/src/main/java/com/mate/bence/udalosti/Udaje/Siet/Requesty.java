package com.mate.bence.udalosti.Udaje.Siet;

import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.Obsah.Obsah;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.Pozicia;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Requesty {

    @FormUrlEncoded
    @POST("udalosti/index.php/prihlasenie")
    Call<Autentifikator> prihlasenie(
            @Field("email") String email,
            @Field("heslo") String heslo,
            @Field("pokus_o_prihlasenie") String prihlasenie);

    @FormUrlEncoded
    @POST("udalosti/index.php/prihlasenie/odhlasit_sa")
    Call<Autentifikator> odhlasenie(
            @Field("email") String email);

    @FormUrlEncoded
    @POST("udalosti/index.php/registracia")
    Call<Autentifikator> registracia(
            @Field("meno") String meno,
            @Field("email") String email,
            @Field("heslo") String heslo,
            @Field("potvrd") String potvrd,
            @Field("nova_registracia") String registracia);

    @FormUrlEncoded
    @POST("udalosti/index.php/udalosti")
    Call<Obsah> udalosti(
            @Field("email") String email,
            @Field("stat") String stat,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("udalosti/index.php/udalosti/udalosti_podla_pozicie")
    Call<Obsah> udalostiPodlaPozicie(
            @Field("email") String email,
            @Field("stat") String stat,
            @Field("okres") String okres,
            @Field("mesto") String mesto,
            @Field("token") String token);

    @GET("json")
    Call<Pozicia> pozicia();
}