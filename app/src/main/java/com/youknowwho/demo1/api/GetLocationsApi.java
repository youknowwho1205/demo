package com.youknowwho.demo1.api;

import com.youknowwho.demo1.api.request.BaseRequest;

/**
 * Api class for GetLocations
 */

public class GetLocationsApi extends BaseApiHelper {

    private static final String GET_LOCATIONS_API = "sample.json";

    public GetLocationsApi(ResponseHelper responseHelper) {
        super(ApiConstants.GET_LOCATIONS, GET_LOCATIONS_API, ApiConstants.GET, responseHelper);
    }

    @Override
    public void invokeAPI(BaseRequest baseRequest) {
        mJsonBody = baseRequest.toJson();

        executeOnExecutor(THREAD_POOL_EXECUTOR);
    }
}
