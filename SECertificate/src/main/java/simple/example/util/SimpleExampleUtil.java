package simple.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SimpleExampleUtil {

	public static byte[] readContent(String arquivo) {
		byte[] result = null;
		try {
			File file = new File(arquivo);
			FileInputStream is = new FileInputStream(file);
			result = new byte[(int) file.length()];
			is.read(result);
			is.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
