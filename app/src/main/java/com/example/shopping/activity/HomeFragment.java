package com.example.shopping.activity;

import static androidx.core.app.NotificationCompat.getCategory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AndroidException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shopping.R;
import com.example.shopping.adapter.CategoryAdapter;
import com.example.shopping.adapter.ProductAdapter;
import com.example.shopping.model.Category;
import com.example.shopping.model.CategoryModel;
import com.example.shopping.model.Product;
import com.example.shopping.model.ProductModel;
import com.example.shopping.retrofit.ApiShopping;
import com.example.shopping.retrofit.RetrofitClient;
import com.example.shopping.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import io.reactivex.rxjava3.core.Observable;



public class HomeFragment extends Fragment {

    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHome;
    CategoryAdapter categoryAdapter;
    ListView listView;
    List<Category> arrCategory;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopping apiShopping;
    List<Product> arrProduct;
    ProductAdapter productAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_home, null);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
//        anhxa(inflater);
        viewFlipper = v.findViewById(R.id.viewlipper);
        recyclerViewHome = v.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);

        //khoi tao list
        arrCategory = new ArrayList<>();
        arrProduct = new ArrayList<>();

        apiShopping = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopping.class);


        if (isConnected(requireContext())) {
            actionViewFlipper();
            getCategory();
            getProduct();
        } else {
            Toast.makeText(getActivity(), "Connect internet fail", Toast.LENGTH_SHORT).show();
        }

        if(Utils.cartList == null){
            Utils.cartList = new ArrayList<>();
        }


        return v;
    }

    private void getProduct() {
        compositeDisposable.add(apiShopping.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if (productModel.isSuccess()){
                                arrProduct = productModel.getResult();
                                productAdapter = new ProductAdapter(getActivity(), arrProduct);
                                recyclerViewHome.setAdapter(productAdapter);

                            }

                        },
                        throwable -> {
                            Toast.makeText(getActivity(), "Load product fail" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));


    }

    private void getCategory() {
        compositeDisposable.add(apiShopping.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if (categoryModel.isSuccess()) {
                                arrCategory = categoryModel.getResult();
                                //khoi tao Adapter
                                categoryAdapter = new CategoryAdapter(getActivity(), arrCategory);
                                listView.setAdapter(categoryAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), "API fail" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void actionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");

        for(int i=0; i< mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            Glide.with(getActivity()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);
    }

    private void anhxa(LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        viewFlipper = v.findViewById(R.id.viewlipper);
        recyclerViewHome = v.findViewById(R.id.recyclerview);





    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            }
        }

        return false;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}