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
    private List<String> bestElems = new ArrayList<String>();
    private int[][] originData;
    private boolean canBeResolved = false;

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
        minSteps = 1000;
        handleElement(0);
        return canBeResolved;
    }

    private boolean checkElements() {
        for (boolean b : data) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    public void printSolution() {
        if (!canBeResolved) {
            return ;
        }
        for (String s : elems) {
            System.out.println("s = " + s);
        }
    }

    private void finished() {
//        if (elems.size() < minSteps && elems.size() > 0) {
        canBeResolved = true;
        if (elems.size() < minSteps) {
            bestElems.clear();
            bestElems.addAll(elems);
        }
        elems.clear();
//            System.out.println("Finished......");
//            minSteps = elems.size();
//            for (String s : elems) {
//                System.out.println("s = " + s);
//            }
//        }
    }

    private int convertRow(int index) {
        return index / cols;
    }

    private int convertCol(int index) {
        return index % cols;
    }

    private void handleElement(int index) {
        if (index >= rows*cols) {
            return ;
        }
        if (checkElements()) {
            finished();
            return ;
        }

        pressElement(index);
        elems.add("<"+convertRow(index)+","+convertCol(index)+">");
        if (checkElements()) {
            finished();
            return ;
        }
        handleElement(nextIndex(index));

        pressElement(index);
        if (!elems.isEmpty()) elems.remove(elems.size() - 1);
        handleElement(nextIndex(index));
    }

    private int nextIndex(int index) {
        return ++index;
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
        resolver.printSolution();
    }

}
