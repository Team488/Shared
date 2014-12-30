/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import xbot.common.properties.PermanentStorageProxy;

/**
 *
 * @author John
 */
public class RobotPermanentStorage extends PermanentStorageProxy {
    
    public RobotPermanentStorage()
    {
        super("RobotPermanentStorage", "/XbotData/Properties.txt");
    }

    protected String ReadFromFile() {
                
        try {
            File file = new File(path);
            if (!file.exists())
            {
                Warning("There was no properties file. Creating and continuing, but robot will be using default values.");
                //we need to create the file if it doesn't exist already.
                file.getParentFile().mkdirs();
                file.createNewFile();
                return "";
            }
            
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            // Now, to get a really long string
            
            char[] buffer = new char[20480];
            int result = reader.read(buffer, 0, 20480);
            
            if (result != -1)
            {
                // We didn't actually read everything.
                // This error isn't actually valid. Commenting out for now.
                // Error("When reading from properties file, did not fully read file!");
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
        	File file = new File(path);
            if (!file.exists())
            {
                Warning("There was no properties file. Creating it.");
                //we need to create the file if it doesn't exist already.
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            else
            {
                Info("Properties file found. Deleting and creating a new one.");
                file.delete();
                file.createNewFile();
            }
            
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write(data);
            writer.close();
            
        } catch (IOException e) {
            Error("IO Exception when writing to the properties file at " + path + ", Message to follow.");
            Error(e.getMessage());
        }
    }
}
