package com.ssg.tracker.controller;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.ssg.tracker.R;
import com.ssg.tracker.model.BazarInfoModel;
import com.ssg.tracker.model.DetailsWorkModel;
import com.ssg.tracker.model.LoginResponse;
import com.ssg.tracker.model.ProductCatModel;
import com.ssg.tracker.ratingbar.SmileRatingBar;
import com.ssg.tracker.restapi.APIClient;
import com.ssg.tracker.restapi.Config;
import com.ssg.tracker.restapi.SSGInfo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsInfoController extends AppCompatActivity implements View.OnClickListener {
    SliderView sliderView;
    ImageView close_view, prevous_view, nextView;
    int cickedposition = 0;
    int totalcount;
    //ArrayList<BazarInfoModel> arrayList;
    String cat_id = "";
    SSGInfo ssgInterface;
    ProductCatModel model;
    ImageView pwdImage;
    TextView title_txt, desc_text, pwdTxt;
    RelativeLayout instruction_click, terms_click, claim_click, visit_click;
    SmileRatingBar ratinfview;
    SpinKitView loadinview;
    TextView click_visit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_view);
        loadViewObject();
        loadinview.setVisibility(View.VISIBLE);
        getProducatDetails();


    }

    private void loadViewObject() {
        click_visit = findViewById(R.id.click_visit);
        loadinview = findViewById(R.id.loadinview);
        ratinfview = findViewById(R.id.ratinfview);
        close_view = findViewById(R.id.close_view);
        prevous_view = findViewById(R.id.prevous_view);
        nextView = findViewById(R.id.nextView);
        pwdTxt = findViewById(R.id.pwdTxt);
        desc_text = findViewById(R.id.desc_text);
        title_txt = findViewById(R.id.title_txt);

        prevous_view.setOnClickListener(this);
        pwdImage = findViewById(R.id.pwdImage);
        nextView.setOnClickListener(this);
        instruction_click = findViewById(R.id.instruction_click);
        terms_click = findViewById(R.id.terms_click);
        claim_click = findViewById(R.id.claim_click);
        visit_click = findViewById(R.id.visit_click);
        instruction_click.setOnClickListener(this);
        terms_click.setOnClickListener(this);
        claim_click.setOnClickListener(this);
        visit_click.setOnClickListener(this);
        getBackData();
        ratinfview.setOnRatingChangeListener(new SmileRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(SmileRatingBar ratingBar, float rating, boolean fromUser) {
                ratingsend(String.valueOf(ratingBar.getRating()));
            }
        });
        close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        click_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model!=null){
                    if(isNetworkConnected()){
                        Intent visit = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getWeb_link()));
                        startActivity(visit);
                    }else{
                        Intent intent = new Intent(ItemsInfoController.this, DashBoardView.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }
        });
    }

    private void getBackData() {
        cat_id = getIntent().getStringExtra("cat_id");
        if (cat_id == null || cat_id.isEmpty()) {
            cat_id = "1";
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_view:
                finish();

                break;
            case R.id.prevous_view:
                System.out.println("cickedposition : " + cickedposition);
                if (cickedposition == totalcount) {
                    cickedposition = 0;
                } else {
                    if (cickedposition < 0) {
                        cickedposition = totalcount;
                        sliderView.setCurrentPagePosition(cickedposition);
                    } else {
                        cickedposition = cickedposition - 1;
                        sliderView.setCurrentPagePosition(cickedposition);
                    }


                }
                break;
            case R.id.nextView:
                System.out.println("cickedposition : " + cickedposition);
                if (cickedposition == totalcount) {
                    cickedposition = 0;
                } else {
                    if (cickedposition > totalcount) {
                        cickedposition = totalcount;
                        sliderView.setCurrentPagePosition(cickedposition);
                    } else {
                        cickedposition = cickedposition + 1;
                        sliderView.setCurrentPagePosition(cickedposition);
                    }


                }

                break;
            case R.id.instruction_click:
                new AlertDialog.Builder(ItemsInfoController.this)
                        .setTitle("Instruction")
                        .setMessage(android.text.Html.fromHtml(model.getInstruction()).toString())

                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.terms_click:
                if(isNetworkConnected()){
//                    Intent terms = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getTerms()));
//                    startActivity(terms);
                    readWebpage(model.getTerms());
//                    showStpInfo(fullString);

                }else{
                    Intent intent = new Intent(ItemsInfoController.this, DashBoardView.class);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.claim_click:
                if(isNetworkConnected()){
                    clainOfferNow();
                }else{
                    Intent intent = new Intent(ItemsInfoController.this, DashBoardView.class);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.visit_click:
                if(model!=null){
                    if(isNetworkConnected()){
                        Intent visit = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getWeb_link()));
                        startActivity(visit);
                    }else{
                        Intent intent = new Intent(ItemsInfoController.this, DashBoardView.class);
                        startActivity(intent);
                        finish();
                    }
                }

                break;
        }
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String page = urls[0];
            //Connecting to the web page
            Connection conn = Jsoup.connect(page);
            //executing the get request
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();e.printStackTrace();
            }
            //Retrieving the contents (body) of the web page
            Elements elements = doc.select("h1,h2,h3,h4,h5");
            StringBuilder sb = null;
            for (Element element : elements) {
                 sb = new StringBuilder(element.toString());

                Element next = element.nextElementSibling();
                while (next != null && !next.tagName().startsWith("h")) {
                    sb.append(next.toString()).append("\n");
                    next = next.nextElementSibling();
                }
                System.out.println(sb);
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Spanned strToHtml = Html.fromHtml(result);
            String formattedText = strToHtml.toString();
            showStpInfo(formattedText);
//            textView.setText(Html.fromHtml(result));
        }
    }

    public void readWebpage(String parse) {
        Spanned strToHtml = Html.fromHtml(parse);
        String formattedText = strToHtml.toString();
        System.out.println("link : "+parse);
//        if(formattedText.length()>0&&formattedText!=null&&!formattedText.isEmpty()&&formattedText.contains("http")){
//            DownloadWebPageTask task = new DownloadWebPageTask();
//            task.execute(new String[] {formattedText});
//        }else{
            showStpInfo(formattedText);
//        }

    }
    private void showStpInfo(String text) {
        LayoutInflater inflater = LayoutInflater.from(ItemsInfoController.this);

        View dialogView = inflater.inflate(R.layout.termcondition_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemsInfoController.this);
        builder.setView(dialogView);

//        builder.setPositiveButton(
//                "Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

        AlertDialog alertDialog = builder.create();
        TextView textView= dialogView.findViewById(R.id.top);
        textView.setText(text);
//        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.login_back);
//        Button buttonbackground = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        if(buttonbackground!=null){
//            buttonbackground.setBackgroundColor(Color.WHITE);
//        }

        RelativeLayout close_view = dialogView.findViewById(R.id.close_view);
        close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
    public class SliderAdapterExample extends
            SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        private Context context;
        private int mCount;

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        public void setCount(int count) {
            this.mCount = count;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_info_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
                }
            });

            Glide.with(viewHolder.itemView)
                    .load(model.getPro_image().get(position))
                    .into(viewHolder.imageViewBackground);


        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return mCount;
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                this.itemView = itemView;
            }
        }


    }

    private void getProducatDetails() {
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        callworkoutelement = ssgInterface.getproductDetails(Config.API_KEY, Config.APP_ID, cat_id);
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("ProducatDetails jsonObject : " + jsonObject);
                parseworout(jsonObject);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }

    private void parseworout(String object) {
        try {
            ArrayList<String> imagelist = new ArrayList<>();
            JSONObject mainObjectdata = new JSONObject(object);
            JSONObject data = mainObjectdata.getJSONObject("data");
            System.out.println("data detail : "+data.toString());
            String powerby_img = data.getString("powerby_img");
            String powerby_link = data.getString("powerby_link");
            String description = data.getString("description");
            System.out.println("description : "+description);
            description= android.text.Html.fromHtml(description).toString();
            String web_link = data.getString("web_link");
            String prod_name = data.getString("prod_name");
            String terms = data.getString("terms");
            String instruction = data.getString("instruction");
            String price = data.getString("price");
            String cat_id = data.getString("cat_id");
            String id = data.getString("id");
            JSONArray pro_image = data.getJSONArray("pro_image");
            for (int i = 0; i < pro_image.length(); i++) {
                JSONObject mainobjec = pro_image.getJSONObject(i);
                imagelist.add(mainobjec.getString("url"));
            }
            model = new ProductCatModel(powerby_img, powerby_link, imagelist, description, web_link, prod_name, terms, instruction, price, cat_id, id);
            showVIew();
            loadinview.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showVIew() {

        sliderView = findViewById(R.id.imageSlider);
        totalcount = model.getPro_image().size();
        final SliderAdapterExample adapter = new SliderAdapterExample(this);
        adapter.setCount(model.getPro_image().size());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                cickedposition = position;
                sliderView.setCurrentPagePosition(position);
            }
        });
        title_txt.setText(model.getProd_name());
        desc_text.setText(model.getDescription());
        Glide.with(ItemsInfoController.this)
                .load(model.getPowerby_img())
                .into(pwdImage);

    }

    private void clainOfferNow() {
        loadinview.setVisibility(View.VISIBLE);
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;
        LoginResponse loginResponse = (LoginResponse) getPreferenceObjectJson(ItemsInfoController.this, "response");
        String userId = null;
        if (loginResponse != null) {
            userId = loginResponse.getUserId();
        }
        String useridvalue = PreferenceManager.getDefaultSharedPreferences(ItemsInfoController.this).getString("userId", "novalue");
//        System.out.println("clainOfferNow "+cat_id+"::::"+ model.getPrice()+"::::"+ model.getId()+"::::"+ model.getCat_id()+":::::"+ model.getId());
        callworkoutelement = ssgInterface.claimOffer(Config.API_KEY, Config.APP_ID, userId, model.getPrice(), model.getId());
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("clainOfferNow jsonObject : " + jsonObject);
                try {
                    JSONObject mainobject = new JSONObject(jsonObject);
                    if (mainobject.getInt("response_code") == 500) {
                        loadinview.setVisibility(View.GONE);
                        Toast.makeText(ItemsInfoController.this, "Insufficient coin ", Toast.LENGTH_SHORT).show();

//                        showConfermationAlert("cuponCode");

                    } else {
                        JSONObject data = mainobject.getJSONObject("data");
                        showConfermationAlert(data.getString("offer_code"));
//                        showCuponInAlert(data.getString("offer_code"));
                        //Toast.makeText(ItemsInfoController.this, "You have successfully claim your offer. ", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
    private void showConfermationAlert(String cuponCode) {
        loadinview.setVisibility(View.GONE);
        new AlertDialog.Builder(ItemsInfoController.this)
                .setTitle("Claim Offer")
                .setMessage("Are you sure you want to claim this offer")

                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        showCuponInAlert(cuponCode);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.offer))
                .show();
    }
    private void showCuponInAlert(String cuponCode) {
        loadinview.setVisibility(View.GONE);
        new AlertDialog.Builder(ItemsInfoController.this)
                .setTitle("Get Cupon")
                .setMessage("Please copy cupon code by click ok : "+cuponCode)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setPrimaryClip(ClipData.newPlainText("text", cuponCode));
                        Toast.makeText(ItemsInfoController.this, "Text Copied!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.offer))
                .show();
    }

    private void ratingsend(String value) {
        loadinview.setVisibility(View.VISIBLE);
        ssgInterface = APIClient.getClient().create(SSGInfo.class);
        Call<JsonElement> callworkoutelement;

        callworkoutelement = ssgInterface.submitRation(Config.API_KEY, Config.APP_ID, cat_id, model.getId(), value);
        callworkoutelement.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //System.out.println("Response body: " + response.body());
                Gson gson = new Gson();
                String jsonObject = gson.toJson(response.body());
                System.out.println("jsonObject : " + jsonObject);
                try {
                    loadinview.setVisibility(View.GONE);
                    JSONObject mainobject = new JSONObject(jsonObject);
                    if (mainobject.getInt("response_code") == 500) {
                        Toast.makeText(ItemsInfoController.this, "Getting error ", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(ItemsInfoController.this, "You have successfully updated. ", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
    //    check Intetnet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
//    get Login responce
    public Object getPreferenceObjectJson(Context c, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                c.getApplicationContext());
        /**** get user data    *****/
        String json = appSharedPrefs.getString("response", "nodata");
        System.out.println("dashboard  called json : " + json);
        if (json.contains("nodata")) {
            return null;
        }
        Gson gson = new Gson();
        LoginResponse selectedUser = gson.fromJson(json, LoginResponse.class);
        return selectedUser;
    }
}
