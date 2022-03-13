package com.example.currencydisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;


public class ValuteAdapter extends RecyclerView.Adapter<ValuteAdapter.ValuteViewHolder>{

    Context context;
    List<Valute> valutes;

    public ValuteAdapter(Context context, List<Valute> valutes) {
        this.context = context;
        this.valutes = valutes;
    }

    @NonNull
    @Override
    public ValuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View valuteItems = LayoutInflater.from(context).inflate(R.layout.valute_item, parent, false);

        return new ValuteViewHolder(valuteItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ValuteViewHolder holder, int position) {
        holder.valuteTitle.setText(valutes.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }

    public static final class ValuteViewHolder extends RecyclerView.ViewHolder{

        TextView valuteTitle;

        public ValuteViewHolder(@NonNull View itemView) {
            super(itemView);

            valuteTitle = itemView.findViewById(R.id.valuteTitle);

        }
    }


}

