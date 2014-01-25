package qa.hs.framework;

public enum Browser {
	
	GRIDCHROME32("gridchrome32"),
    GRIDFIREFOX26("gridfirefox26"),
    GRIDIE10("gridie10"),
    GRIDSAFARI7("gridsafari7"),
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
