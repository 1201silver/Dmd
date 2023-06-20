package ddwu.spring.Dmd.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.support.PagedListHolder;

@SuppressWarnings("serial")
public class Cart implements Serializable {

	/* Private Fields */

	private final Map<Integer, CartItem> itemMap = Collections.synchronizedMap(new HashMap<Integer, CartItem>());

	private final PagedListHolder<CartItem> itemList = new PagedListHolder<CartItem>();

	/* JavaBeans Properties */

	public Cart() {
		this.itemList.setPageSize(4);
	}

	public Iterator<CartItem> getAllCartItems() {
		return itemList.getSource().iterator();
	}

	public PagedListHolder<CartItem> getCartItemList() {
		return itemList;
	}

	public int getNumberOfItems() {
		return itemList.getSource().size();
	}

	/* Public Methods */

	public boolean containsItemId(int itemId) {
		return itemMap.containsKey(itemId);
	}

	public void addItem(Product prod, boolean isInStock) {
		CartItem cartItem = itemMap.get(prod.getId());
		if (cartItem == null) {
			cartItem = new CartItem();
//			cartItem = new CartItem(null, 0, 0, null);
			cartItem.setProd(prod);
			cartItem.setQty(0);
//			cartItem.setInStock(isInStock);
			itemMap.put(prod.getId(), cartItem);
			itemList.getSource().add(cartItem);
		}
		cartItem.incrementQuantity();
	}

	public Product removeItemById(int itemId) {
		CartItem cartItem = itemMap.remove(itemId);
		if (cartItem == null) {
			return null;
		} else {
			itemList.getSource().remove(cartItem);
			return cartItem.getProd();
		}
	}

	public void incrementQuantityByItemId(int itemId) {
		CartItem cartItem = itemMap.get(itemId);
		cartItem.incrementQuantity();
	}

	public void setQuantityByItemId(int itemId, int quantity) {
		CartItem cartItem = itemMap.get(itemId);
		cartItem.setQty(quantity);
	}

	public double getSubTotal() {
		double subTotal = 0;
		Iterator<CartItem> items = getAllCartItems();
		while (items.hasNext()) {
			CartItem cartItem = (CartItem) items.next();
			Product prod = cartItem.getProd();
			int listPrice = prod.getPrice();
			int quantity = cartItem.getQty();
			subTotal += listPrice * quantity;
		}
		return subTotal;
	}

}
