package com.example.tasbeeh;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SavedCountsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SavedTasbeehAdapter adapter;
    private List<TasbeehItem> savedTasbeehItems;
    private SharedPreferencesUtils sharedPreferencesUtils;

    // Inside SavedCountsActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_counts);

        recyclerView = findViewById(R.id.savedCountsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        sharedPreferencesUtils = new SharedPreferencesUtils(getSharedPreferences("TasbeehAppPrefs", MODE_PRIVATE));

        // Load the saved Tasbeeh items
        savedTasbeehItems = sharedPreferencesUtils.getSavedTasbeehItems();

        // Ensure savedTasbeehItems is not null before creating the adapter
        if (savedTasbeehItems == null) {
            savedTasbeehItems = new ArrayList<>(); // Initialize as an empty list if null
        }

        Collections.reverse(savedTasbeehItems);
        adapter = new SavedTasbeehAdapter(savedTasbeehItems);
        recyclerView.setAdapter(adapter);
    }


    // RecyclerView adapter for displaying saved Tasbeeh items
    private class SavedTasbeehAdapter extends RecyclerView.Adapter<SavedTasbeehAdapter.ViewHolder> {
        private List<TasbeehItem> tasbeehItems;

        public SavedTasbeehAdapter(List<TasbeehItem> tasbeehItems) {
            this.tasbeehItems = tasbeehItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_tasbeeh, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (position >= 0 && position < tasbeehItems.size()) {
                TasbeehItem tasbeehItem = tasbeehItems.get(position);

                holder.nameTextView.setText(tasbeehItem.getName());
                holder.countTextView.setText(String.valueOf(tasbeehItem.getCount()));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                String formattedDate = sdf.format(new Date(tasbeehItem.getTimestamp()));
                holder.timestampTextView.setText(formattedDate);
            }
        }

        @Override
        public int getItemCount() {
            return tasbeehItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView;
            public TextView countTextView;
            public TextView timestampTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.savedNameTextView);
                countTextView = itemView.findViewById(R.id.savedCountTextView);
                timestampTextView = itemView.findViewById(R.id.savedTimestampTextView);
            }
        }
    }
}
