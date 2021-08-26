package printingCo.src.serverside;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Shirts {
    private String receipt = "No Order was Processed.";
    private String size;
    private String material; 
    private String brand;
    private String colour;
    private String sleeves;
    private int leadTime;
    private int num;
    private SQLConnection db;

    // Constructors 
    public Shirts(String size, String material, String brand, String colour, String sleeves, int num) {
        this.size = size;
        this.material = material;
        this.brand = brand;
        this.colour = colour;
        this.sleeves = sleeves;
        this.num = num;
        db = new SQLConnection();
        try {
            placedOrder();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Shirts() {
        db = new SQLConnection();
    }

    // Getters and Setters
    public String getReceipt() {
        return receipt;
    }
    public String getSize() {
        return size;
    }
    public String getMaterial() {
        return material;
    }
    public String getBrand() {
        return brand;
    }
    public String getColour() {
        return colour;
    }
    public String getSleeves() {
        return sleeves;
    }
    public int getLeadTime() {
        return leadTime;
    }
    public int getNum() {
        return num;
    }

    public void getReceipt(String receipt) {
        this.receipt = receipt;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public void setMaterial(String material) {
        this.material = material;
    } 
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setColour(String colour) {
        this.colour = colour;
    }
    public void setSleeves(String sleeves) {
        this.sleeves = sleeves;
    }
    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }
    public void setNum(int num) {
        this.num = num;
    }

    // Methods
    /**
     * Calculate the estimated lead time of the specific shirt.
     * Which depends only on the certain variables.
     * @param sz    Size of the shirt.
     * @param mtrl  Material of the shirt.
     * @param brnd  Brand of the shirt.
     * @return      Returns the number of hours for estimated lead time, as an integer.
     */
    public int findLeadTime(String sz, String mtrl, String brnd) {
        int lt = 48;
        return lt;
    }
    /**
     * Check database and inventory to see if shirt exists.
     * @param sz    Size of the shirt to be checked.
     * @param mtrl  Material of the shirt to be checked.
     * @param brnd  Brand of the shirt to be checked.
     * @param clr   Colour of the shirt to be checked.
     * @param slvs  Length of sleaves of the shirt to be checked.
     * @return      Returns the boolean value of wether the shirt exists(True) or not(False) in the database/inventory.
     * @throws SQLException
     */
    public boolean shirtExists(String sz, String mtrl, String brnd, String clr, String slvs) throws SQLException {
        boolean exists = true;
        db.initializeConnection();
        Statement myStmt = db.getConnection().createStatement();
        ResultSet results = myStmt.executeQuery("SELECT * FROM shirts WHERE Size = '" 
            + sz + "' AND Material = '" + mtrl + "' AND Brand = '" + brnd + "' AND Colour = '" + clr + "' AND Sleaves = '" + slvs + "';");
        db.closeConn();
        if (results.next()) {
            exists = false;
        }
        return exists;
    }

    /**
     * Analyzes the placed order and accordingly removes and updates the shirt for the shirts database and inventory.
     * If the shirt is not found in the inventory, notify client and business with need for shirt and state calculated lead time.
     * If shirt exists, return the complete order status as a string to write to the order receipt.
     * @return      Returns the details of the shirt which was requested, as a String.
     * @throws SQLException
     */
    public void placedOrder() throws SQLException {
        db.initializeConnection();
        Statement myStmt = db.getConnection().createStatement();
        ResultSet results = myStmt.executeQuery("SELECT ProductID FROM shirts WHERE Size = '" + size + "' AND Material = '" + material + "' AND Brand = '" + brand + "' AND Colour = '" + colour + "' AND Sleeves = '" + sleeves + "';");
        if (results.next()) {
            String pId = results.getString(1);
            results = myStmt.executeQuery("SELECT Quantity FROM inventory WHERE Product = " + pId + ";");
            results.next();
            if (num < Integer.parseInt(results.getString(1))) {
                try (Statement stmt = db.getConnection().createStatement();) {
                    String stockUpdate = "UPDATE inventory SET Quantity = Quantity - " + Integer.toString(num) + " WHERE Product = " + pId + ";";
                    stmt.executeUpdate(stockUpdate);
                    db.closeConn();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                receipt = "Clothing Type: Shirt\nType: " + sleeves + "\nSize: " + size + "\nMaterial: " + material + "\nBrand: " + brand + "\nColour: " + colour + "\n\nQuantity: " + Integer.toString(num);
            } 
            else if (num == Integer.parseInt(results.getString(1))) {
                try (Statement stmt = db.getConnection().createStatement();) {
                    String stockDelete = "DELETE FROM inventory WHERE Product = " + pId + ";";
                    stmt.executeUpdate(stockDelete);
                    stockDelete = "DELETE FROM shirts WHERE ProductID = " + pId + ";";
                    stmt.executeUpdate(stockDelete);
                    db.closeConn();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                receipt = "Clothing Type: Shirt\nType: " + sleeves + "\nSize: " + size + "\nMaterial: " + material + "\nBrand: " + brand + "\nColour: " + colour + "\n\nQuantity: " + Integer.toString(num);
            } 
            else if (num > Integer.parseInt(results.getString(1))) {
                try (Statement stmt = db.getConnection().createStatement();) {
                    String stockDelete = "DELETE FROM inventory WHERE ProductID = " + pId + ";";
                    stmt.executeUpdate(stockDelete);
                    stockDelete = "DELETE FROM shirts WHERE ProductID = " + pId + ";";
                    stmt.executeUpdate(stockDelete);
                    db.closeConn();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                receipt = "Clothing Tyoe: Shirt\nType: " + sleeves + "\nSize: " + size + "\nMaterial: " + material + "\nBrand: " + brand + "\nColour: " + colour + "\n\nQuantity: " + Integer.toString(num);
            } 
            int est = findLeadTime(this.size, this.material, this.brand);
            receipt += "\n\nEstimated Shipping time is " + Integer.toString(est) + "hrs.\n\n"; 
        } else {
            int est = findLeadTime(this.size, this.material, this.brand);
            receipt = "No Inventory.\nEstimated Processing time is " + Integer.toString(est) + "hrs.\n\nThank you for your patience.";
        } 
    }
    /**
     * Updates and adds a new shirt to the stock and inventory related to the shirts database.
     * @param addSize   Size of the shirt to add to the database.
     * @param addMaterial   Material of the shirt to add to the database.
     * @param addBrand  Brand of the shirt to add to the database.
     * @param addColour Colour of the shirt to add to the database.
     * @param addSleaves    Length of sleeves of the shirt to add to the database.
     * @param quantity  Ammount of shirts that are to be added into the inventory. 
     * @throws SQLException
     */
    public void stockAddShirt (String addSize, String addMaterial, String addBrand, String addColour, String addSleaves, int quantity) throws SQLException {
        db.initializeConnection();
        int pId = 1;
        Statement myStmt = db.getConnection().createStatement();
        ResultSet results = myStmt.executeQuery("SELECT * FROM shirts WHERE Size ='" 
            + addSize + "' AND Material ='" + addMaterial + "' AND Brand ='" + addBrand + "' AND Colour ='" + addColour + "' AND Sleeves ='" + addSleaves + "'" + ";");
        
        if (results.next()) {
            // Take the current PID and search the ->inventory<- and add the ammount.
            pId = Integer.parseInt(results.getString(1));

            try (Statement stmt = db.getConnection().createStatement();) {
                String stockUpdate = "UPDATE inventory SET Quantity = Quantity + " + Integer.toString(quantity) + " WHERE Product = " + Integer.toString(pId) + ";";
                stmt.executeUpdate(stockUpdate);
                db.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try (Statement stmt = db.getConnection().createStatement();){
                int pLeadTime = findLeadTime(addSize, addMaterial, addBrand);
                String allPIDs = "SELECT * shirts WHERE ProductID >= 1 and ProductID < 100;";

                results = stmt.executeQuery(allPIDs);
                int vacant_check = 1;
                while (results.next()) {
                    pId = Integer.parseInt(results.getString("ProductID"));
                    if (pId != vacant_check) break;
                    vacant_check++;
                }
                pId++;

                String insertSql = "INSERT INTO shirts (ProductID, Size, Material, Brand, Colour, Sleeves) VALUES (" 
                    + Integer.toString(pId) + ", '" + addSize + "', '" + addMaterial + "', '" + addBrand + "', '" + addColour + "', '"+ addSleaves + ");";
                String stockSql = "INSERT INTO inventory (Product, Quantity, LeadTime) VALUES (" + Integer.toString(pId) + ", " + Integer.toString(quantity) + ", " + Integer.toString(pLeadTime) + ");";

                stmt.executeUpdate(insertSql);
                stmt.executeUpdate(stockSql);
                db.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
