package com.payinekereg.treelogy.viewholders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.activities.WebViewActivity;
import com.payinekereg.treelogy.constructors.ListTreesConstructor;

import static com.payinekereg.treelogy.constants.MyConstants.ID;

public class ListTreesViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    private ImageView   image       ;
    private TextView    treeName    ;
    private TextView    latinName   ;

    public ListTreesViewHolder(View itemView)
    {
        super(itemView);

        image       = (ImageView)   itemView.findViewById(R.id.listtreesitemimage     );
        treeName    = (TextView)    itemView.findViewById(R.id.listtreesitemtreename   );
        latinName   = (TextView)    itemView.findViewById(R.id.listtreesitemlatinname  );

        itemView.setOnClickListener(this);
    }

    public void bind(ListTreesConstructor item) {

        image.setBackgroundResource(item.getLeaf()) ;
        treeName.setText(item.getTreeName())        ;
        latinName.setText(item.getLatinName())      ;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), WebViewActivity.class);
        intent.putExtra(ID, getAdapterPosition());
        v.getContext().startActivity(intent);
    }
}
