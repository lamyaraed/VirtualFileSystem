package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ContiguousAllocator implements DiskAllocator
{
    private ArrayList<Integer> Blocks = new ArrayList<Integer>();
    private String VFSPath = "VFS.txt";

    public ContiguousAllocator()
    {
        DeleteFile(VFSPath);
    }

    @Override
    public int allocateFile(_File file)
    {
        ArrayList<Integer> StartIndexes = new ArrayList<Integer>();
        ArrayList<Integer> blocksSize = new ArrayList<Integer>();

        GetFreeBlocks(StartIndexes , blocksSize);
        int startIndx = GetStartIndex(StartIndexes , blocksSize , file.getSize());

        if(startIndx !=-1)
            for(int i = 0 ; i < file.getSize() ; i++)
            {
                Blocks.set(startIndx+i,1);
            }

        return startIndx;
    }

    @Override
    public void deAllocateFile(_File file) {
        int start = file.getStartIndex();

        for(int i = start ; i < file.getSize() ; i++)
        {
            Blocks.set(i, -1);
        }
    }

    @Override
    public void InitializeAllocator(int n)
    {
        for(int i = 0 ; i < n ; i++)
        {
            Blocks.add(-1);
        }
    }

    @Override
    public void LoadHardDisk(Directory root)
    {

    }

    @Override
    public void SaveHardDisk(Directory root)
    {
        int nFiles = root.getFiles().size();
        int nFolder = root.getSubDirectories().size();

        String Line = root.getDirectoryPath() + "?"+nFiles+"?"+nFolder+"\r\n";
        AppendOnFile(VFSPath, Line);

        if(nFiles > 0)
            WriteFiles(root.getFiles());
        if(nFolder>0)
            WriteFolder(root.getSubDirectories());
    }

    private void WriteFolder(ArrayList<Directory> Folders)
    {
        for(int i = 0 ; i < Folders.size() ; i++)
        {
            Directory subDirectory = Folders.get(i);
            SaveHardDisk(subDirectory);
        }
    }

    private void WriteFiles(ArrayList<_File> _Files)
    {
        for(int i = 0 ; i < _Files.size() ; i++)
        {
            _File file = _Files.get(i);
            String Lines = file.getFilePath()+"?"+file.getStartIndex()+"?"+file.getSize()+"\r\n";
            AppendOnFile(VFSPath, Lines);
        }
    }

    @Override
    public void DisplayDiskStatus()
    {

    }

    private void GetFreeBlocks(ArrayList<Integer> startIndexes, ArrayList<Integer> blocksSize)
    {
        int Cnt = 0;
        for(int i = 0 ; i < Blocks.size() ; i++)
        {
            if(Blocks.get(i) == -1) // free block
            {
                Cnt++;
            }
            else if(Cnt > 0 && Blocks.get(i)!=-1)
            {
                startIndexes.add(i-Cnt);
                blocksSize.add(Cnt);

                Cnt =0;
            }
        }
    }

    private int GetStartIndex(ArrayList<Integer> startIndexes,
                              ArrayList<Integer> blocksSize, int size)
    {
        int Min = Blocks.size()+1 , startIndx = -1;
        for(int i = 0 ; i < blocksSize.size() ; i++)
        {
            int block = blocksSize.get(i);
            if(block >= size && block <= Min)
            {
                Min = block;
                startIndx = startIndexes.get(i);
            }
        }

        return startIndx;
    }

    private void AppendOnFile(String FilePath , String Lines)
    {
        try {
            Files.write(Paths.get(FilePath), Lines.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String ReadFile(String path)
    {
        BufferedReader br;
        String Lines = "";

        try {
            // print the file name
            if(new File(path).isFile()) {

                // print the content
                br = new BufferedReader(new FileReader(path));
                String line;
                while ((line = br.readLine()) != null) {
                    Lines+= line + '\n';
                }

                System.out.println(Lines);
            }
            else
            {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e ) {
            // TODO Auto-generated catch block
            System.out.println("File Not Found");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }

        return Lines;
    }

    void DeleteFile(String Path)
    {
        try (FileChannel outChan = new FileOutputStream(new File(Path), true).getChannel()) {
            outChan.truncate(0);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Directory root = new Directory();
        root.setDirectoryPath("root/");
        _File file = new _File(1, 10);
        file.setFilePath("newFile");

        Directory d = new Directory();
        d.setDirectoryPath("PATHDD");

        root.addDirectory(d);
        root.addFile(file);

        ContiguousAllocator c = new ContiguousAllocator();
        c.SaveHardDisk(root);

        ReadFile(c.VFSPath);
    }

}
