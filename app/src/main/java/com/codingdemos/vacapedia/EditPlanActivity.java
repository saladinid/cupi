package com.codingdemos.vacapedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codingdemos.flowers.R;
import com.codingdemos.vacapedia.data.CostsModel;
import com.codingdemos.vacapedia.data.DestinationsModel;
import com.codingdemos.vacapedia.handlers.ListAdapter;
import com.codingdemos.vacapedia.handlers.DestinationsLineAdapter;
import com.codingdemos.vacapedia.handlers.SliderAdapter;
import com.codingdemos.vacapedia.rest.AsyncHttpResponse;
import com.codingdemos.vacapedia.rest.RestApis;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditPlanActivity extends AppCompatActivity
        implements View.OnClickListener,
        AsyncHttpResponse.AsyncHttpResponseListener {
    Toolbar mToolbar;
    private static final String TAG = "EditPlanActivity";
    private EditText title;
    private EditText body_copy;
    private EditText content;
    private EditText target_date;
    private EditText target_time;
    private EditText costs;
    private EditText destinations;
    private android.app.AlertDialog.Builder alertDialogBuilder = null;
    private android.app.AlertDialog alertDialog = null;
    private RelativeLayout bn_find_a_restaurant_rl;
    private static TextView bn_find_a_restaurant_tv;
    private String id = null;
    private String titleString = null;
    private String body_copyString = null;
    private String contentString = null;
    private String target_dateString = null;
    private String target_timeString = null;
    private String costsString = null;
    private static String destinationsString = null;
    private JSONArray desPlan = null;
    private JSONArray dataDestinations = null;
    private ArrayList < DestinationsModel > destinationsArrayListBuffer;
    private ArrayList < DestinationsModel > destinationsArrayList;
    private RecyclerView mRecyclerView;
    private String restaurantName = "";
    private JSONArray costJsonArray = new JSONArray();
    private ArrayList < CostsModel > destinationsArrayListc;
    private static String nowDestinationsSelected = "";
    private LinearLayout parentLinearLayout;
    private JSONArray costList;
    private void getIntentData() {
        Intent intent = this.getIntent();
        id = intent.getStringExtra("_id");
        titleString = intent.getStringExtra("title");
        body_copyString = intent.getStringExtra("body_copy");
        contentString = intent.getStringExtra("content");
        target_dateString = intent.getStringExtra("target_date");
        target_timeString = intent.getStringExtra("target_time");
        costsString = intent.getStringExtra("costs");
        JSONArray jdes = null;
        try {
            jdes = new JSONArray(intent.getStringExtra("destinations"));
            desPlan = jdes;
            // Log.d("XXX", "jdes: " + jdes);
            StringBuilder jPlain = new StringBuilder();
            for (int z = 0; z < jdes.length(); z++) {
                JSONObject jdesob = jdes.getJSONObject(z);
                jPlain.append(jdesob.getString("_id"));
                if (z != jdes.length() - 1) {
                    jPlain.append(",");
                }
            }
            // Log.d("XXX", "jPlain: " + jPlain);
            destinationsString = jPlain.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Log.d(TAG, "target_dateString = [" + target_dateString + "]");
        // Log.d(TAG, "target_timeString = [" + target_timeString + "]");
        // Log.d(TAG, "destinationsString = [" + destinationsString + "]");
        // Log.d(TAG, "costsString = [" + costsString + "]");
        JSONArray constList = new JSONArray();
        try {
            constList = new JSONArray(costsString);
            destinationsArrayListc = new ArrayList < > ();
            destinationsArrayListc.clear();
            for (int j = 0; j < constList.length(); j++) {
                JSONObject constItem = constList.getJSONObject(j);
                CostsModel modelc = new CostsModel();
                modelc.set_id(constItem.optString("_id"));
                modelc.setName(constItem.optString("name"));
                modelc.setCost(constItem.optString("cost"));
                destinationsArrayListc.add(modelc);
            }
            costJsonArray = constList;
            costsString = String.valueOf(costJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onAddFieldFill(String name, String cost) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        EditText currentName = rowView.findViewById(R.id.text_edit_text);
        EditText currentCost = rowView.findViewById(R.id.number_edit_text);
        currentName.setText(name);
        currentCost.setText(cost);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        // Log.d("LOG", "count >>>>>>>>> " + String.valueOf(Integer.parseInt(String.valueOf(parentLinearLayout.getChildCount())) - 1));
    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        // Log.d("LOG", "count >>>>>>>>> " + String.valueOf(Integer.parseInt(String.valueOf(parentLinearLayout.getChildCount())) - 1));
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
        // Log.d("LOG", "count >>>>>>>>> " + String.valueOf(Integer.parseInt(String.valueOf(parentLinearLayout.getChildCount())) - 1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_edit);
        mToolbar = findViewById(R.id.toolbar);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mToolbar.setTitle("Note");
        initUI();
    }

    @SuppressLint({
            "LongLogTag",
            "SimpleDateFormat"
    })
    private void initUI() {
        getIntentData();
        try {
            for (int j = 0; j < costJsonArray.length(); j++) {
                JSONObject costItem = costJsonArray.getJSONObject(j);
                onAddFieldFill(costItem.getString("name"), costItem.getString("cost"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView dd_booking_form_tv = (TextView) findViewById(R.id.dd_booking_form_tv);
        dd_booking_form_tv.setOnClickListener(this);
        nowDestinationsSelected = destinationsString;
        bn_find_a_restaurant_rl = (RelativeLayout) findViewById(R.id.bn_find_a_restaurant_rl);
        bn_find_a_restaurant_tv = findViewById(R.id.bn_find_a_restaurant_tv);
        bn_find_a_restaurant_rl.setOnClickListener(this);
        title = (EditText) findViewById(R.id.title);
        body_copy = (EditText) findViewById(R.id.body_copy);
        content = (EditText) findViewById(R.id.content);
        target_date = (EditText) findViewById(R.id.target_date);
        target_time = (EditText) findViewById(R.id.target_time);
        costs = (EditText) findViewById(R.id.costs);
        destinations = (EditText) findViewById(R.id.destinations);
        bn_find_a_restaurant_tv.setText(restaurantName);
        title.setText(titleString);
        body_copy.setText(body_copyString);
        content.setText(contentString);
        target_date.setText(target_dateString);
        target_time.setText(target_timeString);
        destinations.setText(nowDestinationsSelected);
        getKarmaGroupsApiRequest();
    }

    /*
     * AlertDialog for Validation Form
     */
    private void alertWithOk(final Context context, String message) {
        Log.d(TAG, "alertWithOk() called with:  message = [" + message + "]");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        }
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("LongLogTag")
    private void bookValidations() {
        final AsyncHttpResponse responseValidation = new AsyncHttpResponse(this, true);
        if (title.getText() == null || title.length() == 0) {
            alertWithOk(this, "please provide title!");
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                alertDialogBuilder = new AlertDialog.Builder(this);
            }
            afterSuccess();
            synchronized(responseValidation) {
                alertForSuccessfulBookingEnquiry("Thank you, your submission has been sent.");
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void afterSuccess() {
        postBookingRequestJSONApiRequest();
    }

    @SuppressLint("LongLogTag")
    private void postBookingRequestJSONApiRequest() {
        final AsyncHttpResponse response = new AsyncHttpResponse(this, true);
        JSONObject jobjContactDetails = null;
        JSONArray dest = new JSONArray();
        String[] animalsArray = String.valueOf(destinations.getText()).trim().split("\\s*,\\s*");
        for (int i = 0; i < animalsArray.length; i++) {
            dest.put(animalsArray[i]);
        }
        try {
            jobjContactDetails = new JSONObject();
            jobjContactDetails.put("title", String.valueOf(title.getText()).trim());
            jobjContactDetails.put("body_copy", String.valueOf(body_copy.getText()).trim());
            jobjContactDetails.put("content", String.valueOf(content.getText()).trim());
            jobjContactDetails.put("target_date", String.valueOf(target_date.getText()).trim());
            jobjContactDetails.put("target_time", String.valueOf(target_time.getText()).trim());
            jobjContactDetails.put("costs", costList);
            jobjContactDetails.put("destinations", dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this);
        }
        final JSONObject finalJobjContactDetails = jobjContactDetails;
        Log.d(TAG, "finalJobjContactDetails: " + finalJobjContactDetails);
        response.putJson(RestApis.KarmaGroups.vacapediaPlans + "/" + id, finalJobjContactDetails);
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("LongLogTag")
    private void alertForSuccessfulBookingEnquiry(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this);
        }
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent in = new Intent(EditPlanActivity.this, ListPlansActivity.class);
                        EditPlanActivity.this.startActivity( in );
                        EditPlanActivity.this.finish();
                        Toast.makeText(EditPlanActivity.this, "Thank You", Toast.LENGTH_LONG).show();
                    }
                });
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dd_booking_form_tv:
                costList = new JSONArray();
                int parentLong = Integer.parseInt(String.valueOf(parentLinearLayout.getChildCount())) - 1;
                for (int k = 0; k < parentLong; k++) {
                    try {
                        View currentView = parentLinearLayout.getChildAt(k);
                        EditText currentEditName = currentView.findViewById(R.id.text_edit_text);
                        EditText currentEditCost = currentView.findViewById(R.id.number_edit_text);
                        if (!currentEditName.getText().toString().equals("") || !currentEditCost.getText().toString().equals("")) {
                            JSONObject costObj = new JSONObject("{" +
                                    "\"name\":\"" + currentEditName.getText() + "\"," +
                                    "\"cost\":\"" + currentEditCost.getText() + "\"" +
                                    "}");
                            // Log.d(TAG, k + " k >>>>>>>> : " + costObj);
                            costList.put(costObj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                bookValidations();
                break;
            case R.id.bn_find_a_restaurant_rl:
                hideKeyboard();
                displayPopupWindow(bn_find_a_restaurant_rl);
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    private void displayPopupWindow(View v) {
        PopupWindow popupwindow_obj = null;
        try {
            popupwindow_obj = popupDisplay();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (popupwindow_obj != null) {
            popupwindow_obj.showAsDropDown(v, -40, 0, Gravity.CENTER_HORIZONTAL); // where u want show on view click event popupwindow.showAsDropDown(view, x, y);
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onItemClick(AdapterView < ? > arg0, View arg1, int position, long arg3) {
            // TODO Auto-generated method stub
        }
    };

    private PopupWindow popupDisplay() throws JSONException {
        final PopupWindow popupWindow = new PopupWindow(this);
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int dpValue = 40; // margin in dips
        float d = this.getResources().getDisplayMetrics().density;
        int margin = (int)(dpValue * d);
        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.layout_restaurant_dropdown, null);
        }
        JSONArray jsonArray = dataDestinations;
        ArrayList < JSONObject > listItems = getArrayListFromJSONArray(jsonArray);
        ListView listV = (ListView) view.findViewById(R.id.listv);
        ListAdapter adapter = new ListAdapter(this, R.layout.list_layout, R.id.karma_resorts_item, listItems);
        listV.setAdapter(adapter);
        final ListView listv = listV;
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView < ? > parent, View view, int position, long id) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(listv.getItemAtPosition(position)));
                    String selectedName = obj.getString("name");
                    String selectedId = obj.getString("_id");
                    bn_find_a_restaurant_tv.setText(selectedName);
                    nowDestinationsSelected = "";
                    nowDestinationsSelected = destinationsString + ", " + selectedId;
                    destinations.setText(nowDestinationsSelected);
                    popupWindow.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        return popupWindow;
    }

    private ArrayList < JSONObject > getArrayListFromJSONArray(JSONArray jsonArray) {
        ArrayList < JSONObject > aList = new ArrayList < JSONObject > ();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return aList;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void processData() {
        try {
            JSONArray dataJson = desPlan;
            destinationsArrayListBuffer = new ArrayList < > ();
            destinationsArrayList = new ArrayList < > ();
            mRecyclerView = findViewById(R.id.recyclerview);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(EditPlanActivity.this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            destinationsArrayList.clear();
            JSONArray jarry = dataJson;
            ArrayList < DestinationsModel > dma = new ArrayList < > ();
            dma.clear();
            for (int j = 0; j < jarry.length(); j++) {
                JSONObject job = jarry.getJSONObject(j);
                DestinationsModel model = new DestinationsModel();
                model.setMenuID(String.valueOf(j));
                model.setMenuName("nama" + j);
                model.setName(job.optString("name"));
                model.setPostID(job.optString("id"));
                model.setImage(job.optString("image"));
                model.set_id(job.optString("_id"));
                model.setCategory(job.optString("category"));
                model.setLocation(job.optString("location"));
                model.setDescription(job.optString("description"));
                model.setLatitude(job.optString("latitude"));
                model.setLongitude(job.optString("longitude"));
                model.setAddress(job.optString("address"));
                model.setDistance(job.optString("distance"));
                model.setNote(job.optString("note"));
                model.setCosts(job.optString("costs"));
                model.setTotal_cost(job.optString("total_cost"));
                dma.add(model);
                destinationsArrayList.add(model);
            }
            destinationsArrayListBuffer = destinationsArrayList;
            DestinationsLineAdapter myAdapter = new DestinationsLineAdapter(EditPlanActivity.this, destinationsArrayListBuffer);
            mRecyclerView.setAdapter(myAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getKarmaGroupsApiRequest() {
        AsyncHttpResponse response = new AsyncHttpResponse(this, false);
        RequestParams params = new RequestParams();
        response.getAsyncHttp(RestApis.KarmaGroups.vacapediaDestinations, params);
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String url) throws JSONException {
        Log.d("TAG", "onAsyncHttpResponseGet() called with: response = [" + response + "], url = [" + url + "]");
        if (url.equals(RestApis.KarmaGroups.vacapediaDestinations)) {
            Log.d("TAG", "x onAsyncHttpResponseGet() called with: response = [" + response + "], url = [" + url + "]");
            dataDestinations = new JSONArray(response);
            processData();
        }
    }

}