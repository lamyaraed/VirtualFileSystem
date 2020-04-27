package VFileManagement;

import java.util.ArrayList;

import AllocationManagement.DiskAllocator;
import UserManagement.Capability;
import UserManagement.User;
import UserManagement.UserManager;

public class VirtualFileSystem 
{
	Directory root;
	DiskAllocator diskAllocator;
	
	UserManager userManager;
	
	public VirtualFileSystem(DiskAllocator disk)
	{
		root = new Directory();
		root.setDirectoryPath("root");
		diskAllocator = disk;
		diskAllocator.LoadHardDisk(root);
		
		userManager = new UserManager(root);		
	}
	
	public void CloseFileSystem()
	{
		diskAllocator.SaveHardDisk(root);
		userManager.SaveUsersToFile();
	}
	
	public boolean CreateFile(String Path, int size)
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
			return false;
		}
		ArrayList<_File> files = d.getFiles();
		
		for(int i = 0 ; i < files.size() ; i++)
		{
			if(files.get(i).getFilePath().equals(path))
			{
				return false;
			}
		}
		
		return true;
	}

	private boolean checkForValidDirectoryPath(String path, Directory d)//todo 2oly l nada the change
	{
		if(d == null) {
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
		Directory D = null;
		for(int i = 0 ; i < directories.size() ; i++)
		{
			if(D!=null)break;
			if((directories.get(i).getDirectoryPath()+"/").equals(direcoryPath))
			{
				D =directories.get(i);
				return D;
			}
			else 
			{
				D = GetDirectory(direcoryPath , directories.get(i));
			}
		}
		
		return D;
	}


	public boolean DeleteFile(String Path) 
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
	
	public boolean CreateFolder(String Path)
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

	public boolean DeleteFolder(String Path)
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
	public void DisplayDiskStatus()
	{
		diskAllocator.DisplayDiskStatus();
	}
	
	public void DisplayDiskStructure()
	{
		root.printDirectoryStructure(0);
	}
}
