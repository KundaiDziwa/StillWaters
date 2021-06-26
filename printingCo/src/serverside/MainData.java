package printingCo.src.serverside;

import java.io.*;
import java.util.Vector;

public class MainData {
    public static void main(String[] args) throws Exception {
        HandleOrder process = new HandleOrder("OrderDetails", "OrderReview");
        // Transfer the data into the Backend process.
        Vector<String> details = process.getOrder();
        Shirts shirtOrder = new Shirts(details.elementAt(1), details.elementAt(2), details.elementAt(3), details.elementAt(4), details.elementAt(0), Integer.parseInt(details.elementAt(5)));
        String receipt = shirtOrder.getReceipt();
        process.writeData(receipt);
    }
}

class HandleOrder {
    private String input_file;
    private String output_file;
    private String orderType;
    private String vinyls;
    private Vector<String> order = new Vector<String>(6);

    // Constructor
    public HandleOrder(String inFile, String outFile) throws Exception {
        input_file = inFile + ".txt";
        output_file = outFile + ".txt";
    }

    // Getter and Setter
    public Vector<String> getOrder() {
        return order;
    }

    // Methods 
    public void readData() throws IOException, FileNotFoundException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(input_file));
            String line = br.readLine();
            String[] request = line.split(": ");
            // Read the input data and retrieve all of the 
            while (line != null) {
                if (request.length != 0) {
                    if (request[0].equals("Order Type")) {
                        orderType = request[1];
                    }
                    if (request[0].equals("Type")) {
                        order.add(request[1]);
                    }
                    if (request[0].equals("Size")) {
                        order.add(request[1]);
                    }
                    if (request[0].equals("Material")) {
                        order.add(request[1]);
                    }
                    if (request[0].equals("Brand")) {
                        order.add(request[1]);
                    }
                    if (request[0].equals("Colour")) {
                        order.add(request[1]);
                    }
                    if (request[0].equals("Quantity")) {
                        order.add(request[1]);
                    }
                    if (request[0].equals("Logo")) {
                        vinyls = request[1];
                    }
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void writeData(String data) throws IOException, FileNotFoundException {
        String basicInfo = "Review order\n\nOrder Type: " + orderType + "\n\n";
        String description = "Description Information:\nN/A.\nLogo: " + vinyls + "\n\n";
        String status = "Order Placed.";
        
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(output_file));
            bw.write(basicInfo);
            bw.write(data);
            bw.write(description);
            bw.write(status);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                bw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}