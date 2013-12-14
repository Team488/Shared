/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import java.util.Enumeration;

/**
 *
 * @author John
 */
public abstract class PermanentStorageProxy extends TableProxy {
    
    protected boolean writeNeeded;
    protected String path;
    
    public PermanentStorageProxy(String name, String pathToPropertiesFile)
    {
        super(name);
        writeNeeded = false;
        //PATH = "file:///Properties.txt";
        path = pathToPropertiesFile;
    }
    
    public void LoadFromDisk()
    {
        String propertyString = ReadFromFile();
        
        if (propertyString != null)
        {
            if (!propertyString.equals(""))
            {
                ParsePropertyString(propertyString);
            }
            else
            {
                Warning("PropertyString was empty when loading data! No properties loaded.");
            }
        }   
        else
        {
            Warning("PropertyString was null when loading data! No properties loaded.");
        }
    }
    
    protected abstract String ReadFromFile();
    
    protected void ParsePropertyString(String propertyString)
    {
        // 1) split the string by line
        String[] propertyLines = CommonUtils.Split(propertyString, CommonUtils.GetLineSeperator());
        
        // 2) for each propertyLine, split by delimeter and load it into memory
        for (int i = 0; i < propertyLines.length; i++)
        {
            String line = propertyLines[i];
            String[] keyValuePair = CommonUtils.Split(line, CommonUtils.GetPropertyDelimiter());
            String key = keyValuePair[0];
            String value = keyValuePair[1];
            Double dValue = Double.valueOf(value);
            
            table.put(key, dValue);
            
            // For now, we only support doubles. Later we might need some sort of type swtich
            // if we want to support many types.
        }
    }
    
    
    public void setDouble(String key, double value)
    {
        super.setDouble(key, value);
        writeNeeded = true;
    }
    
    public void SaveToDisk()
    {
        if (writeNeeded)
        {
            Info("Saving properties to disk");
            writeNeeded = false;
            String serializedProperties = SerializePropertiesToString();
            WriteToFile(serializedProperties);
        }
        else
        {
            Verbose("Skipping saving properties to disc - write not needed.");
        }
    }
    
    protected String SerializePropertiesToString()
    {
        Info("Serializing Properties to String");
        // Iterate through the hashtable, make a long string.
        StringBuffer buf = new StringBuffer();
        
        Enumeration e = table.keys();
        while (e.hasMoreElements())
        {
            String key = (String)e.nextElement();
            buf
                .append(key)
                .append(CommonUtils.GetPropertyDelimiter())
                .append(table.get(key))
                .append(CommonUtils.GetLineSeperator());
        }
        
        return buf.toString();
    }
    
    protected abstract void WriteToFile(String serializedProperties);
}
