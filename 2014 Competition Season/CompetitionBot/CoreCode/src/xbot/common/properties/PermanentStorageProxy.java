/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.properties;

import java.util.Enumeration;
import xbot.common.CommonUtils;

/**
 * When CommonTools is created, it needs a way to permanently store properties. This
 * functionality is abstracted into the PermanentStorageProxy, which is (for the purposes
 * of CommonTools) able to save and load values from a permanent store.
 * @author John
 */
public abstract class PermanentStorageProxy extends TableProxy {
    
    /**
     * Whether or not values have changed, and thus, a write is needed to save
     * these values.
     */
    protected boolean writeNeeded;
    /**
     * Where to store the values in the table.
     */
    protected String path;
    
    /**
     *
     * @param name  Name of the permanentStore class
     * @param pathToPropertiesFile  Where to save the table
     */
    public PermanentStorageProxy(String name, String pathToPropertiesFile)
    {
        super(name);
        writeNeeded = false;
        //PATH = "file:///Properties.txt";
        path = pathToPropertiesFile;
    }
    
    /**
     * Loads all properties from storage
     */
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
    
    /**
     * Reads the table as a single String from storage
     * @return
     */
    protected abstract String ReadFromFile();
    
    /**
     * Parses the property string and loads the values into the table.
     * @param propertyString
     */
    protected void ParsePropertyString(String propertyString)
    {
        // 1) split the string by line
        String[] propertyLines = CommonUtils.Split(propertyString, CommonUtils.GetLineSeperator());
        
        // 2) for each propertyLine, split by delimeter and load it into memory
        for (int i = 0; i < propertyLines.length; i++)
        {
            String line = propertyLines[i];
            String[] keyValuePair = CommonUtils.Split(line, CommonUtils.GetPropertyDelimiter());
            String type = keyValuePair[0];
            String key = keyValuePair[1];
            String value = keyValuePair[2];
            
            try {
                if (type.equals("double")) {
                    this.setDouble(key, Double.parseDouble(value));
                }
                else if (type.equals("boolean")) {
                    this.setBoolean(key, CommonUtils.parseBoolean(value));
                }
                else if (type.equals("string")) {
                    this.setString(key, value);
                }   
            }
            catch (Exception e) {
                this.Warning("Unable to parse property " + key + " with value " + value);
                this.Warning(e.getMessage());
            }
            // For now, we only support doubles. Later we might need some sort of type swtich
            // if we want to support many types.
        }
    }
    
    
    /**
     *
     * @param key
     * @param value
     */
    public void setDouble(String key, double value)
    {
        super.setDouble("double" + CommonUtils.GetPropertyDelimiter() + key, value);
        writeNeeded = true;
    }
    
    /**
     *
     * @param key
     * @param value
     */
    public void setBoolean(String key, boolean value)
    {
        super.setBoolean("boolean" + CommonUtils.GetPropertyDelimiter() + key, value);
        writeNeeded = true;
    }
    
    /**
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value)
    {
        super.setString("string" + CommonUtils.GetPropertyDelimiter() + key, value);
        writeNeeded = true;
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Double getDouble(String key)
    {
        return super.getDouble("double" + CommonUtils.GetPropertyDelimiter() + key);
        
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key)
    {
        return super.getBoolean("boolean" + CommonUtils.GetPropertyDelimiter() + key);
    }
    
    /**
     *
     * @param key
     * @return
     */
    public String getString(String key)
    {
        return super.getString("string" + CommonUtils.GetPropertyDelimiter() + key);
    }
    
    /**
     * Saves all properties to disk.
     */
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
            //Verbose("Skipping saving properties to disc - write not needed.");
        }
    }
    
    /**
     * Serializes all the properties in the table into a single string (already
     * includes property and line delimeters).
     * @return
     */
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
    
    /**
     * Write the given string to storage in an implementation-specific way.
     * @param serializedProperties
     */
    protected abstract void WriteToFile(String serializedProperties);
}
