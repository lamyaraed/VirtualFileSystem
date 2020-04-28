package AllocationManagement;
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

import VFileManagement.Directory;
import VFileManagement._File;

public class ContiguousAllocator implements DiskAllocator
{	
	private ArrayList<Integer> Blocks = new ArrayList<Integer>();
	
	private String VFSPath = "VFS.txt";
	private int LoadIndex = 1;
	private String[] VFSContentList;
	
	public ContiguousAllocator(int n) 
	{
		for(int i = 0 ; i < n ; i++)
		{
			Blocks.add(-1);
		}
	}
	
	@Override
	public int allocateFile(_File file)
	{
		if(file.getStartIndex()!=-1)
		{
			int start = file.getStartIndex();
			int size = file.getSize();
			boolean validBlocks = true;
			for(int i = 0 ; i < size ; i++)
			{
				if(Blocks.get(i+start)!=-1)
				{
					validBlocks = false;
					break;
					
				}
			}
			if (validBlocks) {
				for(int i = 0 ; i < size ; i++)
				{
					Blocks.set(i+start, 1);
				}
				return start;
			}
			else
			{
				System.out.println("Invalid Index for the Blocks");
				return -1;
			}
			
		}
		
		ArrayList<Integer> StartIndexes = new ArrayList<Integer>();
		ArrayList<Integer> blocksSize = new ArrayList<Integer>();
		
		GetFreeBlocks(StartIndexes , blocksSize);

		int startIndx = GetStartIndex(StartIndexes , blocksSize , file.getSize());
			
		if(startIndx !=-1) {
			file.setStartIndex(startIndx);
			for(int i = 0 ; i < file.getSize() ; i++)
			{
				Blocks.set(startIndx+i,1);
				file.getAllocatedBlocks().add(startIndx+i);
			}
		}
		
		return startIndx;
	}

	@Override
	public void deAllocateFile(_File file) {
		int start = file.getStartIndex();
		
		for(int i = 0 ; i < file.getSize() ; i++)
		{
			Blocks.set(i+start, -1);
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
		LoadIndex = 1;
		
		String VFSContent = ReadFile(VFSPath);
		VFSContentList = VFSContent.split("\n"); 
		String[] rootParamters = VFSContentList[0].split("-");
		
		String rootPath = rootParamters[0];
		int nFiles = Integer.parseInt(rootParamters[1]);
		int nDirectory = Integer.parseInt(rootParamters[2]);
		
		root.setDirectoryPath(rootPath);
		loadFiles(root.getFiles(),nFiles,LoadIndex);
		
		LoadIndex+=nFiles;
		loadDirectories(root.getSubDirectories(),nDirectory,LoadIndex);
		LoadIndex+=nDirectory;
	}
	
	@Override
	public void SaveHardDisk(Directory root) 
	{
		DeleteFile(VFSPath);
		saveDisk(root);

	}
	
	private void saveDisk(Directory root)
	{
		int nFiles = root.getFiles().size();
		int nFolder = root.getSubDirectories().size();
		
		String Line = root.getDirectoryPath() + "-"+nFiles+"-"+nFolder+"\r\n";
		AppendOnFile(VFSPath, Line);
		
		if(nFiles > 0)
			WriteFiles(root.getFiles());
		if(nFolder>0)
			WriteFolder(root.getSubDirectories());
	}
	
	
	@Override
	public void DisplayDiskStatus() 
	{
		int nAllocated = 0 , nFree = 0;
		for(int i = 0 ; i < Blocks.size() ; i++)
		{
			if(i%10 == 0)
				System.out.println();
			System.out.print(Blocks.get(i) + " ");
			
			if(Blocks.get(i)!=-1)
			{
				nAllocated++;
			}
			else
			{
				nFree++;
			}
		}
		
		System.out.println("\n\nNumber of allocated BLocks = " + nAllocated);
		System.out.println("Number of Free BLocks = " + nFree);
		
		System.out.println("Total number of blocks = " + Blocks.size());
	}
	

	private void loadDirectories(ArrayList<Directory> subDirectories, int nDirectory, int startIndex) 
	{
		for(int i = 0 ; i < nDirectory ; i++)
		{
			String[] directoryParamters = VFSContentList[startIndex++].split("-");
			String directoryPath = directoryParamters[0];
			int nFiles = Integer.parseInt(directoryParamters[1]);
			int nSubDirectory = Integer.parseInt(directoryParamters[2]);
			
			Directory subDirectory = new Directory();
			
			subDirectory.setDirectoryPath(directoryPath);
			loadFiles(subDirectory.getFiles(), nFiles, startIndex);

			startIndex+=nFiles;
			loadDirectories(subDirectory.getSubDirectories(), nSubDirectory, startIndex);
			startIndex+=nSubDirectory;
			
			subDirectories.add(subDirectory);
			
		}
	}

	private void loadFiles(ArrayList<_File> files, int nFiles, int startIndex) 
	{
		for(int i = 0 ; i < nFiles ; i++)
		{
			String[] FileParameters = VFSContentList[startIndex+i].split("-");
			String filePath = FileParameters[0];
			int startBlock = Integer.parseInt(FileParameters[1]);
			int nBlocks = Integer.parseInt(FileParameters[2]);
			
			_File file = new _File(nBlocks);
			file.setStartIndex(startBlock);
			file.setFilePath(filePath);
			file.setDeleted(false);
			
			allocateFile(file);
			
			files.add(file);
		}
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
		if(Cnt >0)
		{
			startIndexes.add(Blocks.size()-Cnt);
			blocksSize.add(Cnt);
			
			Cnt =0;
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
    
    private void DeleteFile(String Path)
    {
    	try (FileChannel outChan = new FileOutputStream(new File(Path), true).getChannel()) {
    		  outChan.truncate(0);
    		} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

	private void WriteFolder(ArrayList<Directory> Folders) 
	{
		for(int i = 0 ; i < Folders.size() ; i++)
		{
			Directory subDirectory = Folders.get(i);
			saveDisk(subDirectory);
		}
	}

	private void WriteFiles(ArrayList<_File> _Files) 
	{
		for(int i = 0 ; i < _Files.size() ; i++)
		{
			_File file = _Files.get(i);
			String Lines = file.getFilePath()+"-"+file.getStartIndex()+"-"+file.getSize()+"\r\n";
			AppendOnFile(VFSPath, Lines);
		}
	}
}
