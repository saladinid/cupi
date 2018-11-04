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
import com.bumptech.glide.request.RequestOptions;
import com.codingdemos.flowers.R;
import com.codingdemos.vacapedia.data.PlanModel;
import com.codingdemos.vacapedia.VacaplanActivity;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter < PlanViewHolder > {

    private Context mContext;
    private List <PlanModel> mFlowerList;

    public PlanAdapter(Context mContext, List < PlanModel > mFlowerList) {
        this.mContext = mContext;
        this.mFlowerList = mFlowerList;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final PlanViewHolder holder, final int position) {
        if (mFlowerList.get(position).getImage() != "null" || mFlowerList.get(position).getImage() != null || mFlowerList.get(position).getImage() != "") {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(mContext)
                    .load(mFlowerList.get(position).getImage().replace(" ", "%20"))
                    .apply(options)
                    .into(holder.mImage);
        }
        holder.big_price.setText(mFlowerList.get(position).getCost());
        holder.mTitle.setText(mFlowerList.get(position).getTitle());
        holder.target_date.setText(mFlowerList.get(position).getTarget_date());
        holder.textViewDesc.setText(mFlowerList.get(position).getBody_copy());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VacaplanActivity.class);
                intent.putExtra("name", mFlowerList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("image", mFlowerList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("body_copy", mFlowerList.get(holder.getAdapterPosition()).getBody_copy());
                intent.putExtra("target_date", mFlowerList.get(holder.getAdapterPosition()).getTarget_date());
                intent.putExtra("location", "");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFlowerList.size();
    }

}

class PlanViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle, big_price, target_date, location, textViewDesc;
    CardView mCardView;

    PlanViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.image);
        mTitle = itemView.findViewById(R.id.title);
        mCardView = itemView.findViewById(R.id.card_plan);
        big_price = itemView.findViewById(R.id.big_price);
        target_date = itemView.findViewById(R.id.target_date);
        location = itemView.findViewById(R.id.location);
        textViewDesc = itemView.findViewById(R.id.textViewDesc);

    }
}