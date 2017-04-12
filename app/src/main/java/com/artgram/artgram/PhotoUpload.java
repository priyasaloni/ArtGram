package com.artgram.artgram;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



import java.util.Random;

/**
 * Created by sonal on 09-04-2017.
 */
public class PhotoUpload extends AppCompatActivity{
    private ImageButton select_image;
    private EditText caption;
    private Button submitbutton;
    private Uri image_uri;
    public static final int GALLERY_REQUEST=1;
    public static final int CAMERA_REQUEST_CODE=100;
    private StorageReference storage;
    private ProgressDialog upload_progress;
    private FirebaseAuth mauth;
    private DatabaseReference database;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoupload);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
//        Drawable loginActivityBackground = findViewById(R.id.selectImage).getBackground();
        //      loginActivityBackground.setAlpha(127);
        database=FirebaseDatabase.getInstance().getReference().child("Posts");
        mauth=FirebaseAuth.getInstance();
        upload_progress=new ProgressDialog(this);
        storage= FirebaseStorage.getInstance().getReference();
        caption=(EditText)findViewById(R.id.caption);
        submitbutton=(Button)findViewById(R.id.submit);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
        select_image=(ImageButton)findViewById(R.id.selectImage);
        //verifyStoragePermissions(PhotoUpload.this);

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PhotoUpload.this);

                dialog.setContentView(R.layout.dialog_photoupload);
                dialog.show();
                Button dialogButton1 = (Button) dialog.findViewById(R.id.galleryDialogButton);
                Button dialogButton2 = (Button) dialog.findViewById(R.id.cameraDialogButton);

                // if button is clicked, close the custom dialog
                dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(PhotoUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                //Show Information about why you need the permission
                                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoUpload.this);
                                builder.setTitle("Need Storage Permission");
                                builder.setMessage("This app needs storage permission.");
                                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        ActivityCompat.requestPermissions(PhotoUpload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            } else if (permissionStatus.getBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, false)) {
                                //Previously Permission Request was cancelled with 'Dont Ask Again',
                                // Redirect to Settings after showing Information about why you need the permission
                                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoUpload.this);
                                builder.setTitle("Need Storage Permission");
                                builder.setMessage("This app needs storage permission.");
                                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        sentToSettings = true;
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            } else {
                                //just request the permission
                                ActivityCompat.requestPermissions(PhotoUpload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                            }

                            SharedPreferences.Editor editor = permissionStatus.edit();
                            editor.putBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, true);
                            editor.commit();


                        } else {
                            //You already have the permission, just go ahead.
                            proceedAfterPermission();
                        }
                        Intent gallery_intent = new Intent(Intent.ACTION_GET_CONTENT);
                        gallery_intent.setType("image/*");
                        startActivityForResult(gallery_intent, GALLERY_REQUEST);

                        // Intent intent = new Intent(getBaseContext(), GardenDetails.class);
                        //startActivity(intent);
                    }
                });

                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);

                        // Intent intent = new Intent(getBaseContext(), GardenDetails.class);
                        //startActivity(intent);
                    }
                });
            }
        });
    }
    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }
    private void startPosting()
    {
        upload_progress.setMessage("uploading..");
        upload_progress.show();
        final String caption_str=caption.getText().toString().trim();
        if(!TextUtils.isEmpty(caption_str)&& image_uri!=null)
        {
            StorageReference image_storage=storage.child("Post_Images").child(image_uri.getLastPathSegment());
            image_storage.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri download_uri=taskSnapshot.getDownloadUrl();
                    String user_id=mauth.getCurrentUser().getUid();

                    DatabaseReference new_post=database.push();
                    new_post.child("user_id").setValue(user_id);
                    new_post.child("image").setValue(download_uri.toString());

                    new_post.child("caption").setValue(caption_str);
                    upload_progress.dismiss();
                    Toast.makeText(PhotoUpload.this,"Successfully uploaded",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PhotoUpload.this,TabActivity.class));
                }
            });
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(PhotoUpload.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }}
        if (requestCode == GALLERY_REQUEST   && resultCode == RESULT_OK) {
            image_uri = data.getData();
            select_image.setImageURI(image_uri);
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            image_uri = data.getData();
      //      Log.d("imageUri",image_uri.toString());
            select_image.setImageURI(image_uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(PhotoUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(PhotoUpload.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(PhotoUpload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(PhotoUpload.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    // other 'case' lines to check for other
    // permissions this app might request
}


