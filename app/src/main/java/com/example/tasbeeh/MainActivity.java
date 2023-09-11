package com.example.tasbeeh;
import android.os.Bundle;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView counterTextView;
    private int currentCount = 0;
    private List<TasbeehItem> tasbeehItemList;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private Button saveButton;
    private static final int REQUEST_CODE_UPDATE = 123;
    private boolean isUpdateMode = false;
    private int selectedItemPosition = -1; // Initialize as -1
    private boolean isVibrationEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterTextView = findViewById(R.id.counterTextView);
        Button incrementButton = findViewById(R.id.incrementButton);
        ImageView resetButton = findViewById(R.id.resetButton);
        saveButton = findViewById(R.id.saveButton);
        Button savedCountsButton = findViewById(R.id.savedDhikrButton);
        ImageView vibrationButton = findViewById(R.id.vibrationButton);

        sharedPreferencesUtils = new SharedPreferencesUtils(getSharedPreferences("TasbeehAppPrefs", MODE_PRIVATE));
        tasbeehItemList = sharedPreferencesUtils.getSavedTasbeehItems();

        if (tasbeehItemList == null) {
            tasbeehItemList = new ArrayList<>();
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("action") && intent.getStringExtra("action").equals("continue")) {
            // Get the count from the intent
            int selectedCount = intent.getIntExtra("count", 0);

            // Get the selected item position from the intent
            selectedItemPosition = intent.getIntExtra("position", -1);

            // Update the counter with the selected count
            currentCount = selectedCount;
            updateCounterTextView();

            // Change the "Save" button text to "Update"
            saveButton.setText("Update");

            // Set a flag to indicate "Update" mode
            isUpdateMode = true;
        }
        else{
            currentCount = sharedPreferencesUtils.getCurrentCount();
            updateCounterTextView();
        }

        vibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the vibration state
                isVibrationEnabled = !isVibrationEnabled;
                if (isVibrationEnabled) {
                    // You can start vibrating here when it's enabled
                    startVibration();
                    showToast("Vibration is ON");
                } else {
                    // You can stop vibrating here when it's disabled
                    stopVibration();
                    showToast("Vibration is OFF");
                }
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCount();


                if (isVibrationEnabled) {
                    // Vibrate here when it's enabled
                    startVibration();
                }
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
                if (isUpdateMode) {
                    // Update the count and timestamp of the selected item
                    updateSelectedItem();
                } else {
                    showSaveDialog();
                }
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


        // Reset the flag and selected item position
        isUpdateMode = false;
        selectedItemPosition = -1;

        // Change the "Save" button text back to "Save"
        saveButton.setText("Save");
    }

    private void updateCounterTextView() {
        counterTextView.setText(String.valueOf(currentCount));
    }

    private void updateSelectedItem() {
        if (selectedItemPosition >= 0 && selectedItemPosition < tasbeehItemList.size()) {
            // Get the selected TasbeehItem
            TasbeehItem selectedItem = tasbeehItemList.get(selectedItemPosition);

            // Update the count and timestamp of the selected item
            selectedItem.setCount(currentCount);
            selectedItem.setTimestamp(System.currentTimeMillis());

            // Save the updated list
            sharedPreferencesUtils.saveTasbeehItems(tasbeehItemList);

            // Optionally, update the counter display
            updateCounterTextView();
        }
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

    private void startVibration() {
        // Check if vibration is supported (you should also request permission if needed)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);
        }
    }

    private void stopVibration() {
        // Stop the vibration
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).cancel();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    // Inside SavedCountsActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {
            // Update the "Save" button text to "Update"
            saveButton.setText("Update");
        }
    }


}
