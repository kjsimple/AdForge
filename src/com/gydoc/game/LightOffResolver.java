package com.gydoc.game;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LightOffResolver {

    private int rows;
    private int cols;
    private boolean[] data;
    private int start;
    private int minSteps;
    private boolean flag;
    private List<String> elems = new ArrayList<String>();
    private int[][] originData;

    public LightOffResolver(int[][] _data) {
        originData = _data.clone();
        init();
    }

    private void init() {
        flag = false;
        elems.clear();

        rows = originData.length;
        cols = originData[0].length;
        data = new boolean[rows*cols];
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j++) {
                data[i*cols + j] = (originData[i][j] == 1);
            }
        }
    }

    private void switchLight(int i, int j) {
        data[i*cols+j] = !data[i*cols+j];
    }

    private void pressElement(int index) {
        int i = convertRow(index);
        int j = convertCol(index);
        data[index] = !data[index];
        if (i-1>=0) {
            switchLight(i-1, j);
        }
        if (i+1<rows) {
            switchLight(i+1, j);
        }
        if (j-1>=0) {
            switchLight(i, j-1);
        }
        if (j+1<cols) {
            switchLight(i, j+1);
        }
    }

    public boolean resolve() {
        boolean res = false;
        minSteps = 1000;
        init();
        for (int i = 0; i < data.length; i++) {
            start = i;
            init();
            System.out.println("i = " + i);
            if (handleElement(i)) {
                res = true;
            }
        }
        return res;
    }

    private boolean checkElements() {
        for (boolean b : data) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    private void finished() {
        if (elems.size() < minSteps && elems.size() > 0) {
            System.out.println("Finished......");
            minSteps = elems.size();
            for (String s : elems) {
                System.out.println("s = " + s);
            }
        }
    }

    private int convertRow(int index) {
        return index / cols;
    }

    private int convertCol(int index) {
        return index % cols;
    }

    private boolean handleElement(int index) {
        boolean res = checkElements();
        if (res) {
            finished();
            return true;
        }
        if (start == index && flag) {
            return false;
        }
        flag = true;

        pressElement(index);
        elems.add("<"+convertRow(index)+","+convertCol(index)+">");
        if (checkElements()) {
            finished();
            return true;
        }
        if (handleElement(nextIndex(index))) {
            return true;
        }
        pressElement(index);
        elems.remove(elems.size()-1);
        return handleElement(nextIndex(index));
    }

    private int nextIndex(int index) {
        return (index+1) % (cols*rows);
    }

    public static void main(String[] args) {
        LightOffResolver resolver = new LightOffResolver(new int[][] {
                          {0,0,0,0,0},
                          {0,0,1,0,0},
                          {0,0,0,0,0},
                          {0,0,0,0,1},
                          {0,0,0,0,0}
                         });
        resolver.resolve();
    }

}
