package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.DepartmentManager;
import entities.Order;
import entities.OrderType;
import entities.ParameterChangeRequest;
import entities.ParameterType;
import entities.Park;
import entities.ParkManager;


import entities.ServiceRepesentative;

import entities.Inviter;
import entities.User;
import entities.reportHelper;
import serverboundry.Server;
/** 
 * @author Group 7
 *
 */


public class mysqlConnection {

	private static Connection con = null;
	private static int flag ;
	private static HashMap<String, Park> parks;
	
	/**
	 * Establishes a connection to the database.
	 * 
	 * This method attempts to connect to a MySQL database using JDBC. It establishes a connection 
	 * to the database specified by the URL, user name, and password provided.
	 * If the connection is successful, it initializes a HashMap to store park information.
	 * @throws Exception.
	 */ 
	public static void connecttoDB() {
	    loadJDBCDriver();
	    establishConnection();
	}

	private static void loadJDBCDriver() {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	        System.out.println("Driver definition succeed");
	    } catch (Exception ex) {
	        System.out.println("Driver definition failed: " + ex.getMessage());
	    }
	}

	private static void establishConnection() {
		String url = "jdbc:mysql://127.0.0.1/GoNature?serverTimezone=Asia/Jerusalem";
	    String user = "root";
	    String password = Server.pass_Mysql; // Ensure Server.pass_Mysql is accessible

	    try {
	        con = DriverManager.getConnection(url, user, password);
	        System.out.println("SQL connection succeed");
	        
	        // Initialize parks list and load all parks data
	        parks = new HashMap<>();
	        flag =1;
	        get_AllParks(null); // Ensure this method handles its exceptions internally
	    } catch (SQLException ex) {
	        handleSQLException(ex);
	    }
	}

	private static void handleSQLException(SQLException ex) {
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	public static Connection getConn() {
		return con;
	}
	/**
	 * This method retrieves information for all parks stored in the database and populates
	 * the parksList HashMap with Park objects containing the retrieved information.
	 * Note: This method assumes that the parksList HashMap is initialized and available.
	 */

	 public static ArrayList<Park> get_AllParks(ArrayList<Object> arr) {
	        ArrayList<Park> arrParks = new ArrayList<>();
	        try {
	            String query = "SELECT * FROM parks;";
	            Statement st = con.createStatement();
	            ResultSet result = st.executeQuery(query);
	            while (result.next()) {
	                Park p = new Park(
	                        result.getString("name"),
	                        result.getString("address"),
	                        result.getTime("stayTime"),
	                        result.getInt("maxNumOfVisitors"),
	                        result.getInt("maxNumOfOrders")
	                );
	                p.setCurrentNumOfVisitors(result.getInt("currentNumOfVisitors"));
	                p.setCurrentNumOfUnplannedVisitors(result.getInt("currentNumOfUnplannedVisitors"));
	                arrParks.add(p);
	            }
				if (flag==0) 
	                return arrParks; 
	             else {
	                for (Park park : arrParks) {
	                    parks.put(park.getName(), park);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            if (flag==1) {
	                return null; // Return null in case of error when called for client use
	            }
	        }
			return arrParks;
	 }
		/**
		 * This method checks if an inviter with the given ID number exists in the database
		 * by executing an SQL query. If an inviter with the specified ID number is found,
		 * their details are retrieved and returned.
		 * 
		 * @param arr An ArrayList containing the ID number of the inviter to check.
		 * @return The inviter object with the specified ID number if found, otherwise null.
		 */
		public static Inviter id_isExist(ArrayList<Object> arr) {
			String id = (String) arr.get(0);
			Inviter inviter = null;
			if (con != null) {
				try {
					String query = "SELECT * FROM inviters where idNumber = '" + id + "'";
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					if (rs.next())
						inviter = new Inviter(rs.getString("idNumber"), rs.getBoolean("isGuide"));
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			return inviter;
		}
	    /**
	     * Adds an inviter to the database.
	     * 
	     * @param list An ArrayList containing Inviter objects.
	     * @return true if the inviter is successfully added to the database, false otherwise.
	     */
		public static boolean add_inviterDB(ArrayList<Object> list) {
		    if (con != null && !list.isEmpty() && list.get(0) instanceof Inviter) {
		        Inviter inviter = (Inviter) list.get(0); 
		        try (PreparedStatement stmt = con.prepareStatement("INSERT INTO inviters VALUES (?, ?);")) {
		            stmt.setString(1, inviter.getIdNumber());
		            stmt.setBoolean(2, inviter.isGuide()); 
		            stmt.executeUpdate();
		            return true; 
		        } catch (SQLException e) {
		            e.printStackTrace(); 
		        }
		    }
		    return false; // Return false if the connection is null, the list is empty, or an error occurs.
		}
	/**
	 * this function take Traveler and add it to database in MySQL 
	 * @param list have in first index Traveler 
	 * @return true if it's insert to databas another return false
	 */
	public static boolean change_inviterToguide(ArrayList<Object> list) {
		if (con != null) {
			try {
				if (list.get(0) instanceof Inviter) {
					{
						PreparedStatement stmt = con.prepareStatement("update inviters set IsGuide = ? where IdNumber = ?");
						stmt.setString(2, ((Inviter) list.get(0)).getIdNumber());
						stmt.setBoolean(1, ((Inviter) list.get(0)).isGuide());
						stmt.executeUpdate();
						return true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * This method verifies the credentials of a worker by querying the database
	 * with the provided user name and password. If a worker with matching credentials
	 * is found, their details are retrieved and returned as a Worker object.
	 * @param arr An ArrayList containing the user name and password of the worker to check.
	 * @return A user object representing the authenticated worker, or null if authentication fails.
	 */
	public static User check_User(ArrayList<Object> arr) {
		String username = (String) arr.get(0);
		String password = (String) arr.get(1);
		User user = null;
		if (con != null)
			try {
				String query = "Select * FROM users WHERE userName = '" + username + "'AND password = '" + password
						+ "'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);

				if (rs.next()) {
					String role = rs.getString("role");
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					String workerId = rs.getString("workerId");
					String email = rs.getString("email");
					Park park = parks.get(rs.getString("parkName"));
					switch (role) {
					case "Department Manager":
						DepartmentManager dp = new DepartmentManager(username, password, firstName, lastName, workerId,
								email);
						dp.setParks(new ArrayList<Park>(parks.values()));
						return dp;

					case "Park Manager":
						return new ParkManager(username, password, firstName, lastName, workerId, email, park);

					case "Worker":
						return new User(username, password, firstName, lastName, workerId, email, park);

					case "Service Representative":
						return new ServiceRepesentative(username, password, firstName, lastName, workerId, email);

					default:
						break;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return user;
	}
	/**
	 * This method checks the availability of orders for a specified time, date, number of visitors, and park
	 * by querying the database. It calculates the total number of visitors already 

	 * and compares it with the maximum number of orders allowed for the park. If the total number of visitors plus
	 * the number of visitors in the current order does not exceed the maximum allowed, it returns true, indicating availability.
	 * Otherwise, it returns false, indicating unavailability.
	 * @param arr An ArrayList containing the time, date, number of visitors, and park name.
	 * @return true if orders are available for the specified parameters, false otherwise.
	 */
	 public static boolean check_Availibility(ArrayList<Object> arr) {
				Time t = (Time) arr.get(0);
				Date d = (Date) arr.get(1);
				int numOfvisitors = (int) arr.get(2);
				String parkName = (String) arr.get(3);
				int max = 0;
				ArrayList<Order> orders = new ArrayList<Order>();		
				check_statusOrderInWaitingList(null);
				if (con != null)
					try {
						PreparedStatement ps = con.prepareStatement(
								"Select * From orders Where visitDate=? and visitTime<? and visitTime>? and parkName = ? and orderStatus != 'unconfirmed'");
						ps.setDate(1, d);
						ps.setTime(2, add_Time(t, '+', parks.get(parkName).getStayTime()));
						ps.setTime(3, add_Time(t, '-', parks.get(parkName).getStayTime()));
						ps.setString(4, parkName);
						ResultSet rs = ps.executeQuery();
						System.out.println("add_Time with '+' : " + add_Time(t, '+', parks.get(parkName).getStayTime()));
				        System.out.println("add_Time with '-' : " + add_Time(t, '-', parks.get(parkName).getStayTime()));
						while (rs.next()) {
							Order o = new Order(parks.get(rs.getString("parkName")), rs.getDate("visitDate"),
									rs.getTime("visitTime"), rs.getInt("numOfvisitors"), rs.getString("email"),
									rs.getString("ordererId"), OrderType.valueOf(rs.getString("orderType").toUpperCase()),
									rs.getDouble("price"), rs.getString("orderStatus"), rs.getBoolean("payStatus"),
									rs.getBoolean("arrived"), rs.getString("phoneNumber"), rs.getTime("entrance"),
									rs.getTime("exittime"));
							orders.add(o);
						}
						
						for (Order o : orders) {
							int i = 0;
							if (o.getVisitTime().compareTo(t) < 0) {
								ps = con.prepareStatement(
										"select sum(numOfvisitors) from orders Where visitDate=? and visitTime>=? and visitTime<? and parkName = ? and orderStatus != 'unconfirmed'");
								ps.setDate(1, o.getVisitDate());
								ps.setTime(2, o.getVisitTime());
								Time t1 = o.getVisitTime();
								Time t2 = add_Time(t1, '+', parks.get(parkName).getStayTime());
								ps.setTime(3, t2);
								ps.setString(4, parkName);
								rs = ps.executeQuery();
								System.out.println("rs :"+rs);
								if (rs.next()) {
						            int currentSum = rs.getInt(1);
						            System.out.println("Current sum of visitors: " + currentSum);
						            max = Math.max(max, currentSum);
						        }
							}
						}
						 System.out.println("Max after first loop: " + max);
						System.out.println("sssssssssss"+numOfvisitors);
						ps = con.prepareStatement(
								"select sum(numOfvisitors) from orders Where visitDate=? and visitTime>=? and visitTime<? and parkName = ? and orderStatus != 'unconfirmed'");
						ps.setDate(1, d);
						ps.setTime(2, t);
						System.out.println(ps);
						Time t2 = add_Time(t, '+', parks.get(parkName).getStayTime());
						ps.setTime(3, t2);
						ps.setString(4, parkName);
						rs = ps.executeQuery();
						if (rs.next())
							max = (max > rs.getInt(1)) ? max : rs.getInt(1);
						System.out.println("Max after first loop222: " + max);
						System.out.println(rs.getInt(1));
						Park p = parks.get(parkName);
						System.out.println("Park MaxNumOfOrders: " + p.getMaxNumOfOrders());
						System.out.printf("finallll",max + numOfvisitors + p.getMaxNumOfOrders());
						if (max + numOfvisitors <= p.getMaxNumOfOrders()) {
							return true;
						}
						else {
							return false;
						}
					

					} catch (SQLException e) {
						e.printStackTrace();
					}
				return false;
			}
			
	/**
	 * This method performs addition or subtraction on two Time objects and returns the result as a new Time object.
	 * The operation parameter specifies whether addition or subtraction is performed.
	 * @param t1 The first Time object.
	 * @param operation The operation to perform ('+' for addition, '-' for subtraction).
	 * @param t2 The second Time object.
	 * @return The result of the operation as a new Time object.
	 */
	@SuppressWarnings("deprecation")
	private static Time add_Time(Time t1, char operation, Time t2) {
		int hours1 = t1.getHours();
		int minutes1 = t1.getMinutes();
		int hours2 = t2.getHours();
		int minutes2 = t2.getMinutes();
		int totalhours, totalminutes;
		switch (operation) {
		case '+':
			totalminutes = (minutes1 + minutes2) % 60;
			totalhours = (hours1 + hours2) + (minutes1 + minutes2) / 60;
			if (totalhours > 19) {
				totalhours = 19;
				totalminutes = 1;
			}break;
		case '-':
			totalminutes = minutes1 - minutes2;
			totalhours = hours1 - hours2;
			if (totalminutes < 0) {
				totalminutes = 60 + totalminutes;
				totalhours -= 1;
			}
			if (totalhours < 8) {
				totalhours = 7;
				totalminutes = 59;
			}  break;
		default:
			return null;
		}
		 return Time.valueOf(String.format("%02d:%02d:00", totalhours, totalminutes));
	}     	
	/**
	 * This method adds an order to the database if a valid database connection exists. It takes an ArrayList containing
	 * an Order object as input. It extracts order information from the Order object and inserts it into the 'orders' table
	 * in the database.
	 * @param arr An ArrayList containing an Order object.
	 * @return true if the order is successfully added to the database, false otherwise.
	 */
	public static boolean add_Order(ArrayList<Object> arr) {
		Order order = (Order) arr.get(0);
		

		if (con != null) {
			
			try {
				PreparedStatement ps = con.prepareStatement(
					    "INSERT INTO orders (parkName, visitDate, visitTime, numOfVisitors, email, ordererId, orderType, price, orderStatus, payStatus, arrived, phoneNumber, entrance, exittime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

				ps.setString(1, order.getPark().getName()); 
				ps.setDate(2, order.getVisitDate()); 
				ps.setTime(3, order.getVisitTime());
				ps.setInt(4, order.getNumOfVisitors());
				ps.setString(5, order.getEmail());
				ps.setString(6, order.getOrderer());
				ps.setString(7, order.getOrderType().toString());
				ps.setDouble(8, order.getPrice());
				ps.setString(9, order.getOrderStatus());
				ps.setBoolean(10, order.getPayStatus()); 
				ps.setBoolean(11, false);
				ps.setString(12, order.getPhoneNumber());
				ps.setTime(13, null);
				ps.setTime(14, null);
				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}}
		return false;
	}

	/**
	 * This method searches for an order in the database based on the provided parameters. It takes an ArrayList containing
	 * search criteria as input. It extracts the ID, date, time, and park name from the ArrayList and constructs an SQL query
	 * to search for matching orders in the 'orders' table. If a matching order is found with no exit time specified,
	 * it creates and returns an Order object with the details of the found order.
	 * @param arr An ArrayList containing search criteria (ID, date, time, park name).
	 * @return The Order object representing the found order, or null if no matching order is found.
	 */

	public static Order search_Order(ArrayList<Object> searchParams) {
	    String ordererId = (String) searchParams.get(0);
	    Date visitDate = (Date) searchParams.get(1);
	    Time visitTime = (Time) searchParams.get(2);
	    String parkName = (String) searchParams.get(3);
	    System.out.println("Park Name: " + parkName);
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	         if (con != null) {
	            ps = con.prepareStatement("SELECT * FROM orders WHERE ordererId = ? AND visitDate = ? AND parkName = ?");
	            ps.setString(1, ordererId);
	            ps.setDate(2, visitDate);
	            ps.setString(3, parkName);

	            System.out.println("Executing query: " + ps.toString());
	            rs = ps.executeQuery();

	            while (rs.next()) {
	                Time dbVisitTime = rs.getTime("visitTime");
	                if (dbVisitTime != null && !dbVisitTime.after(visitTime)) {
	                    return fill_Order(rs);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	/**
	 * Creates an Order object from the given ResultSet.
	 * 
	 * @param rs The ResultSet containing order data.
	 * @return The created Order object.
	 * @throws SQLException If a database access error occurs.
	 */
	private static Order fill_Order(ResultSet rs) {
		Order order =null;
		          try {  order= new Order(parks.get(rs.getString("parkName")), 
		            rs.getDate("visitDate"),
		            rs.getTime("visitTime"), 
		            rs.getInt("numOfvisitors"), 
		            rs.getString("email"),
		            rs.getString("ordererId"), 
		            OrderType.valueOf(rs.getString("orderType").toUpperCase()),
		            rs.getDouble("price"), 
		            rs.getString("orderStatus"), 
		            rs.getBoolean("payStatus"),
		            rs.getBoolean("arrived"), 
		            rs.getString("phoneNumber"),
		            rs.getTime("entrance"),
		            rs.getTime("exittime")
		        );
	} catch (SQLException e) {
	    e.printStackTrace();
	    }
	  return order;          
	}
	/**
	 * This method retrieves all orders associated with a Inviter based on the Inviter's ID number.
	 * It queries the database to find orders where the orderer ID matches the provided ID number
	 * and the visit date is on or after the current date. It constructs Order objects for each retrieved
	 * record and adds them to an ArrayList, which is then returned.
	 * @param arr An ArrayList containing the Inviter's ID number.
	 * @return An ArrayList of Order objects associated with the Inviter, or null if no orders are found.
	 */
	public static ArrayList<Order> get_AllOrders(ArrayList<Object> travelerIdNumber) {
		
		    ArrayList<Order> orders = new ArrayList<>();
		    String idNumber = (String) travelerIdNumber.get(0);
		    System.out.println("mysql :" + travelerIdNumber);
		    
		    if (con != null) {
		        try {
		            check_statusOrderInWaitingList(null);
		            PreparedStatement ps = con.prepareStatement(
		                    "SELECT * FROM gonature.orders WHERE ordererId = ? AND visitDate >= ?");
		            ps.setString(1, idNumber);
		            ps.setDate(2, Date.valueOf(LocalDate.now()));

		            System.out.println(ps.toString());
		            ResultSet rs = ps.executeQuery();
		            
		            while (rs.next()) {
		                Order order = fill_Order(rs);
		                orders.add(order);
		            }
		            
		            return orders;
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		    
		    return null;
		}
	/**
	 * This method updates the status of an order in the database based on the provided order object and status.
	 * It prepares an SQL update statement to set the order status to the specified status for the orderer ID, visit time,
	 * and visit date matching those of the provided order. If the update is successful and the status is "unconfirmed",
	 * it updates the waiting list. The method returns true upon successful update, and false otherwise.
	 * @param arr An ArrayList containing the order object and the new status.
	 * @return true if the order status is successfully updated, false otherwise.
	 */
	public static boolean update_StatusforOrder(ArrayList<Object> arr) {
		Order order = (Order) arr.get(0);
		String newStatus = (String) arr.get(1);
		if (con != null)
			try {
				PreparedStatement ps = con.prepareStatement(
						"update orders set orderStatus = ? where ordererId = ? and visitTime = ? and visitDate = ?");
				ps.setString(1, newStatus);
				ps.setString(2, order.getOrderer());
				ps.setTime(3, order.getVisitTime());
				ps.setDate(4, order.getVisitDate());
				ps.executeUpdate();
				if (newStatus.equals("unconfirmed"))
					update_inWaitingList(null);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return false;
	}
	/**
	 * This method retrieves the current Availability status of the specified park
	 * from the parks list and returns a Park object with the relevant information.
	 * @param list An ArrayList containing the name of the park.
	 * @return A Park object representing the Availability status of the park.
	 */
	public static Park check_ParkAvailability(ArrayList<Object> list)
	{
		String parkName = (String) list.get(0);
		Park park = parks.get(parkName);
		Park updatedp = new Park(park.getName(), park.getAddress(), park.getStayTime(), park.getMaxNumOfVisitor(),
				park.getMaxNumOfOrders());
		updatedp.setCurrentNumOfVisitors(park.getCurrentNumOfVisitors());
		updatedp.setCurrentNumOfUnplannedVisitors(park.getCurrentNumOfUnplannedVisitors());
		return updatedp;
	}
	/**
	 * This method updates the arrival status of an order in the database based on the provided order object.
	 * It prepares an SQL update statement to set the arrival status to arrived, entrance time to the current time,
	 * and payment status to paid for the orderer ID, visit date, and visit time matching those of the provided order. 
	 * @param list An ArrayList containing the order object.
	 */
	public static void updateStatus_forArrival(ArrayList<Object> list)	{
		Order order = (Order) list.get(0);
		System.out.println("number update2");
		if (con != null) {
			try {
				System.out.println("number update");
				PreparedStatement stmt = con.prepareStatement(
						"update orders set arrived=1 ,entrance=?, payStatus=1 where ordererId=? and visitDate=? and visitTime=?;");
				stmt.setTime(1, Time.valueOf(LocalTime.now()));
				stmt.setString(2, order.getOrderer());
				stmt.setDate(3, (Date) order.getVisitDate());
				stmt.setTime(4, order.getVisitTime());
				stmt.executeUpdate();
				PreparedStatement updateParkStmt = con.prepareStatement(
				       "update parks set currentNumOfVisitors = currentNumOfVisitors + 1 where name=?;");
				updateParkStmt.setString(1, order.getPark().getName());
				updateParkStmt.executeUpdate();
				 if (list.get(1).equals(OrderType.UNPLANNEDTRAVELER)|| list.get(1).equals(OrderType.UNPLANNEDGUIDE)) { // Check for unplanned type
		                // Update the currentNumOfUnplannedVisitors in the parks table
		                PreparedStatement updateParkStmt1 = con.prepareStatement(
		                        "update parks set currentNumOfUnplannedVisitors = currentNumOfUnplannedVisitors + 1 where name=?;");
		                updateParkStmt1.setString(1, order.getPark().getName()); // Assuming you have the park name
		                updateParkStmt1.executeUpdate();
		            }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/** 
	 * This method updates the exit time of an order in the database based on the provided order object.
	 * It prepares an SQL update statement to set the exit time to the current time for the orderer ID,
	 * visit date, and visit time matching those of the provided order and where the arrival status is set to arrived.
	 * @param list An ArrayList containing the order object.
	 */
	public static void updateStatus_forExit(ArrayList<Object> list)	{
		Order order = (Order) list.get(0);
	    System.out.println("number update3");
	    if (con != null) {
	        try {
	            con.setAutoCommit(false); // Start transaction

	            // Update the orders table for exit time
	            PreparedStatement stmt = con.prepareStatement(
	                "update orders set exittime=? where ordererId=? and visitDate=? and visitTime=? and arrived=1;");
	            stmt.setTime(1, Time.valueOf(LocalTime.now()));
	            stmt.setString(2, order.getOrderer());
	            stmt.setDate(3, (Date) order.getVisitDate());
	            stmt.setTime(4, order.getVisitTime());
	            int updatedRows = stmt.executeUpdate();

	            // If the order has been updated, proceed to update the park visitors count
	            if (updatedRows > 0) {
	                // Decrement the number of visitors in the park
	                PreparedStatement updateParkStmt = con.prepareStatement(
	                    "update parks set currentNumOfVisitors = currentNumOfVisitors - 1 where name=?;");
	                updateParkStmt.setString(1, order.getPark().getName());
	                updateParkStmt.executeUpdate();

	                // Decrement the number of unplanned visitors if the order was unplanned
	                if (order.getOrderType() == OrderType.UNPLANNEDTRAVELER || order.getOrderType() == OrderType.UNPLANNEDGUIDE) {
	                    PreparedStatement updateParkStmt1 = con.prepareStatement(
	                        "update parks set currentNumOfUnplannedVisitors = currentNumOfUnplannedVisitors - 1 where name=?;");
	                    updateParkStmt1.setString(1, order.getPark().getName());
	                    updateParkStmt1.executeUpdate();
	                }
	            }

	            con.commit(); // Commit transaction if all updates are successful
	        } catch (SQLException e) {
	            try {
	                con.rollback(); // Rollback transaction in case of an error
	            } catch (SQLException e2) {
	                e2.printStackTrace();
	            }
	            e.printStackTrace();
	        } finally {
	            try {
	                con.setAutoCommit(true); // Reset auto-commit to true
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	/** 
	 * This method updates the current number of visitors for a park in the database
	 * based on the provided order. It adjusts the number of visitors according to the
	 * order type and the number of visitors associated with the order. If the park becomes
	 * full or stops being full due to this update, it updates the corresponding records in the database.
	 * @param list An ArrayList containing the order object.
	 * @return The updated current number of visitors for the park.
	 */
	public static int update_NumOfVisitor(ArrayList<Object> orderList) {
	    Order order = (Order) orderList.get(0);
	    Park park = parks.get(order.getPark().getName());
	    if (con != null) {
	        try {
	            if (check_needToUpdateParkFullTimes(order, park)) {
	            	PreparedStatement stmt = con.prepareStatement(
	            	        "UPDATE parkFullTimes SET endTime = ? WHERE endTime IS NULL AND parkName = ?");
	            	stmt.setTime(1, Time.valueOf(LocalTime.now()));
	            	stmt.setString(2, park.getName());
	            	stmt.executeUpdate();
	            }
	            update_currentNumVisitor(order, park);
	            if (park.getCurrentNumOfVisitors() == park.getMaxNumOfVisitor()) {
	            	PreparedStatement stm = con.prepareStatement(
	            		    "INSERT INTO timeParkFullVisitor VALUES (?, ?, ?, null)");
	            		stm.setString(1, order.getPark().getName());
	            		stm.setDate(2, Date.valueOf(LocalDate.now()));
	            		stm.setTime(3, Time.valueOf(LocalTime.now()));
	            		stm.executeUpdate();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return park.getCurrentNumOfVisitors();
	}
	/**
	 * Checks if the park full times table should be updated.
	 * 
	 * @param order The order object.
	 * @param park The park object.
	 * @return True if the park full times table should be updated, false otherwise.
	 */
	private static boolean check_needToUpdateParkFullTimes(Order order, Park park) {
	    return order.getNumOfVisitors() < 0 && park.getCurrentNumOfVisitors() == park.getMaxNumOfVisitor();
	}
	/**
	 * Updates the visitor counts in the park.
	 * 
	 * @param order The order object.
	 * @param park The park object.
	 * @throws SQLException If a SQL exception occurs.
	 */
	private static void update_currentNumVisitor(Order order, Park park) throws SQLException {
	    int numberOfVisitors = order.getNumOfVisitors();
	    park.setCurrentNumOfVisitors(park.getCurrentNumOfVisitors() + numberOfVisitors);

	    if (order.getOrderType() == OrderType.UNPLANNEDGUIDE || 
	        order.getOrderType() == OrderType.UNPLANNEDTRAVELER) {
	        park.setCurrentNumOfUnplannedVisitors(park.getCurrentNumOfUnplannedVisitors() + numberOfVisitors);
	    }
	    PreparedStatement stmt = con.prepareStatement(
	        "UPDATE parks SET currentNumOfVisitors = ?, currentNumOfUnplannedVisitors = ? WHERE name = ?");
	    stmt.setInt(1, park.getCurrentNumOfVisitors());
	    stmt.setInt(2, park.getCurrentNumOfUnplannedVisitors());
	    stmt.setString(3, park.getName());
	    stmt.executeUpdate();
	}
	/**
	 * add_ToWaitingList -> Method inserts to 'WaitingList' table in DB a new order.
	 * This method inserts the order only if this order is not exist in the WaitingList Table.
	 * @param Array that contains an order in the first index.
	 * @return True if the order inserted to 'WaitingList' Table, else False.
	 */
	public static boolean add_ToWaitingList(ArrayList<Object> arr) {
		Order order = (Order) arr.get(0);
		
		 int affectedRows;
		if (con != null) {
	        try {
	            PreparedStatement ps = con.prepareStatement(
	                "INSERT INTO waitingList (parkName, visitDate, visitTime, numOfVisitors, email, ordererId, orderType, price, orderStatus, payStatus, arrived, phoneNumber, entrance, exittime) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	            ps.setString(1, order.getPark().getName());
	            ps.setDate(2, order.getVisitDate());
	            ps.setTime(3, order.getVisitTime());
	            ps.setInt(4, order.getNumOfVisitors());
	            ps.setString(5, order.getEmail());
	            ps.setString(6, order.getOrderer());
	            ps.setString(7, order.getOrderType().toString());
	            ps.setDouble(8, order.getPrice());
	            ps.setString(9, order.getOrderStatus());
	            ps.setBoolean(10, order.getPayStatus());
	            ps.setBoolean(11, order.isArrived());
	            ps.setString(12, order.getPhoneNumber());
	            ps.setTime(13, order.getEntrance()); 
	            ps.setTime(14, order.getExit());     
	            affectedRows = ps.executeUpdate();
	            return affectedRows>0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	/**
	 * delete_FromWaitingList -> Method deletes an existing order or not from 'WaitingList' and 'Orders' Tables in DB.
	 * @param Array that contains an order in the first index.
	 * @return True if the order deleted from 'WaitingList' or 'Orders' Table, else False.
	 */
	public static boolean delete_FromWaitingList(ArrayList<Object> arr) {
		Order order = (Order) arr.get(0);
		if (con != null)
			try {
				PreparedStatement ps = con.prepareStatement(
						"DELETE FROM waitingList  WHERE parkName = ? and visitDate = ? and visitTime = ? and ordererId = ?");
				ps.setString(1, order.getPark().getName());
				ps.setDate(2, order.getVisitDate());
				ps.setTime(3, order.getVisitTime());
				ps.setString(4, order.getOrderer());
				ps.executeUpdate();
				////צריך לבדוקקק
				PreparedStatement ps2 = con.prepareStatement(
						"delete from orders where ordererId=? and visitTime=? and visitDate=? and parkName=? ;");
				ps2.setString(1, order.getOrderer());
				ps2.setTime(2, order.getVisitTime());
				ps2.setDate(3, order.getVisitDate());
				ps2.setString(4, order.getPark().getName());
				ps2.executeUpdate();
				update_inWaitingList(null);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return false;
	}
	/**
	 * confirm_FromWaitingList -> Method confirms an order that existed in the 'WaitingList'
	 * by deleting it only from the 'WaitingList' Table in DB and returns true all the time
	 * to ensure that the order have been confirmed.
	 * @param Array that contains an order in the first index.
	 * @return True all the time!
	 */
	public static boolean confirm_FromWaitingList(ArrayList<Object> arr) {

		delete_FromWaitingList(arr);
		return true;
	}
	/**
	 * this method insert parameter to database 'Table parkparameters'
	 * 
	 * @param parameterList startdate , enddate ,ParkName , Status
	 * @param parameterList have in first index (Date senddate)
	 * @param parameterList have in second index (String ParameterType)
	 * @param parameterList have in third index (String ParkName)
	 * @param parameterList have in fourth index (String status)
	 * @param parameterList have in fiveth index (String new value)
	 */
	public static void set_ParameterDB(ArrayList<Object> parameterList) {
		 if (con == null ) {
		       return;
		   }
			try{PreparedStatement stmt = con.prepareStatement(
				    "INSERT INTO parkparameters (sendDate, parameterType, parkName, status, newValue) VALUES (?, ?, ?, ?, ?);"
				);
			stmt.setDate(1, (Date) parameterList.get(0)); 
			stmt.setString(2, ((String) parameterList.get(1))); 
			stmt.setString(3, (String) parameterList.get(2)); 
			stmt.setString(4, (String) parameterList.get(3)); 
			stmt.setString(5, (String) parameterList.get(4)); 
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method retrieves all manager requests associated with a specified park
	 * from the database and returns an ArrayList containing ManagerRequest objects
	 * with the retrieved information.
	 * @param arr An ArrayList containing the park name.
	 * @return An ArrayList containing ManagerRequest objects representing all requests for the specified park.
	 */
	public static ArrayList<ParameterChangeRequest> get_AllRequests(ArrayList<Object> arr) {
		String ParkName = (String) arr.get(0);
		 ArrayList<ParameterChangeRequest> requests = new ArrayList<>();
		    if (con != null) {
		        try {
		            PreparedStatement st = con.prepareStatement("SELECT * FROM parkparameters WHERE parkName = ?");
		            st.setString(1, ParkName);
		            ResultSet rs = st.executeQuery();
		            while (rs.next()) {
		                ParameterType parameterType = ParameterType.valueOf(rs.getString("parameterType"));
		                String newValue = rs.getString("newValue");
		                Park park = parks.get(ParkName);
		                Date sendDate = rs.getDate("sendDate");
		                String status = rs.getString("status");
		                ParameterChangeRequest request = new ParameterChangeRequest(parameterType, newValue, park, sendDate, status);
		                requests.add(request);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		    return requests;
	}
	/**
	 * This method updates the status of a parameter change request in the database based on the provided information.
	 * Additionally, if the new status is "confirmed", it updates the corresponding park information according to the 
	 * parameter type and value provided.
	 * @param arr An ArrayList containing the necessary parameters for the update.
	 */
	public static void change_Parametervalue(ArrayList<Object> arr) {
	    if (con != null) {
	        try {
	            String status = (String) arr.get(0);
	            Date sendDate = (Date) arr.get(1);
	            String parameterType = (String) arr.get(2);
	            String parkName = (String) arr.get(3);
	            String oldStatus = (String) arr.get(4);
	            String newValue = (String) arr.get(5);
	            String updateQuery = "UPDATE parkparameters SET status = ? WHERE sendDate = ? AND ParameterType = ? AND ParkName = ? AND status = ?";
	            PreparedStatement ps = con.prepareStatement(updateQuery);
	            ps.setString(1, status);
	            ps.setDate(2, sendDate);
	            ps.setString(3, parameterType);
	            ps.setString(4, parkName);
	            ps.setString(5, oldStatus);
	            ps.executeUpdate();
	            if (status.equals("confirmed")) {
	                switch (parameterType) {
	                    case "MAXNUMBEROFVISITORS":
	                        parks.get(parkName).setMaxNumOfVisitor(Integer.valueOf(newValue));
	                        update_ParkParameter("maxNumOfVisitors", Integer.valueOf(newValue), parkName);
	                        break;
	                    case "MAXNUMBEROFORDER":
	                        parks.get(parkName).setMaxNumOfOrders(Integer.valueOf(newValue));
	                        update_ParkParameter("maxNumOfOrders", Integer.valueOf(newValue), parkName);
	                        break;
	                    case "STAYTIME":
	                        parks.get(parkName).setStayTime(Time.valueOf(newValue));
	                        update_ParkParameter("stayTime", Time.valueOf(newValue), parkName);
	                        break;
	                    default:
	                        break;
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	/**
	 * Updates a specific parameter of a park in the database.
	 *
	 * @param columnName The name of the column in the 'parks' table to be updated.
	 * @param value      The new value to be set for the specified column.
	 * @param parkName   The name of the park whose parameter is to be updated.
	 * @throws SQLException If a database access error occurs or the SQL statement
	 *                      does not return a ResultSet (such as a CREATE statement).
	 */
	private static void update_ParkParameter(String columnName, Object value, String parkName) throws SQLException {
	    String updateParkQuery = "UPDATE parks SET " + columnName + " = ? WHERE name = ?";
	    PreparedStatement ps = con.prepareStatement(updateParkQuery);
	    ps.setObject(1, value);
	    ps.setString(2, parkName);
	    ps.executeUpdate();
	}
	/**
	 * This method retrieves the entrance time and duration of visits for all arrived orders
	 * on the specified date and park.
	 * @param arr An ArrayList containing the following elements:
	 *            - The date for which the report is generated.
	 *            - The name of the park.
	 * @return An ArrayList containing the entrance time and duration of visits for all arrived orders.
	 *         Each entry in the ArrayList represents a visit and consists of two Time objects:
	 *         - The entrance time.
	 *         - The duration of the visit.
	 */
	@SuppressWarnings("deprecation")
	public static ArrayList<Time> visitReport_Error(ArrayList<Object> arr) {
	    ArrayList<Time> result = new ArrayList<>();
	    if (con != null) {
	        try {
	            Date reportDate = (Date) arr.get(0);
	            int month = reportDate.getMonth() + 1;
	            int year = reportDate.getYear() + 1900;
	            String parkName = (String) arr.get(1);
	            String query = "SELECT * FROM orders WHERE month(visitDate) = ? AND year(visitDate) = ? AND arrived = 1 AND parkName = ?";
	            PreparedStatement ps = con.prepareStatement(query);
	            ps.setInt(1, month);
	            ps.setInt(2, year);
	            ps.setString(3, parkName);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                Time visitTime = rs.getTime("visitTime");
	                Time entranceTime = rs.getTime("entrance"); 
	                Time exitTime = add_Time(entranceTime, '+', new Time(4, 0, 0));
	                if (exitTime.getHours() > 19) 
	                    exitTime = new Time(19, 0, 0);
	                Time duration = new Time(exitTime.getTime() - entranceTime.getTime());
	                result.add(entranceTime);
	                result.add(duration);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}
	/**
	 * Retrieves park manager parameters from the database for a specified park.
	 * This method retrieves park manager parameters from the database for the specified park name.
	 * It constructs and executes an SQL query to select park manager parameters such as stay time,
	 * maximum number of visitors, and maximum number of orders for the given park.
	 * @param arr An ArrayList containing the park name for which park manager parameters are to be retrieved.
	 * @return An ArrayList containing the park manager parameters retrieved from the database.
	 */
	public static ArrayList<Object> getParameter_ofParkManger(ArrayList<Object> arr) {
		ArrayList<Object> result = new ArrayList<>();
		String ParkName = (String) arr.get(0);
		if (con != null) {
			try {
				PreparedStatement stmt = con.prepareStatement("Select * from parks where name = ?;");
				stmt.setString(1, ParkName);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				result.add(rs.getTime("stayTime"));
				result.add(rs.getInt("maxNumOfVisitors"));
				result.add(rs.getInt("maxNumOfOrders"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}	
	/**
	 * This method retrieves all guides from the database by executing an SQL query to select
	 * inviter who are registered as guides (isGuide = true).
	 * @return An ArrayList containing all guides retrieved from the database.
	 */
	public static ArrayList<Inviter> get_AllGuides() {
		ArrayList<Inviter> array = new ArrayList<>();
		if (con != null)
			try {
				String query = "Select * FROM inviters where isGuide=1";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
					Inviter t = new Inviter(rs.getString("idNumber"), true);
					array.add(t);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return array;
	}
	/**
	 * getAllOrders_InWaitingList -> Method that gets from the WaitingList in DB all the orders by
	 * ID number that belongs to a specific inviter and returns it to this inviter.
	 * @param Array that contains an order IDnumber in the first index.
	 * @return ArrayList<order> NEWorders that contains all the orders in WaitingList that belong to the inviter 
	 * with the IDnumber
	 */
	public static ArrayList<Order> getAllOrders_InWaitingList(ArrayList<Object> arr) {
		ArrayList<Order> orders = new ArrayList<Order>();
		String idNumber = (String) arr.get(0);
		if (con != null) {
			try {
				PreparedStatement ps = con.prepareStatement(
						"SELECT * FROM waitinglist WHERE ordererId = ? AND  visitDate >= ? ");
				ps.setString(1, idNumber);
				ps.setDate(2, Date.valueOf(LocalDate.now()));
				ResultSet rs = ps.executeQuery();
				while (rs.next())
					orders.add(fill_Order(rs));
				return orders;
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * update_inWaitingList -> Method updates the waiting list by checking for available spots and notifying inviter.
	 * This method queries the waiting list database for orders with a status of "waiting". For each such order found,
	 * it checks if there are available spots in the park at the specified visit date and time. If spots are available,
	 * a notification is sent to the inviter via SMS and email, informing them that a spot has opened up and they have
	 * two hours to confirm or cancel their place in the park.
	 * @param array An ArrayList containing orders to be processed for updates in the waiting list.
	 * @return True if the operation was successful, false otherwise.
	 */
	public static boolean update_inWaitingList(ArrayList<Object> arr) {
		Order order;
		if (con != null) {
			try {
				PreparedStatement ps = con
						.prepareStatement("SELECT * FROM waitinglist WHERE  orderStatus ='waiting';");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ArrayList<Object> arr2 = new ArrayList<Object>();
					order = fill_Order(rs);
					arr2.add(rs.getTime("visitTime"));
					arr2.add(rs.getDate("visitDate"));
					arr2.add(rs.getInt("numOfvisitors"));
					arr2.add(rs.getString("parkName"));
					if (check_Availibility(arr2)) {
						arr2.clear();
						arr2.add(order);
						transfer_FromWaitingList_ToOrders(arr2);
					}
				}
				return true;

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * transfer_FromWaitingList_ToOrders -> Method that Moves an order from the waiting list to the orders list after confirmation.
	 * This method updates the status of the provided order in the waiting list to "confirmed" and then
	 * moves it to the orders list. It also adds the order details to the orders list in the database.
	 * @param array An ArrayList containing the order to be moved from the waiting list to the orders list.
	 * @return True if the order was successfully moved to the orders list, false otherwise.
	 */
	@SuppressWarnings("deprecation")
	public static boolean transfer_FromWaitingList_ToOrders(ArrayList<Object> arr) {
	    Order order = (Order) arr.get(0);
	    if (con != null) {
	        if (add_Order(arr)) {
	            Time plus2Hour = Time.valueOf(LocalTime.now().plusHours(2));
	            try {
	                PreparedStatement ps = con.prepareStatement(
	                        "update waitingList set orderStatus = ? where ordererId = ? and visitTime = ? and visitDate = ? and parkName=?");
	                ps.setString(1, "confirmed");
	                ps.setString(2, order.getOrderer());
	                ps.setTime(3, order.getVisitTime());
	                ps.setDate(4, order.getVisitDate());
	                ps.setString(5, order.getPark().getName());
	                ps.executeUpdate();
	                return true;
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return false;
	}
	/**
	 * Checks the status of orders in the waiting list and cancels them if necessary.
	 * Sends cancellation notifications to customers via SMS and email for orders with status 'confirmed' and scheduled for past dates or times.
	 * 
	 * @param arr2 An ArrayList containing the Order objects to check.
	 * @return A boolean indicating whether any orders were found and cancelled.
	 *///done 
	public static boolean check_statusOrderInWaitingList(ArrayList<Object> arr2) {
	    if (con != null) {
	        try {
	            PreparedStatement ps = con.prepareStatement(
	                "SELECT * FROM waitingList WHERE (orderStatus = ? AND visitDate < ?) OR (visitTime < ? AND visitDate = ?);");
	            ps.setString(1, "confirmed");
	            ps.setDate(2, Date.valueOf(LocalDate.now()));
	            ps.setTime(3, Time.valueOf(LocalTime.now()));
	            ps.setDate(4, Date.valueOf(LocalDate.now()));

	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                Order order = fill_Order(rs);
	                send_Notification(order,1);
	                ArrayList<Object> cancelArr = new ArrayList<>();
	                cancelArr.add(order);
	                delete_FromWaitingList(cancelArr);
	            }
	            return true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	/**
	 * Sends notification to the customer via SMS and email.
	 * 
	 * @param order The Order object for which the notification is sent.
	 */
		private static void send_Notification(Order order, int typeNotification) {
		    String phoneNumber = order.getPhoneNumber();
		    String email = order.getEmail();
		    Time visitTime = order.getVisitTime();
		    String message = null;
		    switch (typeNotification) {
	        case 1:
	            message = "[" +
	                      "sending SMS to: " + phoneNumber +
	                      "  sending email to: " + email +
	                      "  message : Your order for tomorrow on " + visitTime +
	                      " was automatically cancelled because you haven't replied]";
	            break;
	        case 2:
	        	message = "[" +
		                "Sending SMS to: " + phoneNumber +
		                "  Sending email to: " + email +
		                "  Message : Your order is for tomorrow on " + visitTime +
		                ", You have 2 hours from now to confirm/cancel on the GoNature application]";
	        	break;
	     
	        default:
	            break;
		    }
		    EchoServer.serverController.writeToConsole(message);
		}
		/**
		 * Deletes unconfirmed orders that are scheduled for today and have not received a reply from the customer.
		 * Automatically cancels unconfirmed orders that are scheduled for today and have not received a reply from the customer.
		 * Sends notifications to customers via SMS and email for the cancelled orders.
		 * 
		 * @param arr An ArrayList containing parameters (not used in the refactored method).
		 */
		public static void updatestatusTo_Unconfirmed(ArrayList<Object> arr) {
		    if (con != null) {
		        try {
		            LocalDate currDate = LocalDate.now();
		            LocalTime currTime = LocalTime.now();
		            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE (visitDate<? and orderStatus='waiting') or (visitDate=? and orderStatus='waiting' and visitTime<?);");
		            ps.setDate(1, Date.valueOf(currDate));
		            ps.setDate(2, Date.valueOf(currDate.plusDays(1)));
		            ps.setTime(3, Time.valueOf(currTime.minusHours(2)));
		            ResultSet rs = ps.executeQuery();
		            while (rs.next()) {
		                Order order = fill_Order(rs);
		                send_Notification(order,1);
		            }
		            PreparedStatement updatePs = con.prepareStatement(
		                    "UPDATE orders SET orderStatus = 'unconfirmed' WHERE (visitDate < ? AND orderStatus = 'waiting') OR " +
		                    "(visitDate = ? AND orderStatus = 'waiting' AND visitTime < ?);");
		            updatePs.setDate(1, Date.valueOf(currDate));
		            updatePs.setDate(2, Date.valueOf(currDate.plusDays(1)));
		            updatePs.setTime(3, Time.valueOf(currTime.minusHours(2)));
		            updatePs.executeUpdate();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
		/**
		 * Retrieves orders scheduled for the next day and notifies customers.
		 * Sends notifications to customers via SMS and email for orders with status 'waiting' scheduled for the next day.
		 * 
		 * @param arr 
		 */
		public static void oneDayBefore_notify_inviter(ArrayList<Object> arr) {
		    if (con != null) {
		        try {
		            PreparedStatement ps = con.prepareStatement(
		                    "SELECT * FROM orders WHERE visitDate = ? AND visitTime = ? AND orderStatus = 'waiting';");
		            LocalDate currDate = LocalDate.now();
		            LocalTime currTime = LocalTime.now();
		            Date nextDayDate = Date.valueOf(currDate.plusDays(1));
		            Time currTimeSql = Time.valueOf(currTime);
		            ps.setDate(1, nextDayDate);
		            ps.setTime(2, currTimeSql);
		            ResultSet rs = ps.executeQuery();
		            while (rs.next()) {
		                Order order = fill_Order(rs);
		                send_Notification(order,2);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
		/** 
		 * This method retrieves the usage times for each day of the specified month
		 * for the given park. It calculates the periods when the park was in use and
		 * creates a report detailing the start and end times of each usage period for
		 * each day.
		 * @param arr An ArrayList containing the following elements:
		 *            - The year and month for which the report is generated.
		 *            - The name of the park.
		 * @return An ArrayList containing reportHelper objects representing the usage
		 *         periods for each day of the specified month. Each reportHelper object
		 *         contains the date, start and end times of each usage period for that day.
		 */
		@SuppressWarnings("deprecation")
		public static ArrayList<reportHelper> create_UsageReport(ArrayList<Object> arr) {
		    ArrayList<reportHelper> report = new ArrayList<>();
		    int month = ((Date) arr.get(1)).getMonth();
		    int year = ((Date) arr.get(1)).getYear();
		    String parkName = (String) arr.get(2);
		    if (con != null) {
		        try {
		            YearMonth yMonth = YearMonth.of(year, month + 1);
		            int days = yMonth.lengthOfMonth();
		            for (int i = 1; i <= days; i++) {
		                Date currDate = new Date(year, month, i);
		                ArrayList<Time> times = get_ParkoperatingHours_ForDate(parkName, currDate);
		                ArrayList<reportHelper> dailyReport = process_TimesIntoReport(currDate, times);
		                report.addAll(dailyReport);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		    return report;
		}
		/**
		 * Retrieves the park times for a specified date from the database.
		 * 
		 * @param parkName The name of the park
		 * @param date     The date for which park times are retrieved
		 * @return An ArrayList of Time objects representing park opening and closing times
		 * @throws SQLException If an SQL exception occurs
		 */
		private static ArrayList<Time> get_ParkoperatingHours_ForDate(String parkName, Date date) throws SQLException {
		    ArrayList<Time> times = new ArrayList<>();
		    PreparedStatement pStatement = con.prepareStatement(
		            "SELECT startTime, endTime FROM parkFullTimes WHERE parkName = ? AND visitDate = ? ORDER BY startTime");
		    pStatement.setString(1, parkName);
		    pStatement.setDate(2, date);
		    ResultSet rs = pStatement.executeQuery();
		    while (rs.next()) {
		        times.add(rs.getTime("startTime"));
		        times.add(rs.getTime("endTime"));
		    }
		    return times;
		}
		/**
		 * Processes park times into a daily usage report.
		 * 
		 * @param date  The date for which the report is generated
		 * @param times An ArrayList of Time objects representing park opening and closing times
		 * @return An ArrayList of reportHelper objects representing the daily usage report
		 */
		private static ArrayList<reportHelper> process_TimesIntoReport(Date date, ArrayList<Time> times) {
		    ArrayList<reportHelper> dailyReport = new ArrayList<>();
		    Time startingTime = new Time(8, 0, 0);
		    Time closingTime = new Time(19, 0, 0);
		    for (int j = 0; j < times.size(); j += 2) {
		        Time startTime = times.get(j);
		        Time endTime = times.get(j + 1);
		        if (startTime.compareTo(startingTime) != 0) {
		            dailyReport.add(new reportHelper(date, startingTime + "," + startTime));
		        }
		        startingTime = endTime;
		    }
		    if (startingTime.compareTo(closingTime) != 0) {
		        dailyReport.add(new reportHelper(date, startingTime + "," + closingTime));
		    }
		    return dailyReport;
		}
		/**
		 * This method retrieves the count of unconfirmed and confirmed but not arrived
		 * orders for the specified park and month. It creates a report detailing the
		 * counts of these two types of orders.
		 * @param arr An ArrayList containing the following elements:
		 *            - The year and month for which the report is generated.
		 *            - The date for which the report is generated.
		 *            - The name of the park.
		 * @return An ArrayList containing integers representing the count of unconfirmed
		 *         orders and confirmed but not arrived orders for the specified park and
		 *         month.
		 */
		@SuppressWarnings("deprecation")
	    public static ArrayList<Integer> create_CancelReport(ArrayList<Object> arr) {
	    ArrayList<Integer> res = new ArrayList<>();
	    String parkName = (String) arr.get(2);
	    Date date = (Date) arr.get(1);
	    int year = date.getYear() + 1900;
	    int month = date.getMonth() + 1;
	    if (con != null) {
	        String unconfirmedQuery = "SELECT COUNT(*) FROM orders WHERE month(visitDate) = ? AND orderStatus = 'unconfirmed' AND year(visitDate) = ? AND parkName = ?";
	        String confirmedQuery = "SELECT COUNT(*) FROM orders WHERE month(visitDate) = ? AND orderStatus = 'confirmed' AND arrived = 0 AND year(visitDate) = ? AND parkName = ?";
	        try (PreparedStatement unconfirmedStmt = con.prepareStatement(unconfirmedQuery);
	             PreparedStatement confirmedStmt = con.prepareStatement(confirmedQuery)) {
	            unconfirmedStmt.setInt(1, month);
	            unconfirmedStmt.setInt(2, year);
	            unconfirmedStmt.setString(3, parkName);
	            confirmedStmt.setInt(1, month);
	            confirmedStmt.setInt(2, year);
	            confirmedStmt.setString(3, parkName);
	            try (ResultSet rs1 = unconfirmedStmt.executeQuery()) {
	                if (rs1.next()) {
	                    res.add(rs1.getInt(1));
	                }
	            }
	            try (ResultSet rs2 = confirmedStmt.executeQuery()) {
	                if (rs2.next()) {
	                    res.add(rs2.getInt(1));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return res;
	  }
		/**
		 * Checks if a report of a specific type already exists for a given park and date.
		 * @param arr An ArrayList containing the parameters required for checking the existence of the report:
		 *            - Index 0: String representing the type of report to be checked.
		 *            - Index 1: Date object representing the date for which the report is checked.
		 *            - Index 2: String containing the name of the park for which the report is checked.
		 * @return true if the report of the specified type exists for the given park and date, false otherwise.
		 */
		@SuppressWarnings("deprecation")
		public static boolean check_ReportExistence(ArrayList<Object> arr) {
			if (con != null) {
				String reportType = (String) arr.get(0);
				Date date = (Date) arr.get(1);
				 int month = date.getMonth() + 1;
		            int year = date.getYear() + 1900;
				String ParkName = (String) arr.get(2);
				try {
					PreparedStatement ps = con.prepareStatement(
							"SELECT * FROM reports WHERE month(reportDate) = ? and year(reportDate) = ?  and reportType = ? and park = ? ;");
					ps.setInt(1, month);
					ps.setInt(2, year);
					ps.setString(3, reportType);
					ps.setString(4, ParkName);
					ResultSet result = ps.executeQuery();
					if (result.next()) {
						
						return true;
					} else {
						
						return false;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
	}
		/**
		 * Retrieves a report of a specific type for a given park and date.
		 * @param arr An ArrayList containing the parameters required for retrieving the report:
		 *            - Index 0: String representing the type of report to be retrieved.
		 *            - Index 1: Date object representing the date for which the report is retrieved.
		 *            - Index 2: String containing the name of the park for which the report is retrieved.
		 * @return A String containing the data of the report if it exists, or null if the report is not found.
		 */
		@SuppressWarnings("deprecation")
		public static String get_Report(ArrayList<Object> arr) {
			ResultSet rs = null;
			if (con != null) {
				String reportType = (String) arr.get(0);
				Date date = (Date) arr.get(1);
			     int month = date.getMonth() + 1;
		            int year = date.getYear() + 1900;
				String ParkName = (String) arr.get(2);
				try {
					PreparedStatement st = con.prepareStatement(
							"SELECT * FROM reports where park = ? and month(reportDate) = ? and year(reportDate) = ? and reportType = ? ;");
					st.setString(1, ParkName);
					st.setInt(2, month);
					st.setInt(3, year);
					st.setString(4, reportType);
					rs = st.executeQuery();
					if (rs.next()) {
						return rs.getString("Data");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	/**
	 * Inserts a new report into the database.
	 * @param arr An ArrayList containing the parameters required for inserting the report:
	 *            - Index 0: String representing the type of report.
	 *            - Index 1: Date object representing the date of the report.
	 *            - Index 2: ArrayList of Strings containing the data of the report.
	 *            - Index 3: String containing the name of the park associated with the report.
	 * @param reportType 
	 */
	public static void insert_Report(ArrayList<Object> arr) {
		String reportType = (String) arr.get(0);
		Date date = (Date) arr.get(1);
		ArrayList<String> data = (ArrayList<String>) arr.get(2);
		String parkName = (String) arr.get(3);
		String str = "";
		for (int i = 0; i < data.size(); i++) {
			str += data.get(i) + " ";
		}
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("INSERT INTO reports (reportType,  reportDate, Data, park) VALUES (?, ?, ?, ?);");
			stmt.setString(1, reportType);
			stmt.setDate(2, date);
			stmt.setString(3, str);
			stmt.setString(4, parkName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Generates a report of the total number of visitors for a specific park within a given month.
	 * This method calculates the total number of visitors based on different order types (planned and unplanned inviters, planned and unplanned guides)
	 * and also provides the count of visitors for each day of the week.
	 * @param arr An ArrayList containing the parameters required for generating the report:
	 *            - Index 0: Date object representing the month and year for which the report is generated.
	 *            - Index 1: String containing the name of the park for which the report is generated.
	 * @return An ArrayList containing the following elements:
	 *         - Index 0: Total number of planned inviters for the specified month.
	 *         - Index 1: Total number of planned guides for the specified month.
	 *         - Index 2: Total number of unplanned inviters for the specified month.
	 *         - Index 3: Total number of unplanned guides for the specified month.
	 *         - Index 4 onwards: Number of inviters for each day of the week (Sunday to Saturday),
	 *         considering different order types (planned and unplanned inviters, planned and unplanned guides).
	 */
	@SuppressWarnings("deprecation")
	public static ArrayList<Long> create_TotalVisitorReport(ArrayList<Object> arr) {
	    ArrayList<Long> result = new ArrayList<>();
	    long PLANNEDTRAVELER = 0, PLANNEDGUIDE = 0, UNPLANNEDTRAVELER = 0, UNPLANNEDGUIDE = 0;
	    if (con != null) {
	        try {
	            Date visitDate = (Date) arr.get(1);
	            int month = visitDate.getMonth() + 1;
	            int year = visitDate.getYear() + 1900;
	            String parkName = (String) arr.get(2);
	            String query = "SELECT * FROM orders WHERE month(visitDate) = ? AND year(visitDate) = ? AND arrived = 1 AND parkName = ?";
	            try (PreparedStatement ps = con.prepareStatement(query)) {
	                ps.setInt(1, month);
	                ps.setInt(2, year);
	                ps.setString(3, parkName);
	                try (ResultSet rs = ps.executeQuery()) {
	                    while (rs.next()) {
	                        switch (rs.getString("orderType")) {
	                            case "PLANNEDTRAVELER":
	                                PLANNEDTRAVELER += rs.getInt("numOfVisitors");
	                                break;
	                            case "PLANNEDGUIDE":
	                                PLANNEDGUIDE += rs.getInt("numOfVisitors");
	                                break;
	                            case "UNPLANNEDTRAVELER":
	                                UNPLANNEDTRAVELER += rs.getInt("numOfVisitors");
	                                break;
	                            case "UNPLANNEDGUIDE":
	                                UNPLANNEDGUIDE += rs.getInt("numOfVisitors");
	                                break;
	                        }
	                    }
	                }
	            }
	            result.add(PLANNEDTRAVELER);
	            result.add(PLANNEDGUIDE);
	            result.add(UNPLANNEDTRAVELER);
	            result.add(UNPLANNEDGUIDE);
	            result.addAll(getNuminviter_ByDayinWeek((Date) arr.get(1), parkName, OrderType.PLANNEDTRAVELER.toString()));
	            result.addAll(getNuminviter_ByDayinWeek((Date) arr.get(1), parkName, OrderType.PLANNEDGUIDE.toString()));
	            result.addAll(getNuminviter_ByDayinWeek((Date) arr.get(1), parkName, OrderType.UNPLANNEDTRAVELER.toString()));
	            result.addAll(getNuminviter_ByDayinWeek((Date) arr.get(1), parkName, OrderType.UNPLANNEDGUIDE.toString()));
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}
	/**
	 * Retrieves the number of inviters by day of the week for a specific park and inviter type.
	 * @param date            The date for which the data is retrieved.
	 * @param ParkName        The name of the park.
	 * @param TravelerType    The type of inviter (PlannedInviter, UnPlannedGuide).
	 * @return An ArrayList containing the number of inviters for each day of the week (Sunday to Saturday).
	 */
	@SuppressWarnings("deprecation")
	public static ArrayList<Long> getNuminviter_ByDayinWeek(Date d, String ParkName, String TravelerType) {
		int month = d.getMonth() + 1;
		int year = d.getYear() + 1900;
		ArrayList<Long> arr = new ArrayList<Long>();
		ResultSet rs;
		if (con != null) {
			try {
				PreparedStatement ps;
				for (int i = 1; i < 8; i++) {
					ps = con.prepareStatement(
							"select sum(numOfVisitors) from orders where parkName = ? and month(visitDate) = ? and  year(visitDate) = ? and  DAYOFWEEK(visitDate) = ? and arrived = 1 and orderType = ? ");
					ps.setString(1, ParkName);
					ps.setInt(2, month);
					ps.setInt(3, year);
					ps.setInt(4, i);
					ps.setString(5, TravelerType);
					rs = ps.executeQuery();
					if (rs.next())
						arr.add(rs.getLong(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arr;
	}
	/** 
	 * This method queries the database to retrieve the total number of inviters who arrived at the park during
	 * different time slots specified by the parameter 't' (morning, afternoon, evening) on the given date.
	 * @param Date d,    The date for which the number of visitors is to be retrieved.
	 * @param parkName,  The name of the park.
	 * @param String t,  The time slot ('morning', 'afternoon', or 'evening') for which the number of inviters is to be retrieved.
	 * @return An ArrayList containing the following elements:
	 *         - The total number of inviters for the specified time slot in the morning (8:00 AM - 11:59 AM).
	 *         - The total number of inviters for the specified time slot in the afternoon (12:00 PM - 3:59 PM).
	 *         - The total number of inviters for the specified time slot in the evening (4:00 PM - 11:59 PM).
	 *         If no visitors are found for any time slot, the corresponding element in the ArrayList will be null.
	 */
	public static ArrayList<Integer> getnumOfVisitors_ByTime_date(Date d, String parkName, String time) {
	    ArrayList<Integer> arr = null;
	    int month = d.getMonth() + 1, year = d.getYear() + 1900;
	    if (con != null) {
	        arr = new ArrayList<Integer>();
	        try {
	            String query = "SELECT sum(numOfVisitors) FROM orders WHERE parkName = ? " +
	                    "AND month(visitDate) = ? AND year(visitDate) = ? " +
	                    "AND arrived = 1 AND orderType = ? AND hour(entrance)>=? and  hour(entrance)<?;";
	            try (PreparedStatement ps = con.prepareStatement(query)) {
	                ps.setString(1, parkName);
	                ps.setInt(2, month);
	                ps.setInt(3, year);
	                ps.setString(4, time);
	                for (int i = 1; i <= 3; i++) {
	                    switch (i) {
	                        case 1:
	                            ps.setInt(5, 8);
	                            ps.setInt(6, 12);
	                            break;
	                        case 2:
	                            ps.setInt(5, 12);
	                            ps.setInt(6, 16);
	                            break;
	                        case 3:
	                            ps.setInt(5, 16);
	                            ps.setInt(6, 24);
	                            break;
	                    }
	                    try (ResultSet rs = ps.executeQuery()) {
	                        if (rs.next()) {
	                            arr.add(rs.getInt(1));
	                        }
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return arr;
	}	
	/**
	 * This method calculates the number of visitors based on their stay time (in hours) for the specified month, park, and order type.
	 * @param Date date ,    The date for which the calculation is performed.
	 * @param parkName,   The name of the park.
	 * @param orderType,  The type of order (PlannedInviter, UnPlannedGuide).
	 * @return An ArrayList containing the following inviter count based on stay time:
	 *         - The number of inviters with a stay time less than or equal to 2 hours.
	 *         - The number of inviters with a stay time greater than 2 hours and less than or equal to 3 hours.
	 *         - The number of inviters with a stay time greater than 3 hours.
	 */
	public static ArrayList<Integer> getnumOfVisitors_ByStayTime(Date d, String parkName, String orderType) {
	    ArrayList<Integer> arr = null;
	    int month = d.getMonth() + 1;
	    int year = d.getYear() + 1900;
	    if (con != null) {
	        arr = new ArrayList<>();
	        try {
	            String query = "SELECT SUM(numOfVisitors) FROM orders WHERE parkName = ? " +
	                           "AND MONTH(visitDate) = ? AND YEAR(visitDate) = ? " +
	                           "AND orderType = ? AND ";
	            try (PreparedStatement ps = con.prepareStatement(query)) {
	                for (int i = 1; i <= 3; i++) {
	                    switch (i) {
	                        case 1:
	                        	query += " (entrance - orders.exittime) <= ?";
	                            ps.setString(1, parkName);
	                            ps.setInt(2, month);
	                            ps.setInt(3, year);
	                            ps.setString(4, orderType);
	                            ps.setInt(5,2 );
	                            break;
	                        case 2:
	                        	query +="(entrance - orders.exittime)> ? AND (entrance - orders.exittime)<= ?";
	                            ps.setString(1, parkName);
	                            ps.setInt(2, month);
	                            ps.setInt(3, year);
	                            ps.setString(4, orderType);
	                            ps.setInt(5, 2);
	                            ps.setInt(6, 3);
	                            break;
	                        case 3:
	                            query +="(entrance - orders.exittime) > ?";
	                            ps.setString(1, parkName);
	                            ps.setInt(2, month);
	                            ps.setInt(3, year);
	                            ps.setString(4, orderType);
	                            ps.setInt(5, 3);
	                            break;
	                    }
	                    try (ResultSet rs = ps.executeQuery()) {
	                        if (rs.next()) {
	                            arr.add(rs.getInt(1));
	                        }
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return arr;
	}
	/**
	 * This method retrieves the number of visitors for different order types and their stay times on the specified date and park.
	 * @param arr An ArrayList containing the following elements:
	 *            - The date for which the report is generated.
	 *            - The name of the park.
	 * @return An ArrayList containing the following visitor counts:
	 *         - Number of planned inviters by time slots (8:00-12:00, 12:00-16:00, 16:00-23:00).
	 *         - Number of planned inviters by stay time (<= 2 hours, > 2 hours and <= 3 hours, > 3 hours).
	 *         - Number of planned guides by time slots (8:00-12:00, 12:00-16:00, 16:00-23:00).
	 *         - Number of planned guides by stay time (<= 2 hours, > 2 hours and <= 3 hours, > 3 hours).
	 *         - Number of unplanned inviters by time slots (8:00-12:00, 12:00-16:00, 16:00-23:00).
	 *         - Number of unplanned inviters by stay time (<= 2 hours, > 2 hours and <= 3 hours, > 3 hours).
	 *         - Number of unplanned guides by time slots (8:00-12:00, 12:00-16:00, 16:00-23:00).
	 *         - Number of unplanned guides by stay time (<= 2 hours, > 2 hours and <= 3 hours, > 3 hours).
	 */
	public static ArrayList<Integer> create_VisitReport(ArrayList<Object> arr) {
	    Date date = (Date) arr.get(0);
	    String parkName = (String) arr.get(1);
	    ArrayList<Integer> report = new ArrayList<>();

	    // For PLANNEDTRAVELER
	    report.addAll(getnumOfVisitors_ByTime_date(date, parkName, "PLANNEDTRAVELER"));
	    report.addAll(getnumOfVisitors_ByStayTime(date, parkName, "PLANNEDTRAVELER"));

	    // For PLANNEDGUIDE
	    report.addAll(getnumOfVisitors_ByTime_date(date, parkName, "PLANNEDGUIDE"));
	    report.addAll(getnumOfVisitors_ByStayTime(date, parkName, "PLANNEDGUIDE"));

	    // For UNPLANNEDTRAVELER
	    report.addAll(getnumOfVisitors_ByTime_date(date, parkName, "UNPLANNEDTRAVELER"));
	    report.addAll(getnumOfVisitors_ByStayTime(date, parkName, "UNPLANNEDTRAVELER"));

	    // For UNPLANNEDGUIDE
	    report.addAll(getnumOfVisitors_ByTime_date(date, parkName, "UNPLANNEDGUIDE"));
	    report.addAll(getnumOfVisitors_ByStayTime(date, parkName, "UNPLANNEDGUIDE"));

	    return report;
	}
	
	 /**
	  * Checks if the provided ID number exists in the database table "UserConnections". If the ID number exists and
      * the user is not already connected, it updates the "isConnected" status to 1. If the ID number does not exist,
      * it inserts a new record with the ID number and sets the "isConnected" flag to 1.
      * @param arr An ArrayList containing the ID number to check and update if it exists or insert if it does not.
      * @return true if the ID number was successfully updated or inserted, false if the user is already connected,
      * an error occurred during the database operation, or the ID number already existed and was not updated.
	  */
	 public static boolean checkAndUpdateOrInsertUser(ArrayList<Object> arr) {
		 String idNumber=(String)arr.get(0);
	       
	        String checkSql = "SELECT isConnected FROM UserConnections WHERE idNumber = ?";
	        
	
	        String updateSql = "UPDATE UserConnections SET isConnected = 1 WHERE idNumber = ?";
	        
	       
	        String insertSql = "INSERT INTO UserConnections (idNumber, isConnected) VALUES (?, 0)";
	        
	        try {
	            
	            PreparedStatement checkStmt = con.prepareStatement(checkSql);
	            checkStmt.setString(1, idNumber);
	            ResultSet resultSet = checkStmt.executeQuery();
	            
	            if (resultSet.next()) {
	               
	                int isConnected = resultSet.getInt("isConnected");
	                if (isConnected == 1) {
	                    
	                    System.out.println("User with idNumber " + idNumber + " is already connected.");
	                    return false;
	                } else {
	                    
	                    PreparedStatement updateStmt = con.prepareStatement(updateSql);
	                    updateStmt.setString(1, idNumber);
	                    updateStmt.executeUpdate();
	                    System.out.println("User with idNumber " + idNumber + " is now set to connected.");
	                    return true;
	                }
	            } else {
	                
	                PreparedStatement insertStmt = con.prepareStatement(insertSql);
	                insertStmt.setString(1, idNumber);
	                insertStmt.executeUpdate();
	                System.out.println("New user inserted successfully with isConnected set to 1.");
	                return true;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false; 
	        }
	    }
	 
	 /**-----------------------------------------------------------not checked ------------------------------
	  * Updates the connection status of a user with the specified ID number to disconnected (isConnected = 0) in the database.
	  * @param idNumber The ID number of the user whose connection status needs to be updated to disconnected.
	  * @return true if the user's connection status was successfully updated to disconnected, false otherwise 
	  * (no user found with the given ID number or an SQL error occurred).
	 */
	 public static boolean updateUserConnectionToDisconnected(String idNumber) {
		    
		    String sql = "UPDATE UserConnections SET isConnected = 0 WHERE idNumber = ?";

		    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
		        
		        pstmt.setString(1, idNumber);

		        
		        int affectedRows = pstmt.executeUpdate();

		       
		        if (affectedRows > 0) {
		            System.out.println("User connection for idNumber: " + idNumber + " has been set to disconnected.");
		            return true;
		        } else {
		            System.out.println("No user connection found with idNumber: " + idNumber + ", or it is already set to disconnected.");
		            return false;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false; // Handle this as needed
		    }
		}
	 public static void insertDataFromFilePath(String tableName, String filePath) {
		    String sql = "INSERT INTO " + tableName 
		        + " (workerId, userName, password, firstName, lastName, email, role, parkName) "
		        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		    List<String[]> data = new ArrayList<>();
		    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
		        String line;
		        while ((line = reader.readLine()) != null) {
		            String[] rowData = line.split(",");
		            data.add(rowData);
		        }
		    } catch (IOException e) {
		        System.out.println("Could not read the file: " + e.getMessage());
		        return; // Exit the method if file reading fails
		    }

		    Connection con = getConn(); // Utilize the existing static connection
		    if (con == null) {
		        System.out.println("Failed to establish database connection.");
		        return; // Exit if unable to establish a connection
		    }

		    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
		        con.setAutoCommit(false); // Use transaction for batch insert

		        for (String[] row : data) {
		            String parkName = row[7].trim(); // Assuming parkName is the 8th value in the row
		            if (!parkName.isEmpty() && !doesParkNameExist(parkName, con)) {
		                System.out.println("parkName does not exist: " + parkName);
		                continue; // Skip this row or handle it according to your business logic
		            }

		            for (int i = 0; i < row.length; i++) {
		                if (row[i] != null && !row[i].isEmpty()) {
		                    pstmt.setString(i + 1, row[i].trim());
		                } else {
		                    pstmt.setNull(i + 1, java.sql.Types.VARCHAR); // Handle potential NULL values
		                }
		            }
		            pstmt.addBatch(); // Add to batch
		        }
		        
		        pstmt.executeBatch(); // Execute the batch
		        con.commit(); // Commit transaction
		        System.out.println("Data import completed successfully.");
		    } catch (SQLException e) {
		        System.out.println("Error executing the database batch insert: " + e.getMessage());
		        try {
		            if (con != null) con.rollback(); // Attempt to rollback changes on error
		        } catch (SQLException rollbackEx) {
		            System.out.println("Error during transaction rollback: " + rollbackEx.getMessage());
		        }
		    }
		    
		}
	 public static boolean doesParkNameExist(String parkName, Connection con) {
		    String sql = "SELECT COUNT(*) FROM parks WHERE name = ?";
		    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
		        pstmt.setString(1, parkName);
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            return rs.getInt(1) > 0;
		        }
		    } catch (SQLException e) {
		        System.out.println("Database error while checking park name: " + e.getMessage());
		    }
		    return false;
		}
	 
	 public static void deleteAllUsers() {
		    String query = "DELETE FROM users";
		    try (PreparedStatement statement = con.prepareStatement(query)) {
		        statement.executeUpdate();
		    } catch (SQLException e) {
		        System.err.println("Error deleting all users: " + e.getMessage());
		    }
		}
 
}
