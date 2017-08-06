package com.payinekereg.treelogy.tabs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.activities.Contribute;
import com.payinekereg.treelogy.activities.EntranceActivity;
import com.payinekereg.treelogy.activities.MainActivity;
import com.payinekereg.treelogy.activities.SearchActivity;
import com.payinekereg.treelogy.activities.Tutorial;

import static com.payinekereg.treelogy.constants.MyConstants.*;

import static com.payinekereg.treelogy.constants.Constants_English.eCAMERA     ;
import static com.payinekereg.treelogy.constants.Constants_English.eGALLERY    ;


/**
 * Created by Emre on 3/11/2016.
 */
public class NewObservationTAB extends Fragment {

    private final boolean lang = EntranceActivity.lang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newobservation, container, false);
        Button btnSelectCamera = (Button) rootView.findViewById(R.id.btnSelectPhoto1);
        Button btnSelectGallery = (Button) rootView.findViewById(R.id.btnSelectPhoto2);
        Button btnSelectContribute = (Button) rootView.findViewById(R.id.btnSelectPhoto3);

        if(lang)
        {
            btnSelectCamera.setText(eCAMERA);
            btnSelectGallery.setText(eGALLERY);
            btnSelectContribute.setText("CONTRIBUTE");
        }

        btnSelectCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        btnSelectGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });

        btnSelectContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Contribute.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null)
        {
            if (requestCode == REQUEST_CAMERA )
                try
                {
                    onResultCamera(data);
                }
                catch (FileNotFoundException e) {e.printStackTrace();}
            else if(requestCode == SELECT_FILE)
                try
                {
                    onResultGallery(data);
                }
                catch (FileNotFoundException e) {e.printStackTrace();}
        }
    }

    private void onResultCamera(Intent data) throws FileNotFoundException {

        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        bmp = scaleDown(bmp, 256);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] byteArray = stream.toByteArray();
        try {stream.close();} catch (IOException e) {e.printStackTrace();}

        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("image", byteArray);
        startActivity(intent);
    }

    private void onResultGallery(Intent data) throws FileNotFoundException {

        Uri selectedImage = data.getData();

        Bitmap bmp = decodeUri(selectedImage);
        bmp = scaleDown(bmp, 256);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] byteArray = stream.toByteArray();
        try {stream.close();} catch (IOException e) {e.printStackTrace();}

        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("image", byteArray);
        startActivity(intent);
    }

   private Bitmap scaleDown(Bitmap realImage, int newImageSize) {

        final int old_width     = realImage.getWidth()  ;   // 1200
        final int old_height    = realImage.getHeight() ;   // 800

                                                            // 256
        float ratio = Math.max(
                (float) newImageSize / old_width,           // 256 / 1200
                (float) newImageSize / old_height);         // 256 / 800

        int new_width = Math.round(ratio * old_width);
        int new_height = Math.round(ratio * old_height);

        return Bitmap.createScaledBitmap(realImage, new_width, new_height, true);
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 256;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true)
        {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            ListTreesTAB.filteredModelList = ListTreesTAB.mTrees;
        }
    }
}