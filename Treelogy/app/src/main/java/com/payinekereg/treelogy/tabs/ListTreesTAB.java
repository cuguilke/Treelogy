package com.payinekereg.treelogy.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.activities.EntranceActivity;
import com.payinekereg.treelogy.adapters.MyRecyclerViewAdapter;
import com.payinekereg.treelogy.constants.MyConstants;
import com.payinekereg.treelogy.constructors.ListTreesConstructor;

public class ListTreesTAB extends Fragment  implements SearchView.OnQueryTextListener  {

    public static ArrayList<ListTreesConstructor>    mTrees              ;
    public static ArrayList<ListTreesConstructor>    filteredModelList   ;
    private MyRecyclerViewAdapter               mAdapter            ;
    private RecyclerView                        mRecyclerView       ;

    private boolean lang = EntranceActivity.lang                    ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        return mRecyclerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addLeaf();

        mAdapter = new MyRecyclerViewAdapter(getActivity(), mTrees);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        onQueryTextChange("");
    }

    @Override
    public boolean onQueryTextChange(String query) {
        filteredModelList = filter(mTrees, query);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<ListTreesConstructor> filter(List<ListTreesConstructor> trees, String query) {
        query = query.toLowerCase();

        final ArrayList<ListTreesConstructor> filteredTreeList = new ArrayList<>();
         for (ListTreesConstructor tree : trees)
        {
            final String text = tree.getTreeName().toLowerCase();
            if (text.contains(query))
                filteredTreeList.add(tree);
        }
        return filteredTreeList;
    }

    private void addLeaf()
    {
        mTrees              = new ArrayList<>();
        filteredModelList   = new ArrayList<>();

        String [] leaves_en     = MyConstants.leaves_en     ;
        String [] leaves_tr     = MyConstants.leaves_tr_shown     ;
        String [] latinnames    = MyConstants.latinnames    ;

        int[] leaveint          = MyConstants.leaveint      ;
        int[] treeint           = MyConstants.treeint       ;

        if(lang)
        for(int i = 0 ; i < leaves_en.length ; i++)
        {
            ListTreesConstructor list = new ListTreesConstructor();
            list.setTreeName(leaves_en[i]);
            list.setLatinName(latinnames[i]);
            list.setLeaf(leaveint[i]);
            list.setTree(treeint[i]);
            mTrees.add(list);
            filteredModelList.add(list);
        }
        else
            for(int i = 0 ; i < leaves_tr.length ; i++)
            {
                ListTreesConstructor list = new ListTreesConstructor();
                list.setTreeName(leaves_tr[i]);
                list.setLatinName(latinnames[i]);
                list.setLeaf(leaveint[i]);
                list.setTree(treeint[i]);
                mTrees.add(list);
                filteredModelList.add(list);
            }
    }
}
