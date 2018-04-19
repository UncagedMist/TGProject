package tbc.techbytecare.kk.touristguideproject.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import tbc.techbytecare.kk.touristguideproject.Adapter.MyAdapter2;
import tbc.techbytecare.kk.touristguideproject.Database.DBHelper;
import tbc.techbytecare.kk.touristguideproject.R;

public class WalletsFragment extends Fragment {

    public static final String KEY_NUMBER = "number";
    public static final String KEY_CASH = "cash";

    ListView list;
    MyAdapter2 adapter;
    DBHelper mydb;

    public static WalletsFragment newInstance() {
        return new WalletsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallets, container, false);

        mydb = new DBHelper(getActivity());
        ArrayList array_list = mydb.getAllWallets();

        ArrayList<HashMap<String, String>> walletsList = new ArrayList<HashMap<String, String>>();
        for (int i=0;i<array_list.size();i+=2)
        {
            HashMap<String, String> map = new HashMap <String, String>();
            map.put(KEY_NUMBER, array_list.get(i).toString());
            map.put(KEY_CASH, array_list.get(i+1).toString());
            walletsList.add(map);
        }

        list=(ListView)v.findViewById(R.id.listView2);

        // Getting adapter by passing xml data ArrayList
        adapter=new MyAdapter2(getActivity(), walletsList);
        list.setAdapter(adapter);

        return v;
    }
}
