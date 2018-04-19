package tbc.techbytecare.kk.touristguideproject.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tbc.techbytecare.kk.touristguideproject.Fragments.TransactionsFragment;
import tbc.techbytecare.kk.touristguideproject.Fragments.WalletsFragment;
import tbc.techbytecare.kk.touristguideproject.R;

public class MoneyOrderActivity extends AppCompatActivity {

    private static Context context;

    public static Context getAppContext() {
        return MoneyOrderActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoneyOrderActivity.context = getApplicationContext();
        setContentView(R.layout.activity_money_order);
        updateUI();

        registerReceiver(broadcastReceiver, new IntentFilter("UPDATE_UI"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void updateUI() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        //To use TAbLayout add material design dependency in your gradle file
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int index) {
                switch (index) {
                    case 0:
                        return TransactionsFragment.newInstance();
                    default:
                        return WalletsFragment.newInstance();
                }

            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Transactions";
                    default:
                        return "Wallets";
                }

            }
        };

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
