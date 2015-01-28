package br.gov.frameworkdemoiselle.timestamp;

public final class Token {

	private static String value;

	public static String getValue() {
		return value;
	}

	public static void setValue(String value) {
		Token.value = value;
	}

	public static boolean isEmpty() {
		return Token.value == null;
	}

	public static void clear() {
		Token.value = null;
	}
}
