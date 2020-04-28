package UserManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import AllocationManagement.ContiguousAllocator;
import VFileManagement.Directory;
import VFileManagement.VirtualFileSystem;

public class UserManager 
{
	private ArrayList<User> allUsers;
	private User LoggedInUser;
	private User Admin;
	private Directory root;
	
	public ArrayList<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(ArrayList<User> allUsers) {
		this.allUsers = allUsers;
	}

	public User getLoggedInUser() {
		return LoggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		LoggedInUser = loggedInUser;
	}

	public User getAdmin() {
		return Admin;
	}

	public void setAdmin(User admin) {
		Admin = admin;
	}

	public Directory getRoot() {
		return root;
	}

	public void setRoot(Directory root) {
		this.root = root;
	}

	public UserManager(Directory root) 
	{
		this.root = root;
		allUsers = new ArrayList<User>();
		
		Admin = new User("Admin", "Admin");
		Admin.setCapability(root,Capability.CREATE_DELETE);
		LoggedInUser = Admin;
		
		LoadUserFromFile(); 
	}

	/*Load From Users file and Capabilities file -> store in allUsers array*/
	public void LoadUserFromFile() 
	{
		allUsers.clear();
		LoadUsers();
		LoadCapabilities();
	}
	
	private void LoadCapabilities() 
	{
		
		String Lines = ReadFile("capabilities.txt");
		String[] capabilities = Lines.split("\n");
		
		for(int i = 0  ; i < capabilities.length ; i++)
		{
			String[] capabilityParmaters = capabilities[i].split(",");
			String directoryName = capabilityParmaters[0];
		
			for(int j = 1 ; j < capabilityParmaters.length ; j+=2)
			{
				String userName = capabilityParmaters[j];
				String userCapability = capabilityParmaters[j+1];
				
				User user = getUser(userName);
			
				if(user != null)
				{
					Directory directory = root.getSubDirectory(directoryName);
				 	Capability capability =  getCapability(userCapability);
					user.setCapability(directory, capability);
				}
			}
		}
		
	}

	private Capability getCapability(String userCapability) 
	{
		if(userCapability.equals("00"))
		{
			return Capability.NONE;
		}
		else if(userCapability.equals("01"))
		{
			return Capability.DELETE_ONLY;
		}
		else if(userCapability.equals("10"))
		{
			return Capability.CREATE_ONLY;
		}
		else // 11
		{
			return Capability.CREATE_DELETE;
		}

	}

	private User getUser(String userName) 
	{
		for(int i = 0 ; i < allUsers.size() ; i++)
		{
			if(allUsers.get(i).getName().equals(userName))
			{
				return allUsers.get(i);
			}
		}
		return null;
	}

	private void LoadUsers() 
	{
		String Lines = ReadFile("user.txt");
		String[] users = Lines.split("\n");
		
		for(int i = 0  ; i < users.length ; i++)
		{
			String[] userParm = users[i].split(",");
			String name = userParm[0];
			String password = userParm[1];
			User user = new User(name, password);
			allUsers.add(user);
		}
	}
	

	/* save in Users file and Capabilities file*/
	public void SaveUsersToFile()
	{
		DeleteFile("user.txt");
		DeleteFile("capabilities.txt");
		saveUsers();
		saveCapabilities();
	}
	
	private void saveCapabilities() {
		// TODO Auto-generated method stub
		
	}

	private void saveUsers() {
		// TODO Auto-generated method stub
		
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
	
	/*This command will display the name of the current logged in user.*/
	public void TellUser()
	{
		
	}
	
	// return -1 if the user can not be created
	/*Create object “User” and store this user in list of users.*/
	public int CreateUser(User user)
	{
		return -1;
	}
	
	// return -1 if the user not found
	/*Get this user object from users list and edit its Capabilities*/
	public int GrantUser(User user , Directory directory , Capability capability)
	{
		return -1;
	}
	
	// return -1 if the user not found
	/*set this user as the logged in user*/
	public int LoginUser(User user)
	{
		return -1;	
	}
	
	// return -1 if the user not found	
	/*Delete this user from the array*/
	public int DeleteUser(User user)
	{
		return -1;
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
	
	
	/*
	public static void main(String[] args) {
		ContiguousAllocator disk = new ContiguousAllocator(100); 
		VirtualFileSystem vSystem = new VirtualFileSystem(disk);
		
		Directory d = vSystem.getRoot();
		UserManager userManager = new UserManager(d);
	}
    */
}

