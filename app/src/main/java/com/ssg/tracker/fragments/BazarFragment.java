package com.ssg.tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssg.tracker.R;
import com.ssg.tracker.controller.DashBoardView;
import com.ssg.tracker.controller.FriendsController;
import com.ssg.tracker.controller.ItemsInfoController;
import com.ssg.tracker.controller.UserController;
import com.ssg.tracker.controller.WorkOutDetailsView;
import com.ssg.tracker.model.BazarInfoModel;
import com.ssg.tracker.model.SectionModel;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BazarFragment extends Fragment {
    SpinKitView loadinview;
    SSGInfo ssgInterface;

    RecyclerView sectioned_recycler_view;
    RelativeLayout friends_click, user_click;

    public BazarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bazar_list, container, false);
        getVisisbleViews(view);
        return view;
    }

    private void getVisisbleViews(View view) {
        sectioned_recycler_view = view.findViewById(R.id.bazar_new_list);
        friends_click = view.findViewById(R.id.friends_click);
        user_click = view.findViewById(R.id.user_click);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sectioned_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        sectioned_recycler_view.setLayoutManager(linearLayoutManager);
        loadBazarData();
        user_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    Intent intent = new Intent(getActivity(), UserController.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
//                    showToastMessage("Please check your internet connection");
                }

            }
        });
        friends_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    Intent intent = new Intent(getActivity(), FriendsController.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), DashBoardView.class);
                    startActivity(intent);
                    getActivity().finish();
//                    showToastMessage("Please check your internet connection");
                }
            }
        });
    }

    private void loadBazarData() {
//        ArrayList<SectionModel> sectionModelArrayList = new ArrayList<>();
//        //for loop for sections
//        for (int i = 1; i <= 3; i++) {
//            ArrayList<BazarInfoModel> itemArrayList = new ArrayList<>();
//            //for loop for items
//            for (int j = 1; j <= 6; j++) {
//                itemArrayList.add(new BazarInfoModel("12", "OriFlame", "https://media-asia-cdn.oriflame.com/-/media/IN/Images/Catalog/Products/34413.ashx?u=1805230337&q=70", "https://media-asia-cdn.oriflame.com/-/media/IN/Images/Catalog/Products/34413.ashx?u=1805230337&q=70", "Soap"));
//            }
//
//            //add the section and items to array list
//            sectionModelArrayList.add(new SectionModel("Popular " + i, itemArrayList));
//        }
        getProducts();


    }

    public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder> {


        class SectionViewHolder extends RecyclerView.ViewHolder {
            private TextView sectionLabel, showAllButton;
            private RecyclerView itemRecyclerView;

            public SectionViewHolder(View itemView) {
                super(itemView);
                sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
                showAllButton = (TextView) itemView.findViewById(R.id.section_show_all_button);
                itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.sectioned_recycler_view);
            }
        }

        private Context context;
        private ArrayList<SectionModel> sectionModelArrayList;

        public SectionRecyclerViewAdapter(Context context, ArrayList<SectionModel> sectionModelArrayList) {
            this.context = context;
            this.sectionModelArrayList = sectionModelArrayList;
        }

        @Override
        public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bazar_view, parent, false);
            return new SectionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SectionViewHolder holder, int position) {
            final SectionModel sectionModel = sectionModelArrayList.get(position);
            holder.sectionLabel.setText(sectionModel.getSectionLabel());

            //recycler view for items
            holder.itemRecyclerView.setHasFixedSize(true);
            holder.itemRecyclerView.setNestedScrollingEnabled(false);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            holder.itemRecyclerView.setLayoutManager(gridLayoutManager);
            ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(context, sectionModel.getItemArrayList());
            holder.itemRecyclerView.setAdapter(adapter);

            //show toast on click of show all button
            holder.showAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You clicked on Show All of : " + sectionModel.getSectionLabel(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return sectionModelArrayList.size();
        }


    }

    public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView itemLabel;
            ImageView imageView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                itemLabel = (TextView) itemView.findViewById(R.id.item_label);
                imageView = itemView.findViewById(R.id.item_image);
            }
        }

        private Context context;
        private ArrayList<BazarInfoModel> arrayList;

        public ItemRecyclerViewAdapter(Context context, ArrayList<BazarInfoModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bazar_info, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            BazarInfoModel modelBazar = arrayList.get(position);
            holder.itemLabel.setText(modelBazar.getuName());
            String imageUrl = modelBazar.getuImage();
            Glide.with(getActivity()).load(imageUrl).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkConnected()){
                        Intent intent = new Intent(getActivity(), ItemsInfoController.class);
                        intent.putParcelableArrayListExtra("product", arrayList);
                        intent.putExtra("cat_id",modelBazar.getId());
                        intent.putExtra("pos", position);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), DashBoardView.class);
                        startActivity(intent);
                        getActivity().finish();
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }


    private void getProducts() {

        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getproduct(Config.API_KEY, Config.APP_ID);
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject getProducts : " + jsonObject);
                getProduct(jsonObject);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void getProduct(String jsonObject) {
        ArrayList<SectionModel> sectionModelArrayList = new ArrayList<>();

        try {
            // loadinview.setVisibility(View.GONE);
            JSONObject mainobject = new JSONObject(jsonObject);
            JSONArray data = mainobject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject mainobjec = data.getJSONObject(i);
                ArrayList<BazarInfoModel> itemArrayList = new ArrayList<>();
                //for loop for items
                JSONArray products = mainobjec.getJSONArray("products");
                for (int j = 0; j < products.length(); j++) {
                    JSONObject proobj = products.getJSONObject(j);
                    System.out.println("santi called :"+j);
                    itemArrayList.add(new BazarInfoModel(proobj.getString("id"), proobj.getString("prod_name"), proobj.getString("prod_img1"), proobj.getString("prod_img2"), proobj.getString("instruction")));
                }
                if(products.length()>0){
                    sectionModelArrayList.add(new SectionModel(mainobjec.getString("cat_name"), itemArrayList));

                }

                //add the section and items to array list

            }
            SectionRecyclerViewAdapter adapter = new SectionRecyclerViewAdapter(getActivity(), sectionModelArrayList);
            sectioned_recycler_view.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible&&getActivity()!=null) {
            //Do your stuff here
            if(isNetworkConnected()){
//                userId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("userId", "novalue");
//                getProfile();
//                getDashBoard();
            }else{
                Intent intent = new Intent(getActivity(), DashBoardView.class);
                startActivity(intent);
                getActivity().finish();
//                showToastMessage("Please check your internet connection");
            }

        }

        super.setMenuVisibility(visible);
    }
    //    check Intetnet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
