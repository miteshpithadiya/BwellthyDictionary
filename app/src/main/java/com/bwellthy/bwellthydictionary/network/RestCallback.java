package com.bwellthy.bwellthydictionary.network;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by miteshp on 13/01/2016.
 */
public abstract class RestCallback<T> implements Callback<T> {
    public abstract void failure(RestError restError);

    @Override
    public void success(T t, Response response) {
    }

    @Override
    public void failure(RetrofitError error) {
        RetrofitError.Kind kind = error.getKind();

        if (kind.equals(RetrofitError.Kind.CONVERSION)) {
            failure(new RestError("Error occurred while reading the data!"));
        } else if (kind.equals(RetrofitError.Kind.HTTP)) {
            switch (error.getResponse().getStatus()) {
                case 401:
                    failure(new RestError("Your session has expired!"));
                    break;
                case 403:
                    failure(new RestError("Your session has expired!"));
                    break;
                case 404:
                    failure(new RestError("The requested resource is not available!"));
                    break;
                case 405:
                    failure(new RestError("The request method is not supported!"));
                    break;
                case 408:
                    failure(new RestError("The server timed out waiting for the request!"));
                    break;
                case 500:
                    failure(new RestError("Some error occurred at the server!"));
                    break;
            }
        } else if (kind.equals(RetrofitError.Kind.NETWORK)) {
            failure(new RestError("An error occurred while communicating to the server."));
        } else if (kind.equals(RetrofitError.Kind.UNEXPECTED)) {
            failure(new RestError("An internal error occurred."));
        } else {
            failure(new RestError("An unknown error occurred."));
        }
    }
}
