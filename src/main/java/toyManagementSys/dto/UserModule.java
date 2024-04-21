package toyManagementSys.dto;

public class UserModule {
	private int id;
	private String name;
	private String email;
	private long contactNo;
	private String password;
	private double wallet;

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

	public long getContactNo() {
		return contactNo;
	}

	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getWallet() {
		return wallet;
	}

	public void setWallet(double wallet) {
		this.wallet = wallet;
	}

	public UserModule(int id, String name, String email, long contactNo, String password, double wallet) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.contactNo = contactNo;
		this.password = password;
		this.wallet = wallet;
	}

	public String toString() {
		return "UserModule [id=" + id + ", name=" + name + ", email=" + email + ", contactNo=" + contactNo
				+ ", password=" + password + ", wallet=" + wallet + "]";
	}
}
