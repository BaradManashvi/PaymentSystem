package com.example.payment.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.payment.R;
import com.example.payment.helper.BottomNavHelp;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView toolbartext;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private OffersFragment offersFragment;
    private PaymentFragment paymentFragment;
    private TransactionFragment transactionFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = item -> {
        int id = item.getItemId();
        if (id == R.id.navigation_home) {
            toolbartext.setText("Phone Payment");
            setUpFragment(homeFragment);
            return true;
        } else if (id == R.id.navigation_offers) {
            toolbartext.setText("Offers");
            setUpFragment(offersFragment);
            return true;
        } else if (id == R.id.navigation_payment) {
            toolbartext.setText("Scan & Pay");
            setUpFragment(paymentFragment);
            return true;
        } else if (id == R.id.navigation_account) {
            toolbartext.setText("My Account");
            setUpFragment(accountFragment);
            return true;
        } else if (id == R.id.navigation_transactions) {
            toolbartext.setText("Transaction");
            setUpFragment(transactionFragment);
            return true;
        }
        return false;
    };

    private void setUpFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.home_view, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        toolbartext.setText("Phone Payment");

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        BottomNavHelp.removeShiftMode(bottomNavigationView);

        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.home_view, homeFragment);
        beginTransaction.commit();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        toolbartext = findViewById(R.id.toolbartext);
        bottomNavigationView = findViewById(R.id.navigation);
        homeFragment = HomeFragment.newInstance();
        accountFragment = AccountFragment.newInstance();
        offersFragment = OffersFragment.newInstance();
        paymentFragment = PaymentFragment.newInstance();
        transactionFragment = TransactionFragment.newInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notify) {
            Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.scanNpay) {
            Toast.makeText(this, "Scan any code", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
