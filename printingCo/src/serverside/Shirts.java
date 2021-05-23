package printingCo.src.serverside;

import java.sql.*;

public class Shirts {
    private String receipt = "No Order was Processed.";
    private String size;
    private String material; 
    private String brand;
    private String colour;
    private String sleaves;
    private int leadTime;
    private int num;
    private SQLConnection db;

    // Constructions 
    public Shirts(String size, String material, String brand, String colour, String sleaves, int num) {
        this.size = size;
        this.material = material;
        this.brand = brand;
        this.colour = colour;
        this.sleaves = sleaves;
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
    public String getSleaves() {
        return sleaves;
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
    public void setSleaves(String sleaves) {
        this.sleaves = sleaves;
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
        int lt = 24;
        if (!brnd.equals("BellaCanvas")) {
            lt = 48;
        } 
        if (!mtrl.contains("cotton") || !mtrl.contains("polyester")) {
            lt = 72;
        }
        if (!sz.equals("m.S") || !sz.equals("w.S") || !sz.equals("m.M") || !sz.equals("w.M") || !sz.equals("m.L") || !sz.equals("w.L")) {
            lt = 96;
        }
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
        ResultSet results = myStmt.executeQuery("SELECT * FROM shirts WHERE Size ='" 
            + sz + "' AND Material ='" + mtrl + "' AND Brand ='" + brnd + "' AND Colour ='" + clr + "' AND Sleaves ='" + slvs + "'" + ";");
        db.closeConn();
        if (results == null) {
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
        ResultSet results = myStmt.executeQuery("SELECT * FROM shirts WHERE Size ='" 
            + size + "' AND Material ='" + material + "' AND Brand ='" + brand + "' AND Colour ='" + colour + "' AND Sleaves ='" + sleaves + "'" + ";");
        if (results != null) {
            int Pid = results.getInt("ProductID");
            results = myStmt.executeQuery("SELECT Quantity FROM inventory WHERE ProductID = " + Pid + ";");
            if (num < results.getInt("Quantity")) {
                try (Statement stmt = db.getConnection().createStatement();) {
                    String stockUpdate = "UPDATE inventory SET Quantity = Quantity - " + num + " WHERE ProductId = " + Pid + ";";
                    stmt.executeUpdate(stockUpdate);
                    db.closeConn();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                receipt = "";
            } 
            else if (num == results.getInt("Quantity")) {
                try (Statement stmt = db.getConnection().createStatement();) {
                    String stockDelete = "DELETE FROM inventory WHERE ProductID = " + Pid + ";";
                    stmt.executeUpdate(stockDelete);
                    stockDelete = "DELETE FROM shirts WHERE ProductID = " + Pid + ";";
                    db.closeConn();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                receipt = "";
            } 
            else if (num > results.getInt("Quantity")) {
                try (Statement stmt = db.getConnection().createStatement();) {
                    String stockDelete = "DELETE FROM inventory WHERE ProductID = " + Pid + ";";
                    stmt.executeUpdate(stockDelete);
                    stockDelete = "DELETE FROM shirts WHERE ProductID = " + Pid + ";";
                    db.closeConn();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int est = findLeadTime(this.size, this.material, this.brand);
                receipt = "";
            }
        } else {
            int est = findLeadTime(this.size, this.material, this.brand);
            receipt = "";
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
        int Pid = 0;
        Statement myStmt = db.getConnection().createStatement();
        ResultSet results = myStmt.executeQuery("SELECT * FROM shirts WHERE Size ='" 
            + addSize + "' AND Material ='" + addMaterial + "' AND Brand ='" + addBrand + "' AND Colour ='" + addColour + "' AND Sleaves ='" + addSleaves + "'" + ";");
        
        if (results != null) {
            // Take the current PID and search the ->inventory<- and add the ammount.
            Pid = results.getInt("ProductID");

            try (Statement stmt = db.getConnection().createStatement();) {
                String stockUpdate = "UPDATE inventory SET Quantity = Quantity + " + quantity + " WHERE ProductId = " + Pid + ";";
                stmt.executeUpdate(stockUpdate);
                db.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try (Statement stmt = db.getConnection().createStatement();){
                int pLeadTime = findLeadTime(addSize, addMaterial, addBrand);
                String allPIDs = "SELECT * shirts WHERE ProductID >= 100 and ProductID < 200;";

                results = stmt.executeQuery(allPIDs);
                while (results.next()) {
                    Pid = results.getInt("ProductID");
                }
                Pid++;

                String insertSql = "INSERT INTO shirts (ProductID, Size, Material, Brand, Colour, Sleaves) VALUES (" 
                    + Pid + ", '" + addSize + "', '" + addMaterial + "', '" + addBrand + "', '" + addColour + "', '"+ addSleaves + ");";
                String stockSql = "INSERT INTO inventory (ProductID, Quantity, LeadTime) VALUES (" + Pid + ", " + quantity + ", " + pLeadTime + ");";

                stmt.executeUpdate(insertSql);
                stmt.executeUpdate(stockSql);
                db.closeConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
