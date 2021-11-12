package lastie_wangechian_signin.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    //widgets and variables
    ImageButton signinButton;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    private String client_token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //intiliazing
        signinButton = findViewById(R.id.button_signin);

        createRequest();

    }

    private void createRequest() {
        //configure the Google Sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("449578568333-veoftdbdr0gj0i0bhqnllipjrsecvipo.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //building a GoogleSigninCLient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        //button listener
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initialize sign in intent
                Intent signIntent = mGoogleSignInClient.getSignInIntent();
                //start activity for result
                startActivityForResult(signIntent, 100);
            }
        });

        //intialize the firebase dependancies
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //check whether user is login
        if(firebaseUser != null){
            //redirect to profile activity.
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check condition
        if (requestCode == 100) {
            //checking requestcode is equally 100
            //intiliaze task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            //check condition
            if (signInAccountTask.isSuccessful()) {
                //when it is success, intialize string
                String its_wrap = "Google sign in a sucess";
                //display toast
                displayToast(its_wrap);

                try {
                    //intiliaze sign in to account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    //check condition
                    if (googleSignInAccount != null) {
                        //when google account is not null, then initialize auth credentials
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken(), null);

                        //check credentials
                        mAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            //redirect to profile activity.
                                            Intent intent_signIn = new Intent(getApplicationContext(), ProfileActivity.class);
                                            intent_signIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent_signIn);
                                            finish();
                                        }else{
                                            //display error message.
                                            Toast.makeText(getApplicationContext(), "Failed: " +
                                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String its_wrap) {
        //show message
        Toast.makeText(getApplicationContext(), its_wrap, Toast.LENGTH_SHORT).show();
    }
}