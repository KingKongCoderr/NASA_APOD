package jaihind.gobblessamerica.peerprogrammingretrofit.Adapter;

import android.content.Context;
import android.graphics.Matrix;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import jaihind.gobblessamerica.peerprogrammingretrofit.Model.Nasa;
import jaihind.gobblessamerica.peerprogrammingretrofit.R;

/**
 * Created by nande on 1/24/2017.
 */

public class NasaAdapter extends RecyclerView.Adapter<NasaAdapter.ViewHolder> {

public List<Nasa> list;
    public Context context;
    int i=0;

    public ViewHolder holder;

    public ScaleGestureDetector scalegestdetector;
    public Matrix matrix=new Matrix();



    public NasaAdapter(List<Nasa> list,Context context) {
        this.list = list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewitem,parent,false);
        scalegestdetector=new ScaleGestureDetector(context,new ScaleListener());
        return new ViewHolder(v);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        scalegestdetector.onTouchEvent(ev);
        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float ScaleFactor=detector.getScaleFactor();
            ScaleFactor = Math.max(0.1f, Math.min(ScaleFactor, 5.0f));
            matrix.setScale(ScaleFactor, ScaleFactor);
            holder.mimage_view.setImageMatrix(matrix);

            return true;

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Nasa obj=list.get(position);

        holder.mdate_tv.setText(obj.getDate());
        String copyright="null";
       // holder.mcopyright_tv.setText("@Copyright:"+obj.getCopyright());
       if(copyright== String.valueOf(obj.getCopyright())){

        }
        else {
           holder.mcopyright_tv.setText("@Copyright:"+obj.getCopyright());
        }
       // String url=obj.getHdurl();
        //Picasso.with(context).load(url).resize(300,300).into(holder.mimage_view);
        Glide.with(context).load(obj.getHdurl()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(R.drawable.loading).error(R.drawable.notfound).into(holder.mimage_view);
        holder.mname_tv.setText(obj.getTitle());
       /* holder.mname_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mname_tv.setText(obj.getExplanation());
            }
        });*/
        holder.mimage_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i%2==0)
                    holder.mname_tv.setText(obj.getExplanation());
                else
                    holder.mname_tv.setText(obj.getTitle());
                i++;

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mdate_tv,mname_tv,mcopyright_tv;
        private ImageView mimage_view;

        public ViewHolder(View itemView) {
            super(itemView);
            mdate_tv = (TextView)itemView.findViewById(R.id.date_tv);
            mname_tv = (TextView)itemView.findViewById(R.id.card_tv);
            mimage_view = (ImageView)itemView.findViewById(R.id.card_iv);
            mcopyright_tv=(TextView)itemView.findViewById(R.id.copyright_tv);
        }

    }

}
