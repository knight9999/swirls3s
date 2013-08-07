package info.nfuture.eclipse.lib;

public class SimpleTabSetting {
	protected int tabWidth = 2;
	protected boolean isTabsToSpacesConversionEnabled;
	
	public SimpleTabSetting(boolean sw,int width) {
		isTabsToSpacesConversionEnabled = sw;
		tabWidth = width;
	}
	
	public int getTabWidth() {
		return tabWidth;
	}
	public void setTabWidth(int tabWidth) {
		this.tabWidth = tabWidth;
	}
	public boolean isTabsToSpacesConversionEnabled() {
		return isTabsToSpacesConversionEnabled;
	}
	public void setTabsToSpacesConversionEnabled(
			boolean isTabsToSpacesConversionEnabled) {
		this.isTabsToSpacesConversionEnabled = isTabsToSpacesConversionEnabled;
	}
}
