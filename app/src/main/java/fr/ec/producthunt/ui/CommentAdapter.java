package fr.ec.producthunt.ui;

import android.util.Log;
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
import fr.ec.producthunt.data.model.Comment;


public class CommentAdapter extends BaseAdapter {

    private List<Comment> dataSource = Collections.emptyList();

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

        CommentAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            viewHolder = new CommentAdapter.ViewHolder();


            viewHolder.picture = convertView.findViewById(R.id.picture);
            viewHolder.username = convertView.findViewById(R.id.username);
            viewHolder.fullName = convertView.findViewById(R.id.fullname);
            viewHolder.headLine = convertView.findViewById(R.id.headline);
            viewHolder.content = convertView.findViewById(R.id.content);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommentAdapter.ViewHolder) convertView.getTag();
        }
        final Comment comment = dataSource.get(position);
        viewHolder.username.setText(comment.getUserName());
        viewHolder.fullName.setText(comment.getFullName());
        viewHolder.headLine.setText(comment.getHeadLine());
        viewHolder.content.setText(comment.getBody());

        Picasso.with(parent.getContext())
                .load(comment.getImageUrl())
                .centerCrop()
                .fit()
                .into(viewHolder.picture);



        return convertView;
    }

    public void showComments(List<Comment> comments) {
        dataSource = comments;

        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView picture;
        TextView username;
        TextView fullName;
        TextView headLine;
        TextView content;
    }


}
