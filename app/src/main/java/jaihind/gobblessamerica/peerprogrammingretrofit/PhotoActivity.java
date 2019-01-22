package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {
    ImageButton mtakePhoto_bt,msubmit_bt;
    String mCurrentPhotoPath;
    ImageView mcamera_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mtakePhoto_bt = (ImageButton) findViewById(R.id.photo_button);
        mcamera_iv=(ImageView)findViewById(R.id.captured_iv);
        msubmit_bt=(ImageButton) findViewById(R.id.submit_bt);
        mtakePhoto_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                            requestPermission(2);
                        } else {
                            photoFile = createImageFile();
                        }


                    } catch (Exception ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                       /* Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                                "com.example.android.fileprovider",
                                photoFile);*/
                      //  Uri photoURI = Uri.fromFile(photoFile);
                        Uri photoURI= FileProvider.getUriForFile(getBaseContext(),getApplicationContext().getPackageName()+".provider",
                                createImageFile());
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 123);
                    }
                }
            }
        });
        msubmit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()){

                    FragmentManager frag_mgr=getSupportFragmentManager();
                    SubmitDialog dialog=new SubmitDialog();
                    dialog.show(frag_mgr,"Submit Dialgo");

                }else {
                    Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
                }




            }
        });

    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode== RESULT_OK){
            Toast.makeText(this, "Picture saved in Gallery/NASA APOD/ folder", Toast.LENGTH_LONG).show();
            setPic(mCurrentPhotoPath);
            galleryAddPic(mCurrentPhotoPath);
        }
    }

    private void setPic(String path) {
        // Get the dimensions of the View
        int targetW = mcamera_iv.getWidth();
        int targetH = mcamera_iv.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        //Bitmap bitmap=(Bitmap)data.getExtras().get(mCurrentPhotoPath);
        mcamera_iv.setImageBitmap(bitmap);
    }
    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir= this.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            String location = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/NASA APOD/";
            File storageDir=new File(location);
            storageDir.mkdirs();
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir  /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }
    private void requestPermission(int requestcode) {
        if(requestcode==1){
            ActivityCompat.requestPermissions(this  ,new String[]{Manifest.permission.CALL_PHONE},1);}
        if(requestcode==2){
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);}
    }
      

        // TODO: 2/15/2017 implement link feature
        // TODO: 2/15/2017 implement notifications
    // TODO: 2/16/2017 implement proper zoom capability use a library if needed 



}
