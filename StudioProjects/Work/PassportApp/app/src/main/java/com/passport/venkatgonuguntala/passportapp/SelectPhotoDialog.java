package com.passport.venkatgonuguntala.passportapp;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SelectPhotoDialog extends DialogFragment {

    private static final String TAG = "SelectPhotoDialog";

    public static final int CAMERA_REQUEST_CODE = 5467; //random number
    public static final int PICKFILE_REQUEST_CODE = 8352; //random number

    public interface OnPhotoReceivedListener {
        void getImagePath(Uri imagePath);
    }

    private OnPhotoReceivedListener onPhotoReceivedListener;

    private String currentPhotoPath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_photo, container, false);

        //textview for selecting an image from memory
        TextView selectPhoto = view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: accessing phones memory.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICKFILE_REQUEST_CODE);
            }
        });

        //textview for starting a camera
        TextView takePhoto = view.findViewById(R.id.dialogOpenCamera);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: starting camera");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    //create a file where photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.d(TAG, "onClick: " + ex.getMessage());
                    }
                    if ( photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                    "com.example.android.fileprovider",
                                    photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    }
                }
            }
        });

        return view;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  //prefix
                ".jpg",  //suffix
                storageDir      //directory
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult(): image: " + selectedImageUri);

            //send the bitmap and fragment to interface
            onPhotoReceivedListener.getImagePath(selectedImageUri);
            getDialog().dismiss();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Log.d(TAG, "onActivityResult(): image uri: " + currentPhotoPath);
            onPhotoReceivedListener.getImagePath(Uri.fromFile(new File(currentPhotoPath)));
            getDialog().dismiss();
        }

    }

    @Override
    public void onAttach(Context context) {
        try {
            onPhotoReceivedListener = (OnPhotoReceivedListener) getActivity();
        } catch (ClassCastException ex) {
            Log.e(TAG, "onAttach: ClassCastException", ex.getCause());
        }
        super.onAttach(context);
    }
}
