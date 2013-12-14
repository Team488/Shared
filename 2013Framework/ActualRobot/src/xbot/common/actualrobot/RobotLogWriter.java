/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.microedition.io.Connector;
import xbot.common.*;
import xbot.common.logging.*;

/**
 *
 * @author Alex
 */
public class RobotLogWriter extends LogWriter {
    private static final String PATH = "file:///jLog.txt";
   
    public void writeToStorage(String entry)
    {
        try {
            FileConnection c = (FileConnection) Connector.open(PATH, Connector.READ_WRITE);
            if (!c.exists())
            {
                //we need to create the file if it doesn't exist already.
                c.create();
            }
            OutputStreamWriter writer = new OutputStreamWriter(c.openOutputStream());
            writer.write(entry);
            c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
