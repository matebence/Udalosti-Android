package com.mate.bence.udalosti.Udaje.Siet;

import com.mate.bence.udalosti.Udaje.Siet.Model.Akcia.Akcia;
import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.Obsah.Obsah;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.LocationIQ;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.Pozicia;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Requesty {

    @FormUrlEncoded
    @POST("prihlasenie/prihlasit")
    Call<Autentifikator> prihlasenie(
            @Field("email") String email,
            @Field("heslo") String heslo,
            @Field("pokus_o_prihlasenie") String prihlasenie);

    @FormUrlEncoded
    @POST("prihlasenie/odhlasit")
    Call<Autentifikator> odhlasenie(
            @Field("email") String email);

    @FormUrlEncoded
    @POST("registracia")
    Call<Autentifikator> registracia(
            @Field("meno") String meno,
            @Field("email") String email,
            @Field("heslo") String heslo,
            @Field("potvrd") String potvrd,
            @Field("nova_registracia") String registracia);

    @FormUrlEncoded
    @POST("udalosti")
    Call<Obsah> udalosti(
            @Field("email") String email,
            @Field("stat") String stat,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("udalosti/zoznam_podla_pozicie")
    Call<Obsah> udalostiPodlaPozicie(
            @Field("email") String email,
            @Field("stat") String stat,
            @Field("okres") String okres,
            @Field("mesto") String mesto,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("zaujmy/zoznam")
    Call<Obsah> zaujmy(
            @Field("email") String email,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("zaujmy")
    Call<Akcia> zaujem(
            @Field("email") String email,
            @Field("token") String token,
            @Field("idUdalost") int idUdalost);

    @FormUrlEncoded
    @POST("zaujmy/potvrd")
    Call<Obsah> potvrd(
            @Field("email") String email,
            @Field("token") String token,
            @Field("idUdalost") int idUdalost);

    @FormUrlEncoded
    @POST("zaujmy/odstran")
    Call<Akcia> odstranZaujem(
            @Field("email") String email,
            @Field("token") String token,
            @Field("idUdalost") int idUdalost);

    @GET("reverse.php")
    Call<LocationIQ> pozicia(
            @Query("key") String token,
            @Query("lat") double zemepisnaSirka,
            @Query("lon") double zemepisnaDlzka,
            @Query("format") String format,
            @Query("accept-language") String jazyk);

    @GET("json")
    Call<Pozicia> pozicia();
}