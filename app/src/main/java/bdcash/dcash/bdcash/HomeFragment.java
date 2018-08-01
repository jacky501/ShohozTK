package bdcash.dcash.bdcash;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static bdcash.dcash.bdcash.Constant.ADMOB_APP_ID;
import static bdcash.dcash.bdcash.Constant.INTERESTIALS_AD_UNIT_ID;
import static bdcash.dcash.bdcash.Constant.REWARD_VIDEO_AD_UNIT_ID;
import static bdcash.dcash.bdcash.Constant.TIME_IN_MILISECONDS;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements RewardedVideoAdListener{

    public static String currentAd = "";
    private RelativeLayout btnHideShadow;
    private TextView timerText;
    private Button quins5,quins4,quins3,quins1;
    private int clickBalance = 0;
    private int totalBalance = 0;
    private long timeLeftInMilis = TIME_IN_MILISECONDS;
    private long mEndIime=0;
    private RewardedVideoAd mRewardedVideoAd;
    private CountDownTimer countDownTimer;
    private int isTimerRunning = 0;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myClickHistory;
    String unique_id;
    int todaysTotalEarning;
    private InterstitialAd mInterstitialAd;
    private int quins5Counter = 0;
    private int quins4Counter = 0;
    private int quins3Counter = 0;
    private int quins1Counter = 0;
    private int totalClick = 0;
    private int isAdClicked = 0;
    Date currentDate;
    String presentDate;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Earn Money");
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(INTERESTIALS_AD_UNIT_ID);

        unique_id = getDeviceUniqueID(getActivity());
//
//
//        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Time").child(unique_id);
        myClickHistory = database.getReference("Click_history").child(Constant.UNIQUE_ID);
        myRef.keepSynced(true);
        myClickHistory.keepSynced(true);



        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getContext(), ADMOB_APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);


        timerText = view.findViewById(R.id.timerText);
        btnHideShadow = view.findViewById(R.id.btnHideShadow);
        quins5 = view.findViewById(R.id.quins4);
        quins4 = view.findViewById(R.id.quins24);
        quins3 = view.findViewById(R.id.quins3);
        quins1 = view.findViewById(R.id.quins1);


        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        loadRewardedVideoAd();

        quins1.setVisibility(View.GONE);
        quins3.setVisibility(View.GONE);
        quins4.setVisibility(View.GONE);
        quins5.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                quins5.setVisibility(View.VISIBLE);
            }
        },3000);



                    quins5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentAd = "REWARD4";
                            if (mRewardedVideoAd.isLoaded()) {
                                mRewardedVideoAd.show();
                                quins5Counter = quins5Counter+1;
                                quins5.setVisibility(View.GONE);
                            }else
                            {
                                Toast.makeText(getContext(), "ad is loading.....", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    quins4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            currentAd = "REWARD24";
                            if (mRewardedVideoAd.isLoaded()) {
                                mRewardedVideoAd.show();
                                quins4Counter = quins4Counter+1;
                                quins4.setVisibility(View.GONE);
                            }else
                            {
                                Toast.makeText(getContext(), "ad is loading.....", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    quins3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            currentAd = "REWARD3";
                            if (mRewardedVideoAd.isLoaded()) {
                                mRewardedVideoAd.show();
                                quins3Counter = quins3Counter+1;
                                quins3.setVisibility(View.GONE);
                            }else
                            {
                                Toast.makeText(getContext(), "ad is loading.....", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        myClickHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ClickHistory clickHistory = dataSnapshot.child(presentDate).getValue(ClickHistory.class);
                if (clickHistory != null) {
                    quins5Counter = clickHistory.getQuins5();
                    quins4Counter = clickHistory.getQuins4();
                    quins3Counter = clickHistory.getQuins3();
                    quins1Counter = clickHistory.getQuins1();
                    totalClick = clickHistory.getTotal_click();
                    isAdClicked = clickHistory.getIsAdClicked();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Time time = dataSnapshot.getValue(Time.class);

                            if (time != null) {
                                totalBalance = time.getTotal_balance();
                                clickBalance = time.getClickBalance();
                                todaysTotalEarning = time.getEachDayEarning();

                                timeLeftInMilis = time.getLast_time();
                                isTimerRunning = time.getIsTimerRunning();


                                if (isTimerRunning == 1) {
                                    mEndIime = time.getEnd_time();
                                    timeLeftInMilis = mEndIime - System.currentTimeMillis();

                                    if (timeLeftInMilis < 0) {
                                        timerText.setVisibility(View.INVISIBLE);
                                        btnHideShadow.setVisibility(View.INVISIBLE);
                                        isTimerRunning = 0;
                                        timeLeftInMilis = TIME_IN_MILISECONDS;
                                        mEndIime = 0;
                                        clickBalance = 0;
                                        Time time1 = new Time(TIME_IN_MILISECONDS, 0, totalBalance, 0, 0, todaysTotalEarning);
                                        myRef.setValue(time1);
                                    } else {
                                        quins5.setEnabled(false);
                                        quins4.setEnabled(false);
                                        quins3.setEnabled(false);
                                        quins1.setEnabled(false);
                                        btnHideShadow.setVisibility(View.VISIBLE);
                                        timerText.setVisibility(View.VISIBLE);
                                        UpdateCountdownText();
                                        startTimer();

                                    }
                                } else if (isTimerRunning == 2) {
                                    quins5.setEnabled(false);
                                    quins4.setEnabled(false);
                                    quins3.setEnabled(false);
                                    quins1.setEnabled(false);
                                    btnHideShadow.setVisibility(View.VISIBLE);
                                    timerText.setVisibility(View.VISIBLE);
                                    IntervalDay();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    quins1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                                quins1Counter = quins1Counter+1;
                                if (isAdClicked==0) {
                                    Toast.makeText(getContext(), "বোনাস ! বোনাস !! \nএই এড এ ক্লিক করে এখনি ৬ কুইন্স বোনাস নিয়ে নিন ", Toast.LENGTH_LONG).show();
                                }else if (isAdClicked>1)
                                {
                                    Toast.makeText(getContext(), "সাবধান !! \nআপনি আজকে পূর্বে ভুল ভাবে কাজ করেছেন। \nএই এড এ আর ক্লিক করবেন না । \nতাছাড়া আপনার একাউন্ট টি সাসপেন্ড করা হবে   ", Toast.LENGTH_LONG).show();
                                }else
                                {
                                    Toast.makeText(getContext(), "এই এড এ আপনি একবার ক্লিক করেছেন । \nআর ক্লিক করবেন না নাহলে আপনার ১০ কুইন্স কেটে নেওয়া হবে ", Toast.LENGTH_LONG).show();
                                }
                                quins1.setVisibility(View.GONE);
                            }
                        }
                    });


                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {

                            quins1.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            if (isAdClicked==0) {
                                isAdClicked=isAdClicked+1;
                                clickBalance = clickBalance + 6;
                                totalBalance = totalBalance + 6;
                                todaysTotalEarning = todaysTotalEarning + 6;
                                Time time = new Time(timeLeftInMilis, mEndIime, totalBalance, isTimerRunning, clickBalance, todaysTotalEarning);
                                myRef.setValue(time);
                                ClickHistory clickHistory = new ClickHistory(quins5Counter, quins4Counter, quins3Counter, quins1Counter, totalClick, todaysTotalEarning, isAdClicked);
                                myClickHistory.child(presentDate).setValue(clickHistory);
                            }else
                            {
                                isAdClicked=isAdClicked+1;
                                totalBalance = totalBalance - 10;
                                Time time = new Time(timeLeftInMilis, mEndIime, totalBalance, isTimerRunning, clickBalance, todaysTotalEarning);
                                myRef.setValue(time);
                            }
                        }

                        @Override
                        public void onAdClosed() {
                            if (isAdClicked==1)
                            {
                                Toast.makeText(getContext(), "আপনি ৬ বোনাস কুইন্স পেয়েছেন", Toast.LENGTH_LONG).show();

                            }else if (isAdClicked>1)
                            {
                                Toast.makeText(getContext(), "আপনি ভুল ভাবে কাজ করেছেন । \n আপনার একাউন্ট থেকে ১০ কুইন্স কেটে নেওয়া হয়েছে", Toast.LENGTH_LONG).show();

                            }else
                            {
                                Toast.makeText(getContext(), "আপনি ১ কুইন্স উপার্জন করেছেন", Toast.LENGTH_LONG).show();
                            }

                            clickBalance = clickBalance + 1;
                            totalBalance = totalBalance + 1;
                            todaysTotalEarning = todaysTotalEarning+1;
                            Time time = new Time(timeLeftInMilis,mEndIime,totalBalance,isTimerRunning,clickBalance,todaysTotalEarning);
                            myRef.setValue(time);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }

                    });

        currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        presentDate = dateFormat.format(currentDate);


        return view;
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(REWARD_VIDEO_AD_UNIT_ID,
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
                quins3.setEnabled(true);
                quins1.setEnabled(true);
                btnHideShadow.setVisibility(View.INVISIBLE);
                timerText.setVisibility(View.INVISIBLE);
                timeLeftInMilis = TIME_IN_MILISECONDS;
                Time time = new Time(timeLeftInMilis,0,totalBalance,0,0,todaysTotalEarning);
                myRef.setValue(time);
            }
        };
        countDownTimer.start();

    }

    private void UpdateCountdownText() {

        int hours = (int) (timeLeftInMilis / (60 * 60 * 1000)) % 24;
        int minutes = (int) (timeLeftInMilis / (60 * 1000) % 60);
        int seconds = (int) (timeLeftInMilis / (1000) % 60);


        StringBuilder sb = new StringBuilder();
        sb.append("একটু বিশ্রাম নিন ").append(String.valueOf(hours)+" : ").append(String.valueOf(minutes)+" : ").append(String.valueOf(seconds));
        timerText.setText(sb.toString());


    }

    public void IntervalDay(){

        long timer = getRemainingDays();
        CountDownTimer countDownTimer = new CountDownTimer(timer,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished/(60*60*1000))%24;
                int minutes = (int) (millisUntilFinished/(60*1000)%60);
                int seconds = (int) (millisUntilFinished/(1000)%60);

                StringBuilder sb = new StringBuilder();
                sb.append("আজকে অনেক উপার্জন করেছেন।  আগামীকাল আবার চেষ্টা করুন ").append(String.valueOf(hours)+" : ").append(String.valueOf(minutes)+" : ").append(String.valueOf(seconds));
                timerText.setText(sb.toString());
            }

            @Override
            public void onFinish() {

                isTimerRunning = 0;
                todaysTotalEarning=0;
                quins5.setEnabled(true);
                quins4.setEnabled(true);
                quins3.setEnabled(true);
                quins1.setEnabled(true);
                btnHideShadow.setVisibility(View.INVISIBLE);
                timerText.setVisibility(View.INVISIBLE);
                myRef.child("isTimerRunning").setValue(isTimerRunning);
                myRef.child("eachDayEarning").setValue(0);
            }
        };

        countDownTimer.start();

    }
    private long getRemainingDays() {
        Date currentDate = new Date();
        Date futureDate;

        if (currentDate.getMonth() <=11)
        {
            futureDate = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate()+1);
        }else
        {
            futureDate = new Date(currentDate.getYear()+1,1,1);
        }

        return futureDate.getTime() - currentDate.getTime();
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
            Time time = new Time(timeLeftInMilis,mEndIime,totalBalance,isTimerRunning,clickBalance,todaysTotalEarning);
            myRef.setValue(time);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRewardedVideoAdLoaded() {

        quins5.setVisibility(View.VISIBLE);
        quins4.setVisibility(View.VISIBLE);
        quins3.setVisibility(View.VISIBLE);
        quins1.setVisibility(View.VISIBLE);
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
        ClickHistory clickHistory = new ClickHistory(quins5Counter,quins4Counter,quins3Counter,quins1Counter,totalClick,todaysTotalEarning,isAdClicked);
        myClickHistory.child(presentDate).setValue(clickHistory);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        switch (currentAd)
        {
            case ("REWARD4"):

                todaysTotalEarning = todaysTotalEarning+rewardItem.getAmount();
                clickBalance = clickBalance + rewardItem.getAmount();
                totalBalance = totalBalance + rewardItem.getAmount();
                currentAd = "";
                Toast.makeText(getContext(), "আপনি ৫ কুইন্স উপার্জন করেছেন", Toast.LENGTH_LONG).show();
                break;

            case ("REWARD24"):
                todaysTotalEarning = todaysTotalEarning+rewardItem.getAmount();
                clickBalance = clickBalance + rewardItem.getAmount()-1;
                totalBalance = totalBalance + rewardItem.getAmount()-1;
                currentAd = "";
                Toast.makeText(getContext(), "আপনি ৪ কুইন্স উপার্জন করেছেন", Toast.LENGTH_LONG).show();
                break;
            case ("REWARD3"):
                todaysTotalEarning = todaysTotalEarning+rewardItem.getAmount()-2;
                int amount3 = rewardItem.getAmount()-1;
                clickBalance = clickBalance + amount3;
                totalBalance = totalBalance + amount3;
                currentAd = "";
                Toast.makeText(getContext(), "আপনি ৩ কুইন্স উপার্জন করেছেন", Toast.LENGTH_LONG).show();
                break;

            default:
                currentAd="";
                break;



        }

        totalClick = quins1Counter+quins3Counter+quins4Counter+quins5Counter;

        if (clickBalance >= 45) {

            if (isTimerRunning==0) {
                quins5.setEnabled(false);
                quins4.setEnabled(false);
                quins3.setEnabled(false);
                quins1.setEnabled(false);
                btnHideShadow.setVisibility(View.VISIBLE);
                timerText.setVisibility(View.VISIBLE);
                isTimerRunning=1;
                UpdateCountdownText();
                startTimer();
            }

        }else if (todaysTotalEarning>=200)
        {
            if (isTimerRunning==0) {
                quins5.setEnabled(false);
                quins4.setEnabled(false);
                quins3.setEnabled(false);
                quins1.setEnabled(false);
                btnHideShadow.setVisibility(View.VISIBLE);
                timerText.setVisibility(View.VISIBLE);
                isTimerRunning=2;
                IntervalDay();
            }
        }


        Time time = new Time(timeLeftInMilis,mEndIime,totalBalance,isTimerRunning,clickBalance,todaysTotalEarning);
        myRef.setValue(time);

        ClickHistory clickHistory = new ClickHistory(quins5Counter,quins4Counter,quins3Counter,quins1Counter,totalClick,todaysTotalEarning,isAdClicked);
        myClickHistory.child(presentDate).setValue(clickHistory);

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

        ClickHistory clickHistory = new ClickHistory(quins5Counter,quins4Counter,quins3Counter,quins1Counter,totalClick,todaysTotalEarning,isAdClicked);
        myClickHistory.child(presentDate).setValue(clickHistory);

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
