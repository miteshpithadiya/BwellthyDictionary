package com.bwellthy.bwellthydictionary.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwellthy.bwellthydictionary.R;
import com.bwellthy.bwellthydictionary.models.Word;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;

/**
 * Created by miteshp on 13/01/16.
 */
public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    private List<Word> mWordList;

    private DisplayImageOptions mOptions;

    private ImageLoader mImageLoader;

    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public TextView txtPrimaryText;

        public TextView txtSecondaryText;

        public ImageView imgAvatar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtPrimaryText = (TextView) view.findViewById(R.id.txt_primary);
            txtSecondaryText = (TextView) view.findViewById(R.id.txt_secondary);
            imgAvatar = (ImageView) view.findViewById(R.id.img_logo);
        }
    }

    public WordListAdapter(final Context context, final List<Word> wordList) {

        this.mContext = context;
        this.mWordList = wordList;
        ImageLoaderConfiguration mConfig = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread
                        .NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder
                        (QueueProcessingType.LIFO).build();
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(mConfig);

        this.mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable
                .ic_loading_image).showImageForEmptyUri(R.drawable.ic_empty_image)
                .showImageOnFail(R.drawable.ic_error_image).cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .adapter_avatar_three_line_text, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Word word = mWordList.get(position);
        holder.txtPrimaryText.setText(word.getWord());
        holder.txtSecondaryText.setText(word.getMeaning());

        final String strUrl = "http://appsculture.com/vocab/images/" + word.getId() + ".png";
        mImageLoader.displayImage(strUrl, holder.imgAvatar, mOptions);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWordCard(word);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    /**
     * Load a word card for better visual experience.
     */
    private void loadWordCard(final Word word) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_word_card, null);

        final TextView txtPrimaryText = (TextView) view.findViewById(R.id.txt_primary);
        txtPrimaryText.setText(word.getWord());

        final TextView txtSecondaryText = (TextView) view.findViewById(R.id.txt_secondary);
        txtSecondaryText.setText(word.getMeaning());

        final String strUrl = "http://appsculture.com/vocab/images/" + word.getId() + ".png";
        mImageLoader.displayImage(strUrl, (ImageView) view.findViewById(R.id.img_image), mOptions);

        new AlertDialog.Builder(mContext).setView(view).create().show();
    }
}
