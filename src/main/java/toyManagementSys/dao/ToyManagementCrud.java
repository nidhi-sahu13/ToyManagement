package toyManagementSys.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import com.mysql.cj.jdbc.Driver;

import toyManagementSys.dto.AdminModule;
import toyManagementSys.dto.ToyItem;
import toyManagementSys.dto.ToyStore;
import toyManagementSys.dto.UserModule;

public class ToyManagementCrud {
	static Scanner sc = new Scanner(System.in);

	public Connection createTable() throws Exception {
		DriverManager.registerDriver(new Driver());

		FileInputStream stream = new FileInputStream("dbConfig.properties");
		Properties p = new Properties();
		p.load(stream);

		Connection conn = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/ToyManagementSystem?createDatabaseIfNotExist=true", p);

		PreparedStatement admin = conn.prepareStatement(
				"create table if not exists AdminTable(id int primary key,name varchar(45),email varchar(25) unique,password varchar(15),contactNo bigInt(10) unique,toyStore varchar(45))");
		admin.executeUpdate();
		PreparedStatement user = conn.prepareStatement(
				"create table if not exists UserTable(id int,name varchar(45),email varchar(25),contactNo bigInt(10) unique,password varchar(45),wallet bigint)");
		user.executeUpdate();
		PreparedStatement toy = conn.prepareStatement(
				"create table if not exists ToyTable(ToyId int primary key auto_increment,toyName varchar(40),toyemail varchar(45),FOREIGN KEY (toyemail) REFERENCES ADMINTABLE(email),color varchar(25) ,quanity int,price bigint)");
		toy.executeUpdate();
		return conn;
	}

	public void insertData(AdminModule adminM) throws Exception {
		Connection conn = createTable();
		PreparedStatement admin = conn.prepareStatement("insert into AdminTable values(?,?,?,?,?,?)");
		admin.setInt(1, adminM.getId());
		admin.setString(2, adminM.getName());
		admin.setString(3, adminM.getEmail());
		admin.setString(4, adminM.getPassword());
		admin.setLong(5, adminM.getcontactNo());
		admin.setString(6, adminM.getToyStore());
		admin.executeUpdate();
	}

	public AdminModule fetchAdminDetail(String email1) throws Exception {
		Connection conn = createTable();
		PreparedStatement details = conn.prepareStatement("select * from AdminTable where email=?");
		details.setString(1, email1);
		ResultSet res = details.executeQuery();
		if (res.next()) {
			int id = res.getInt(1);
			String name = res.getString(2);
			String email = res.getString(3);
			String password = res.getString(4);
			long contactNo = res.getLong(5);
			String toyStore = res.getString(6);

			return new AdminModule(id, name, email, password, contactNo, toyStore);
		} else {
			return null;
		}
	}

	public AdminModule updateAdmindata(AdminModule admin, int i, Object o) throws Exception {
		Connection conn = createTable();
		PreparedStatement ps;
		if (i == 1) {
			String n = (String) o;
			ps = conn.prepareStatement("update AdminTable set name=? where id=?");
			ps.setString(1, n);
			ps.setInt(2, admin.getId());
			ps.executeUpdate();

			return new AdminModule(admin.getId(), n, admin.getEmail(), admin.getPassword(), admin.getcontactNo(),
					admin.getToyStore());
		} else if (i == 2) {
			String e = (String) o;

			ps = conn.prepareStatement("update AdminTable set email=? where id=?");
			ps.setString(1, e);
			ps.setInt(2, admin.getId());
			ps.executeUpdate();
			PreparedStatement ps1 = conn.prepareStatement("update toyTable set toyemail=? where toyemail=?");
			ps1.setString(1, e);
			ps1.setString(2, admin.getEmail());
			ps1.executeUpdate();

			return new AdminModule(admin.getId(), admin.getName(), e, admin.getPassword(), admin.getcontactNo(),
					admin.getToyStore());
		} else if (i == 3) {
			String pwd = (String) o;
			ps = conn.prepareStatement("update AdminTable set password=? where id=?");
			ps.setString(1, pwd);
			ps.setInt(2, admin.getId());
			ps.executeUpdate();
			return new AdminModule(admin.getId(), admin.getName(), admin.getEmail(), pwd, admin.getcontactNo(),
					admin.getToyStore());
		} else if (i == 4) {
			long c = (long) o;
			ps = conn.prepareStatement("update AdminTable set contactNo=? where id=?");
			ps.setLong(1, c);
			ps.setInt(2, admin.getId());
			ps.executeUpdate();
			return new AdminModule(admin.getId(), admin.getName(), admin.getEmail(), admin.getPassword(), c,
					admin.getToyStore());
		} else if (i == 5) {
			String tS = (String) o;
			ps = conn.prepareStatement("update AdminTable set toystore=? where id=?");
			ps.setString(1, tS);
			ps.setInt(2, admin.getId());
			ps.executeUpdate();
			return new AdminModule(admin.getId(), admin.getName(), admin.getEmail(), admin.getPassword(),
					admin.getcontactNo(), tS);
		} else {
			return null;
		}
	}

	public void deleteAdmin(AdminModule admin) throws Exception {
		Connection conn = createTable();
		PreparedStatement deleteToy = conn.prepareStatement("delete from toyTable where toyemail=?");
		deleteToy.setString(1, admin.getEmail());
		deleteToy.executeUpdate();
		PreparedStatement delete = conn.prepareStatement("delete from AdminTable where id=?");
		delete.setInt(1, admin.getId());
		delete.executeUpdate();
	}

	public void uploadToyByAdmin(ToyStore toy) throws Exception {
		Connection conn = createTable();
		PreparedStatement ps = conn
				.prepareStatement("insert into ToyTable( toyname , toyemail ,color ,quanity,price) values(?,?,?,?,?)");
		ps.setString(1, toy.getToyName());
		ps.setString(2, toy.getToyemail());
		ps.setString(3, toy.getColor());
		ps.setInt(4, toy.getQuantiy());
		ps.setDouble(5, toy.getPrice());
		ps.executeUpdate();
	}

	public void updateToybyAdmin(ToyStore toy, int i, Object o) throws Exception {
		Connection conn = createTable();
		PreparedStatement ps;
		if (i == 1) {
			String n = (String) o;
			ps = conn.prepareStatement("update ToyTable set Toyname=? where toyId=?");
			ps.setString(1, n);
			ps.setInt(2, toy.getToyId());
			ps.executeUpdate();

		} else if (i == 2) {
			String c = (String) o;
			ps = conn.prepareStatement("update ToyTable set color=? where toyId=?");
			ps.setString(1, c);
			ps.setInt(2, toy.getToyId());
			ps.executeUpdate();

		} else if (i == 3) {
			double p = (double) o;
			ps = conn.prepareStatement("update ToyTable set price=? where toyId=?");
			ps.setDouble(1, p);
			ps.setInt(2, toy.getToyId());
			ps.executeUpdate();

		} else if (i == 4) {
			int quantity = (int) o;
			ps = conn.prepareStatement("update ToyTable set quanity=? where toyid=?");
			ps.setLong(1, quantity);
			ps.setInt(2, toy.getToyId());
			ps.executeUpdate();
		} else {

		}
	}

	public ResultSet fetchToy(int id) throws Exception {
		Connection conn = createTable();
		PreparedStatement ps = conn.prepareStatement("Select * from Toytable where toyId=?");
		ps.setInt(1, id);
		ResultSet res = ps.executeQuery();
		return res;

	}

	public void fetchAllToy() throws Exception {

		Connection conn = createTable();
		PreparedStatement ps = conn.prepareStatement("Select * from Toytable");
		ResultSet res = ps.executeQuery();
		System.out.println("\n\n\t\t\tTOYID\t" + " TOYNAME" + " TOYCOLOR " + " QUANTITY  " + "  PRICE");
		System.out.println("\t\t\t----------------------------------------------");
		while (res.next()) {

			System.out.println("\t\t\t" + res.getInt(1) + "\t " + res.getString(2) + "\t  " + res.getString(4)
					+ "\t      " + res.getInt(5) + "\t       " + res.getDouble(6));
		}
		System.out.println("\t\t\t----------------------------------------------");

	}

	// user

	public void insertUserData(UserModule user) throws Exception {
		Connection conn = createTable();
		PreparedStatement user1 = conn.prepareStatement("insert into UserTable values(?,?,?,?,?,?)");
		user1.setInt(1, user.getId());
		user1.setString(2, user.getName());
		user1.setString(3, user.getEmail());
		user1.setLong(4, user.getContactNo());
		user1.setString(5, user.getPassword());
		user1.setDouble(6, user.getWallet());
		user1.executeUpdate();
	}

	public UserModule fetchUserDetail(String email1) throws Exception {

		Connection conn = createTable();
		PreparedStatement details = conn.prepareStatement("select * from UserTable where email=?");
		details.setString(1, email1);
		ResultSet res = details.executeQuery();
		if (res.next()) {
			int id = res.getInt(1);
			String name = res.getString(2);
			String email = res.getString(3);
			long contactNo = res.getLong(4);
			String password = res.getString(5);
			double wallet = res.getDouble(6);

			return new UserModule(id, name, email, contactNo, password, wallet);
		} else {
			return null;
		}
	}

	public UserModule updateUserdata(UserModule user, int i, Object o) throws Exception {
		Connection conn = createTable();
		PreparedStatement ps;
		if (i == 1) {
			String n = (String) o;
			ps = conn.prepareStatement("update UserTable set name=? where id=?");
			ps.setString(1, n);
			ps.setInt(2, user.getId());
			ps.executeUpdate();

			return new UserModule(user.getId(), n, user.getEmail(), user.getContactNo(), user.getPassword(),
					user.getWallet());
		} else if (i == 2) {
			String e = (String) o;
			ps = conn.prepareStatement("update UserTable set email=? where id=?");
			ps.setString(1, e);
			ps.setInt(2, user.getId());
			ps.executeUpdate();
			return new UserModule(user.getId(), user.getName(), e, user.getContactNo(), user.getPassword(),
					user.getWallet());

		} else if (i == 3) {
			String pwd = (String) o;
			ps = conn.prepareStatement("update UserTable set password=? where id=?");
			ps.setString(1, pwd);
			ps.setInt(2, user.getId());
			ps.executeUpdate();
			return new UserModule(user.getId(), user.getName(), user.getEmail(), user.getContactNo(), pwd,
					user.getWallet());
		} else if (i == 4) {
			long c = (long) o;
			ps = conn.prepareStatement("update UserTable set contactNo=? where id=?");
			ps.setLong(1, c);
			ps.setInt(2, user.getId());
			ps.executeUpdate();
			return new UserModule(user.getId(), user.getName(), user.getEmail(), c, user.getPassword(),
					user.getWallet());
		} else if (i == 5) {
			double w = (double) o;
			ps = conn.prepareStatement("update UserTable set wallet=? where id=?");
			ps.setDouble(1, w);
			ps.setInt(2, user.getId());
			ps.executeUpdate();
			return new UserModule(user.getId(), user.getName(), user.getEmail(), user.getContactNo(),
					user.getPassword(), w);
		} else {
			return null;
		}
	}

	public void deleteUser(UserModule user) throws Exception {
		Connection conn = createTable();
		PreparedStatement delete = conn.prepareStatement("delete from UserTable where id=?");
		delete.setInt(1, user.getId());
		delete.executeUpdate();
	}

	public ResultSet fetchToyById(int id) throws Exception {
		Connection conn = createTable();
		PreparedStatement ps = conn.prepareStatement("Select * from Toytable where Toyid=?");
		ps.setInt(1, id);
		ResultSet res = ps.executeQuery();
		return res;

	}
	
	public double generateBill(ArrayList<ToyItem> al) throws Exception {
		Connection con = createTable();
		double total = 0;
		System.out.println("\n\t\t\t\tYour Cart items are..\n\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		for(ToyItem t:al) {
			PreparedStatement ps = con.prepareStatement("select toyname,price from ToyTable where toyid=?");
			ps.setInt(1, t.item);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				total +=rs.getDouble(2)*t.getQty();
				System.out.print("\t\t\t\t"+t.item);
				System.out.print("  \t" + rs.getString("toyname"));
				System.out.print("   \t\tqty:" +t.getQty());
				System.out.print("    \t+" + rs.getDouble("price"));
				System.out.println();
			}
		}
		System.out.println("\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		return total;
	}
	
	

	public double payment(String username) throws Exception {
		Connection con = createTable();
		PreparedStatement ps = con.prepareStatement("select wallet from usertable where name=?");
		ps.setString(1, username);
		double bal = 0;

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			bal = rs.getDouble(1);

		}
		return bal;
	}

	public void upadteWallet(String uname, double amount) throws Exception {
		Connection con = createTable();
		PreparedStatement ps = con.prepareStatement("update usertable set wallet=wallet+? where name=?");
		ps.setDouble(1, amount);
		ps.setString(2, uname);
		ps.execute();
		
	}
	
	public void upadteWallet1(String uname, double amount) throws Exception {
		Connection con = createTable();
		PreparedStatement ps = con.prepareStatement("update usertable set wallet=wallet-? where name=?");
		ps.setDouble(1, amount);
		ps.setString(2, uname);
		ps.execute();
		
	}

}
