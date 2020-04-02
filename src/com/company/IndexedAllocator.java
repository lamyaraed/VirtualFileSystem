package com.company;
import javax.print.DocFlavor;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class IndexedAllocator implements DiskAllocator{

    private ArrayList<Integer> blocks = new ArrayList<Integer>();
    private HashMap<Integer, ArrayList<Integer>> IndexBlocks = new HashMap<Integer, ArrayList<Integer>>();
    private ArrayList<Pair> StoredFiles  = new ArrayList<Pair>();
    private HashMap<String , Integer> StoredFilesToIndexFiles = new HashMap<String, Integer>();

    //private HashMap<String, Integer> StoredFiles = new HashMap<String, Integer>();
    String VFSPath = "VFSindexed.txt";


    IndexedAllocator(int n){
        InitializeAllocator(n);
    }
    @Override
    public void InitializeAllocator(int n) {
        // TODO Auto-generated method stub
        for(int i = 0 ;i < n ; i++) {
            blocks.add(-1);
        }
    }

    @Override
    public int allocateFile(_File file) {

        Integer startIndex = blocks.indexOf(-1);
        ArrayList<Integer> blocksForFile = new ArrayList<Integer>();

        if(startIndex != -1) {
            blocks.set(startIndex, 1);

            for(int i = 0 ;i < file.getSize() ; i++) {
                int freeSpace = blocks.indexOf(-1);
                if(freeSpace > -1) {
                    blocksForFile.add(freeSpace);
                    blocks.set(freeSpace, 1);
                }
                else {
                    System.out.println("there is not enough space to accomidate your file! please free some space.");
                    blocks.set(startIndex, -1);
                    for(Integer e :  blocksForFile) // deallocatin
                        blocks.set(e, -1);
                    return -1;
                }
            }
           // StoredFiles.add(new Pair(file.getFilePath() , startIndex));
            StoredFilesToIndexFiles.put(file.getFilePath() ,startIndex );
            file.setAllocatedBlocks(blocksForFile);
            file.setStartIndex(startIndex);
            IndexBlocks.put(startIndex, blocksForFile);

        }
        else {

            System.out.println("there is not enough space to accomidate your file! please free some space.");
            return -1;
        }
        return startIndex;
    }

    @Override
    public void deAllocateFile(_File file) {
        // TODO Auto-generated method stub
        int i;
        for(i = 0 ;i < StoredFiles.size();i++){
            if(StoredFiles.get(i).path.equals(file.getFilePath())){
                ArrayList<Integer> bl = IndexBlocks.get(StoredFiles.get(i).indexBlock);
                IndexBlocks.remove(StoredFiles.get(i).indexBlock);
                StoredFiles.remove(i);
                for(int j = 0 ;j < bl.size();j++){
                    blocks.set(bl.get(j), -1);
                }
            }
        }
    }

    @Override
    public void LoadHardDisk(Directory root) {
        // TODO Auto-generated method stub

        File file = new File("VFSindexed2.txt");

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String st1 = sc.nextLine();
                //System.out.println(st1);
                String[] filesFromSize = st1.split(" \\| "); //filesFromSize[0] has files , filesFromSize[1] has file/directory size
                String[] files =filesFromSize[0].split("\\/");;
                String[] sizes = filesFromSize[1].split(" ");

                //	System.out.println(sizes[sizes.length -1 ]);
                if(files.length == 2) {
                    if(files[1].contains(".")) {
                        root.addFile(new _File(filesFromSize[0], sizes.length));
                        allocateFileFromDisk(sizes, filesFromSize[0]);
                    }
                    else {
                        root.addDirectory(new Directory(filesFromSize[0]));
                    }
                }
                else {
                    Directory cur = root;
                    String CurPath = root.getDirectoryPath() +'/';
                    for(int i = 1 ; i < files.length - 1 ; i++) {
                        cur = cur.getSubDirectory(CurPath + files[i]);
                        CurPath +=  files[i]  + "/";
                    }
                    if(files[files.length - 1].contains(".")) {
                        cur.addFile(new _File(filesFromSize[0], sizes.length));
                        allocateFileFromDisk(sizes, filesFromSize[0]);
                    }
                    else {
                        cur.addDirectory(new Directory(filesFromSize[0]));
                    }
                }

            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        for(int i = 0 ; i < StoredFiles.size() ; i ++){
            StoredFilesToIndexFiles.put(StoredFiles.get(i).path,StoredFiles.get(i).indexBlock);
        }

    }
    void allocateFileFromDisk(String []arr , String name) {
        int startIndex = Integer.parseInt(arr[0]);
        ArrayList<Integer> blocksForFile= new ArrayList<Integer>();

        blocks.set(startIndex, 0);
        for(int i = 1 ; i < arr.length ; i++) {
            blocksForFile.add(Integer.parseInt(arr[i]));
            blocks.set(Integer.parseInt(arr[i]), 1);
        }
        StoredFiles.add(new Pair(name , startIndex));
        IndexBlocks.put(startIndex, blocksForFile);

    }

    @Override
    public void SaveHardDisk(Directory root) {
        // TODO Auto-generated method stub
        String path;
       if(root.getDirectoryPath().equals("root"))
            clearFile();
        else
           AppendOnFile(root.getDirectoryPath() + " | -1");

        ArrayList<_File> rootFiles = root.getFiles();
        ArrayList<Directory> rootDirectories = root.getSubDirectories();

        saveFiles(root.getFiles());
        for(int i = 0 ; i < root.getSubDirectories().size();i++)
            SaveHardDisk(rootDirectories.get(0));

    }
    void saveFiles(ArrayList<_File> files){
        for(int i = 0 ;i < files.size();i++){
            saveFile(files.get(i).getFilePath());
        }
    }
    void saveFile(String path){
        Integer indexFile =  StoredFilesToIndexFiles.get(path);
        path = path  + " | "  + indexFile + " ";
        ArrayList<Integer> allocatedBlocks = IndexBlocks.get(indexFile);
        for (int j = 0; j < allocatedBlocks.size(); j++) {
            path += allocatedBlocks.get(j) + " ";
        }
        AppendOnFile(path);
        path = "";
    }
    void clearFile() {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("VFSindexed2.txt", false));
            out.close();
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e);
        }
    }
    void AppendOnFile(String str ){
        try{

            BufferedWriter out = new BufferedWriter(new FileWriter("VFSindexed2.txt", true));
            out.write(str+"\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e);
        }
    }
    @Override
    public void DisplayDiskStatus() {
        // TODO Auto-generated method stub
        for(int i = 0 ; i < blocks.size() ; i++)
        {
            if(i%10 == 0)
                System.out.println();
            System.out.print(blocks.get(i) + " ");

        }
    }

}
