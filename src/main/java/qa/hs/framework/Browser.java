package qa.hs.framework;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    INTERNET_EXPLORER("ie"),
    SAFARI("safari"), 
    PHANTOMJS("phantomjs");
    
    String moniker;
    
    public String getMoniker() {
		return moniker;
	}

	public void setMoniker(String moniker) {
		this.moniker = moniker;
	}

	Browser( String moniker ) {
        this.moniker = moniker;
    }
    
}
