package br.gov.frameworkdemoiselle.applet;

public final class Session {

	private static String jsessionid = null;

	public final static String getJsessionid() {
		return jsessionid;
	}

	public final static void setJsessionid(String jsessionid) {
		Session.jsessionid = jsessionid;
	}
	
	
}
