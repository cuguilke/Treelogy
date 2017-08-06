package com.payinekereg.treelogy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.constants.MyConstants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static com.payinekereg.treelogy.constants.MyConstants.REQUEST_CAMERA;
import static com.payinekereg.treelogy.constants.MyConstants.SELECT_FILE;

/**
 * Created by emreakin on 10.07.2016.
 */
public class Contribute extends AppCompatActivity {

    private boolean lang = EntranceActivity.lang;
    private Button btn1;
    private Button btn2;
    private ImageView iv;
    private EditText et;
    private Button cancel;
    private Button contribute;
    private String latin_name;

    private byte[] byteArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contribute);

        btn1 = (Button) findViewById(R.id.contribute_btn1);
        btn2 = (Button) findViewById(R.id.contribute_btn2);
        iv = (ImageView)findViewById(R.id.contribute_iv);
        et = (EditText) findViewById(R.id.contribute_et);
        cancel = (Button) findViewById(R.id.contribute_cancel);
        contribute = (Button) findViewById(R.id.contribute_contribute);

        if(lang)
        {
            btn1.setText("CAMERA");
            btn2.setText("GALLERY");
            et.setHint("Please enter latin name of the leaf");
            cancel.setText("CANCEL");
            contribute.setText("CONTRIBUTE NOW");
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byteArray = null;
                finish();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });


        contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(byteArray == null)
                {
                    if(lang)
                        Toast.makeText(Contribute.this, "Please choose image", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(Contribute.this, "Fotoğraf yüklenmedi", Toast.LENGTH_LONG).show();
                }
                else if(et.getText().toString().trim().equals(""))
                {
                    if (lang)
                        Toast.makeText(Contribute.this, "Please enter latin name of the leaf", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(Contribute.this, "Lütfen türün latince ismini giriniz", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(MyConstants.connectionCheck(Contribute.this))
                    {
                        latin_name = et.getText().toString().trim();
                        new SendingImageToServer().execute(byteArray);
                    }
                    else
                        if(lang)
                            Toast.makeText(Contribute.this, "Network connection needed", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Contribute.this, "İnternet bağlantısı gerekli", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private class SendingImageToServer extends AsyncTask<byte[], Void, Boolean> {

        ProgressDialog pDialog;
        byte[] image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Contribute.this);
            if (lang) {
                pDialog.setTitle("Please wait");
                pDialog.setMessage("Sending to Server");
            } else {
                pDialog.setTitle("Bekleyin");
                pDialog.setMessage("Sunucuya gönderiliyor");
            }
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(byte[]... image) {

            this.image = image[0];
            try {
                donateImage(image[0], image[0].length, latin_name);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            pDialog.dismiss();

            if(success)
                if(lang)
                    Toast.makeText(Contribute.this, "Thank You!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Contribute.this, "Teşekkürler", Toast.LENGTH_LONG).show();

            finish();
        }
    }


    public int donateImage(byte[] image, int size, String imagename) throws IOException, SocketTimeoutException
    {
        int i;
        String imagesize = Integer.toString(size);
        String namesize = Integer.toString(imagename.length());
        String msg;
        Socket sock = new Socket("104.197.247.77", 50009);
        sock.setTcpNoDelay(true);
        DataOutputStream os2 = new DataOutputStream(sock.getOutputStream());
        BufferedReader inFromServer2 = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        try
        {
            //Receive '1' from server
            msg = inFromServer2.readLine();
            if(msg.equals("1"))
            {
                //Send donate trigger
                os2.writeBytes("donate");
                os2.flush();
                //Send imagesize
                os2.writeBytes(imagesize + '#');
                os2.flush();
                //Get response imagesize
                msg = inFromServer2.readLine();
                if(msg.equals(imagesize + '#'))
                {
                    //Send "OK"
                    os2.writeBytes("OK");
                    os2.flush();
                    //send latin name size
                    os2.writeBytes(namesize + "#");
                    os2.flush();
                    //send latin name of the image
                    for(i=0;i<imagename.length();i++)
                    {
                        os2.writeBytes(Character.toString(imagename.charAt(i)));
                        os2.flush();
                    }
                    //Send image
                    for(i=0;i<size;i++)
                    {
                        os2.write(image[i]);
                        os2.flush();
                    }
                    //Terminate the connection
                    msg = inFromServer2.readLine();
                }
            }
        }
        catch (SocketTimeoutException e)
        {
            e.printStackTrace();
        }
        finally
        {
            os2.close();
            inFromServer2.close();
            sock.close();
        }
        return 0;
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
        byteArray = stream.toByteArray();
        try {stream.close();} catch (IOException e) {e.printStackTrace();}

        iv.setImageBitmap(bmp);
    }

    private void onResultGallery(Intent data) throws FileNotFoundException {

        Uri selectedImage = data.getData();

        Bitmap bmp = decodeUri(selectedImage);
        bmp = scaleDown(bmp, 256);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
        try {stream.close();} catch (IOException e) {e.printStackTrace();}

        iv.setImageBitmap(bmp);
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
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

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

        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }
}

