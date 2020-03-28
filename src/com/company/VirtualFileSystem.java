package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class VirtualFileSystem 
{
	Directory root;
	DiskAllocator diskAllocator;
	
	public VirtualFileSystem(DiskAllocator disk)
	{
		root = new Directory();
		root.setDirectoryPath("root/");
		diskAllocator = disk;
		diskAllocator.LoadHardDisk(root);
	}
	
	public void CloseFileSystem()
	{
		diskAllocator.SaveHardDisk(root);
	}
	
	boolean CreateFile(String Path , int size) 
	{
		String[] pathRoot = Path.split("/");
		
		String DirecoryPath = "";
		for(int i = 0 ; i < pathRoot.length-1 ; i++)
		{
			DirecoryPath+=pathRoot[i]+"/";
		}
				
		Directory D =  GetDirectory(DirecoryPath , root);
	
		if(checkForValidDirectory(Path , D)) 
		{
			_File file = new _File(size); // create the file
			file.setFilePath(Path);
			file.setDeleted(false);	
			
			if(diskAllocator.allocateFile(file) != -1 )// fill the blocks array in the file object
			{
				//	diskAllocator.allocateFile(file); 
				D.addFile(file);
				System.out.println("File Added");
				return true;
			}
			else
			{
				System.out.println("Can't add this file");
				return false;
			}
		}
		else
		{
			System.out.println("Can't add this file");
			return false;
		}
	}
	
	private boolean checkForValidDirectory(String path, Directory d) 
	{
		if(d == null) {
		//	System.out.println("Couldnot find the Directory " + path);
			return false;
		}
		ArrayList<_File> files = d.getFiles();
		
		for(int i = 0 ; i < files.size() ; i++)
		{
			if(files.get(i).getFilePath().equals(path))
			{
				//System.out.println("Rename the file and save again");
				return false;
			}
		}
		
		return true;
	}

	private boolean checkForValidDirectoryPath(String path, Directory d)//todo 2oly l nada the change
	{
		if(d == null) {
			//	System.out.println("Couldnot find the Directory " + path);
			return false;
		}

		ArrayList<Directory> subDirectories =  d.getSubDirectories();

		for(int i = 0 ; i < subDirectories.size() ; i++)
			if(subDirectories.get(i).getDirectoryPath().equals(path))
				return false;

		return true;
	}

	private Directory GetDirectory(String direcoryPath , Directory root2) 
	{	
		if((root2.getDirectoryPath()+"/").equals(direcoryPath))
		{
			
			return root2;
		}
		
		ArrayList<Directory> directories = root2.getSubDirectories();
		
		for(int i = 0 ; i < directories.size() ; i++)
		{
			//System.out.println(directories.get(i).getDirectoryPath());
			if((directories.get(i).getDirectoryPath()+"/").equals(direcoryPath))
			{
				return directories.get(i);
			}
			else 
			{
				GetDirectory(direcoryPath , directories.get(i)); 
			}
		}
		
		return null;
	}


	boolean DeleteFile(String Path) 
	{
		
		String[] pathRoot = Path.split("/");
		
		String DirecoryPath = "";
		for(int i = 0 ; i < pathRoot.length-1 ; i++)
		{
			DirecoryPath+=pathRoot[i]+"/";
		}
				
		Directory D =  GetDirectory(DirecoryPath , root);
		
		return	deleteFileFromDirectory(Path , D);	
	}
	
	
	private boolean deleteFileFromDirectory(String path, Directory d) 
	{
		ArrayList<_File> files = d.getFiles();
		
		for(int i = 0 ; i < files.size() ; i++)
		{
			if(files.get(i).getFilePath().equals(path))
			{
				diskAllocator.deAllocateFile(files.get(i));
				d.getFiles().remove(files.get(i));
				
				System.out.println("File is deleted");
				return true;
			}
		}
		
		System.out.println("Can not delete this file");
		return false;
	}
	boolean CreateFolder(String Path)
	{

		String[] pathRoot = Path.split("/");

		String DirecoryPath = "";
		for(int i = 0 ; i < pathRoot.length-1 ; i++)
		{
			DirecoryPath+=pathRoot[i]+"/";
		}

		Directory D =  GetDirectory(DirecoryPath , root);

		if(checkForValidDirectoryPath(Path , D))
		{
			Directory dir = new Directory(); // create the file
			dir.setDirectoryPath(Path);
			dir.setDeleted(false);

			D.addDirectory(dir);

			System.out.println("Directory Added");

			return true;
		}
		else
		{
			System.out.println("Directory with this name already exists");
			return false;
		}
	}

	boolean DeleteFolder(String Path)
	{

		String[] pathRoot = Path.split("/");

		String DirecoryPath = "";
		for(int i = 0 ; i < pathRoot.length-1 ; i++)
		{
			DirecoryPath+=pathRoot[i]+"/";
		}

		Directory D =  GetDirectory(DirecoryPath , root);

		return	deleteDirectoryFromDirectory(Path , D);
	}

	private boolean deleteDirectoryFromDirectory(String path, Directory d)
	{
		ArrayList<Directory> directories = d.getSubDirectories();

		for(int i = 0 ; i < directories.size() ; i++)
		{
			if(directories.get(i).getDirectoryPath().equals(path))
			{
				d.getSubDirectories().remove(directories.get(i));
				System.out.println("Directory is deleted");
				return true;
			}
		}

		System.out.println("Can not delete this Directory, it doesnt exist!");
		return false;
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
/*
*
* public static void main(String[] args) {
		IndexedAllocator IA = new IndexedAllocator(100);
		VirtualFileSystem vf = new VirtualFileSystem(IA);
		vf.DeleteFile("root/file3.txt");
		vf.CreateFile("root/file3.txt" ,5);
		vf.CreateFolder("root/folder1/folder5");
		//vf.DeleteFolder("root/folder1/folder4")
		vf.CloseFileSystem();
		System.out.println("");
	}
* */
}
