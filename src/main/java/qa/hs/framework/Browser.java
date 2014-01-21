package qa.hs.framework;

public enum Browser 
{
    CHROME("chrome"),
    FIREFOX("firefox"),
    INTERNET_EXPLORER("ie"),
    SAFARI("safari");
    
    String moniker;
    
    Browser( String moniker ) {
        this.moniker = moniker;
    }
    
}
