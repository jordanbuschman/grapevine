package com.jar.hfth.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by August on 3/1/15.
 */
public class PostAdapter extends ArrayAdapter<Posts> {
    private final List<Posts> post;

    public PostAdapter(Context context, int resource, List<Posts> post){

        super(context, resource, post);
        this.post = post;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return getPostView(position, convertView, parent);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getPostView(position, convertView, parent);
    }


    public View getPostView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.layout_single, null);
     //   TextView date = (TextView)row.findViewById(R.id.date);
     //   date.setText(post.get(position).getDate() + "");
    //    TextView user = (TextView)row.findViewById(R.id.username);
     //   user.setText(post.get(position).getUser() + "");
        TextView title = (TextView)row.findViewById(R.id.textView);
        title.setText(post.get(position).getTitle() + "");
        ImageView image = (ImageView)row.findViewById(R.id.imageView);

        switch (post.get(position).getGrove()){

            case 2:
                image.setImageResource(R.drawable.food);
                break;
            case 3:
                image.setImageResource(R.drawable.tent);
                break;
            default:
                image.setImageResource(R.drawable.reciprocity);
                break;
        }





        return row; }
}
