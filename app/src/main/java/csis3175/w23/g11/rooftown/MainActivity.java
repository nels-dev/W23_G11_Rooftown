package csis3175.w23.g11.rooftown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import csis3175.w23.g11.rooftown.messages.AllChatsFragment;

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

        bottomNav = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();

        BadgeDrawable badgeDrawable = bottomNav.getOrCreateBadge(R.id.bottomNavMenuMessages);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(3);

        bottomNav.setOnItemSelectedListener((@NonNull MenuItem item) -> {
            if (item.getItemId() == R.id.bottomNavMenuHome) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
                getSupportActionBar().setTitle("Rooftown");
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuRoommates) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, roommatesFragment).commit();
                getSupportActionBar().setTitle("Roommates");
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuMessages) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, allChatsFragment).commit();
                getSupportActionBar().setTitle("Messages");
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuPosting) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, postingFragment).commit();
                getSupportActionBar().setTitle("Posting");
                return true;
            } else if (item.getItemId() == R.id.bottomNavMenuProfile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, profileFragment).commit();
                getSupportActionBar().setTitle("Profile");
                return true;
            } else {
                return false;
            }
        });
    }
}