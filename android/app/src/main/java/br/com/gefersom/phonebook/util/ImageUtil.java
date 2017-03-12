package br.com.gefersom.phonebook.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.IOException;

import br.com.gefersom.phonebook.R;

/**
 * Created by me on 21/9/2016.
 */
public class ImageUtil {

    static String TAG = ImageUtil.class.getSimpleName();

    static public boolean loadBitmapOrUseAFakeBitmap(String uri, ImageView imageView, String personID) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageView.getContext().getContentResolver(), Uri.parse(uri));
            imageView.setImageBitmap(bitmap);

            return true;
        } catch (Exception e) {
            imageView.setImageResource( getFakeBitmapForContact(Integer.valueOf(personID)) );
        }

        return false;
    }

    static public boolean loadBitmap(String uri, ImageView imageView, int defaultImageId) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageView.getContext().getContentResolver(), Uri.parse(uri));
            imageView.setImageBitmap(bitmap);

            return true;
        } catch (Exception e) {
            imageView.setImageResource( defaultImageId );
        }

        return false;
    }

    public static int getFakeBitmapForContact(int index) {
        switch (index % 5) {
            case 0:
                return R.drawable.ic_account_circle_blue_36dp;
            case 1:
                return R.drawable.ic_account_circle_green_36dp;
            case 2:
                return R.drawable.ic_account_circle_orange_36dp;
            case 3:
                return R.drawable.ic_account_circle_purple_36dp;
            case 4:
                return R.drawable.ic_account_circle_grey_36dp;
        }

        return R.drawable.ic_account_circle_grey_36dp;
    }

    static public void loadImageInBackground(ImageView imageView, String imageUri, String personID) {
        new LoadBitmapInBackground(imageView, Integer.valueOf(personID)).execute(imageUri);
    }

    static public class LoadBitmapInBackground extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;
        private final Context context;
        private final int personID;

        public LoadBitmapInBackground(ImageView imageView, int personID) {
            this.context = imageView.getContext();
            this.imageView = imageView;
            this.personID = personID;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String uri = params[0];
            if (uri == null) {
                return null;
            }

            try {
                return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(uri));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            else{
                imageView.setImageResource( getFakeBitmapForContact(personID) );
            }
        }
    }
}
