package csis3175.w23.g11.rooftown.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Pair;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.UUID;

public class ImageFileHelper {

    private static final String TAG = "IMAGE_FILE_HELPER";

    public static void readImage(Context context, String fileName, CallbackListener<Bitmap> imageCallback) {
        File image = new File(context.getFilesDir(), fileName);
        if (image.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            imageCallback.callback(bitmap);
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storage.getReference().child(fileName).getFile(image).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                imageCallback.callback(bitmap);
            });
        }
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
