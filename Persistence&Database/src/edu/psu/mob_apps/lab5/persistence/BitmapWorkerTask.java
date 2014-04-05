package edu.psu.mob_apps.lab5.persistence;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

class BitmapWorkerTask extends AsyncTask<BitmapWorkerTaskArgs, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    
    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(BitmapWorkerTaskArgs... params) {
        return decodeSampledBitmapFromResource(params[0].fileName, params[0].maxWidth, params[0].maxHeight);
    }    
    
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
    // Once complete, see if ImageView is still around and set bitmap.

            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    
    public static Bitmap decodeSampledBitmapFromResource(String fileName,
            int maxWidth, int maxHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int maxWidth, int maxHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > maxHeight || width > maxWidth) {
	
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = (int) Math.ceil((float) height / 
	                                           (float) maxHeight);
	        final int widthRatio = (int) Math.ceil((float) width / (float) maxWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will 
	        // guarantee a final image with both dimensions larger than or equal 
	        // to the requested height and width.
	        inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}

}
