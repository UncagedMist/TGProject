package tbc.techbytecare.kk.touristguideproject.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tbc.techbytecare.kk.touristguideproject.R;

public class HomePlaceViewHolder extends RecyclerView.ViewHolder {

    public TextView txtPlaceName;
    public ImageView imgPlaceImage;

    public HomePlaceViewHolder(View itemView) {
        super(itemView);

        txtPlaceName = itemView.findViewById(R.id.txtPlaceName);
        imgPlaceImage = itemView.findViewById(R.id.imgPlaceImage);
    }
}
