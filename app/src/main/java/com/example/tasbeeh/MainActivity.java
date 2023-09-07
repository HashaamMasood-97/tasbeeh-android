package com.example.tasbeeh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView counterTextView;
    private int currentCount = 0;
    private List<TasbeehItem> tasbeehItemList;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterTextView = findViewById(R.id.counterTextView);
        Button incrementButton = findViewById(R.id.incrementButton);
        Button resetButton = findViewById(R.id.resetButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button savedCountsButton = findViewById(R.id.savedDhikrButton);

        sharedPreferencesUtils = new SharedPreferencesUtils(getSharedPreferences("TasbeehAppPrefs", MODE_PRIVATE));
        tasbeehItemList = sharedPreferencesUtils.getSavedTasbeehItems();

        if (tasbeehItemList == null) {
            tasbeehItemList = new ArrayList<>();
        }

        currentCount = sharedPreferencesUtils.getCurrentCount();
        updateCounterTextView();

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCount();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCount();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveDialog();
            }
        });

        savedCountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSavedCountsActivity();
            }
        });
    }

    private void incrementCount() {
        currentCount++;
        updateCounterTextView();
    }

    private void resetCount() {
        currentCount = 0;
        updateCounterTextView();
    }

    private void updateCounterTextView() {
        counterTextView.setText(String.valueOf(currentCount));
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Tasbeeh Name");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (!name.isEmpty()) {
                    saveTasbeeh(name);
                }

                navigateToSavedCountsActivity();

                // Clear the state of the counter (optional)
                resetCount();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void saveTasbeeh(String name) {
        long timestamp = System.currentTimeMillis();
        TasbeehItem tasbeehItem = new TasbeehItem(name, currentCount, timestamp);

        if (tasbeehItemList == null) {
            tasbeehItemList = new ArrayList<>();
        }

        tasbeehItemList.add(0,tasbeehItem);
        currentCount = 0;
        updateCounterTextView();



        sharedPreferencesUtils.saveTasbeehItems(tasbeehItemList);
        sharedPreferencesUtils.setCurrentCount(currentCount);

    }

    private void navigateToSavedCountsActivity() {
        Intent intent = new Intent(MainActivity.this, SavedCountsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the data from SharedPreferences
        tasbeehItemList = sharedPreferencesUtils.getSavedTasbeehItems();
        if (tasbeehItemList == null) {
            tasbeehItemList = new ArrayList<>();
        }
        updateCounterTextView();
    }
}
