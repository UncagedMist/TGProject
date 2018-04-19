package tbc.techbytecare.kk.touristguideproject.Helper;

import android.app.Application;
import android.content.Context;

/**
 * Created by kundan on 3/28/2018.
 */

public class MainAppHelper extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}
