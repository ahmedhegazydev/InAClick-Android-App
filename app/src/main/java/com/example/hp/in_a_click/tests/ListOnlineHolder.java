package com.example.hp.in_a_click.tests;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.hp.in_a_click.R;

/**
 * Created by hp on 10/31/2017.
 */

public class ListOnlineHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private static ClickListener clickListener;
    TextView tvUserEmail = null;

    public ListOnlineHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);


        tvUserEmail = itemView.findViewById(R.id.tvUserEmail);


    }


    public void setOnItemClickListener(ClickListener clickListener) {
        ListOnlineHolder.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(getAdapterPosition(), v);
    }

    @Override
    public boolean onLongClick(View v) {
        clickListener.onItemLongClick(getAdapterPosition(), v);
        return false;
    }


    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

}
