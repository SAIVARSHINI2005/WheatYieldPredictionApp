package com.example.wheatyieldpredictionproject;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheatyieldpredictionproject.R;
import com.example.wheatyieldpredictionproject.FarmingBestPractice;

import java.util.List;

public class FarmingAdapter extends RecyclerView.Adapter<FarmingAdapter.ViewHolder> {

    private List<FarmingBestPractice> bestPractices;

    public FarmingAdapter(List<FarmingBestPractice> bestPractices) {
        this.bestPractices = bestPractices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_practice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FarmingBestPractice practice = bestPractices.get(position);
        holder.titleTextView.setText(practice.getTitle());
        holder.descriptionTextView.setText(practice.getDescription());
    }

    @Override
    public int getItemCount() {
        return bestPractices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
