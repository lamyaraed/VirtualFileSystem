package UserManagement;

public class UserCapabilityPair {
	
	private User user;
	private Capability capability;
	
	public UserCapabilityPair() {
		// TODO Auto-generated constructor stub
	}
	
	public UserCapabilityPair(User u , Capability c)
	{
		user = u;
		capability = c;
	}

	public Capability getValue() {
		return capability;
	}

	public void setValue(Capability capability) {
		this.capability = capability;
	}

	public User getKey() {
		return user;
	}

	public void setKey(User user) {
		this.user = user;
	}
	
}
