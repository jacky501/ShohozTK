package bdcash.dcash.bdcash;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class MainActivity extends AppCompatActivity{

//    public static String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
//    public static long TIME_IN_MILISECONDS = 100000;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
//    public static String currentAd = "";
//    private TextView timerText, nowBalance;
//    private Button quins5,quins4;
//    private int counter = 0;
//    private int clickBalance = 0;
//    private int totalBalance = 0;
//    private long timeLeftInMilis = TIME_IN_MILISECONDS;
//    private long mEndIime;
//    private RewardedVideoAd mRewardedVideoAd;
//    private CountDownTimer countDownTimer;
//    private int isTimerRunning = 0;
//    FirebaseDatabase database;
//    DatabaseReference myRef;
//    String unique_id;
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationDrawer;
    public static ActionBarDrawerToggle actionBarDrawerToggle;
    public static int navItemIndex = 0;

    private static final String TAG_HOME = "home";
    private static String CURRENT_TAG=TAG_HOME;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//       unique_id = getDeviceUniqueID(MainActivity.this);
//
//
//        // Write a message to the database
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("Time").child(unique_id);
//        myRef.keepSynced(false);




        toolbar = findViewById(R.id.myToolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationDrawer = findViewById(R.id.main_drawer);


        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                invalidateOptionsMenu();
            }
        };


        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        setUpNavigationView();


        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
//        MobileAds.initialize(this, ADMOB_APP_ID);
//
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
//        mRewardedVideoAd.setRewardedVideoAdListener(this);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//
//            requestReadPhoneStatePermission();
//        }
//
//        timerText = findViewById(R.id.timerText);
//        nowBalance = findViewById(R.id.nowBalance);
//        quins5 = findViewById(R.id.quins5);
//        quins4 = findViewById(R.id.quins4);
//
//
//
//        loadRewardedVideoAd();
//
//        quins5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                counter = counter + 1;
//                currentAd = "REWARD5";
//                if (mRewardedVideoAd.isLoaded()) {
//                    mRewardedVideoAd.show();
//                }
//            }
//        });
//
//        quins4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                currentAd = "REWARD4";
//                if (mRewardedVideoAd.isLoaded()) {
//                    mRewardedVideoAd.show();
//                }
//            }
//        });
//
//
//        Toast.makeText(this, getDeviceUniqueID(this), Toast.LENGTH_SHORT).show();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Time time = dataSnapshot.getValue(Time.class);
//
//
//                totalBalance = time.getTotal_balance();
//                nowBalance.setText(String.valueOf(totalBalance));
//
//                timeLeftInMilis = time.getLast_time();
//                isTimerRunning =time.getIsTimerRunning();
//
//
//                if (isTimerRunning==1) {
//                    mEndIime = time.getEnd_time();
//                    timeLeftInMilis = mEndIime-System.currentTimeMillis();
//
//                    if (timeLeftInMilis<0)
//                    {
//                        myRef.child("last_time").setValue(TIME_IN_MILISECONDS);
//                        myRef.child("end_time").setValue(0);
//                        myRef.child("total_balance").setValue(totalBalance);
//                        myRef.child("isTimerRunning").setValue(0);
//                        isTimerRunning=0;
//                        timeLeftInMilis=TIME_IN_MILISECONDS;
//                        mEndIime=0;
//                    }else {
//                        UpdateCountdownText();
//                        startTimer();
//                        quins5.setEnabled(false);
//                        quins4.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



    }

    private void loadHomeFragment() {

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            return;
        }

        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_fragment, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

        drawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            default:
                return new HomeFragment();
        }
    }
    private void setUpNavigationView() {
        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

                    default:
                        navItemIndex = 0;
                }
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

    }




//    private void loadRewardedVideoAd() {
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());
//    }
//
//    @Override
//    public void onRewardedVideoAdLoaded() {
//
//
//    }
//
//    @Override
//    public void onRewardedVideoAdOpened() {
//
//    }
//
//    @Override
//    public void onRewardedVideoStarted() {
//
//    }
//
//    @Override
//    public void onRewardedVideoAdClosed() {
//       loadRewardedVideoAd();
//
//    }
//
//    @Override
//    public void onRewarded(RewardItem rewardItem) {
//
//        switch (currentAd)
//        {
//            case ("REWARD5"):
//
//
//                clickBalance = clickBalance + rewardItem.getAmount();
//                Toast.makeText(this, String.valueOf(rewardItem.getAmount() + "\n"+ rewardItem.getType()
//                        +"\n"+clickBalance), Toast.LENGTH_SHORT).show();
//                totalBalance = totalBalance + rewardItem.getAmount();
//                nowBalance.setText(String.valueOf(totalBalance));
//
//                currentAd = "";
//                break;
//
//            case ("REWARD4"):
//                int amount = rewardItem.getAmount()-1;
//                Toast.makeText(this,String.valueOf(amount),Toast.LENGTH_LONG).show();
//
//                default:
//                    currentAd="";
//                    break;
//
//
//
//        }
//
//        if (clickBalance >= 10) {
//
//            if (isTimerRunning==0) {
//                quins5.setEnabled(false);
//                quins4.setEnabled(false);
//                isTimerRunning=1;
//                UpdateCountdownText();
//                startTimer();
//            }
//
//        }
//
//
//
//
//
//    }
//
//    @Override
//    public void onRewardedVideoAdLeftApplication() {
//
//    }
//
//    @Override
//    public void onRewardedVideoAdFailedToLoad(int i) {
//
//    }
//
//    @Override
//    public void onRewardedVideoCompleted() {
//        loadRewardedVideoAd();
//
//    }

//    public final void Interval(long time)
//    {
//        timeLeftInMilis = time;
//        if (time>0L)
//        {
//            handler = new Handler();
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//
//                    timeLeftInMilis -=1L;
//                    if (timeLeftInMilis>1L)
//                    {
//                        StringBuilder sb = new StringBuilder();
//                        sb.append("please wait ");
//                        Object[] array = new Object[3];
//                        array[0]=Long.valueOf(timeLeftInMilis/3600L);
//                        array[1]=Long.valueOf(timeLeftInMilis%3600L/60L);
//                        array[2]=Long.valueOf(timeLeftInMilis % 60L);
//                        timerText.setText(String.format("%02d:%02d:%02d",array)+"time");
//                        handler.postDelayed(runnable,1000L);
//                    }
//                }
//            };
//            handler.postDelayed(runnable,1000L);
//        }
//    }

//    private void startTimer() {
//
//        mEndIime = System.currentTimeMillis()+timeLeftInMilis;
//
//        countDownTimer = new CountDownTimer(timeLeftInMilis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//                timeLeftInMilis = millisUntilFinished;
//
//                UpdateCountdownText();
//
//            }
//
//            @Override
//            public void onFinish() {
//
//                mEndIime=0;
//                clickBalance = 0;
//                isTimerRunning = 0;
//                quins5.setEnabled(true);
//                quins4.setEnabled(true);
//                timeLeftInMilis = TIME_IN_MILISECONDS;
//                myRef.child("last_time").setValue(TIME_IN_MILISECONDS);
//                myRef.child("end_time").setValue(0);
//                myRef.child("total_balance").setValue(totalBalance);
//                myRef.child("isTimerRunning").setValue(0);
//            }
//        };
//        countDownTimer.start();
//
//    }
//
//    private void UpdateCountdownText() {
//
//        int hours = (int) (timeLeftInMilis / (60 * 60 * 1000)) % 24;
//        int minutes = (int) (timeLeftInMilis / (60 * 1000) % 60);
//        int seconds = (int) (timeLeftInMilis / (1000) % 60);
//
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("please wait ").append(String.valueOf(hours)+":").append(String.valueOf(minutes)+":").append(String.valueOf(seconds));
//        timerText.setText(sb.toString());
//
//
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putLong("milisLeft", timeLeftInMilis);
//        outState.putInt("totalbalance", totalBalance);
//        outState.putInt("isTimerRunning", isTimerRunning);
//        outState.putLong("endTime",mEndIime);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        timeLeftInMilis = savedInstanceState.getLong("milisLeft");
//        isTimerRunning = savedInstanceState.getInt("isTimerRunning");
//        totalBalance = savedInstanceState.getInt("totalbalance");
//
//        nowBalance.setText(String.valueOf(totalBalance));
//
//        if (isTimerRunning==1) {
//            mEndIime = savedInstanceState.getLong("endTime");
//            timeLeftInMilis = mEndIime-System.currentTimeMillis();
//            quins5.setEnabled(false);
//            quins4.setEnabled(false);
//            UpdateCountdownText();
//            startTimer();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        try {
////            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
////            DatabaseReference databaseReference = firebaseDatabase.getReference("Time");
////
////            databaseReference.child("user_id").setValue(unique_id);
////            databaseReference.child("last_time").setValue(timeLeftInMilis);
////            databaseReference.child("end_time").setValue(mEndIime);
////            databaseReference.child("total_balance").setValue(totalBalance);
////            databaseReference.child("isTimerRunning").setValue(isTimerRunning);
//
//            Time time = new Time(timeLeftInMilis,mEndIime,totalBalance,isTimerRunning);
//            myRef.setValue(time);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mRewardedVideoAd.pause(MainActivity.this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mRewardedVideoAd.resume(MainActivity.this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mRewardedVideoAd.destroy(MainActivity.this);
//        finish();
//    }


    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @NonNull
    private String getDeviceIpAddress() {
        String actualConnectedToNetwork = null;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) {
                actualConnectedToNetwork = getWifiIp();
            }
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = getNetworkInterfaceIpAddress();
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = "127.0.0.1";
        }
        return actualConnectedToNetwork;
    }

    @Nullable
    private String getWifiIp() {
        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            int ip = mWifiManager.getConnectionInfo().getIpAddress();
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                    + ((ip >> 24) & 0xFF);
        }
        return null;
    }


    @Nullable
    public String getNetworkInterfaceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String host = inetAddress.getHostAddress();
                        if (!TextUtils.isEmpty(host)) {
                            return host;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
        return null;
    }

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

//    public String getIMEI(Activity activity) {
//            TelephonyManager telephonyManager = (TelephonyManager) activity
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//        return telephonyManager.getDeviceId();
//    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("permission_read_phone_state_rationale")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                alertAlert("permissions_not_granted_read_phone_state");
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


}
