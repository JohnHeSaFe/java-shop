package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao {
	@Override
	public void connect() {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		// locate file, path and name
		String fileRoute = System.getProperty("user.dir");
		String separator = File.separator;
		File file = new File(fileRoute + separator + "files" + separator + "inputInventory.txt");

		// list to return
		ArrayList<Product> inventory = new ArrayList<>();

		try {
			// wrap in proper classes
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			// read first line
			String line = br.readLine();

			// process and read next line until end of file
			while (line != null) {
				// split in sections
				String[] sections = line.split(";");

				String name = "";
				double wholesalerPrice = 0.0;
				int stock = 0;

				// read each sections
				for (int i = 0; i < sections.length; i++) {
					// split data in key(0) and value(1)
					String[] data = sections[i].split(":");

					switch (i) {
					case 0:
						// format product name
						name = data[1];
						break;

					case 1:
						// format price
						wholesalerPrice = Double.parseDouble(data[1]);
						break;

					case 2:
						// format stock
						stock = Integer.parseInt(data[1]);
						break;

					default:
						break;
					}
				}
				// add product to inventory
				inventory.add(new Product(name, new Amount(wholesalerPrice), true, stock));

				// read next line
				line = br.readLine();
			}
			fr.close();
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		// define file name based on date
		LocalDate myObj = LocalDate.now();
		String fileName = "inventory_" + myObj.toString() + ".txt";

		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

		try {
			// wrap in proper classes
			FileWriter fw;
			fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);

			// write line by line
			int counterProduct = 0;
			for (Product product : inventory) {
				// format first line TO BE -> 1;Product:Manzana;Stock:10;
				StringBuilder firstLine = new StringBuilder(
						(counterProduct + 1) + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";");
				pw.write(firstLine.toString());
				fw.write("\n");

				// increment counter sales
				counterProduct++;
			}
			// write count of products
			pw.write("Total number of products:" + counterProduct + ";");
			
			// close files
			pw.close();
			fw.close();

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean addProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(int id) {
		// TODO Auto-generated method stub
		return false;
	}

}