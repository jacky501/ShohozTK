package bdcash.dcash.bdcash;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class MainActivity extends AppCompatActivity{

    TextView profilename,acId,total_balance;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationDrawer;
    public static ActionBarDrawerToggle actionBarDrawerToggle;
    public static int navItemIndex = 0;

    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_HOME = "home";
    private static final String TAG_WITHDRAW = "withdraw";
    private static final String TAG_WITHDRAW_HISTORY = "withdraw history";
    private static final String TAG_REFERRAL = "referral";
    private static final String TAG_NOTICE = "referral";
    private static String CURRENT_TAG=TAG_DASHBOARD;
    View navHeader;
    String shareText;
    String fbGroupUrl="https://www.facebook.com/groups/DigitalCashProApp/";

    FirebaseDatabase database;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            requestReadPhoneStatePermission();
        }

        Constant.UNIQUE_ID = getDeviceUniqueID(MainActivity.this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.keepSynced(true);


        toolbar = findViewById(R.id.myToolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationDrawer = findViewById(R.id.main_drawer);
        navHeader = navigationDrawer.getHeaderView(0);
        profilename = navHeader.findViewById(R.id.drawer_profile_name);
        acId = navHeader.findViewById(R.id.drawer_profile_account);
        total_balance = navHeader.findViewById(R.id.drawer_total_balance);


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
            CURRENT_TAG = TAG_DASHBOARD;
            loadHomeFragment();
        }

        setUpNavigationView();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.child("User")
                        .child(getDeviceUniqueID(MainActivity.this))
                        .getValue(User.class);
                profilename.setText(user.getFull_name());
                acId.setText(getDeviceUniqueID(MainActivity.this));
                int totalBalance = dataSnapshot.child("Time").child(getDeviceUniqueID(MainActivity.this)).child("total_balance")
                        .getValue(Integer.class);
                total_balance.setText(String.valueOf(totalBalance)+" Quins");

                Map map = (Map) dataSnapshot.child("Share").getValue();
                shareText = map.get("share_text").toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!fbGroupUrl.startsWith("http://") && !fbGroupUrl.startsWith("https://")) {
            fbGroupUrl = "http://" + fbGroupUrl;
        }

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
                DashboardFragment dashboardFragment = new DashboardFragment();
                return dashboardFragment;
            case 1:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 2:
                WithdrawFragment withdrawFragment = new WithdrawFragment();
                return withdrawFragment;
            case 3:
                WithdrawHistoryFragment withdrawHistoryFragment = new WithdrawHistoryFragment();
                return withdrawHistoryFragment;
            case 4:
                ReferralFragment referralFragment = new ReferralFragment();
                return referralFragment;
            case 5:
                NoticeFragment noticeFragment = new NoticeFragment();
                return noticeFragment;
            default:
                return new DashboardFragment();
        }
    }
    private void setUpNavigationView() {
        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;

                    case R.id.dashboard:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;
                    case R.id.home:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.withdraw:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_WITHDRAW;
                        break;
                    case R.id.withdrawHistory:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_WITHDRAW_HISTORY;
                        break;
                    case R.id.referel:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_REFERRAL;
                        break;
                    case R.id.notice:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_NOTICE;
                        break;
                    case R.id.share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case R.id.facebookGroup:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbGroupUrl));
                        startActivity(browserIntent);
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


    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
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
