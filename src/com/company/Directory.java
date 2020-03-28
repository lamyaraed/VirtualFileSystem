package com.company;

import java.util.ArrayList;

public class Directory 
{
	private String directoryPath;

	private ArrayList<_File> files = new ArrayList<_File>();
	
	private ArrayList<Directory> subDirectories = new ArrayList<Directory>();
	
	private boolean deleted = false;

	public void printDirectoryStructure(int level)
	{
	 /*	this method prints the directory name and its files
		then makes recursion to loop on the subDirectories to print their structure too.

		The level parameter can be used to print spaces before the directory name is
	 	printed to show its level in the structure 
	 */
	}

	Directory(){}

	Directory(String name ){
		directoryPath =  name;
	}
	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public ArrayList<_File> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<_File> files) {
		this.files = files;
	}

	public ArrayList<Directory> getSubDirectories() {
		return subDirectories;
	}

	public void setSubDirectories(ArrayList<Directory> subDirectories) {
		this.subDirectories = subDirectories;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void addDirectory(Directory D)
	{
		subDirectories.add(D);
	}
	
	public void addFile(_File D)
	{
		files.add(D);
	}

	public Directory getSubDirectory(String DirectoryName) {
		for(int i = 0 ; i < subDirectories.size();i++) {
			if(subDirectories.get(i).directoryPath.equals(DirectoryName))
				return subDirectories.get(i);
		}
		System.out.println("No such Directory");
		return null;
	}
	
}
