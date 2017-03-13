package com.arjunarao.arjunrao.beacon;

/**
 * Created by Arjun Rao on 1/15/2017.
 */

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] firstLine;
    private String[] secondLine;
    private int[] imageIds;
    private Listener listener;

    public static interface Listener{
        public void onLongClick(int position);
    }


    public MyAdapter(String[] firstLine,String[] secondLine,int[] imageIds){
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.imageIds  = imageIds;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;



        public ViewHolder(CardView v) {
            super(v);
            cardView = v;


        }
    }
        public void setListener(Listener listener){
            this.listener = listener;
        }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cardview,parent,false);

        return new MyAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;
        ImageView imageView = (ImageView)cardView.findViewById(R.id.icon);
        Drawable drawable = cardView.getResources().getDrawable(imageIds[position]);
        imageView.setImageDrawable(drawable);

        TextView textView = (TextView) cardView.findViewById(R.id.firstLine);
        textView.setText(firstLine[position]);

        TextView textView1 = (TextView) cardView.findViewById(R.id.secondLine);
        textView1.setText(secondLine[position]);
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {




                if(listener != null){
                    listener.onLongClick(position);
                }
                return true;
            }
        });



    }

    @Override
    public int getItemCount() {
        return firstLine.length;
    }

}