package jaihind.gobblessamerica.peerprogrammingretrofit.Network;

import jaihind.gobblessamerica.peerprogrammingretrofit.Network.services.NasaService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nande on 1/23/2017.
 */

public class NetworkManager {
    public static final String Base_Url="https://api.nasa.gov/";

    private Retrofit mRetrofit;

    private NasaService mNasaService;

    public NetworkManager(){
        mRetrofit= new Retrofit.Builder().baseUrl(Base_Url).addConverterFactory(GsonConverterFactory.create()).build();

        mNasaService= mRetrofit.create(NasaService.class);

    }


    public NasaService getNasaService(){

        return mNasaService;
    }

}
