package bdcash.dcash.bdcash;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements RewardedVideoAdListener{

    public static String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    public static long TIME_IN_MILISECONDS = 100000;
    public static String currentAd = "";
    private TextView timerText, nowBalance;
    private Button quins5,quins4;
    private int counter = 0;
    private int clickBalance = 0;
    private int totalBalance = 0;
    private long timeLeftInMilis = TIME_IN_MILISECONDS;
    private long mEndIime=0;
    private RewardedVideoAd mRewardedVideoAd;
    private CountDownTimer countDownTimer;
    private int isTimerRunning = 0;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String unique_id;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unique_id = getDeviceUniqueID(getActivity());
//
//
//        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Time").child(unique_id);
        myRef.keepSynced(false);



        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getContext(), ADMOB_APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);


        timerText = view.findViewById(R.id.timerText);
        nowBalance = view.findViewById(R.id.nowBalance);
        quins5 = view.findViewById(R.id.quins5);
        quins4 = view.findViewById(R.id.quins4);



        loadRewardedVideoAd();

        quins5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter + 1;
                currentAd = "REWARD5";
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });

        quins4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentAd = "REWARD4";
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });


        Toast.makeText(getContext(), getDeviceUniqueID(getActivity()), Toast.LENGTH_SHORT).show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Time time = dataSnapshot.getValue(Time.class);


                totalBalance = time.getTotal_balance();
                nowBalance.setText(String.valueOf(totalBalance));

                timeLeftInMilis = time.getLast_time();
                isTimerRunning =time.getIsTimerRunning();


                if (isTimerRunning==1) {
                    mEndIime = time.getEnd_time();
                    timeLeftInMilis = mEndIime-System.currentTimeMillis();

                    if (timeLeftInMilis<0)
                    {
                        myRef.child("last_time").setValue(TIME_IN_MILISECONDS);
                        myRef.child("end_time").setValue(0);
                        myRef.child("total_balance").setValue(totalBalance);
                        myRef.child("isTimerRunning").setValue(0);
                        isTimerRunning=0;
                        timeLeftInMilis=TIME_IN_MILISECONDS;
                        mEndIime=0;
                    }else {
                        UpdateCountdownText();
                        startTimer();
                        quins5.setEnabled(false);
                        quins4.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
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

                mEndIime=0;
                clickBalance = 0;
                isTimerRunning = 0;
                quins5.setEnabled(true);
                quins4.setEnabled(true);
                timeLeftInMilis = TIME_IN_MILISECONDS;
                myRef.child("last_time").setValue(TIME_IN_MILISECONDS);
                myRef.child("end_time").setValue(0);
                myRef.child("total_balance").setValue(totalBalance);
                myRef.child("isTimerRunning").setValue(0);
            }
        };
        countDownTimer.start();

    }

    private void UpdateCountdownText() {

        int hours = (int) (timeLeftInMilis / (60 * 60 * 1000)) % 24;
        int minutes = (int) (timeLeftInMilis / (60 * 1000) % 60);
        int seconds = (int) (timeLeftInMilis / (1000) % 60);


        StringBuilder sb = new StringBuilder();
        sb.append("please wait ").append(String.valueOf(hours)+":").append(String.valueOf(minutes)+":").append(String.valueOf(seconds));
        timerText.setText(sb.toString());


    }


//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putLong("milisLeft", timeLeftInMilis);
//        outState.putInt("totalbalance", totalBalance);
//        outState.putInt("isTimerRunning", isTimerRunning);
//        outState.putLong("endTime",mEndIime);
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
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

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            Time time = new Time(timeLeftInMilis,mEndIime,totalBalance,isTimerRunning);
            myRef.setValue(time);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        switch (currentAd)
        {
            case ("REWARD5"):


                clickBalance = clickBalance + rewardItem.getAmount();
                Toast.makeText(getContext(), String.valueOf(rewardItem.getAmount() + "\n"+ rewardItem.getType()
                        +"\n"+clickBalance), Toast.LENGTH_SHORT).show();
                totalBalance = totalBalance + rewardItem.getAmount();
                nowBalance.setText(String.valueOf(totalBalance));

                currentAd = "";
                break;

            case ("REWARD4"):
                int amount = rewardItem.getAmount()-1;
                Toast.makeText(getContext(),String.valueOf(amount),Toast.LENGTH_LONG).show();

            default:
                currentAd="";
                break;



        }

        if (clickBalance >= 10) {

            if (isTimerRunning==0) {
                quins5.setEnabled(false);
                quins4.setEnabled(false);
                isTimerRunning=1;
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
}
