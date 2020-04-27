package UserManagement;
import java.util.Map;

import VFileManagement.Directory;

public class User {
	
	private String name;
	private String password;
	private Map<Directory, Capability> capabilities;

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

	public User(String name, String password) 
	{
		this.name = name;
		this.password = password;
	}
}
