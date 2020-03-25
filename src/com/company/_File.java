package com.company;

import java.util.ArrayList;

public class _File
{
    private String filePath;
    private ArrayList<Integer> allocatedBlocks = new ArrayList<Integer>();
    private boolean deleted;

    private int startIndex;
    private int size;

    public _File(int start , int s) {
        // TODO Auto-generated constructor stub
        setStartIndex(start);
        setSize(s);
    }

    public ArrayList<Integer> getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public void setAllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
