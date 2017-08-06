package com.payinekereg.treelogy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.constructors.ListTreesConstructor;
import com.payinekereg.treelogy.viewholders.ListTreesViewHolder;

public class MyRecyclerViewAdapter  extends RecyclerView.Adapter<ListTreesViewHolder>  {

    private final LayoutInflater mInflater          ;
    private final List<ListTreesConstructor> mTrees ;

    public MyRecyclerViewAdapter(Context context, List<ListTreesConstructor> trees) {
        mInflater = LayoutInflater.from(context);
        mTrees = new ArrayList<>(trees);
    }

    @Override
    public ListTreesViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        final View itemView = mInflater.inflate(R.layout.listtreesitem, parent, false);
        return new ListTreesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListTreesViewHolder holder, int position) {
        final ListTreesConstructor tree = mTrees.get(position);
        holder.bind(tree);
    }

    @Override
    public int getItemCount() {
        return mTrees.size();
    }

    public void animateTo(List<ListTreesConstructor> trees) {
        applyAndAnimateRemovals(trees);
        applyAndAnimateAdditions(trees);
        applyAndAnimateMovedItems(trees);
    }

    private void applyAndAnimateRemovals(List<ListTreesConstructor> newTrees) {
        for (int i = mTrees.size() - 1; i >= 0; i--)
        {
            final ListTreesConstructor tree = mTrees.get(i);
            if (!newTrees.contains(tree))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<ListTreesConstructor> newTrees) {
        for (int i = 0, count = newTrees.size(); i < count; i++)
        {
            final ListTreesConstructor model = newTrees.get(i);
            if (!mTrees.contains(model))
                addItem(i, model);
        }
    }

    private void applyAndAnimateMovedItems(List<ListTreesConstructor> newTrees) {
        for (int toPosition = newTrees.size() - 1; toPosition >= 0; toPosition--)
        {
            final ListTreesConstructor tree = newTrees.get(toPosition);
            final int fromPosition = mTrees.indexOf(tree);
            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }

    public ListTreesConstructor removeItem(int position) {
        final ListTreesConstructor tree = mTrees.remove(position);
        notifyItemRemoved(position);
        return tree;
    }

    public void addItem(int position, ListTreesConstructor tree) {
        mTrees.add(position, tree);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ListTreesConstructor tree = mTrees.remove(fromPosition);
        mTrees.add(toPosition, tree);
        notifyItemMoved(fromPosition, toPosition);
    }
}
