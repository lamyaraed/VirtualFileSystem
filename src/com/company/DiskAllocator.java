package com.company;

public interface DiskAllocator
{
    void InitializeAllocator(int n); // set all blocks as an empty blocks

    int allocateFile(_File file);
    void deAllocateFile(_File file);

    void LoadHardDisk(Directory root); // load all files and folders from VFS.txt File and store it in the root directory
    void SaveHardDisk(Directory root); // save all files and folders in VFS.txt File

    void DisplayDiskStatus();
}

