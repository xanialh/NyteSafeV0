package com.example.nytesafev0;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private Button startMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        NavigationView navigationView = findViewById(R.id.navigationView);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textViewTitle = findViewById(R.id.textViewTitle);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> textViewTitle.setText(navDestination.getLabel()));

        startMapButton = (Button) findViewById(R.id.mapStart);
        startMapButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View clickedElement) { // When something in this page is clicked
                startActivity(new Intent(this, MapsActivity.class));
        }
}
