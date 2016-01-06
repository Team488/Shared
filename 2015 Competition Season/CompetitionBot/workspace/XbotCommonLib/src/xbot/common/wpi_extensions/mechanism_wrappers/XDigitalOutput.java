package xbot.common.wpi_extensions.mechanism_wrappers;

public interface XDigitalOutput extends XBaseIO {
    
    public int getChannel();
	
	public void set(boolean value);
	
}
