package UserManagement;

import java.util.ArrayList;

import VFileManagement.Directory;

public class UserManager 
{
	ArrayList<User> allUsers;
	User LoggedInUser;
	
	public UserManager(Directory root) 
	{
		LoggedInUser = new User("Admin", "Admin");
		LoggedInUser.setCapability(root,Capability.CREATE_DELETE);
		
		LoadUserFromFile(); 
	}

	/*Load From Users file and Capabilities file -> store in allUsers array*/
	public void LoadUserFromFile() {
		// TODO Auto-generated method stub
		
	}
	
	/* save in Users file and Capabilities file*/
	public void SaveUsersToFile()
	{
		// TODO Auto-generated method stub
	}
	
	
	/*This command will display the name of the current logged in user.*/
	public void TellUser()
	{
		
	}
	
	// return -1 if the user can not be created
	public int CreateUser(User user)
	{
		return -1;
	}
	
	// return -1 if the user not found
	public int GrantUser(User user , Directory directory , Capability capability)
	{
		return -1;
	}
	
	// return -1 if the user not found
	public int LoginUser(User user)
	{
		return -1;	
	}
	
	// return -1 if the user not found	
	public int DeleteUser(User user)
	{
		return -1;
	}
	

}

