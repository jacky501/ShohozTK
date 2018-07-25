package dcash.bdcash.dcash;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static dcash.bdcash.dcash.Constant.ADMOB_APP_ID;
import static dcash.bdcash.dcash.Constant.TIME_IN_MILISECONDS;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {


    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private TextView timerText, nowBalance, txtTimerHour, txtTimerMinute, txtTimerSecond;
    private Button quins5;
    private int counter = 0;
    private int clickBalance = 0;
    private int totalBalance = 0;
    private long timeLeftInMilis = TIME_IN_MILISECONDS;
    private long mEndIime;
    private RewardedVideoAd mRewardedVideoAd;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, ADMOB_APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            requestReadPhoneStatePermission();
        }

        timerText = findViewById(R.id.timerText);
        nowBalance = findViewById(R.id.nowBalance);
        quins5 = findViewById(R.id.quins5);
        txtTimerHour = findViewById(R.id.txtTimerHour);
        txtTimerMinute = findViewById(R.id.txtTimerMinute);
        txtTimerSecond = findViewById(R.id.txtTimerSecond);



        loadRewardedVideoAd();

        quins5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter + 1;
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });




        Toast.makeText(this, getIMEI(this), Toast.LENGTH_LONG).show();

    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        clickBalance = clickBalance + 5;
        Toast.makeText(MainActivity.this, String.valueOf(clickBalance), Toast.LENGTH_SHORT).show();
        totalBalance = totalBalance + clickBalance;
        nowBalance.setText(String.valueOf(totalBalance));

        if (clickBalance == 5) {
            txtTimerHour.setVisibility(View.VISIBLE);
            txtTimerMinute.setVisibility(View.VISIBLE);
            txtTimerSecond.setVisibility(View.VISIBLE);
            if (!isTimerRunning) {
                quins5.setEnabled(false);
                isTimerRunning = true;
                UpdateCountdownText();
                startTimer();
            }

        }


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

        loadRewardedVideoAd();
    }

    private void startTimer() {

        mEndIime = System.currentTimeMillis()+timeLeftInMilis;

        countDownTimer = new CountDownTimer(timeLeftInMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftInMilis = millisUntilFinished;

                UpdateCountdownText();

            }

            @Override
            public void onFinish() {
                clickBalance = 0;
                isTimerRunning = false;
                quins5.setEnabled(true);
                timeLeftInMilis = TIME_IN_MILISECONDS;
                txtTimerHour.setVisibility(View.INVISIBLE);
                txtTimerMinute.setVisibility(View.INVISIBLE);
                txtTimerSecond.setVisibility(View.INVISIBLE);
            }
        };
        countDownTimer.start();

    }

    private void UpdateCountdownText() {

        int hours = (int) (timeLeftInMilis / (60 * 60 * 1000)) % 24;
        int minutes = (int) (timeLeftInMilis / (60 * 1000) % 60);
        int seconds = (int) (timeLeftInMilis / (1000) % 60);


        txtTimerHour.setText(String.valueOf(hours));
        txtTimerMinute.setText(String.valueOf(minutes));
        txtTimerSecond.setText(String.valueOf(seconds));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("milisLeft", timeLeftInMilis);
        outState.putInt("totalbalance", totalBalance);
        outState.putBoolean("isTimerRunning", isTimerRunning);
        outState.putLong("endTime",mEndIime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeLeftInMilis = savedInstanceState.getLong("milisLeft");
        isTimerRunning = savedInstanceState.getBoolean("isTimerRunning");
        totalBalance = savedInstanceState.getInt("totalbalance");

        nowBalance.setText(String.valueOf(totalBalance));

        if (isTimerRunning) {
            mEndIime = savedInstanceState.getLong("endTime");
            timeLeftInMilis = mEndIime-System.currentTimeMillis();
            UpdateCountdownText();
            quins5.setEnabled(false);
            startTimer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences
                = getSharedPreferences("Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("milisLeft", timeLeftInMilis);
        editor.putBoolean("isTimerRunning", isTimerRunning);
        editor.putInt("totalbalance", totalBalance);
        editor.putLong("endTime",mEndIime);

        editor.apply();

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences
                = getSharedPreferences("Prefs", MODE_PRIVATE);

        timeLeftInMilis = sharedPreferences.getLong("milisLeft", timeLeftInMilis);
        isTimerRunning = sharedPreferences.getBoolean("isTimerRunning", false);
        totalBalance = sharedPreferences.getInt("totalbalance", 0);
        nowBalance.setText(String.valueOf(totalBalance));

        if (isTimerRunning) {
            mEndIime = sharedPreferences.getLong("endTime",0);
            timeLeftInMilis = mEndIime-System.currentTimeMillis();
            UpdateCountdownText();
            startTimer();
            quins5.setEnabled(false);
        }else
        {
            timerText.setVisibility(View.INVISIBLE);
            txtTimerHour.setVisibility(View.INVISIBLE);
            txtTimerMinute.setVisibility(View.INVISIBLE);
            txtTimerSecond.setVisibility(View.INVISIBLE);
        }

    }

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
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    public String getIMEI(Activity activity) {
            TelephonyManager telephonyManager = (TelephonyManager) activity
                    .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

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
