package com.example.shopping.retrofit;

//import android.database.Observable;
import io.reactivex.rxjava3.core.Observable;

import com.example.shopping.model.CategoryModel;
import com.example.shopping.model.ProductModel;

import retrofit2.http.GET;

public interface ApiShopping {
    @GET("getCategory.php")
    Observable<CategoryModel> getCategory();

    @GET("getProduct.php")
    Observable<ProductModel> getProduct();

}
