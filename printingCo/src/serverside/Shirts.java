package printingCo.src.serverside;

import java.sql.*;

public class Shirts {
    private String size;
    private String colour;
    private String material; 
    private String brand;
    private String sleaves;
    private int leadTime;
    private int num;
    private SQLConnection db = new SQLConnection();

    // Constructions 
    public Shirts(String size, String colour, String material, String brand, String sleaves, int num) {
        this.size = size;
        this.colour = colour;
        this.material = material;
        this.brand = brand;
        this.sleaves = sleaves;
        this.num = num;
    }

    // Getters and Setters
    public String getSize() {
        return size;
    }
    public String getColour() {
        return colour;
    }
    public String getMaterial() {
        return material;
    }
    public String getBrand() {
        return brand;
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

    public void setSize(String size) {
        this.size = size;
    }
    public void setColour(String colour) {
        this.colour = colour;
    }
    public void setMaterial(String material) {
        this.material = material;
    } 
    public void setBrand(String brand) {
        this.brand = brand;
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

    public void addStockShirts (String addSize, String addColour, String addMaterial, String addBrand, String addSleaves, int quantity) throws SQLException {
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
