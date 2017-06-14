package com.br.italofeitosa.quicktranslate.retrofit;

import com.br.italofeitosa.quicktranslate.model.Resouce;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author italofeitosa on 14/06/17.
 */
public interface ResouceService {

    String REQUEST = "get_resources_since";

    @GET(REQUEST)
    Call<Resouce> getResourcesSince();
}
