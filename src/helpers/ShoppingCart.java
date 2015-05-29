/**
 * 
 */
package helpers;

import java.util.ArrayList;
import java.util.List;

import models.ProductWithCategoryName;

/**
 * @author Jules Testard
 *
 */
public class ShoppingCart {
	
	private List<Integer> quantities;
	
	private List<ProductWithCategoryName> productWithCategoryNames;
	
	public ShoppingCart() {
		this.productWithCategoryNames = new ArrayList<ProductWithCategoryName>();
		this.quantities = new ArrayList<Integer>();
	}
	
	public void addToShoppingCart(int quantity, ProductWithCategoryName p) {
		this.productWithCategoryNames.add(p);
		this.quantities.add(quantity);
	}
	
	public void empty() {
		this.productWithCategoryNames = new ArrayList<ProductWithCategoryName>();
		this.quantities = new ArrayList<Integer>();
	}

	/**
	 * @return the quantities
	 */
	public List<Integer> getQuantities() {
		return quantities;
	}

	/**
	 * @param quantities the quantities to set
	 */
	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}

	/**
	 * @return the products
	 */
	public List<ProductWithCategoryName> getProducts() {
		return productWithCategoryNames;
	}

	/**
	 * @param productWithCategoryNames the products to set
	 */
	public void setProducts(List<ProductWithCategoryName> productWithCategoryNames) {
		this.productWithCategoryNames = productWithCategoryNames;
	}

}
