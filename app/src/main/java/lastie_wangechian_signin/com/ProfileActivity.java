package lastie_wangechian_signin.com;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView profileName;
    Button btnLogout;
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //map respective widgets
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        btnLogout = findViewById(R.id.button_logout);
        //intialize firebase
        mAuth = FirebaseAuth.getInstance();
        //get current user login
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //check whether still logged in
        if (firebaseUser != null) {
            //if not null, seek google username and fetch the profile picture
            //how to set image into the imageview
            Picasso.get().load(firebaseUser.getPhotoUrl()).into(profileImage);
            //how to set user name into the textview
            profileName.setText(firebaseUser.getDisplayName());

        }

        googleSignInClient = GoogleSignIn.getClient(ProfileActivity.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign out from Google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //check condition
                        if (task.isSuccessful()) {
                            //signout if successful
                            mAuth.signOut();
                            Toast.makeText(getApplicationContext(), "logout successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}