package com.example.shopping.retrofit;

//import android.database.Observable;
import io.reactivex.rxjava3.core.Observable;

import com.example.shopping.model.CategoryModel;
import com.example.shopping.model.ProductModel;
import com.example.shopping.model.UserModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiShopping {
    @GET("getCategory.php")
    Observable<CategoryModel> getCategory();

    @GET("getProduct.php")
    Observable<ProductModel> getProduct();


    @POST("order.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("phonenumber") String phonenumber,
            @Field("totalprice") String totalPrice,
            @Field("iduser") int id,
            @Field("address") String address,
            @Field("amount") int amount,
            @Field("detail") String detail

    );


}
