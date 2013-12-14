/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import java.util.Hashtable;
import xbot.common.ITableProxy;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.microedition.io.Connector;
import xbot.common.CommonTools;
import xbot.common.PermanentStorageProxy;
import xbot.common.TableProxy;

/**
 *
 * @author John
 */
public class RobotPermanentStorage extends PermanentStorageProxy {
    
    public RobotPermanentStorage()
    {
        super("RobotPermanentStorage", "file:///Properties.txt");
    }

    protected String ReadFromFile() {
                
        try {
            FileConnection c = (FileConnection) Connector.open(path, Connector.READ_WRITE);
            if (!c.exists())
            {
                Warning("There was no properties file. Creating and continuing, but robot will be using default values.");
                //we need to create the file if it doesn't exist already.
                c.create();
                return "";
            }
            InputStreamReader reader = new InputStreamReader(c.openInputStream());
            // Now, to get a really long string
            
            char[] buffer = new char[2048];
            int result = reader.read(buffer, 0, 2048);
            
            if (result != -1)
            {
                // We didn't actually read everything.
                Error("When reading from properties file, did not fully read file!");
            }
            
            // Otherwise, we're good to parse this into a string.
            String propertyString = String.valueOf(buffer);
            propertyString = propertyString.trim();
            return propertyString;
            
            
        } catch (IOException e) {
            Error("IO Exception when reading the properties file at " + path + ", Message to follow.");
            Error(e.getMessage());
        }       
        
        return "";
    }

    protected void WriteToFile(String data) {
        try
        {
            FileConnection c = (FileConnection) Connector.open(path, Connector.READ_WRITE);
            if (!c.exists())
            {
                Warning("There was no properties file. Creating it.");
                //we need to create the file if it doesn't exist already.
                c.create();
            }
            else
            {
                Info("Properties file found. Deleting and creating a new one.");
                c.delete();
                c.create();
            }
            
            OutputStreamWriter writer = new OutputStreamWriter(c.openOutputStream());
            writer.write(data);
            c.close();            
            
        } catch (IOException e) {
            Error("IO Exception when writing to the properties file at " + path + ", Message to follow.");
            Error(e.getMessage());
        }
    }
}
