package fr.ec.producthunt.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Collection;



public class CollectionAdapter extends BaseAdapter {

    private List<Collection> dataSource = Collections.emptyList();



    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        CollectionAdapter.ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
            viewHolder = new CollectionAdapter.ViewHolder();


            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.backgroundImage = convertView.findViewById(R.id.background_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CollectionAdapter.ViewHolder) convertView.getTag();
        }

        final Collection collection = dataSource.get(position);


        viewHolder.name.setText(collection.getName());
        viewHolder.title.setText(collection.getTitle());


        Picasso.with(parent.getContext())
                .load(collection.getImageUrl())
                .centerCrop()
                .fit()
                .into(viewHolder.backgroundImage);

        return convertView;
    }

    public void showCollections(List<Collection> collections) {
        dataSource = collections;

        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView name;
        TextView title;
        ImageView backgroundImage;
    }


}
