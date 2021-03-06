package my.app.smarthouse.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import io.reactivex.annotations.NonNull;

public class PictureResize {

    public static Bitmap getResizedBitmap(@NonNull Bitmap bm,@NonNull int newWidth,@NonNull int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
