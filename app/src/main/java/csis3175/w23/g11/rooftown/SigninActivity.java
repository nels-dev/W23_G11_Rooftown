package csis3175.w23.g11.rooftown;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import csis3175.w23.g11.rooftown.common.AppDatabase;
import csis3175.w23.g11.rooftown.databinding.ActivitySigninBinding;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.user.data.repository.UserProfileRepository;

public class SigninActivity extends AppCompatActivity {

    private static final String TAG = "SIGNIN";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> signInActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().requestIdToken(getString(R.string.default_web_client_id)).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, options);

        binding.btnGoogleSignIn.setOnClickListener(this::signIn);
        signInActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);
        mAuth = FirebaseAuth.getInstance();

        if (null != mAuth.getCurrentUser()) {
            Toast.makeText(this, "Welcome back, " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
    }

    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Log.d(TAG, "Google Sign-in successful, forwarding the token to firebase");
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
            String idToken = account.getIdToken();
            if (idToken != null) {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(this, this::onCompleteFirebaseAuth);
            } else {
                Log.e(TAG, "Google returned user with no ID Token");
            }
        } else {
            Log.w(TAG, "Signin cancelled");
        }
    }

    private void onCompleteFirebaseAuth(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");
            FirebaseUser user = mAuth.getCurrentUser();
            Log.i(TAG, "Firebase User Data: ");

            Toast.makeText(this, "Sign in successful. Hi " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

            UserProfile up = new UserProfile();
            up.setUserId(user.getUid());
            up.setUserName(user.getDisplayName());

            // Initialize database
            AppDatabase.init(this.getApplicationContext());

            new UserProfileRepository().createIfNotExist(up);

            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCredential:failure", task.getException());
        }
    }

    public void signIn(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInActivityResultLauncher.launch(signInIntent);
    }
}