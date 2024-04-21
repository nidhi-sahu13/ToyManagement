package toyManagementSys.dto;

public class AdminModule {
	private int id;
	private String name;
	private String email;
	private String password;
	private long contactNo;
	private String toyStore;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getcontactNo() {
		return contactNo;
	}
	public void setcontactNo(long contactNo) {
		this.contactNo = contactNo;
	}
	public String getToyStore() {
		return toyStore;
	}
	public void setToyStore(String toyStore) {
		this.toyStore = toyStore;
	}
	public AdminModule(int id, String name, String email, String password, long contactNo, String toyStore) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.contactNo = contactNo;
		this.toyStore = toyStore;
	}
	public String toString() {
		return "AdminModule [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", contactNo="
				+ contactNo + ", toyStore=" + toyStore + "]";
	}
}
