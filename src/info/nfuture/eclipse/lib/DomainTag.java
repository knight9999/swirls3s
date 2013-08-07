package info.nfuture.eclipse.lib;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class DomainTag {
	protected IToken token;
	protected String key;

	public DomainTag(IToken token,String key) {
		this.token = token;
		this.key = key;
	}
	
	public IToken getToken() {
		return token;
	}
	public void setToken(IToken token) {
		this.token = token;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		if (token == null) {
			return null;
		}
		return (String) token.getData();
	}
}
