package language;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleCode {
	
	Locale l;
	ResourceBundle rb;
	
	public LocaleCode() {
		l = new Locale("nl","BE");
		rb = ResourceBundle.getBundle("language.Language", l);
	}
	
	public void setTaal(String lang, String country)
	{
		l = new Locale(lang,country);
		rb = ResourceBundle.getBundle("language.Language", l);
	}
	
	public String vertaalStrings(String Key) 
	{
		return rb.getString(Key);
	}

}
