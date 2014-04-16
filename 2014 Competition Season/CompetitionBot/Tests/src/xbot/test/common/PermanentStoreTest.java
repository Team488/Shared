/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

import xbot.common.properties.PermanentStorageProxy;
import xbot.common.CommonUtils;


/**
 *
 * @author John
 */
public class PermanentStoreTest extends PermanentStorageProxy {

    private StringBuffer buf;
    public String savedText;
    private String separator = "\n";
    
    public PermanentStoreTest()
    {
        super("PermanentStoreTest", "");
        buf = new StringBuffer();
        savedText = "";
    }
    
    public void AddTestDouble(String key, double value)
    {
        buf.append("double")
           .append(CommonUtils.GetPropertyDelimiter())
           .append(key)
           .append(CommonUtils.GetPropertyDelimiter())
           .append(value)
           .append(separator);
    }
    
    public void AddTestBoolean(String key, boolean value)
    {
        buf.append("boolean")
           .append(CommonUtils.GetPropertyDelimiter())
           .append(key)
           .append(CommonUtils.GetPropertyDelimiter())
           .append(value)
           .append(separator);
    }
        
    public void AddTestString(String key, String value)
    {
        buf.append("string")
           .append(CommonUtils.GetPropertyDelimiter())
           .append(key)
           .append(CommonUtils.GetPropertyDelimiter())
           .append(value)
           .append(separator);
    }
    
    @Override
    protected String ReadFromFile() {
        if (savedText.length() > 0)
        {
            return savedText;
        }        
        
        if (buf == null)
        {
            return "";
        }
        return buf.toString();
    }    

    @Override
    protected void WriteToFile(String serializedProperties) {
        savedText = serializedProperties;
    }
}
