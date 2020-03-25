package com.company;

public class VirtualFileSystem
{
    Directory root;
    DiskAllocator diskAllocator;

    public VirtualFileSystem(DiskAllocator disk)
    {
        root.setDirectoryPath("root/");
        diskAllocator = disk;
    }


    boolean CreateFile(String Path , int size)
    {

        return false;
    }

    void CreateFolder(String Path)
    {

    }

    void DeleteFile(String Path)
    {

    }

    void DeleteFolder(String Path)
    {

    }

    void DisplayDiskStatus()
    {
        diskAllocator.DisplayDiskStatus();
    }

    void DisplayDiskStructure()
    {
        root.printDirectoryStructure(0);
        /*
         * This command will display the files and folders in your system file in a tree structure(root)
         */
    }
}

