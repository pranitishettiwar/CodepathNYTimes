package com.codepath.nytimessearch.adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.model.Article;

/**
 * Created by praniti on 9/19/17.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the data item for position
        Article article = this.getItem(position);

        //check to see if existing view being reused
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        //find the image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(article != null ? article.getHeadline() : null);

        String thumbnail = article.getThumbNail();

        if(!TextUtils.isEmpty(thumbnail)){
            Glide.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;
    }
}
