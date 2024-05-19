package com.example.shopping.retrofit;

//import android.database.Observable;

import com.example.shopping.model.CategoryModel;
import com.example.shopping.model.ProductModel;
import com.example.shopping.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiShopping {
    @GET("getCategory.php")
    Observable<CategoryModel> getCategory();

    @GET("getProduct.php")
    Observable<ProductModel> getProduct();

    @POST("postUserRegister.php")
    @FormUrlEncoded
    Observable<UserModel> postUserRegister(
            @Field("userId") String userId,
            @Field("userEmail") String userEmail,
            @Field("userName") String userName,
            @Field("userPassword") String userPassword,
            @Field("profileImageUrl") String profileImageUrl
    );



    @POST("order.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("phonenumber") String phonenumber,
            @Field("totalprice") String totalPrice,
            @Field("iduser") String id,
            @Field("address") String address,
            @Field("amount") int amount,
            @Field("detail") String detail

    );


}
