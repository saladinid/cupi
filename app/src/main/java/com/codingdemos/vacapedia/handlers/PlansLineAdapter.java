package com.codingdemos.vacapedia.handlers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codingdemos.flowers.R;
import com.codingdemos.vacapedia.EditPlanActivity;
import com.codingdemos.vacapedia.EditPromoActivity;
import com.codingdemos.vacapedia.data.PlanModel;
import com.codingdemos.vacapedia.data.PromosModel;

import java.util.List;

public class PlansLineAdapter extends RecyclerView.Adapter < PlanLineViewHolder > {

    private Context mContext;
    private List <PlanModel> mFlowerList;

    public PlansLineAdapter(Context mContext, List <PlanModel> mFlowerList) {
        this.mContext = mContext;
        this.mFlowerList = mFlowerList;
    }

    @Override
    public PlanLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_item_notification, parent, false);
        return new PlanLineViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final PlanLineViewHolder holder, int position) {
//        Glide.with(mContext)
//                .load(mFlowerList.get(position).getImage().replace(" ", "%20"))
//                .into(holder.mImage);
        holder.mTitle.setText(mFlowerList.get(position).getTitle());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, EditPlanActivity.class);
                mIntent.putExtra("_id", mFlowerList.get(holder.getAdapterPosition()).get_id());
                mIntent.putExtra("body_copy", mFlowerList.get(holder.getAdapterPosition()).getBody_copy());
                mIntent.putExtra("content", mFlowerList.get(holder.getAdapterPosition()).getContent());
                mIntent.putExtra("target_date", mFlowerList.get(holder.getAdapterPosition()).getTarget_date());
                mIntent.putExtra("title", mFlowerList.get(holder.getAdapterPosition()).getTitle());
                mIntent.putExtra("target_time", mFlowerList.get(holder.getAdapterPosition()).getTarget_time());
                mIntent.putExtra("destinations", String.valueOf(mFlowerList.get(holder.getAdapterPosition()).getDestinations()));
                //mIntent.putExtra("destinations", String.valueOf(mFlowerList.get(holder.getAdapterPosition()).getDestinations()).substring(1, String.valueOf(mFlowerList.get(holder.getAdapterPosition()).getDestinations()).length()-1));
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFlowerList.size();
    }
}

class PlanLineViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    CardView mCardView;

    PlanLineViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mCardView = itemView.findViewById(R.id.cardview);
    }

}
