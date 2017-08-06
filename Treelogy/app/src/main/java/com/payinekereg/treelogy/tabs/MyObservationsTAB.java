package com.payinekereg.treelogy.tabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.activities.EntranceActivity;
import com.payinekereg.treelogy.adapters.MyAdapter;
import com.payinekereg.treelogy.constants.MyConstants;
import com.payinekereg.treelogy.constructors.MyObservationsConstructor;

/**
 * Created by Emre on 3/11/2016.
 */

public class MyObservationsTAB extends Fragment {

    private ArrayList<MyObservationsConstructor> actorsList;
    private MyAdapter adapter;
    private final boolean lang = EntranceActivity.lang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.myobservations, container, false);
        final ListView listview = (ListView) rootView.findViewById(R.id.myobservations_lv);

        actorsList = new ArrayList<>();
        adapter = new MyAdapter(getContext(), R.layout.myobservationsinclude, actorsList);
        listview.setAdapter(adapter);

        AsyncTaskLoadFiles myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(adapter);
        myAsyncTaskLoadFiles.execute();

        registerForContextMenu(listview);
        return rootView;
    }


    private class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        File targetDirector;
        MyAdapter myTaskAdapter;

        private AsyncTaskLoadFiles(MyAdapter adapter) {
            myTaskAdapter = adapter;
        }

        @Override
        protected void onPreExecute() {

            String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String targetPath = ExternalStorageDirectoryPath + "/TreeLogy/";
            targetDirector = new File(targetPath);
            myTaskAdapter.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File[] files = targetDirector.listFiles();
            if(files != null)
                for (File file : files)
                {
                    publishProgress(file.getAbsolutePath());
                    if (isCancelled()) break;
                }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            MyObservationsConstructor constructor = new MyObservationsConstructor();
            String path = values[0];
            constructor.setImage(decodeSampledBitmapFromUri(path, 120, 120));

            String [] leaves_tr         = MyConstants.leaves_tr         ;
            String [] leaves_shown                                   ;
            if(lang)
                leaves_shown   = MyConstants.leaves_en               ;
            else
                leaves_shown   = MyConstants.leaves_tr_shown         ;

            String [] latinnames        = MyConstants.latinnames        ;

            int i;
            for(i = 0 ; i < leaves_tr.length ; i++)
                if(path.contains(leaves_tr[i]))
                    break;

            constructor.setPath(path);
            constructor.setTreeName(leaves_shown[i]);
            constructor.setLatinName(latinnames[i]);

            myTaskAdapter.add(constructor);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            myTaskAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
            if (width > height)
                inSampleSize = Math.round((float) height / (float) reqHeight);
            else
                inSampleSize = Math.round((float) width / (float) reqWidth);

        return inSampleSize;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            ListTreesTAB.filteredModelList = ListTreesTAB.mTrees;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(lang)
            menu.add(0, v.getId(), 0, "Delete");//groupId, itemId, order, title
        else
            menu.add(0, v.getId(), 0, "Sil");//groupId, itemId, order, title
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = (int) info.id;

        String path = actorsList.get(position).getPath();
        File file = new File(path);
        boolean isDeleted = file.delete();
        if(isDeleted)
        {
            refreshGallery(file);
            actorsList.remove(position);
            adapter.notifyDataSetChanged();

            if(lang)
                Toast.makeText(getActivity(), "Observation deleted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Gözlem silindi", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
    private void refreshGallery(File file) {
//        Intent mediaDeleteIntent = new Intent( Intent.ACTION_DELETE);
//        mediaDeleteIntent.setData(Uri.fromFile(file));
//        getActivity().sendBroadcast(mediaDeleteIntent);
/*
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
*/
    }
}
