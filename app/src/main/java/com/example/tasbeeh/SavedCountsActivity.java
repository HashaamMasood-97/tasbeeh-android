package com.example.tasbeeh;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.Collections;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
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
    private TextView emptyTextView;




    // Inside SavedCountsActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_counts);
        setTitle("Tasbeeh List");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyTextView = findViewById(R.id.emptyTextView);


        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.savedCountsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        sharedPreferencesUtils = new SharedPreferencesUtils(getSharedPreferences("TasbeehAppPrefs", MODE_PRIVATE));

        savedTasbeehItems = sharedPreferencesUtils.getSavedTasbeehItems();

        // Ensure savedTasbeehItems is not null before creating the adapter
        if (savedTasbeehItems == null) {
            savedTasbeehItems = new ArrayList<>(); // Initialize as an empty list if null
        }


        adapter = new SavedTasbeehAdapter(savedTasbeehItems);
        recyclerView.setAdapter(adapter);

        if (savedTasbeehItems.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }

        adapter.setOnItemClickListener(new SavedTasbeehAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
            @Override
            public void onBarIconClick(int position) {
                // Handle the bar icon click here, e.g., show a dialog or perform any desired action
                View clickedView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                showPopupMenu(position, clickedView);
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Respond to the back button press on the Toolbar
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // RecyclerView adapter for displaying saved Tasbeeh items
    private static class SavedTasbeehAdapter extends RecyclerView.Adapter<SavedTasbeehAdapter.ViewHolder> {
        private List<TasbeehItem> tasbeehItems;

        public SavedTasbeehAdapter(List<TasbeehItem> tasbeehItems) {
            this.tasbeehItems = tasbeehItems;
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
            void onBarIconClick(int position);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_tasbeeh, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (position >= 0 && position < tasbeehItems.size()) {
                TasbeehItem tasbeehItem = tasbeehItems.get(position);

                holder.nameTextView.setText(tasbeehItem.getName());
                holder.countTextView.setText(String.valueOf(tasbeehItem.getCount()));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                String formattedDate = sdf.format(new Date(tasbeehItem.getTimestamp()));
                holder.timestampTextView.setText(formattedDate);
            }

            holder.barIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onBarIconClick(position);
                    }
                }
            });




        }

        @Override
        public int getItemCount() {
            return tasbeehItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView;
            public TextView countTextView;
            public TextView timestampTextView;
            public ImageView barIconImageView;

            public ViewHolder(View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.savedNameTextView);
                countTextView = itemView.findViewById(R.id.savedCountTextView);
                timestampTextView = itemView.findViewById(R.id.savedTimestampTextView);
                barIconImageView = itemView.findViewById(R.id.barIconImageView);



                /*itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }); */

                barIconImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                            onItemClickListener.onBarIconClick(position);
                        }
                    }
                });

            }
        }
    }





    private void showPopupMenu(final int position, View anchorView) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView, Gravity.END); // Show the popup menu anchored to the toolbar
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_delete) {
                    showSecondDeleteConfirmationDialog(position);
                    return true;
                } else if (item.getItemId() == R.id.menu_continue) {
                    continueSelectedItem(position);
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }



    private void showSecondDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the item from the list and update the RecyclerView
                        deleteItem(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, just close the dialog
                    }
                })
                .create()
                .show();
    }




    private void deleteItem(int position) {
        if (position >= 0 && position < savedTasbeehItems.size()) {
            savedTasbeehItems.remove(position);
            adapter.notifyItemRemoved(position);

            // Update your SharedPreferencesUtils to save the updated list
            sharedPreferencesUtils.saveTasbeehItems(savedTasbeehItems);
            adapter.notifyDataSetChanged();

            if (savedTasbeehItems.isEmpty()) {
                emptyTextView.setVisibility(View.VISIBLE);
            } else {
                emptyTextView.setVisibility(View.GONE);
            }

        }
    }
    private void continueSelectedItem(final int position) {
        int selectedCount = savedTasbeehItems.get(position).getCount();

        // Create an intent to navigate to MainActivity and pass the count and selected item position
        Intent intent = new Intent(SavedCountsActivity.this, MainActivity.class);
        intent.putExtra("action", "continue");
        intent.putExtra("count", selectedCount);
        intent.putExtra("position", position); // Pass the selected item position
        startActivity(intent);
    }






}
