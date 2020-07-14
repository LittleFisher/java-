package bigHomework;

public class User {
	private long Id;
	private String password;
	private String userName;
	private long phoneNumber;
	public User() {
		super();
	}
	public User(long id, String password, String userName, long phoneNumber) {
		super();
		Id = id;
		this.password = password;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
	}
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
