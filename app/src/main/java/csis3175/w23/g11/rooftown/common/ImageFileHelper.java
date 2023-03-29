package csis3175.w23.g11.rooftown.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Helper class for saving and loading images
 * Three optimizations are in place
 * 1. In-memory and on-disk cache is added so that images takes the shortest possible route to get loaded
 * 2. Before putting in in-memory cache, thumbnail images are optimized by resampling
 * 3. Image loading runs asynchronously and the callback (which usually is setting ImageView runs on the main thread
 */
public class ImageFileHelper {

    private static final String TAG = "IMAGE_FILE_HELPER";

    public static Map<String, Bitmap> THUMBNAIL_CACHE = new HashMap<>();

    public static void readImage(Context context, String fileName, CallbackListener<Bitmap> imageCallback) {
        readImage(context, fileName, imageCallback, true);
    }

    public static void readImage(Context context, String fileName, CallbackListener<Bitmap> imageCallback, boolean thumbnail) {
        AsyncTask.execute(() -> {
            if (thumbnail && THUMBNAIL_CACHE.containsKey(fileName)) {
                Log.d(TAG, "Cache Access, image size: " + THUMBNAIL_CACHE.get(fileName).getByteCount());
                invokeCallback(imageCallback, THUMBNAIL_CACHE.get(fileName));
            }

            File image = new File(context.getFilesDir(), fileName);
            if (image.exists()) {
                loadLocalImage(image, thumbnail, fileName, imageCallback);
            } else {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReference().child(fileName).getFile(image).addOnSuccessListener(taskSnapshot -> {
                    loadLocalImage(image, thumbnail, fileName, imageCallback);
                });
            }
        });
    }

    private static void invokeCallback(CallbackListener<Bitmap> imageCallback, Bitmap image) {
        new Handler(Looper.getMainLooper()).post(() -> imageCallback.callback(image));
    }

    private static void loadLocalImage(File image, boolean thumbnail, String fileName, CallbackListener<Bitmap> imageCallback) {
        Bitmap bitmap = decodeAndOptimize(image.getAbsolutePath(), thumbnail);
        if (thumbnail) THUMBNAIL_CACHE.put(fileName, bitmap);
        invokeCallback(imageCallback, bitmap);
    }

    private static Bitmap decodeAndOptimize(String path, boolean thumbnail) {
        if (!thumbnail) return BitmapFactory.decodeFile(path);

        BitmapFactory.Options decodeBoundsOpt = new BitmapFactory.Options();
        decodeBoundsOpt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, decodeBoundsOpt);
        int origHeight = decodeBoundsOpt.outHeight;
        int origWidth = decodeBoundsOpt.outWidth;

        BitmapFactory.Options loadWithResampling = new BitmapFactory.Options();
        loadWithResampling.inSampleSize = Math.max(origHeight, origWidth) / 300;
        return BitmapFactory.decodeFile(path, loadWithResampling);
    }

    public static void saveImage(Context context, Uri filePath, CallbackListener<Pair<String, Bitmap>> imageCallback) {
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        String fileName = UUID.randomUUID().toString();
        StorageReference imageFileRef = storage.child(fileName);
        imageFileRef.putFile(filePath).addOnSuccessListener(uploadTask -> {
            File file = new File(context.getFilesDir(), fileName);
            imageFileRef.getFile(file).addOnSuccessListener(downloadTask -> {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageCallback.callback(Pair.create(fileName, bitmap));
            });
        });
    }
}
