package com.x74R45.protocol;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Storage {
	
	private static Object locker = new Object();
	
	/* Command 0
	 * Format of the message: 
	 *   0-end itemName
	 */
	public static int countItem(String itemName) throws SQLException {
		synchronized (locker) {
			System.out.println("Received a request to count " + itemName + ".");
			
			PreparedStatement st = DBInteractor.getConnection()
					.prepareStatement("SELECT item_amount FROM item WHERE item_name = ?;");
			st.setString(1, itemName);
			ResultSet rs = st.executeQuery();
			int res = 0;
			if (rs.next())
				res = rs.getInt("item_amount");
			st.close();
			rs.close();
			return res;
		}
	}
	
	/* Command 1
	 * Format of the message: 
	 *   0-3   amount
	 *   4-end name
	 */
	public static void addItem(String name, int amount) throws SQLException {
		synchronized (locker) {
			System.out.println("Adding " + amount + " units of " + name + " to the storage.");
			
			PreparedStatement st = DBInteractor.getConnection().prepareStatement(
					"SELECT * FROM item WHERE item_name = ?;", ResultSet.CONCUR_UPDATABLE);
			st.setString(1, name);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				st.close();
				rs.close();
				st = DBInteractor.getConnection().prepareStatement(
						"UPDATE item SET item_amount = GREATEST(0, item_amount + ?) WHERE item_name = ?;");
				st.setInt(1, amount);
				st.setString(2, name);
				st.executeUpdate();
				st.close();
			} else {
				st.close();
				rs.close();
				st = DBInteractor.getConnection().prepareStatement(
						"INSERT INTO item (item_name, item_amount) VALUES (?, GREATEST(0, ?));");
				st.setString(1, name);
				st.setInt(2, amount);
				st.executeUpdate();
			}
		}
	}
	
	/* Command 2
	 * Format of the message: 
	 *   0-3   amount
	 *   4-end name
	 */
	public static void subtractItem(String name, int amount) throws SQLException {
		addItem(name, -amount);
	}
	
	/* Command 3
	 * Format of the message: 
	 *   0-end name
	 */
	public static void addGroup(String name) throws SQLException {
		synchronized (locker) {
			System.out.println("Creating group " + name + ".");
			
			PreparedStatement st = DBInteractor.getConnection().prepareStatement(
					"INSERT INTO `group` (group_name) VALUES (?);");
			st.setString(1, name);
			st.executeUpdate();
			st.close();
		}
	}

	/* Command 4
	 * Format of the message: 
	 *   0-3       Len
	 *   4-Len+3   itemName
	 *   Len+4-end groupName
	 */
	public static void addItemToGroup(String itemName, String groupName) throws SQLException {
		synchronized (locker) {
			System.out.println("Adding item " + itemName + " to group " + groupName + '.');
			
			PreparedStatement st = DBInteractor.getConnection().prepareStatement(
					"INSERT INTO item_group (item_id, group_id) VALUES (" +
					"(SELECT item_id FROM item WHERE item_name = ?), " +
					"(SELECT group_id FROM `group` WHERE group_name = ?));");
			st.setString(1, itemName);
			st.setString(2, groupName);
			st.executeUpdate();
			st.close();
		}
	}
	
	/* Command 5
	 * Format of the message: 
	 *   0-3   price
	 *   4-end itemName
	 */
	public static void setPrice(String itemName, int price) throws SQLException {
		synchronized (locker) {
			System.out.println("Setting the price of " + itemName + " to " + price + '.');
			
			PreparedStatement st = DBInteractor.getConnection().prepareStatement(
					"UPDATE item SET item_price = ? WHERE item_name = ?;");
			st.setBigDecimal(1, new BigDecimal(price).scaleByPowerOfTen(-2));
			st.setString(2, itemName);
			st.executeUpdate();
			st.close();
		}
	}
	
	/* Command 6
	 * Format of the message: 
	 *   0-end item
	 */
	public static void deleteItem(String item) throws SQLException {
		synchronized (locker) {
			System.out.println("Deleting item " + item + '.');
			
			PreparedStatement st = DBInteractor.getConnection().prepareStatement(
					"DELETE FROM item WHERE item_name = ?;");
			st.setString(1, item);
			st.executeUpdate();
			st.close();
		}
	}
	
	/* Command 7
	 * Format of the message: 
	 *   0-end group
	 */
	public static void deleteGroup(String group) throws SQLException {
		synchronized (locker) {
			System.out.println("Deleting group " + group + '.');
			
			PreparedStatement st = DBInteractor.getConnection().prepareStatement(
					"DELETE FROM `group` WHERE group_name = ?;");
			st.setString(1, group);
			st.executeUpdate();
			st.close();
		}
	}
	
	/* Command 8
	 * Format of the message: 
	 *   0-end group
	 */
	public static ArrayList<Item> getItemsInGroup(String group) throws SQLException {
		synchronized (locker) {
			System.out.println("Getting items in group " + group + '.');
			
			PreparedStatement st = DBInteractor.getConnection().prepareStatement(
					"SELECT item_name, item_amount, item_price " +
					"FROM `group` INNER JOIN item_group ON `group`.group_id = item_group.group_id " +
					"INNER JOIN item ON item_group.item_id = item.item_id " +
					"WHERE group_name = ?;");
			st.setString(1, group);
			ResultSet rs = st.executeQuery();
			ArrayList<Item> res = new ArrayList<>();
			while (rs.next()) {
				String name = rs.getString("item_name");
				int amount = rs.getInt("item_amount");
				int price = (int)(rs.getBigDecimal("item_price").floatValue()*100);
				res.add(new Item(name, amount, price));
			}
			st.close();
			rs.close();
			return res;
		}
	}
	
	public static int getPrice(String itemName) throws SQLException {
		synchronized (locker) {
			System.out.println("Received a request to check the price of " + itemName + ".");
			
			PreparedStatement st = DBInteractor.getConnection()
					.prepareStatement("SELECT item_price FROM item WHERE item_name = ?;");
			st.setString(1, itemName);
			ResultSet rs = st.executeQuery();
			int res = 0;
			if (rs.next())
				res = (int) (rs.getBigDecimal("item_price").floatValue() * 100);
			st.close();
			rs.close();
			return res;
		}
	}
}