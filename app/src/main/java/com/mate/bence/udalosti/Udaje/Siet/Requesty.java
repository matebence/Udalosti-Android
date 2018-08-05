package com.mate.bence.udalosti.Udaje.Siet;

import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.Odpoved.Odpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.Pozicia;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface Requesty {

    @FormUrlEncoded
    @POST("udalosti/index.php/prihlasovanie")
    Call<Autentifikator> prihlasenie(
            @Field("email") String email,
            @Field("heslo") String heslo,
            @Field("pokus_o_prihlasenie") String prihlasenie);

    @FormUrlEncoded
    @POST("udalosti/index.php/prihlasovanie/odhlasit_sa")
    Call<Autentifikator> odhlasenie(
            @Field("email") String email);

    @Multipart
    @POST("udalosti/index.php/registracia")
    Call<Autentifikator> registracia(
            @Part("meno") RequestBody meno,
            @Part("email") RequestBody email,
            @Part("heslo") RequestBody heslo,
            @Part("potvrd") RequestBody potvrd,
            @Part("pohlavie") RequestBody pohlavie,
            @Part("idTelefonu") RequestBody idTelefonu,
            @Part MultipartBody.Part file,
            @Part("nova_registracia") RequestBody registracia);

    @FormUrlEncoded
    @POST("udalosti/index.php/pomoc")
    Call<Autentifikator> zabudnuteHeslo(
            @Field("email") String email,
            @Field("zabudnute_heslo") String zabudnuteHeslo);

    @Multipart
    @POST("udalosti/index.php/nastavenia")
    Call<Autentifikator> aktualizacia(
            @Part("email") RequestBody email,
            @Part("meno") RequestBody meno,
            @Part("heslo") RequestBody heslo,
            @Part("potvrd") RequestBody potvrd,
            @Part MultipartBody.Part file,
            @Part("token") RequestBody token,
            @Part("aktualizuj_udaje") RequestBody aktualizaciaUdajov);

    @FormUrlEncoded
    @POST("udalosti/index.php/nastavenia/odstranenie_uctu")
    Call<Odpoved> odstranenieUctu(
            @Field("email") String email,
            @Field("odstran") String odstranenieUctu,
            @Field("token") String token);



    @FormUrlEncoded
    @POST("udalosti/index.php/zaujmy/zaujem_o_udalost")
    Call<Odpoved> zaujem(
            @Field("email") String email,
            @Field("zaujem") String zaujem,
            @Field("id_udalost") int idUdalost,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("udalosti/index.php/pozvanky/nova_pozvanka")
    Call<Odpoved> pozvat(
            @Field("email") String email,
            @Field("pozvany") int idPouzivatela,
            @Field("id_udalost") int idUdalost,
            @Field("token") String token,
            @Field("pozvanka") String pozvanka);


    @FormUrlEncoded
    @POST("udalosti/index.php/vztahy/posli_ziadost_o_priatelstvo")
    Call<Odpoved> novaZiadost(
            @Field("email") String email,
            @Field("nove_priatelstvo") int pouzivatel,
            @Field("token") String token,
            @Field("ziadost_o_priatelsvo") String novaZiadost);

    @FormUrlEncoded
    @POST("udalosti/index.php/vztahy/zrusenie_priatelstva")
    Call<Odpoved> odstraneniePriatelstva(
            @Field("email") String email,
            @Field("priatel") int idPouzivatel,
            @Field("zrusenie") String zruseniePriatelstva,
            @Field("token") String token);


    @FormUrlEncoded
    @POST("udalosti/index.php/zaujmy/odstranenie_zo_zaujmov")
    Call<Odpoved> odstranenieZoZaujmov(
            @Field("email") String email,
            @Field("udalost") int idZaujemUdalosti,
            @Field("odstranenie") String odstranenie,
            @Field("token") String token);



    @FormUrlEncoded
    @POST("udalosti/index.php/vztahy/odpoved_na_ziadost")
    Call<Void> odpovedNaZiadost(
            @Field("email") String email,
            @Field("odpoved_na_ziadost") int odpoved,
            @Field("id_ziadost") int idZiadost,
            @Field("token") String token);

    @GET
    Call<ResponseBody> obrazok(@Url String adresaObrazka);

    @GET("json")
    Call<Pozicia> pozicia();
}