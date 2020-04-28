package UserManagement;
import java.util.HashMap;
import java.util.Map;

import VFileManagement.Directory;

public class User {
	
	private String name;
	private String password;
	
	private Map<Directory, Capability> capabilities = new HashMap<Directory, Capability>();

	public User(String name, String password) 
	{
		this.name = name;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Map<Directory, Capability> getCapabilities() {
		return capabilities;
	}
	
	public void setCapabilities(Map<Directory, Capability> capabilities) {
		this.capabilities = capabilities;
	}

	public void setCapability(Directory file, Capability capability) {
		capabilities.put(file, capability);
	}
	
	public Capability getCapability(String DirectoryPath)
	{
		for (Map.Entry<Directory,Capability> entry : capabilities.entrySet()) 
		{
			String temp = entry.getKey().getDirectoryPath();
			if(temp.equals(DirectoryPath))
				return entry.getValue();
		}
		return null;
	}

	@Override
	public String toString() {
		String user = name;	
		return user;
	}
	
	public void printCapabilities()
	{
		for (Map.Entry<Directory,Capability> entry : capabilities.entrySet())  
            System.out.println("Directory = " + entry.getKey() + 
                             ", Capability = " + entry.getValue()); 
	}
}
