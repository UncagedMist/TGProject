package tbc.techbytecare.kk.touristguideproject.Common;

import tbc.techbytecare.kk.touristguideproject.MapModel.Results;
import tbc.techbytecare.kk.touristguideproject.Model.UserGuide;
import tbc.techbytecare.kk.touristguideproject.Model.UserTourist;
import tbc.techbytecare.kk.touristguideproject.Remote.IGoogleAPIService;
import tbc.techbytecare.kk.touristguideproject.Remote.RetrofitClient;

public class Common {

    public static Results currentResults;

    public static UserTourist currentTourist;
    public static UserGuide currentGuide;

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService()   {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
