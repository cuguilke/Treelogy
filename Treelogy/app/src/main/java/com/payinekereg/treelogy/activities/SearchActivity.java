package com.payinekereg.treelogy.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.constants.MyConstants;

import static com.payinekereg.treelogy.constants.Constants_English.eACCEPT_SAVE;
import static com.payinekereg.treelogy.constants.Constants_English.eCANCEL;
import static com.payinekereg.treelogy.constants.Constants_English.eSAVED;
import static com.payinekereg.treelogy.constants.Constants_English.eSEARCH;
import static com.payinekereg.treelogy.constants.MyConstants.connectionCheck;

/**
 * Created by Emre on 4/24/2016.
 */
public class SearchActivity extends AppCompatActivity {

    private final boolean lang = EntranceActivity.lang;
    private Dialog dialog   ;
    private String path     ;

    private String[] names       = new String[5];
    private String[] percentages = new String[5];

    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ll = (LinearLayout) findViewById(R.id.searchactivity_ll);
        ll.setVisibility(View.GONE);

        final byte[] data = getIntent().getByteArrayExtra("image");
        final Bitmap bmp = BitmapFactory.decodeByteArray(data , 0, data.length);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.serverdialog);
        dialog.setCancelable(false);
        dialog.show();

        final TextView tv   = (TextView)    dialog.findViewById(R.id.serverdialog_title );
        final ImageView iv  = (ImageView)   dialog.findViewById(R.id.serverdialog_iv    );
        final Button btn1   = (Button)      dialog.findViewById(R.id.serverdialog_btn1  );
        final Button btn2   = (Button)      dialog.findViewById(R.id.serverdialog_btn2  );

        init(tv, btn1, btn2);
        iv.setImageBitmap(bmp);

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialog.dismiss();
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(connectionCheck(SearchActivity.this))
                {
                    try {search(data, bmp);}
                    catch (IOException e) {e.printStackTrace();}
                }
                else
                {
                    if(lang)
                        Toast.makeText(SearchActivity.this, "Network connection needed", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(SearchActivity.this, "İnternet bağlantısı gerekli", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(TextView tv, Button btn1, Button btn2)
    {
        if (lang)
        {
            tv.setText(eSEARCH);
            btn1.setText(eCANCEL);
            btn2.setText(eSEARCH);
        }
    }

    private void search(byte[] image, Bitmap bmp) throws IOException
    {
        ImageView iv = (ImageView) findViewById(R.id.searchactivity_iv);
        if (iv != null)
            iv.setImageBitmap(bmp);

        new SendingImageToServer().execute(image);

    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            try
            {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/TreeLogy");
                dir.mkdirs();

                String fileName = String.format("%s_%d.jpg", path, System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                refreshGallery(outFile);
            }
            catch (IOException e) {e.printStackTrace();}
            return null;
        }
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    private class SendingImageToServer extends AsyncTask<byte[], Void, Boolean> {

        ProgressDialog pDialog;
        byte[] image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.dismiss();

            pDialog = new ProgressDialog(SearchActivity.this);
            if(lang)
            {
                pDialog.setTitle("Please wait");
                pDialog.setMessage("Querying");
            }
            else
            {
                pDialog.setTitle("Bekleyin");
                pDialog.setMessage("Sorgu yapılıyor");
            }
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(byte[]... image) {

            this.image = image[0];
            try {sendImage(image[0], image[0].length); return true;}
            catch (IOException e) {e.printStackTrace(); return false;}
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if(success)
            {
                ll.setVisibility(View.VISIBLE);

                TextView tv = (TextView) findViewById(R.id.searchactivity_tv);
                if(lang)
                    tv.setText("Top 5 Results");
                else
                    tv.setText("İlk 5 Sonuç");

                ImageView iv1 = (ImageView) findViewById(R.id.searchactivity_iv1);
                ImageView iv2 = (ImageView) findViewById(R.id.searchactivity_iv2);
                ImageView iv3 = (ImageView) findViewById(R.id.searchactivity_iv3);
                ImageView iv4 = (ImageView) findViewById(R.id.searchactivity_iv4);
                ImageView iv5 = (ImageView) findViewById(R.id.searchactivity_iv5);

                TextView name1 = (TextView)  findViewById(R.id.searchactivity_name1);
                TextView name2 = (TextView)  findViewById(R.id.searchactivity_name2);
                TextView name3 = (TextView)  findViewById(R.id.searchactivity_name3);
                TextView name4 = (TextView)  findViewById(R.id.searchactivity_name4);
                TextView name5 = (TextView)  findViewById(R.id.searchactivity_name5);

                TextView latinname1 = (TextView)  findViewById(R.id.searchactivity_latinname1);
                TextView latinname2 = (TextView)  findViewById(R.id.searchactivity_latinname2);
                TextView latinname3 = (TextView)  findViewById(R.id.searchactivity_latinname3);
                TextView latinname4 = (TextView)  findViewById(R.id.searchactivity_latinname4);
                TextView latinname5 = (TextView)  findViewById(R.id.searchactivity_latinname5);

                String[] leaves_tr      = MyConstants.leaves_tr         ;
                String [] leaves_shown;
                if(lang)
                    leaves_shown= MyConstants.leaves_en         ;
                else
                    leaves_shown= MyConstants.leaves_tr_shown   ;

                String[] latin_names    = MyConstants.latinnames        ;
                int []   leaveint       = MyConstants.leaveint          ;

                int i1=0,i2=0,i3=0,i4=0,i5=0;
                for(; i1 < leaves_tr.length ; i1++)
                    if(names[0].equals(leaves_tr[i1]))
                        break;
                for(; i2 < leaves_tr.length ; i2++)
                    if(names[1].equals(leaves_tr[i2]))
                        break;
                for(; i3 < leaves_tr.length ; i3++)
                    if(names[2].equals(leaves_tr[i3]))
                        break;
                for(; i4 < leaves_tr.length ; i4++)
                    if(names[3].equals(leaves_tr[i4]))
                        break;
                for(; i5 < leaves_tr.length ; i5++)
                    if(names[4].equals(leaves_tr[i5]))
                        break;

                if(i1 == leaves_tr.length || i2 == leaves_tr.length || i3 == leaves_tr.length ||
                        i4 == leaves_tr.length || i5 == leaves_tr.length)
                {
                    if(lang)
                        Toast.makeText(SearchActivity.this, "Please Update TreeLogy", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(SearchActivity.this, "Lütfen uygulamayı güncelleyin", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    finish();
                }
                else
                {
                    name1.setText("%" + percentages[0] + " " + leaves_shown[i1]);
                    iv1.setImageResource(leaveint[i1]);
                    latinname1.setText(latin_names[i1]);

                    name2.setText("%" + percentages[1] + " " + leaves_shown[i2]);
                    iv2.setImageResource(leaveint[i2]);
                    latinname2.setText(latin_names[i2]);

                    name3.setText("%" + percentages[2] + " " + leaves_shown[i3]);
                    iv3.setImageResource(leaveint[i3]);
                    latinname3.setText(latin_names[i3]);

                    name4.setText("%" + percentages[3] + " " + leaves_shown[i4]);
                    iv4.setImageResource(leaveint[i4]);
                    latinname4.setText(latin_names[i4]);

                    name5.setText("%" + percentages[4] + " " + leaves_shown[i5]);
                    iv5.setImageResource(leaveint[i5]);
                    latinname5.setText(latin_names[i5]);

                    Button btn1 = (Button) findViewById(R.id.searchactivity_btn1);
                    Button btn2 = (Button) findViewById(R.id.searchactivity_btn2);
                    Button btn3 = (Button) findViewById(R.id.searchactivity_btn3);
                    Button btn4 = (Button) findViewById(R.id.searchactivity_btn4);
                    Button btn5 = (Button) findViewById(R.id.searchactivity_btn5);

                    if(lang)
                    {
                        btn1.setText(eACCEPT_SAVE);
                        btn2.setText(eACCEPT_SAVE);
                        btn3.setText(eACCEPT_SAVE);
                        btn4.setText(eACCEPT_SAVE);
                        btn5.setText(eACCEPT_SAVE);
                    }

                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            path = names[0];
                            new SaveImageTask().execute(image);
                            if(lang)
                                Toast.makeText(SearchActivity.this, eSAVED, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SearchActivity.this, "Treelogy klasörüne kaydedildi", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    });
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            path = names[1];
                            new SaveImageTask().execute(image);
                            if(lang)
                                Toast.makeText(SearchActivity.this, eSAVED, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SearchActivity.this, "Treelogy klasörüne kaydedildi", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    btn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            path = names[2];
                            new SaveImageTask().execute(image);
                            if(lang)
                                Toast.makeText(SearchActivity.this, eSAVED, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SearchActivity.this, "Treelogy klasörüne kaydedildi", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    btn4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            path = names[3];
                            new SaveImageTask().execute(image);
                            if(lang)
                                Toast.makeText(SearchActivity.this, eSAVED, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SearchActivity.this, "Treelogy klasörüne kaydedildi", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    btn5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            path = names[4];
                            new SaveImageTask().execute(image);
                            if(lang)
                                Toast.makeText(SearchActivity.this, eSAVED, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SearchActivity.this, "Treelogy klasörüne kaydedildi", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    pDialog.dismiss();
                }
            }
            else
            {
                pDialog.dismiss();
                Toast.makeText(SearchActivity.this, "Unexpected problem occured, try again later", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendImage(byte[] image, int size) throws IOException
    {
        String imagesize = Integer.toString(size);
        String msg;
        Socket sock = new Socket("104.197.247.77", 50009);

        sock.setTcpNoDelay(true);
        DataOutputStream os = new DataOutputStream(sock.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        try
        {
            //Receive '1' from server
            msg = inFromServer.readLine();
            if(msg.equals("1"))
            {
                //Send imagesize
                os.writeBytes(imagesize + '#');
                os.flush();
                //Get response imagesize
                msg = inFromServer.readLine();
                if(msg.equals(imagesize + '#'))
                {
                    //Send "OK"
                    os.writeBytes("OK");
                    os.flush();
                    //Send image
                    for(int i=0;i<size;i++)
                    {
                        os.write(image[i]);
                        os.flush();
                    }
                    //Get result header

                    msg = inFromServer.readLine();

                    JSONObject json = new JSONObject(msg);
                    JSONObject json1 = (JSONObject) json.get("1");
                    JSONObject json2 = (JSONObject) json.get("2");
                    JSONObject json3 = (JSONObject) json.get("3");
                    JSONObject json4 = (JSONObject) json.get("4");
                    JSONObject json5 = (JSONObject) json.get("5");

                    names[0] = json1.getString("name");
                    names[1] = json2.getString("name");
                    names[2] = json3.getString("name");
                    names[3] = json4.getString("name");
                    names[4] = json5.getString("name");

                    percentages[0] = json1.getString("percentage");
                    percentages[1] = json2.getString("percentage");
                    percentages[2] = json3.getString("percentage");
                    percentages[3] = json4.getString("percentage");
                    percentages[4] = json5.getString("percentage");

                    //Receive 1 from the server
                    msg = inFromServer.readLine();
                    //Terminate the connection
                    os.writeBytes("destroy");
                    os.flush();
                }
                else
                {
                    //Send "NO"
                    os.writeBytes("NO");
                    os.flush();
                    msg = inFromServer.readLine();
                    os.writeBytes("destroy");
                    os.flush();
                }
            }
        }
        catch (JSONException | IOException e) {e.printStackTrace();}
        finally
        {
            os.close();
            inFromServer.close();
            sock.close();
        }
    }
}
