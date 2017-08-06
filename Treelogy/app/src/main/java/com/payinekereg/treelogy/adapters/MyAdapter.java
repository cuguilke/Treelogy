package com.payinekereg.treelogy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.activities.WebViewActivity;
import com.payinekereg.treelogy.constants.MyConstants;
import com.payinekereg.treelogy.constructors.MyObservationsConstructor;

/**
 * Created by Emre on 5/16/2016.
 */
public class MyAdapter extends ArrayAdapter<MyObservationsConstructor> {
    ArrayList<MyObservationsConstructor> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    Context context;

    public MyAdapter(Context context, int resource, ArrayList<MyObservationsConstructor> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        Resource = resource;
        actorList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null)
        {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.rl           = (RelativeLayout)  v.findViewById(R.id.myobservationsinclude_rl)       ;
            holder.image        = (ImageView)       v.findViewById(R.id.myobservationsinclude_image)    ;
            holder.info         = (ImageView)       v.findViewById(R.id.myobservationsinclude_info)     ;
            holder.name         = (TextView)        v.findViewById(R.id.myobservationsinclude_name)     ;
            holder.latinName    = (TextView)        v.findViewById(R.id.myobservationsinclude_latinname);

            v.setTag(holder);
        }
        else
            holder = (ViewHolder) v.getTag();


        final MyObservationsConstructor list_item = actorList.get(position);

        String[] latinnames = MyConstants.latinnames;
        int i = 0;
        for( ; i < latinnames.length ; i++)
            if(list_item.getLatinName().equals(latinnames[i]))
                break;

        final int i_copy = i;
        holder.image.setImageBitmap(list_item.getImage());
        holder.name.setText(list_item.getTreeName());
        holder.latinName.setText(list_item.getLatinName());

        holder.info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(MyConstants.ID, i_copy);
                context.startActivity(intent);
            }
        });

        return v;
    }

    private class ViewHolder {

        RelativeLayout  rl          ;
        ImageView       image       ;
        ImageView       info        ;
        TextView        name        ;
        TextView        latinName   ;
    }
}
