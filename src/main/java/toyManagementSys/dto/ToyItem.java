package toyManagementSys.dto;

public class ToyItem {
	public int item;
	private int qty;
	
	public ToyItem(int item, int qty) {
		super();
		this.item = item;
		this.setQty(qty);
		
	}
	@Override
	public String toString() {
		return "\nToyItem [item=" + item + ", qty=" + getQty() + "]";
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
}
