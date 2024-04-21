package toyManagementSys.dto;

public class ToyStore {
	private int toyId;
	private String toyName;
	private String toyemail;
	private String color;
	private int quantiy;
	private double price;
	@Override
	public String toString() {
		return "ToyStore [toyId=" + toyId + ", toyName=" + toyName + ", toyemail=" + toyemail + ", color=" + color
				+ ", quantiy=" + quantiy + ", price=" + price + "]";
	}
	public int getToyId() {
		return toyId;
	}
	public void setToyId(int toyId) {
		this.toyId = toyId;
	}
	public String getToyName() {
		return toyName;
	}
	public void setToyName(String toyName) {
		this.toyName = toyName;
	}
	public String getToyemail() {
		return toyemail;
	}
	public void setToyemail(String toyemail) {
		this.toyemail = toyemail;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getQuantiy() {
		return quantiy;
	}
	public void setQuantiy(int quantiy) {
		this.quantiy = quantiy;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public ToyStore( String toyName, String toyemail, String color, int quantiy, double price) {
		super();
		//this.toyId = toyId;
		this.toyName = toyName;
		this.toyemail = toyemail;
		this.color = color;
		this.quantiy = quantiy;
		this.price = price;
	}
	public ToyStore() {
		
	}
	public ToyStore(int id) {
		this.toyId=id;
	}
	
}
