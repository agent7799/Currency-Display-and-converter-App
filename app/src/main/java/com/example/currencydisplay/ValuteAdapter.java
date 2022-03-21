package com.example.currencydisplay;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ValuteAdapter extends RecyclerView.Adapter<ValuteAdapter.ValuteViewHolder>{

    Context context;
    static List<Valute> valutes;


//    public interface ListItemClickListener {
//        void onListItemClick(int clickedItemIndex);
//    }


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
        String text = (position + 1)
                + "\t " + valutes.get(position).getNominal() + " "
                + valutes.get(position).getName() + "\n"
                + "\t\t\t\t" + valutes.get(position).getValue() + " руб.";

        holder.valuteTitle.setText(text);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int clickedPosition = getAdapterPosition();
//                Context context = view.getContext();
//                Class destinationActivity = ConverterActivity.class;
//                Intent intent = new Intent(context, destinationActivity);
//                intent.putExtra(Intent.EXTRA_INDEX, clickedPosition);
//                context.startActivity(intent);
//            }
   //     });

    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }




    class ValuteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView valuteTitle;

        public ValuteViewHolder(@NonNull View itemView) {
            super(itemView);
            valuteTitle = itemView.findViewById(R.id.valuteTitle);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Context context = view.getContext();
            Intent intent = new Intent(context, ConverterActivity.class);
            intent.putExtra("clickedPosition", clickedPosition);
            context.startActivity(intent);
            Log.d("MyLog", "itemClick " + clickedPosition + " ");
        }

    }
}

