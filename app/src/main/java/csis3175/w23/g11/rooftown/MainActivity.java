package csis3175.w23.g11.rooftown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    HomeFragment homeFragment = new HomeFragment();
    RoommatesFragment roommatesFragment = new RoommatesFragment();
    MessagesFragment messagesFragment = new MessagesFragment();
    PostingFragment postingFragment = new PostingFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        BadgeDrawable badgeDrawable = bottomNav.getOrCreateBadge(R.id.messages);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(3);

        bottomNav.setOnItemSelectedListener((@NonNull MenuItem item) -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.roommates) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, roommatesFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.messages) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, messagesFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.posting) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, postingFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
            } else {
                return false;
            }
        });
    }
}