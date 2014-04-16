/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package timesender;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author aschokk
 */
public class TimeSender {
    static long updateDelayInMS = 1000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Starting time sender");
        try {
            NetworkTable.setClientMode();
            NetworkTable.setIPAddress("10.4.88.2");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        NetworkTable table = NetworkTable.getTable("SmartDashboard");
        
        while(true) {
            System.out.println("loop");
            try {
                Thread.sleep(updateDelayInMS);
            } catch (InterruptedException e) {
                
            }
            Date now = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
            String fstring = df.format(now);
            System.out.println("Sending " + fstring);
            table.putString("DSTimestamp", fstring);
        }
       
    }
    
}
