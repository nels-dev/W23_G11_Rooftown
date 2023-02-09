package csis3175.w23.g11.rooftown;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninActivity extends AppCompatActivity{

    private static final String TAG = "SIGNIN";
    private SignInButton btnGoogleSignIn;
    private GoogleSignInClient signInClient;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> signInActivityResultLauncher;

    private Button btnSignout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        signInClient = GoogleSignIn.getClient(this, options);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(this::signIn);
        signInActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);
        mAuth = FirebaseAuth.getInstance();

        //Signout button is for demo purpose only, should be moved to profile page eventually
        btnSignout = findViewById(R.id.btnSignout);
        btnSignout.setOnClickListener(this::signOut);
    }

    public void signOut(View v){
        if (null != mAuth.getCurrentUser()){
            mAuth.signOut();
            signInClient.signOut();
            Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT).show();
        }else{
            Log.w(TAG, "No current user detected.");
        }
    }


    public void onActivityResult(ActivityResult result){
        if(result.getResultCode()==RESULT_OK){
            Log.d(TAG, "Google Signin successful, forwarding the signin to firebase");
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
            String idToken = account.getIdToken();
            if (idToken !=  null) {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(this, this::onCompleteFirebaseAuth);
            }
        }
    }

    private void onCompleteFirebaseAuth(Task<AuthResult> task){
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");
            FirebaseUser user = mAuth.getCurrentUser();
            Log.i(TAG, "Firebase User Data: ");
            Log.i(TAG, user.getDisplayName());
            Log.i(TAG, user.getEmail());
            Log.i(TAG, user.getUid());
            Toast.makeText(this, "Sign in successful. Hi " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCredential:failure", task.getException());
        }
    }

    public void signIn(View v) {
        Intent signInIntent = signInClient.getSignInIntent();
        signInActivityResultLauncher.launch(signInIntent);
    }
}