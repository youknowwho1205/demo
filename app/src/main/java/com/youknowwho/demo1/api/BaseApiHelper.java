package com.youknowwho.demo1.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.youknowwho.demo1.activities.IntellimentApplication;
import com.youknowwho.demo1.api.request.BaseRequest;
import com.youknowwho.demo1.api.response.BaseResponse;
import com.youknowwho.demo1.api.response.GetLocationsResponse;
import com.youknowwho.demo1.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Core class to handle network operations on background thread in parallel
 */
public abstract class BaseApiHelper extends AsyncTask<Void, Void, ArrayList<? extends BaseResponse>> {

    private static final String TAG = "BaseApiHelper";

    // A3 base url
    private static final String BASE_URL = "http://express-it.optusnet.com.au/";

    public String mApi;
    protected String mJsonBody;
    protected ResponseHelper mResponseHelper;
    private int mApiType;
    private String mApiName;
    private int mRequestType;
    private HttpURLConnection mHttpURLConnection;
    private Context mContext;

    protected BaseApiHelper(int apiType, String apiName, int requestType, ResponseHelper
            responseHelper) {
        mApiType = apiType;
        mResponseHelper = responseHelper;
        mRequestType = requestType;
        mApiName = apiName;
        mContext = IntellimentApplication.getContext();
    }



    @Override
    protected ArrayList<? extends BaseResponse> doInBackground(Void... params) {

        String result = sendRequest();
//        if (result == null) return null;

        return retrieveResponse(result);
    }

    /**
     * @return
     */
    public String sendRequest() {

        try {

            mApi = BASE_URL + mApiName;

            Log.d(TAG, "Api:" + mApi);
            Log.d(TAG, "Request:" + mJsonBody);


            URL url = new URL(mApi);

            mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setRequestProperty("Content-Type", "application/json");
            mHttpURLConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            mHttpURLConnection.setReadTimeout(Constants.READ_TIMEOUT);
            mHttpURLConnection.setDoInput(true);

            switch (mRequestType) {

                case ApiConstants.GET:
                    mHttpURLConnection.setRequestMethod("GET");
                    mHttpURLConnection.connect();
                    break;

                case ApiConstants.POST:
                    mHttpURLConnection.setRequestMethod("POST");
                    mHttpURLConnection.setDoOutput(true);
                    OutputStream outputStream = mHttpURLConnection.getOutputStream();
                    outputStream.write(mJsonBody.getBytes("UTF-8"));
                    outputStream.flush();
                    break;
            }

            if (mHttpURLConnection == null) return null;

            if (mHttpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Response Code = " + mHttpURLConnection.getResponseCode());
                return null;
            }

            if (mHttpURLConnection.getInputStream() == null) return null;
            BufferedReader br = new BufferedReader(new InputStreamReader((mHttpURLConnection
                    .getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();

            while ((output = br.readLine()) != null) {
                builder.append(output);
                builder.append('\r');
            }

            String response = builder.toString();

            Log.d(TAG, "Response: " + response);
            //longInfo(response);

            if (mHttpURLConnection != null) mHttpURLConnection.disconnect();

            return response;
//            return null;
        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }

    /**
     * Parse response to respective object
     *
     * @param response
     * @return
     */
    private ArrayList<? extends BaseResponse> retrieveResponse(String response) {
        ArrayList<? extends BaseResponse> baseResponse = null;
        switch (mApiType) {

            case ApiConstants.GET_LOCATIONS:
                if (response != null) {
                    baseResponse = GetLocationsResponse.fromJson(response);
                } /*else {
                    baseResponse = new GetLocationsResponse();
                }*/
                break;


            default:
                break;
        }

        /*if (baseResponse != null) {
            baseResponse.setApiType(mApiType);
            baseResponse.setResponseJson(response == null ? "" : response);
        }*/

        return baseResponse;
    }



    @Override
    protected void onPostExecute(ArrayList<? extends BaseResponse> result) {
        super.onPostExecute(result);
        if (result == null) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setApiType(mApiType);
            mResponseHelper.onFail(baseResponse);
            return;
        }


        mResponseHelper.onSuccess(result);
    }


    /**
     * @param baseRequest
     */
    protected abstract void invokeAPI(BaseRequest baseRequest);


    public interface ResponseHelper {
        public void onSuccess(ArrayList<? extends BaseResponse> response);

        public void onFail(BaseResponse baseResponse);

    }
}