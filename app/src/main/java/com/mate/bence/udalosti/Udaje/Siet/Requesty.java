package com.mate.bence.udalosti.Udaje.Siet;

import com.mate.bence.udalosti.Udaje.Siet.Model.Akcia.Akcia;
import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.Obsah.Obsah;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.LocationIQ;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Requesty {

    @FormUrlEncoded
    @POST("udalosti/index.php/prihlasenie/prihlasit")
    Call<Autentifikator> prihlasenie(
            @Field("email") String email,
            @Field("heslo") String heslo,
            @Field("pokus_o_prihlasenie") String prihlasenie);

    @FormUrlEncoded
    @POST("udalosti/index.php/prihlasenie/odhlasit")
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
    @POST("udalosti/index.php/zaujmy")
    Call<Akcia> zaujem(
            @Field("token") String token,
            @Field("email") String email,
            @Field("idUdalost") String idUdalost);

    @FormUrlEncoded
    @POST("udalosti/index.php/zaujmy/odstran")
    Call<Akcia> odstranZaujem(
            @Field("token") String token,
            @Field("email") String email,
            @Field("idUdalost") String idUdalost);

    @FormUrlEncoded
    @POST("udalosti/index.php/zaujmy/zoznam")
    Call<Obsah> zaujmy(
            @Field("token") String token,
            @Field("email") String email);

    @FormUrlEncoded
    @POST("udalosti/index.php/udalosti")
    Call<Obsah> udalosti(
            @Field("email") String email,
            @Field("stat") String stat,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("udalosti/index.php/udalosti/zoznam_podla_pozicie")
    Call<Obsah> udalostiPodlaPozicie(
            @Field("email") String email,
            @Field("stat") String stat,
            @Field("okres") String okres,
            @Field("mesto") String mesto,
            @Field("token") String token);

    @GET("reverse.php")
    Call<LocationIQ> pozicia(
            @Query("key") String token,
            @Query("lat") double zemepisnaSirka,
            @Query("lon") double zemepisnaDlzka,
            @Query("format") String format,
            @Query("accept-language") String jazyk);
}