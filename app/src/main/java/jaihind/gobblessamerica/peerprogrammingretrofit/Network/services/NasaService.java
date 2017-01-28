package jaihind.gobblessamerica.peerprogrammingretrofit.Network.services;

import jaihind.gobblessamerica.peerprogrammingretrofit.Model.Nasa;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nande on 1/23/2017.
 */

public interface NasaService {


    @GET("planetary/apod")
    Call<Nasa> gettodayPicture(@Query("api_key") String apiKey);

}
