package com.example.sawek.books;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONAdapter extends RecyclerView.Adapter<JSONAdapter.JSONViewHolder>{

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @NonNull
    @Override
    public JSONViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.row_book, null);
        return new JSONViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JSONViewHolder jsonViewHolder, int i) {
        JSONObject jsonObject =  mJsonArray.optJSONObject(i);
        if (jsonObject.has("cover_i")) { //czy posiada obrazek

            String imageID = jsonObject.optString("cover_i");

            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";

            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_books).into(jsonViewHolder.thumbnailImageView);
        } else {

            jsonViewHolder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }
        String bookTitle = "";
        String authorName = "";

        if (jsonObject.has("title")) {
            bookTitle = jsonObject.optString("title");
        }

        if (jsonObject.has("author_name")) {
            authorName = jsonObject.optJSONArray("author_name").optString(0);
        }

        jsonViewHolder.titleTextView.setText(bookTitle);
        jsonViewHolder.authorTextView.setText(authorName);


    }

    @Override
    public int getItemCount() {
        return mJsonArray.length();
    }

    public class JSONViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
        public JSONViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailImageView = itemView.findViewById(R.id.img_thumbnail);
            titleTextView = itemView.findViewById(R.id.text_title);
            authorTextView = itemView.findViewById(R.id.text_author);
        }
    }

    public void updateData(JSONArray jsonArray) {

        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
}