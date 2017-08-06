package com.payinekereg.treelogy.constructors;

import android.graphics.Bitmap;
/**
 * Created by Emre on 5/2/2016.
 */
public class MyObservationsConstructor {

    private Bitmap  image   ;
    private int leaf        ;
    private int tree        ;
    private String treeName ;
    private String latinName;
    private String path     ;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getLeaf() {
        return leaf;
    }

    public void setLeaf(int leaf) {
        this.leaf = leaf;
    }

    public int getTree() {
        return tree;
    }

    public void setTree(int tree) {
        this.tree = tree;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
