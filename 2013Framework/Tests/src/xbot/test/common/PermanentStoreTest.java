/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

import xbot.common.PermanentStorageProxy;

/**
 *
 * @author John
 */
public class PermanentStoreTest extends PermanentStorageProxy {

    private StringBuffer buf;
    public String savedText;
    private String separator = System.getProperty("line.separator");
    
    public PermanentStoreTest()
    {
        super("PermanentStoreTest", "");
        buf = new StringBuffer();
        savedText = "";
    }
    
    public void AddTestProperty(String key, double value)
    {
        buf.append(key).append(",").append(value).append(separator);
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
