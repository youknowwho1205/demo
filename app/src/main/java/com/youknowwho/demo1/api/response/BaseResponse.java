package com.youknowwho.demo1.api.response;

public class BaseResponse {


    private int mApiType;

    private String mResponseJson;

    /*public static List<BaseResponse> fromJson(String pResult) {

        Gson lGson = new Gson();

        try {
            Type listType = new TypeToken<List<BaseResponse>>() {}.getType();
            return lGson.fromJson(pResult, listType);
        } catch (JsonSyntaxException e) {
            return null;
        }

    }*/

    public int getApiType() {
        return mApiType;
    }

    public void setApiType(int mApiType) {
        this.mApiType = mApiType;
    }


    public String getResponseJson() {
        return mResponseJson;
    }

    public void setResponseJson(String responseJson) {
        mResponseJson = responseJson;
    }
}