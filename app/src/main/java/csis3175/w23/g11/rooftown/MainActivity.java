package csis3175.w23.g11.rooftown;

import android.Manifest;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import csis3175.w23.g11.rooftown.messages.ui.view.AllChatsFragment;
import csis3175.w23.g11.rooftown.messages.ui.viewmodel.ChatViewModel;
import csis3175.w23.g11.rooftown.user.ui.view.ProfileFragment;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;
import csis3175.w23.g11.rooftown.roommates.ui.view.MapViewFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    HomeFragment homeFragment = new HomeFragment();
    RoommatesFragment roommatesFragment = new RoommatesFragment();
    AllChatsFragment allChatsFragment = new AllChatsFragment();
    PostingFragment postingFragment = new PostingFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    private BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        DatabaseHelper.init(this);

        //Register listener to handle authentication expired or sign out
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null) {
                Toast.makeText(MainActivity.this, "You have signed out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    }
                    Boolean coarseLocationGranted = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false);
                    }
                    if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        bottomNav = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
        badgeDrawable = bottomNav.getOrCreateBadge(R.id.bottomNavMenuMessages);
        badgeDrawable.setVisible(false);

        loadAndListenToChatsAsync();

        bottomNav.setOnItemSelectedListener((@NonNull MenuItem item) -> {
            if (item.getItemId() == R.id.bottomNavMenuHome) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuRoommates) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, roommatesFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuMessages) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, allChatsFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuPosting) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, postingFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuProfile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, profileFragment).commit();
                return true;
            } else {
                return false;
            }
        });

        // Such that the bottom navigation will hide when keyboard is showing
        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> bottomNav.setVisibility(isOpen ? View.GONE : View.VISIBLE));
    }

    private void loadAndListenToChatsAsync() {
        new Handler().post(()->{
            ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
            viewModel.loadData();
            viewModel.getNumberOfUnread().observe(this, numOfUnread -> {
                if(numOfUnread >0){
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(numOfUnread);
                }else{
                    badgeDrawable.setVisible(false);
                }
            });
        });

    }

    public void switchToMapViewFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, new MapViewFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}