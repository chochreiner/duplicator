package at.ac.tuwien;

import org.apache.wicket.protocol.http.WebApplication;

public class WicketApplication extends WebApplication
{    
	public WicketApplication()
	{
	}
	
	public Class<HomePage> getHomePage()
	{
		return HomePage.class;
	}

}
