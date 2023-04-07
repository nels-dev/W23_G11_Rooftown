package csis3175.w23.g11.rooftown;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import csis3175.w23.g11.rooftown.common.AppDatabase;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.common.LocationHelper;
import csis3175.w23.g11.rooftown.messages.ui.view.AllChatsFragment;
import csis3175.w23.g11.rooftown.messages.ui.viewmodel.ChatViewModel;
import csis3175.w23.g11.rooftown.posts.ui.view.MyPostFragment;
import csis3175.w23.g11.rooftown.posts.ui.view.RoommatesFragment;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;
import csis3175.w23.g11.rooftown.user.ui.view.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN";
    BottomNavigationView bottomNav;
    HomeFragment homeFragment = new HomeFragment();
    RoommatesFragment roommatesFragment = new RoommatesFragment();
    AllChatsFragment allChatsFragment = new AllChatsFragment();
    MyPostFragment myPostFragment = new MyPostFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    private BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        AppDatabase.init(this.getApplicationContext());

        //Register listener to handle authentication expired or sign out
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null) {
                Toast.makeText(MainActivity.this, "You have signed out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
            }
            Boolean coarseLocationGranted = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
            }
            if (fineLocationGranted != null && fineLocationGranted) {
                // Precise location access granted.
                Log.d(TAG, "Precise location access granted.");
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                // Only approximate location access granted.
                Log.d(TAG, "Only approximate location access granted.");
            } else {
                // No location access granted.
                Log.d(TAG, "No location access granted.");
            }
        });

        locationPermissionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});

        bottomNav = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
        badgeDrawable = bottomNav.getOrCreateBadge(R.id.bottomNavMenuMessages);
        badgeDrawable.setVisible(false);

        loadAndListenToChatsAsync();
        loadPosts();

        bottomNav.setOnItemSelectedListener((@NonNull MenuItem item) -> {
            if (item.getItemId() == R.id.bottomNavMenuHome) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).addToBackStack(TAG).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuRoommates) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, roommatesFragment).addToBackStack(TAG).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuMessages) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, allChatsFragment).addToBackStack(TAG).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuPosting) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, myPostFragment).addToBackStack(TAG).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuProfile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, profileFragment).addToBackStack(TAG).commit();
                return true;
            } else {
                return false;
            }
        });

        // Such that the bottom navigation will hide when keyboard is showing
        KeyboardVisibilityEvent.setEventListener(this, isOpen -> bottomNav.setVisibility(isOpen ? View.GONE : View.VISIBLE));
    }

    private void loadAndListenToChatsAsync() {
        new Handler().post(() -> {
            ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
            viewModel.loadData();
            viewModel.getNumberOfUnread().observe(this, numOfUnread -> {
                if (numOfUnread > 0) {
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(numOfUnread);
                } else {
                    badgeDrawable.setVisible(false);
                }
            });
        });
    }

    private void loadPosts() {
        PostViewModel viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        LocationHelper.retryUntilCurrentLocationIsAvailable(this, loc -> {
            if (loc != null) {
                viewModel.loadData(new LatLng(loc.getLatitude(), loc.getLongitude()), CurrentUserHelper.getCurrentUid());
            }
        });
    }
}