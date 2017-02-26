package com.example.arjunrao.beacon;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Arjun Rao on 1/14/2017.
 */

 class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private String[] captions;
    private int[] imageIds;

    public OptionsAdapter(String[] captions,int[] imageIds){
        this.captions = captions;
        this.imageIds  = imageIds;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }
    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cards,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView)cardView.findViewById(R.id.info_image);
        Drawable drawable = cardView.getResources().getDrawable(imageIds[position]);
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(captions[position]);
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(captions[position]);


    }

    @Override
    public int getItemCount() {
       return captions.length;
    }
}
