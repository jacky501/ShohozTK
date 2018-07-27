package bdcash.dcash.bdcash;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText email,password,confirmPassword,fullName,address,phnNumber;
    Button submit;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String unique_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        myRef.keepSynced(false);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        phnNumber = findViewById(R.id.phnNumber);
        submit = findViewById(R.id.submit);

        unique_id = getDeviceUniqueID(this);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(unique_id).exists())
                {
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        User user = new User(email.getText().toString(),password.getText().toString(),
                                fullName.getText().toString(),address.getText().toString(),
                                Integer.parseInt(phnNumber.getText().toString()));

                        myRef.child(unique_id).setValue(user);
                        Time time = new Time(100000,0,0,0);
                        DatabaseReference databaseReference = database.getReference("Time");
                        databaseReference.child(unique_id).setValue(time);
                        Toast.makeText(SignUpActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id;
        device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
}
