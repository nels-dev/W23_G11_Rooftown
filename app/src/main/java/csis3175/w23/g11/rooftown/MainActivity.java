package csis3175.w23.g11.rooftown;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import csis3175.w23.g11.rooftown.messages.ui.view.AllChatsFragment;
import csis3175.w23.g11.rooftown.user.ui.view.ProfileFragment;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    HomeFragment homeFragment = new HomeFragment();
    RoommatesFragment roommatesFragment = new RoommatesFragment();
    AllChatsFragment allChatsFragment = new AllChatsFragment();
    PostingFragment postingFragment = new PostingFragment();
    ProfileFragment profileFragment = new ProfileFragment();

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

        bottomNav = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();

        BadgeDrawable badgeDrawable = bottomNav.getOrCreateBadge(R.id.bottomNavMenuMessages);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(3);

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
}