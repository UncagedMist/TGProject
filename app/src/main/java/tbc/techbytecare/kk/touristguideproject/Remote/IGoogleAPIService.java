package tbc.techbytecare.kk.touristguideproject.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tbc.techbytecare.kk.touristguideproject.MapModel.MyPlaces;
import tbc.techbytecare.kk.touristguideproject.MapModel.PlaceDetail;

public interface IGoogleAPIService {

    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);

    @GET
    Call<PlaceDetail> getDetailPlaces(@Url String url);
}
