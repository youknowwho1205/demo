package com.youknowwho.demo1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.youknowwho.demo1.activities.BaseActivity;
import com.youknowwho.demo1.api.BaseApiHelper;
import com.youknowwho.demo1.api.GetLocationsApi;
import com.youknowwho.demo1.api.request.BaseRequest;
import com.youknowwho.demo1.api.response.BaseResponse;
import com.youknowwho.demo1.api.response.GetLocationsResponse;
import com.youknowwho.demo1.api.response.models.Fromcentral;
import com.youknowwho.demo1.api.response.models.Location;
import com.youknowwho.demo1.utils.DialogManager;
import com.youknowwho.demo1.utils.Utility;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements BaseApiHelper.ResponseHelper,AdapterView.OnItemSelectedListener, View.OnClickListener {

    private ProgressDialog mProgressDialog;
    private ArrayList<GetLocationsResponse> listLocations;
    private LinearLayout layCar;
    private LinearLayout layTrain;
    private TextView txtCar;
    private TextView txtCarTime;
    private TextView txtTrain;
    private TextView txtTrainTime;
    private Button btnNavigate;
    private GetLocationsResponse getLocationsResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        callGetLocationsApi();


    }

    /**
     * Method to populate Spinner with values from server
     */
    private void populateSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> listNames = new ArrayList<>();
        for (int i = 0; i < listLocations.size(); i++) {

            listNames.add(listLocations.get(i).getName());

        }

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listNames);

        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(locationAdapter);

        spinner.setOnItemSelectedListener(this);
    }

    private void initUI() {

        layCar = (LinearLayout)findViewById(R.id.lay_car);
        layTrain = (LinearLayout)findViewById(R.id.lay_train);

        txtCar = (TextView)findViewById(R.id.txt_car);
        txtCarTime = (TextView)findViewById(R.id.txt_car_time);
        txtTrain = (TextView)findViewById(R.id.txt_train);
        txtTrainTime = (TextView)findViewById(R.id.txt_train_time);

        btnNavigate = (Button)findViewById(R.id.btn_navigate);
        btnNavigate.setOnClickListener(this);

    }


    /**
     * Method to call GetLocationsApi
     */
    private void callGetLocationsApi() {

        if (Utility.isConnectedToInternet(this)) {

            mProgressDialog = ProgressDialog.show(this, "", "Loading");

            GetLocationsApi getLocationsApi = new GetLocationsApi(this);
            getLocationsApi.invokeAPI(new BaseRequest(this));

        } else {
            // OFFLINE MODE
            DialogManager.showErrorDialog(this, "", getString(R.string
                    .no_internet_connection));
        }
    }

    /**
     * Call back method of success from server
     * @param response response from server
     */
    @Override
    public void onSuccess(ArrayList<? extends BaseResponse> response) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();

        if(response != null){

            if(response.size() > 0) {

                if (response.get(0) instanceof GetLocationsResponse) {

                    listLocations = (ArrayList<GetLocationsResponse>) response;

                    populateSpinner();




                }

            }else {
                DialogManager.showErrorDialog(this, "", "No Records Found!");
            }
        }else {

            DialogManager.showErrorDialog(this, "", getResources().getString(R.string.req_timed_out));
        }





    }

    @Override
    public void onFail(BaseResponse baseResponse) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        getLocationsResponse = listLocations.get(position);

        Fromcentral fromcentral = getLocationsResponse.getFromcentral();
        if(fromcentral.getCar() != null){
            layCar.setVisibility(View.VISIBLE);
            txtCar.setText("Car");
            txtCarTime.setText(fromcentral.getCar());
        }else {
            layCar.setVisibility(View.GONE);
        }

        if(fromcentral.getTrain() != null){
            layTrain.setVisibility(View.VISIBLE);
            txtTrain.setText("Train");
            txtTrainTime.setText(fromcentral.getTrain());
        }else {
            layTrain.setVisibility(View.GONE);
        }
        System.out.println();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {

        view.startAnimation(new AlphaAnimation(1f, 0.5f));

        switch (view.getId()){

            case R.id.btn_navigate:

                if(getLocationsResponse == null){
                    getLocationsResponse = listLocations.get(0);
                }

                displayMap();

                break;
        }

    }

    /**
     * Method to display Map with the location values fetched from the server
     */
    private void displayMap() {
        Location location = getLocationsResponse.getLocation();

        String label = getLocationsResponse.getName();
        String uriLoc = "geo:" + location.getLatitude() + "," + location.getLongitude();
        String query = location.getLatitude() + "," + location.getLongitude() + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriLoc + "?q=" + encodedQuery;
        Uri uri = Uri.parse(uriString);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
