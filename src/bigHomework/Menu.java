package bigHomework;

public class Menu {
	private String foodName;
	private int price; 
	private int valuation;//ÆÀ¼Û1-10
	private long id;
	private String address;
	private String shopName;

	public Menu(String foodName, int price, String shopName) {
		super();
		this.foodName = foodName;
		this.price = price;
		this.shopName = shopName;
	}
	public Menu(String foodName, int price, int valuation, long id, String address, String shopName) {
		super();
		this.foodName = foodName;
		this.price = price;
		this.valuation = valuation;
		this.id = id;
		this.address = address;
		this.shopName = shopName;
	}
	public Menu(String foodName, int price, int valuation, String address, String shopName) {
		super();
		this.foodName = foodName;
		this.price = price;
		this.valuation = valuation;
		this.address = address;
		this.shopName = shopName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getValuation() {
		return valuation;
	}
	public void setValuation(int valuation) {
		this.valuation = valuation;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
