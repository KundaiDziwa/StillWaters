package printingCo.src.serverside;

import java.sql.*;

public class SQLConnection {
    private final String DBURL;
    private final String USERNAME;
    private final String PASSWORD;
    private Connection dbConnect;

    // Constructors
    public SQLConnection() {
        DBURL = "jbdc:mysql://localhost/PrintingDatabase";
        USERNAME = "KundaiTD";
        PASSWORD = "waters316";
    }
    public SQLConnection(String dburl, String username, String password) {
        DBURL = dburl;
        USERNAME = username;
        PASSWORD = password;
    }
    
    // Getters
    public String getDBURL() {
        return DBURL;
    }
    public String getUSERNAME() {
        return USERNAME;
    }
    public String getPASSWORD() {
        return PASSWORD;
    }
    public Connection getConnection() {
        return dbConnect;
    }

    // Methods
    public void initializeConnection() {
        try{
            dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void closeConn() {
        try {
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
