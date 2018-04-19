package tbc.techbytecare.kk.touristguideproject.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import tbc.techbytecare.kk.touristguideproject.Adapter.MyAdapter;
import tbc.techbytecare.kk.touristguideproject.Database.DBHelper;
import tbc.techbytecare.kk.touristguideproject.R;

public class TransactionsFragment extends Fragment {

    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECEIVER = "receiver";
    public static final String KEY_AMOUNT = "amount";

    ListView list;
    MyAdapter adapter;
    DBHelper mydb;

    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transactions, container, false);

        mydb = new DBHelper(getActivity());
        ArrayList array_list = mydb.getAllTransactions();

        ArrayList<HashMap<String, String>> transList = new ArrayList<HashMap<String, String>>();
        for (int i=0;i<array_list.size();i+=3)
        {
            HashMap<String, String> map = new HashMap <String, String>();
            map.put(KEY_SENDER, array_list.get(i).toString());
            map.put(KEY_RECEIVER, array_list.get(i+1).toString());
            map.put(KEY_AMOUNT, array_list.get(i+2).toString());
            transList.add(map);
        }

        Collections.reverse(transList);
        list=(ListView)v.findViewById(R.id.listView1);

        // Getting adapter by passing xml data ArrayList
        adapter=new MyAdapter(getActivity(), transList);
        list.setAdapter(adapter);

        return v;
    }

}
