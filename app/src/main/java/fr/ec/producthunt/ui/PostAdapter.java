package fr.ec.producthunt.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Post;

import java.util.Collections;
import java.util.List;

public class PostAdapter extends BaseAdapter {

    private List<Post> dataSource = Collections.emptyList();

    public PostAdapter() {
    }

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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = position == 0 ? 0 : 1;
        return viewType;

    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            if (position == 0) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_post_item, parent, false);
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.subtitle = convertView.findViewById(R.id.subtitle);
            viewHolder.postImage = convertView.findViewById(R.id.img_product);
            viewHolder.comments = convertView.findViewById(R.id.number_of_comments);



            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Post post = dataSource.get(position);

        viewHolder.title.setText(post.getTitle());
        viewHolder.subtitle.setText(post.getSubTitle());


        viewHolder.comments.setText(String.valueOf(post.getCommentsCount()) + " commentaires" );

        Picasso.with(parent.getContext())
                .load(post.getImageUrl())
                .centerCrop()
                .fit()
                .into(viewHolder.postImage);


        final Context context = parent.getContext();

        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("POST_ID", post.getId());
                intent.putExtra("POST_TITLE", post.getTitle());
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public void showPosts(List<Post> posts) {
        dataSource = posts;

        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView title;
        TextView subtitle;
        ImageView postImage;
        TextView comments;
    }
}
