package toyManagementSys.controller;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Scanner;

import toyManagementSys.dao.ToyManagementCrud;
import toyManagementSys.dto.AdminModule;
import toyManagementSys.dto.ToyItem;
import toyManagementSys.dto.ToyStore;
import toyManagementSys.dto.UserModule;

public class ToyManagementMain {
	static ToyManagementCrud crud = new ToyManagementCrud();
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {

		crud.createTable();
		boolean check = true;
		System.out.println("\t\t\t\t\t~~ WELCOME TO TOY MANAGEMENT SYSTEM ~~");
		System.out.println("\t\t\t\t\t----------------------------------------");
		do {
			System.out.print(
					"\n\t\t1.Admin\t 2.User\t 3.Exit...!!!\n\t\t------------------------- \n\t\tEnter your choice....");
			int choice = sc.nextInt();
			switch (choice) {
			case 1: {
				System.out.println("\n\n\t\t\tWelcome to Admin Portal....\n\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~");
				save();
			}
				break;
			case 2: {
				System.out.println("\n\t\t\tWelcome to User Portal...\n\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~");
				saveUser();
			}
				break;
			case 3: {
				check = false;
			}
				break;
			default:
				break;
			}

		} while (check);

	}

	public static void saveUser() throws Exception {

		boolean check = true;
		do {
			System.out.print("\n\t\t\t 1.Register 2.Login 3.Exit\n\t\t\t  Enter your choice...");
		
				int choice = sc.nextInt();
			
			
			switch (choice) {
			case 1: {
				registerUser();
				check = false;
			}
				break;
			case 2: {
				loginUser();
				check = false;
			}
				break;
			case 3: {
				check = false;
			}
				break;
			default:
				break;
			}

		} while (check);
	}

	public static void loginUser() throws Exception {
		System.out.println("\n\t\t\t\t\tLOGIN TO YOUR PROFILE....!!!!\n\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.print("\t\t\t\t\tEnter Your Email...");
		String email = sc.next();
		System.out.print("\t\t\t\t\tEnter Your Password...");
		String password = sc.next();

		UserModule userM = crud.fetchUserDetail(email);
		if (userM != null) {
			if (password.equals(userM.getPassword())) {
				System.out.println("\t\t\t\t\tLogin Success dear..." + userM.getName());
				loginUserOption(userM);
			} else {
				System.out.println("\t\t\t\t\tEntered wrong password..please check it");
				loginUser();
			}
		} else {
			System.out.println("\t\t\t\t\tEntered wrong email..try again");
			loginUser();
		}

	}

	public static void loginUserOption(UserModule user) throws Exception {
		System.out.print("\n\n\t\t\t\t\t\t" + user.getName().toUpperCase()
				+ " !  Which you want to see or do???\n\t\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\t\t\t\t\t\t 1.Profile  2.ToyStore 3.Exit \n\t\t\t\t\t\tEnter your choice...");
		int choice = sc.nextInt();

		if (choice == 1) {
			userProfile(user);
		}

		else if (choice == 2) {
			toyStoreForUser(user);
		}

		else {
			save();
		}

	}

	public static void toyStoreForUser(UserModule user) throws Exception {
		crud.fetchAllToy();
		ArrayList<ToyItem> al = new ArrayList<ToyItem>();
		System.out.print("\t\t\t\t\tDo you want to buy toys...?\n\t\t\t\t\t Yes or No...");
		String yes = sc.next();
		int cart = 0;
		if (yes.equalsIgnoreCase("yes")) {
			addToCart(al, cart);
		} else {
			System.out.println("Now you can logout...");
			save();
		}
	}

	public static void addToCart(ArrayList<ToyItem> al, int cart) throws Exception {

		System.out.println("\n\t\t\t\t\tPlease choose the items form ToyStore:\n\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.print("\n\t\t\t\t\tEnter the toyid:");
		int item = sc.nextInt();
		ResultSet toyss = crud.fetchToy(item);

		if (toyss.next()) {
			System.out.print("\t\t\t\t\tEnter the quantity:");
			int qty = sc.nextInt();
			if (qty <= toyss.getInt("quanity")) {
				cart++;
				al.add(new ToyItem(item, qty));
				System.out.print("\n\t\t\t\t\tWhether you need to buy more toy?\n\t\t\t\t\tyes or no....");
				String yesN = sc.next();
				if (yesN.equalsIgnoreCase("yes")) {
					addToCart(al, cart);
				} else {
					purchase(al);

				}
			} else {
				System.out.println("\n\t\t\t\t\t\t\t******stock is not available******");
				addToCart(al, cart);
			}
		} else {
			System.out.println("\n\t\t\t\t\t\t\tToyId is not found..You can buy another toy!");
			addToCart(al, cart);
		}
	}

	public static void purchase(ArrayList<ToyItem> al) throws Exception {
		double total = crud.generateBill(al);
		System.out.print("\t\t\t\t\t\tThe total amount: " + total);
		System.out.println("\n\t\t\t\t\t\tproceed to payment...");
		System.out.print("\t\t\t\t\t\tEnter username:");
		String uname = sc.next();
		double wallet = crud.payment(uname);
		double bal = wallet - total;
		if (bal < 0) {
			System.out.print("\t\t\t\t\t\t\tInsufficient amount in wallet.\n\t\t\t\t\t\t\t Do you want to add amount to wallet? yes/no:");
			String check = sc.next();
			if (check.equals("yes")) {
				System.out.print("\t\t\t\t\t\t\tEnter amount:");
				double amt = sc.nextDouble();
				crud.upadteWallet(uname, amt);
				System.out.println("\n\t\t\t\tAmount added!\n\t\t\t\tNow you can purchase..!!\n\t\t\t-----------------------------------------");
				purchase(al);
			}else {
				System.out.println("\t\t\t\t\tIt seems you dont want to buy this product..so you can logout");
				saveUser();
			}
		} else {
			System.out.println("\t\t\t\t\tPayment successfull....\n\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("\t\t\t\t\tYour current balance is:" + bal);
			crud.upadteWallet1(uname, total);
			billdisplay(total, al, uname);
		}
	}

	public static void billdisplay(double total, ArrayList<ToyItem> al, String uname) throws Exception {
		
		System.out.println("\n\t\t\t\tYour Bill   " + uname+"\n\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("\n\t\t\t\tToyId\t  Quanity");
		System.out.println("\t\t\t\t--------------------------");
		for (ToyItem t : al) {
			System.out.println("\t\t\t\t" + t.item + "     		  " + t.getQty());
		}
		System.out.println("\t\t\t\tTotal bill\t" + total);
		System.out.println("\t\t\t\t---------------------------");
		
		System.out.println("\t\t\t\t\tSuccessfully you purchased the toy...");
		System.out.println("\t\t\t\t\tThankyou for Shopping");
		
		save();
	}

	public static void userProfile(UserModule user) throws Exception {
		System.out.println(" \n\t\t\t\t\t" + user.getName().toUpperCase()
				+ ", you will be able to see or do some changes in your profile\n\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n\t\t\t\t\t1.Details 2.Update Profile 3.Delete 4.Exit");
		System.out.print("\t\t\t\t\tEnter your choice...");
		int choice = sc.nextInt();
		if (choice == 1) {
			System.out.println("\n\t\t\t\t\t Profile  " + user.getName().toUpperCase());
			System.out.println("\t\t\t\t\t-----------------------------------------");

			System.out.println("\t\t\t\t\t| Id      \t" + user.getId() + "       \t\t|\n\t\t\t\t\t| Name    \t"
					+ user.getName() + "      \t\t|\n\t\t\t\t\t| Email   \t" + user.getEmail()
					+ " \t|\n\t\t\t\t\t| Password\t" + user.getPassword() + "     \t\t|\n\t\t\t\t\t| Phone No\t"
					+ user.getContactNo() + "    \t\t|\n\t\t\t\t\t| Wallet\t" + user.getWallet() + "    \t\t|");

			System.out.println("\t\t\t\t\t-----------------------------------------");
			userMoreOption(user);
		} else if (choice == 2) {
			updateUserDetails(user);
		} else if (choice == 3) {
			crud.deleteUser(user);
			System.out.println("\t\t\t\t\t\tYour Profile  is deleted \n\t\t\t\t\t\t You can Register again");
			saveUser();
		} else {
			loginUserOption(user);
		}

	}

	public static void updateUserDetails(UserModule user) throws Exception {
		System.out.print(
				"\n\t\t\t\t\tEnter your choice to update:\n\t\t\t\t\t------------------------------------\n\t\t\t\t\t\t1.NAME\t2.EMAIL\t3.PASSWORD 4.PHONE NO 5.WALLET....");
		int choice = sc.nextInt();
		try {
			if (choice == 1) {
				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new name....");
				String name = sc.nextLine();
				UserModule u = crud.updateUserdata(user, choice, name);
//				System.out.println(a);
				System.out.println("\t\t\t\t\tSuccesfully Updated Name Column");
				updateMoreUserColumn(user);
				userProfile(u);

			} else if (choice == 2) {
				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new email...");
				String email = sc.nextLine();
				UserModule u = crud.updateUserdata(user, choice, email);
				System.out.println("\t\t\t\t\tSuccesfully Updated Email column");
				updateMoreUserColumn(user);
				userProfile(u);

			} else if (choice == 3) {
//				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new password...");
				String password = sc.nextLine();
				UserModule u = crud.updateUserdata(user, choice, password);
				System.out.println("\t\t\t\t\tSuccesfully Updated password column");
				updateMoreUserColumn(user);
				userProfile(u);

			} else if (choice == 4) {
				System.out.print("\t\t\t\t\tEnter a new Phone no...");
				long phn = sc.nextLong();
				UserModule u = crud.updateUserdata(user, choice, phn);

				System.out.println("\t\t\t\t\tSuccesfully Updated Phone no column");
				updateMoreUserColumn(user);
				userProfile(u);

			} else if (choice == 5) {
				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new toyStore");
				double wallet = sc.nextDouble();
				UserModule u = crud.updateUserdata(user, choice, wallet);
				System.out.println("\t\t\t\t\tSuccesfully Updated ToyStore column");
				updateMoreUserColumn(user);
				userProfile(u);
			} else {
				System.out.println("\n\t\t\t\t\tIncorrect Update Option...!");
				updateUserDetails(user);
			}
		} catch (SQLException e) {
			System.out.println("\n\t\t\t\t\tGiven Record not found to Update...!");
			e.printStackTrace();
		}

	}

	public static void updateMoreUserColumn(UserModule user) throws Exception {
		System.out.print("\t\t\t\t\tdo you want to update more column??? \n\t\t\t\t\t yes or no...");
		String yes = "yes", no = "no";
		String yN = sc.next();

		if (yes.equals(yN)) {
			updateUserDetails(user);
		} else {
//			adminProfile(admin);
			loginUserOption(user);
		}

	}

	public static void userMoreOption(UserModule user) throws Exception {
		System.out.print("\t\t\t\t\t\tDo you want see more ???\n\t\t\t\t\t\t yes or no....");
		String yes = "yes";
		String yN = sc.next();

		if (yes.equals(yN)) {
			userProfile(user);
		} else {
			loginUserOption(user);
		}

	}

	public static void registerUser() {
		try {
			System.out.print("\n\t\t\t\tENTER YOUR ID..");
			int id = sc.nextInt();
			System.out.print("\n\t\t\t\tENTER YOUR NAME..");
			sc.nextLine();
			String name = sc.nextLine();
			System.out.print("\n\t\t\t\tENTER YOUR EMAIL..");
			String email = sc.nextLine();
			System.out.print("\n\t\t\t\tENTER YOUR PASSWORD..");
			String password = sc.nextLine();
			System.out.print("\n\t\t\t\tENTER YOUR PHONE NO..");
			long phn = sc.nextLong();
			System.out.print("\n\t\t\t\tENTER YOUR WALLET BALANCE..");
			sc.nextLine();
			double wallet = sc.nextDouble();

			UserModule user = new UserModule(id, name, email, phn, password, wallet);
			crud.insertUserData(user);

			System.out.println("\n\t\t\t\tThankyou for Register.." + user.getName().toUpperCase());
		} catch (Exception e) {
			System.out.println("\t\t\t\t\tsomething problem in data please try again....");
			registerAdmin();
		}

	}

	public static void save() throws Exception {
		boolean check = true;
		do {
			System.out.print("\n\t\t\t 1.Register 2.Login 3.Exit\n\t\t\t  Enter your choice...");
			int choice = sc.nextInt();
			switch (choice) {
			case 1: {
				registerAdmin();
				check = false;
			}
				break;
			case 2: {
				loginAdmin();
				check = false;
			}
				break;
			case 3: {
				check = false;
			}
				break;
			default:
				break;
			}

		} while (check);

	}

	public static void loginAdmin() throws Exception {
		System.out.println("\n\t\t\t\t\tLOGIN TO YOUR PROFILE....!!!!\n\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.print("\t\t\t\t\tEnter Your Email...");
		String email = sc.next();
		System.out.print("\t\t\t\t\tEnter Your Password...");
		String password = sc.next();

		AdminModule adminM = crud.fetchAdminDetail(email);
		if (adminM != null) {
			if (password.equals(adminM.getPassword())) {
				System.out.println("\t\t\t\t\tLogin Success dear..." + adminM.getName());
				loginAdminOption(adminM);
			} else {
				System.out.println("\t\t\t\t\tEntered wrong password..please check it");
				loginAdmin();
			}
		} else {
			System.out.println("\t\t\t\t\tEntered wrong email..try again");
			loginAdmin();
		}

	}

	public static void loginAdminOption(AdminModule admin) throws Exception {

		System.out.print("\n\n\t\t\t\t\t\t" + admin.getName().toUpperCase()
				+ " !  Which you want to see or do???\n\t\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\t\t\t\t\t\t 1.Profile  2.ToyStore 3.Exit \n\t\t\t\t\t\tEnter your choice...");
		int choice = sc.nextInt();

		if (choice == 1) {
			adminProfile(admin);
		}

		else if (choice == 2) {
			toyStore(admin);
		}

		else {
			// exit
			save();
		}

	}

	public static void toyStore(AdminModule admin) throws Exception {
		System.out.println(
				"\n\n\t\t\t\t\t\tWhat do you want to do???\n\t\t\t\t\t\t--------------------------------------- \n\t\t\t\t\t\t 1.Upload Toy  2.Update Toy 3.See All Toys 4.Exit");
		System.out.print("\t\t\t\t\t\tEnter your choice...");
		int choice = sc.nextInt();
		if (choice == 1) {
			uploadToy(admin);
		} else if (choice == 2) {
			updateToy(admin);
		} else if (choice == 3) {
			System.out.println("\n\n\t\t\t\tData of All Toys....\n\t\t\t\t~~~~~~~~~~~~~~~~~~~~~");
			crud.fetchAllToy();
			moreOption(admin);

		} else if (choice == 4) {
			save();
		} else {
			System.out.println("\t\t\t\t\t\t\twrite valid options");
			toyStore(admin);
		}
	}

	public static void moreOption(AdminModule admin) throws Exception {
		System.out.print("\n\tDo you want see more??? \n\t\tyes or no....");
		String yes = "yes";
		String yN = sc.next();

		if (yes.equals(yN)) {
			toyStore(admin);
		} else {
			System.out.println("Logged out...!");
			save();
		}

	}

	public static void updateToy(AdminModule admin) throws Exception {
		System.out.print(
				"\n\t\t\t\t\t\t\t1.ToyName   2.Color\\t3.Price\\t4.Quantity\n\t\t\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n\t\t\t\t\t\t\tEnter your choice to update:");
		int choice = sc.nextInt();
		System.out.print("\n\t\t\t\t\t\tEnter the id of toy...");
		int id = sc.nextInt();
		ToyStore toy = new ToyStore(id);
		if (choice == 1) {
			sc.nextLine();
			System.out.print("\t\t\t\t\t\tEnter new toyName..");
			String tN = sc.nextLine();
			crud.updateToybyAdmin(toy, choice, tN);
			updateToyMoreColumn(admin);
			System.out.println("\t\t\t\t\t\t\tSuccessfully updates the ToyName  of the toy");
		} else if (choice == 2) {
			sc.nextLine();
			System.out.print("\t\t\t\t\t\tEnter new color..");
			String color = sc.nextLine();
			crud.updateToybyAdmin(toy, choice, color);
			System.out.println("\t\t\t\t\t\t\tSuccessfully updates the color  of the toy");
			updateToyMoreColumn(admin);
		} else if (choice == 3) {
			System.out.print("\t\t\t\t\t\tEnter new Price..");
			double price = sc.nextDouble();
			crud.updateToybyAdmin(toy, choice, price);
			System.out.println("\t\t\t\t\t\t\tSuccessfully updates the Price  of the toy");
			updateToyMoreColumn(admin);
		} else if (choice == 4) {
			System.out.print("\t\t\t\t\t\tEnter new Quantity..");
			int quantity = sc.nextInt();
			crud.updateToybyAdmin(toy, choice, quantity);
			System.out.println("\t\t\t\t\t\t\tSuccessfully updates the Quantity  of the toy");
			updateToyMoreColumn(admin);
		} else {
			System.out.println("\n\t\t\t\t\tIncorrect Update Option...!");
			updateToy(admin);
		}
	}

	public static void updateToyMoreColumn(AdminModule admin) throws Exception {
		System.out.print("\n\t\t\t\t\t\t\tDo you want to update more toy??? \n\t\t\t\t\t\t\tyes or no....");
		String yes = "yes";
		String yN = sc.next();

		if (yes.equals(yN)) {
			updateToy(admin);
		} else {
			toyStore(admin);
		}
	}

	public static void uploadToy(AdminModule admin) throws Exception {
		System.out.println("\n\t\t\t\t\t\t*****Upload toy******");
		sc.nextLine();
		System.out.print("\n\t\t\t\t\t\tEnter the toy name..");
		String toyName = sc.nextLine();
		System.out.print("\t\t\t\t\t\tEnter the color..");
		String color = sc.next();
		System.out.print("\t\t\t\t\t\tEnter the Quantity..");
		int quantity = sc.nextInt();
		System.out.print("\t\t\t\t\t\tEnter the Price..");
		double price = sc.nextDouble();

		ToyStore toy = new ToyStore(toyName, admin.getEmail(), color, quantity, price);
		crud.uploadToyByAdmin(toy);
		uploadMoreToy(admin);

	}

	public static void uploadMoreToy(AdminModule admin) throws Exception {
		System.out.print("\t\t\t\t\t\tDo you want to upload more toy??? \n\t\t\t\t\t\tyes or no....");
		String yes = "yes";
		String yN = sc.next();

		if (yes.equals(yN)) {
			uploadToy(admin);
		} else {
			toyStore(admin);
		}
	}

	public static void adminProfile(AdminModule admin) throws Exception {
		System.out.println(" \n\t\t\t\t\t" + admin.getName().toUpperCase()
				+ ", you will be able to see or do some changes in your profile\n\t\t\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n\t\t\t\t\t1.Details 2.Update Profile 3.Delete 4.Exit");
		System.out.print("\t\t\t\t\tEnter your choice...");
		int choice = sc.nextInt();
		if (choice == 1) {
			System.out.println("\n\t\t\t\t\t Profile  " + admin.getName().toUpperCase());
			System.out.println("\t\t\t\t\t-----------------------------------------");

			System.out.println("\t\t\t\t\t| Id      \t" + admin.getId() + "       \t\t|\n\t\t\t\t\t| Name    \t"
					+ admin.getName() + "      \t\t|\n\t\t\t\t\t| Email   \t" + admin.getEmail()
					+ " \t|\n\t\t\t\t\t| Password\t" + admin.getPassword() + "     \t\t|\n\t\t\t\t\t| Phone No\t"
					+ admin.getcontactNo() + "    \t\t|\n\t\t\t\t\t| ToyStore\t" + admin.getToyStore() + "    \t\t|");

			System.out.println("\t\t\t\t\t-----------------------------------------");
			adminMoreOption(admin);
		} else if (choice == 2) {
			updateAdminDetails(admin);
		} else if (choice == 3) {
			crud.deleteAdmin(admin);
			System.out.println("\t\t\t\t\t\tYour Profile  is deleted \n\t\t\t\t\t\t You can Register again");
			save();
		} else {
			loginAdminOption(admin);
		}

	}

	public static void adminMoreOption(AdminModule admin) throws Exception {
		System.out.print("\t\t\t\t\t\tDo you want see more ???\n\t\t\t\t\t\t yes or no....");
		String yes = "yes";
		String yN = sc.next();

		if (yes.equals(yN)) {
			adminProfile(admin);
		} else {
			loginAdminOption(admin);
		}

	}

//update the admin columns
	public static void updateAdminDetails(AdminModule admin) throws Exception {
		System.out.print(
				"\n\t\t\t\t\tEnter your choice to update:\n\t\t\t\t\t------------------------------------\n\t\t\t\t\t\t1.NAME\t2.EMAIL\t3.PASSWORD 4.PHONE NO 5.TOYSTORE....");
		int choice = sc.nextInt();
		try {
			if (choice == 1) {
				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new name....");
				String name = sc.nextLine();
				AdminModule a = crud.updateAdmindata(admin, choice, name);
				System.out.println(a);
				System.out.println("\t\t\t\t\tSuccesfully Updated Name Column");
				updateMoreColumn(admin);
				adminProfile(a);

			} else if (choice == 2) {
				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new email...");
				String email = sc.nextLine();
				AdminModule a = crud.updateAdmindata(admin, choice, email);
				System.out.println("\t\t\t\t\tSuccesfully Updated Email column");
				updateMoreColumn(admin);
				adminProfile(a);

			} else if (choice == 3) {
				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new password...");
				String password = sc.nextLine();
				AdminModule a = crud.updateAdmindata(admin, choice, password);
				System.out.println("\t\t\t\t\tSuccesfully Updated password column");
				updateMoreColumn(admin);
				adminProfile(a);

			} else if (choice == 4) {
				System.out.print("\t\t\t\t\tEnter a new Phone no...");
				long phn = sc.nextLong();
				AdminModule a = crud.updateAdmindata(admin, choice, phn);
				System.out.println("\t\t\t\t\tSuccesfully Updated Phone no column");
				updateMoreColumn(admin);
				adminProfile(a);

			} else if (choice == 5) {
				sc.nextLine();
				System.out.print("\t\t\t\t\tEnter a new toyStore");
				String toystore = sc.nextLine();
				AdminModule a = crud.updateAdmindata(admin, choice, toystore);
				System.out.println("\t\t\t\t\tSuccesfully Updated ToyStore column");
				updateMoreColumn(admin);
				adminProfile(a);
			} else {
				System.out.println("\n\t\t\t\t\tIncorrect Update Option...!");
				updateAdminDetails(admin);
			}
		} catch (SQLException e) {
//			System.out.println("\n\t\t\t\t\tGiven Record not found to Update...!");
			e.printStackTrace();
		}

	}

	public static void updateMoreColumn(AdminModule admin) throws Exception {
		System.out.print("\t\t\t\t\tdo you want to update more column??? \n\t\t\t\t\t yes or no...");
		String yes = "yes", no = "no";
		String yN = sc.next();

		if (yes.equals(yN)) {
			updateAdminDetails(admin);
		} else {
//			adminProfile(admin);
			loginAdminOption(admin);
		}
	}

	// Register the Admin
	public static void registerAdmin() {
		System.out.print("\n\t\t\t\tENTER YOUR ID..");
		int id = sc.nextInt();
		System.out.print("\n\t\t\t\tENTER YOUR NAME..");
		sc.nextLine();
		String name = sc.nextLine();
		System.out.print("\n\t\t\t\tENTER YOUR EMAIL..");
		String email = sc.nextLine();
		System.out.print("\n\t\t\t\tENTER YOUR PASSWORD..");
		String password = sc.nextLine();
		System.out.print("\n\t\t\t\tENTER YOUR PHONE NO..");
		long phn = sc.nextLong();
		System.out.print("\n\t\t\t\tENTER YOUR TOYSTORE..");
		sc.nextLine();
		String toyStore = sc.nextLine();

		AdminModule admin = new AdminModule(id, name, email, password, phn, toyStore);
		try {
			crud.insertData(admin);
			System.out.println("\n\t\t\t\tThankyou for Register.." + admin.getName().toUpperCase());

		} catch (Exception e) {
			System.out.println("\t\t\t\t\tsomething problem in data please try again....");
			registerAdmin();
		}
	}

}
