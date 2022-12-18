package com.techelevator.currency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SalesReport {
    BigDecimal totalCost;
    // map and other variables
    Map<String, Integer>  inventorySales = new HashMap<>();
    //private BigDecimal totalCost = new BigDecimal(0.00);
    // reference to cvs file
    File vendingInventory = new File("C:\\Users\\xbox2\\OneDrive\\Desktop\\Capstone1\\module-1-capstone\\vendingmachine.csv");


    //method that reads vendingmechine.cvs and populates the inventory sales map and total cost
    public void populateSales(){


        //try catch blocks
        try(Scanner populateMap = new Scanner (vendingInventory)) {

            //loop through file
            while (populateMap.hasNextLine()) {

                //split each line into string array excluding |
                String[] findName = populateMap.nextLine().split(Pattern.quote("|"));

                //because everything is separated by | in cvs
                // only need to add the second element  that is position 1 in array
                inventorySales.put(findName[1], 0);
            }
            populateMap.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found at" + vendingInventory.getAbsolutePath());
        }

    }

    File logger = new File("C:\\Users\\xbox2\\OneDrive\\Desktop\\Capstone1\\module-1-capstone\\log.txt");

    //method that updates and prints/writes to file
    public void updateSalesReport(){
        //populates the sales report with current inventory
        populateSales();

        //variables needed for big decimal
        MathContext mc = new MathContext(4);
        double tempCost = 0.00;

        //try and catch where inventory and total cost gets updated
        try(Scanner readLog = new Scanner (logger)){
            while (readLog.hasNextLine()){
                String templine = readLog.nextLine();
                for(String itemName: inventorySales.keySet()) {
                    if(templine.contains(itemName)){

                        //update num sold of item
                        inventorySales.put(itemName, inventorySales.get(itemName) + 1);

                        //isolate the cost of the item from the string in the log.txt file
                        String findCost = templine.substring(templine.indexOf("$")+1, templine.length()-6);

                        //update the value of the double
                        tempCost += Double.parseDouble(findCost);

                        //make the total cost reflect the current cost of the double
                        totalCost = new BigDecimal(tempCost, mc);
                    }
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("no log.txt found");
            System.out.println("No sales report available");
        }
        printSalesReport(totalCost);
    }

    public void printSalesReport(BigDecimal totals){
        try(PrintWriter newSalesReport = new PrintWriter("saleReport.txt")){
            //loop through the map and print key and value in key | value format
            for(Map.Entry keyValue: inventorySales.entrySet()){
                newSalesReport.println(keyValue.getKey() +  "|" + keyValue.getValue());
            }
            newSalesReport.println("");
            newSalesReport.println("total = " + totals);

        }
        catch(FileNotFoundException e){
            System.out.println("File not found or created");
        }

    }


}
