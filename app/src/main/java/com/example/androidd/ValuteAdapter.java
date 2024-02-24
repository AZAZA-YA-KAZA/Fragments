package com.example.androidd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidd.Values;

import java.util.ArrayList;

public class ValuteAdapter extends RecyclerView.Adapter<ValuteAdapter.ValuteHolder> {

    ArrayList<Values> valutes = new ArrayList<>();

    public ValuteAdapter(ArrayList<Values> valutes) {
        this.valutes = valutes;
    }

    @NonNull
    @Override
    public ValuteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.one_value,parent,false);

        return new ValuteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValuteHolder holder, int position) {
        Values valute = valutes.get(position);
        holder.name.setText(valute.getName());
        holder.value.setText(valute.getValue());
        holder.picture.setImageBitmap(valute.getPicture());
    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }

    class ValuteHolder extends RecyclerView.ViewHolder{
        TextView name,value;
        ImageView picture;


        public ValuteHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            value = itemView.findViewById(R.id.value);
            picture = itemView.findViewById(R.id.picture);
        }
    }
}
