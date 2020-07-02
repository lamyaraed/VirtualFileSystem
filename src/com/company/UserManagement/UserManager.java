package UserManagement;

import java.util.ArrayList;

public interface UserManager {

	ArrayList<User> getAllUsers();
	
	User getUser(String UserName);
	
	void setAllUsers(ArrayList<User> allUsers);

	User getLoggedInUser();

	void setLoggedInUser(User loggedInUser);

	User getAdmin();

	/*Load From Users file and Capabilities file -> store in allUsers array*/
	void LoadUserFromFile();

	/* save in Users file and Capabilities file*/
	void SaveUsersToFile();

	/*This command will display the name of the current logged in user.*/
	void TellUser();

	// return -1 if the user can not be created
	/*Create object �User� and store this user in list of users.*/
	int CreateUser(String userName, String password);

	// return -1 if the user not found
	/*Get this user object from users list and edit its Capabilities*/
	int GrantUser(String userName, String directoryPath, String Cap);

	// return -1 if the user not found
	/*set this user as the logged in user*/
	int LoginUser(String userName, String password);

	// return -1 if the user not found	
	/*Delete this user from the array*/
	int DeleteUser(String userName);

    boolean HasCapabilityInDirectory(String Directory , String Command);
}