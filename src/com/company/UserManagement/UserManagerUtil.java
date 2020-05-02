package UserManagement;

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
import java.util.HashMap;
import java.util.Map;

import AllocationManagement.ContiguousAllocator;
import VFileManagement.Directory;
import VFileManagement.VirtualFileSystem;
//import javafx.util.Pair;

public class UserManagerUtil implements UserManager
{
	private ArrayList<User> allUsers;
	private User LoggedInUser;
	private User Admin;
	private Directory root;
	
	public UserManagerUtil(Directory root) 
	{
		this.root = root;
		allUsers = new ArrayList<User>();
		
		Admin = new User("Admin", "Admin");
		Admin.setCapability(root,Capability.CREATE_DELETE);
		LoggedInUser = Admin;
		
		LoadUserFromFile();
		allUsers.add(Admin);
	}
	
	@Override
	public ArrayList<User> getAllUsers() {
		return allUsers;
	}

	@Override
	public void setAllUsers(ArrayList<User> allUsers) {
		this.allUsers = allUsers;
	}

	@Override
	public User getLoggedInUser() {
		return LoggedInUser;
	}

	@Override
	public void setLoggedInUser(User loggedInUser) {
		LoggedInUser = loggedInUser;
	}

	@Override
	public User getAdmin() {
		return Admin;
	}

	/*Load From Users file and Capabilities file -> store in allUsers array*/
	@Override
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

	public User getUser(String userName) 
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
	@Override
	public void SaveUsersToFile()
	{
		DeleteFile("user.txt");
		DeleteFile("capabilities.txt");
		saveUsers();
		saveCapabilities();
	}
	
	private void saveCapabilities() 
	{
		//String -> Directory Path
		Map<String, ArrayList<UserCapabilityPair>> DirUserCapabilityMap = new 
									HashMap<String, ArrayList<UserCapabilityPair>>();
		
		for(int i = 0 ; i < allUsers.size() ; i++)
		{
			User currUser = allUsers.get(i);
			Map<Directory, Capability> Capabilities = currUser.getCapabilities();
			
			for (Map.Entry<Directory,Capability> entry : Capabilities.entrySet())
			{
				String DirectoryPath = entry.getKey().getDirectoryPath();
				Capability capability = entry.getValue();
				
				ArrayList<UserCapabilityPair> arrayList  = DirUserCapabilityMap.get(DirectoryPath);
				
				if(arrayList == null)
				{
					arrayList = new ArrayList<UserCapabilityPair>();
				}
				
				arrayList.add(new UserCapabilityPair(currUser, capability));
				DirUserCapabilityMap.put(DirectoryPath, arrayList);
			}
		}
		
		String Lines = "";
		for (Map.Entry<String, ArrayList<UserCapabilityPair>> entry : DirUserCapabilityMap.entrySet())
		{
			String DirectoryPath = entry.getKey();
			ArrayList<UserCapabilityPair> Users_capabilities = entry.getValue();
			
			Lines+=DirectoryPath + ",";
			for(int i = 0 ; i < Users_capabilities.size() ; i++)
			{
				UserCapabilityPair pair = Users_capabilities.get(i);
				String capability = convertCapabilityToDigits(pair.getValue());
				Lines+=pair.getKey() + "," + capability;
				
				if(i!=Users_capabilities.size()-1)
				{
					Lines+=",";
				}
			}
			Lines+="\r\n";
		}
		
		AppendOnFile("capabilities.txt", Lines);
	}

	private String convertCapabilityToDigits(Capability value) {
		String result = null;
		if(value == Capability.CREATE_DELETE)
		{
			result = "11";
		}
		else if(value == Capability.CREATE_ONLY)
		{
			result = "10";
		}
		else if(value == Capability.DELETE_ONLY)
		{
			result = "01";
		}
		else
		{
			result = "00";
		}
		
		return result;
	}

	private void saveUsers() 
	{
		allUsers.remove(Admin);

		String Lines = "";
		for(int i = 0 ; i < allUsers.size() ; i++)
		{
			Lines += allUsers.get(i).getName() + "," +allUsers.get(i).getPassword()+"\r\n";
		}
		AppendOnFile("user.txt" , Lines);
	}
	
	private void AppendOnFile(String FilePath , String Lines)
    {
    	try {
		    Files.write(Paths.get(FilePath), Lines.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }

	private void DeleteFile(String Path)
    {
    	try (FileChannel outChan = new FileOutputStream(new File(Path), true).getChannel()) {
    		  outChan.truncate(0);
    		} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
    }
	
	/*This command will display the name of the current logged in user.*/
	@Override
	public void TellUser()
	{
		System.out.println( "Logged in user :  " + LoggedInUser);
	}
	
	// return -1 if the user can not be created
	/*Create object �User� and store this user in list of users.*/
	@Override
	public int CreateUser(String userName , String password)
	{
		User check = getUser(userName);
		if (check == null && LoggedInUser.getName().equals(Admin.getName()))
		{
			User user = new User(userName, password);
			allUsers.add(user);
			System.out.println(userName + " added successfully.");
			return 1;
		}
		else {
			System.out.println("You can not create this user");
			return -1;
		}
	}
	
	// return -1 if the user not found
	/*Get this user object from users list and edit its Capabilities*/
	@Override
	public int GrantUser(String userName , String directoryPath , String Cap)
	{
		Capability capability  = getCapability(Cap);

		Directory directory = root.getSubDirectory(directoryPath);
		User user = getUser(userName);
		if (user != null && directory != null && LoggedInUser.getName().equals(Admin.getName()))
		{
			user.setCapability(directory, capability);
			System.out.println("User Capabilities edited successfully..");
			return 1;
		}
		else {
			System.out.println("There is no such user with this name");
			return -1;
		}
	}
	
	// return -1 if the user not found
	/*set this user as the logged in user*/
	@Override
	public int LoginUser(String userName , String password)
	{
		User user = getUser(userName);
		if (user != null && password.equals(user.getPassword()))
		{
			LoggedInUser = user;
			System.out.println("Logged in successfully");
			return 1;
		}
		else {
			System.out.println("You cannot log in");
			return -1;
		}
	}	
	
	// return -1 if the user not found	
	/*Delete this user from the array*/
	@Override
	public int DeleteUser(String userName)
	{
		User check = getUser(userName);
		if (check != null)
		{
			allUsers.remove(check);
			System.out.println(userName + " removes successfully..");
			return 1;
		}
		else { 
			System.out.println("There is no such user with this name");
			return -1;
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
			}
			else
			{
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e ) {
			System.out.println("File Not Found");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return Lines;
	}

	public static void main(String[] args) {
		ContiguousAllocator disk = new ContiguousAllocator(100); 
		VirtualFileSystem vSystem = new VirtualFileSystem(disk);
		
		Directory d = vSystem.getRoot();
		UserManager userManager = new UserManagerUtil(d);
		userManager.TellUser();

		userManager.TellUser();

		userManager.CreateUser("Naddda", "mypasswddord");
		userManager.GrantUser("Nada", "root/folder2", "01");

		userManager.SaveUsersToFile();
	}

	@Override
	public boolean HasCapabilityInDirectory(String Directory , String Command){
		Capability capability = LoggedInUser.getCapability(Directory);
		if(capability==null)
			return false;
		else{
			if(Command.equals("CreateFile") || Command.equals("CreateFolder")){
				if(capability.equals(Capability.CREATE_ONLY) || capability.equals(Capability.CREATE_DELETE))
					return true;
				else
					return false;
			}
			else{
				if(capability.equals(Capability.DELETE_ONLY) || capability.equals(Capability.CREATE_DELETE))
					return true;
				else
					return false;
			}

		}
	}

}

