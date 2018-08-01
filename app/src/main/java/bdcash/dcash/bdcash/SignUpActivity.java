package bdcash.dcash.bdcash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class SignUpActivity extends AppCompatActivity {

    EditText email,password,fullName,address,phnNumber;
    CircularProgressButton submit;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String unique_id;
    ProgressDialog progressDialog;
    CheckBox termsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        myRef.keepSynced(false);


        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("ডাটা লোড হচ্ছে");
        progressDialog.setMessage("অনুগ্রহ পূর্বক অপেক্ষা করুন");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();



//
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        phnNumber = findViewById(R.id.phnNumber);
        submit = findViewById(R.id.submit);
        termsAndConditions = findViewById(R.id.termsAndCondition);

        unique_id = getDeviceUniqueID(this);

        if (Utilities.isNetworkAvailable(this)) {

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(unique_id).exists()) {
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    progressDialog.dismiss();
                }
            });

        }else
        {
            progressDialog.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please connect with internet and try again")
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setPositiveButton("Okay!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utilities.isNetworkAvailable(SignUpActivity.this)) {

                    if (TextUtils.isEmpty(email.getText().toString()))
                    {
                        email.setError("Please input email address");
                    }
                    if (TextUtils.isEmpty(password.getText().toString()))
                    {
                        password.setError("Please input your password");
                    }
                    if (TextUtils.isEmpty(fullName.getText().toString()))
                    {
                        fullName.setError("Please input your full name");
                    }
                    if (TextUtils.isEmpty(address.getText().toString()))
                    {
                        address.setError("Please input your address");
                    }
                    if (TextUtils.isEmpty(phnNumber.getText().toString()))
                    {
                        phnNumber.setError("Please input valid phone number");
                    }

                    if (termsAndConditions.isChecked())
                    {
                        submit.startAnimation();
                        submit.doneLoadingAnimation(R.color.colorPrimary,
                                getBitmapFromVectorDrawable(getApplicationContext(),
                                        R.drawable.ic_done_black_24dp));

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                User user = new User(email.getText().toString(), password.getText().toString(),
                                        fullName.getText().toString(), address.getText().toString(),
                                        Integer.parseInt(phnNumber.getText().toString()),unique_id,0,0);

                                myRef.child(unique_id).setValue(user);
                                Time time = new Time(Constant.TIME_IN_MILISECONDS, 0, 0, 0, 0,0);
                                DatabaseReference databaseReference = database.getReference("Time");
                                databaseReference.child(unique_id).setValue(time);
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else
                    {
                        Toast.makeText(SignUpActivity.this, "You must accept terms and conditions", Toast.LENGTH_LONG).show();
                    }

                }else
                {
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("No Internet Connection")
                            .setMessage("Please connect with internet and try again")
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setNeutralButton("Okay!!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        });


    }

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
