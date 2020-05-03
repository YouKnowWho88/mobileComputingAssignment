package com.example.assignment3;
import android.Manifest;
import android.app.DownloadManager;


import android.content.ActivityNotFoundException;

import android.content.Context;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private String baseurl="https://raw.githubusercontent.com/revolunet/PythonBooks/master/";
    private Context context;
    private String[] booknames;
    private String[] level;
    private String[] detail;
    private String[] coverurls;
    private String[] book_urls;
    private DownloadManager downloadManager;


    public RecyclerViewAdapter(Context context,String json,DownloadManager dm){
        this.context=context;
        this.downloadManager=dm;
        JSONObject j = null;
        try {
            j = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonarray = null;
        try {
            jsonarray = j.getJSONArray("books");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonarray != null) {
            this.booknames = new String[jsonarray.length()];
            this.book_urls = new String[jsonarray.length()];
            this.coverurls = new String[jsonarray.length()];
            this.detail = new String[jsonarray.length()];
            this.level = new String[jsonarray.length()];


            for (int i = 0; i < jsonarray.length(); i++) {
                try {
                    booknames[i] = jsonarray.getJSONObject(i).getString("title");
                    level[i] = jsonarray.getJSONObject(i).getString("level");
                    detail[i] = jsonarray.getJSONObject(i).getString("info");
                    coverurls[i] = jsonarray.getJSONObject(i).getString("cover");
                    book_urls[i] = jsonarray.getJSONObject(i).getString("url");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View row= LayoutInflater.from(context).inflate(R.layout.list_item,null);
        ViewHolder vh=new ViewHolder(row);
        return vh;
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.title.setText(booknames[position]);
        holder.subtitle.setText(level[position]);
        holder.description.setText(detail[position]);
        final String extension=book_urls[position].substring((book_urls[position].length()-3));
        if(extension.equals("pdf") || extension.equals("zip")){
            holder.btn.setText("Download");

            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int result=PackageManager.PERMISSION_DENIED;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    } else {
                        result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    if (result == PackageManager.PERMISSION_GRANTED)
                    {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(book_urls[position]));
                        request.setAllowedOverRoaming(false);
                        request.setTitle("GadgetSaint");
                        request.setDescription("Downloading " + "GadgetSaint");
                        request.setVisibleInDownloadsUi(true);


                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + "GadgetSaint" +"."+extension);
                    downloadManager.enqueue(request);
                    }else{

                    }
                }
            });
        }
        else {
            holder.btn.setText("Online");
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book_urls[position]));
                        context.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Picasso.get().load(baseurl+coverurls[position]).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return booknames.length;
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView subtitle;
        public TextView description;
        public Button btn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.bookname);
            subtitle=itemView.findViewById(R.id.level);
            description=itemView.findViewById(R.id.longtext);
            btn=itemView.findViewById(R.id.btn);

        }
    }

}

