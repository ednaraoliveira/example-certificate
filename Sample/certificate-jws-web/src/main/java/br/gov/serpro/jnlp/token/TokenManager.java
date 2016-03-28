package br.gov.serpro.jnlp.token;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManager {
	
	private static Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());

	public static String put(String file) {
		String token = UUID.randomUUID().toString();

		if (map.containsValue(file)) {
			map.remove(token);
		}
		map.put(token, file);

		return token;
	}

	public static String get(String token) {
		return map.get(token);
	}

	public static void destroy(String token) {
		map.remove(token);
	}

}
