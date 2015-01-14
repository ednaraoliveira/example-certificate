package simple.example.linux;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;

public class CheckCertificateSignerLinux {

	private static final Logger logger = LoggerFactory
			.getLogger(CheckCertificateSignerLinux.class);

	public static void main(String[] args) throws KeyStoreException {

		byte[] signed = null;

		/* Obtendo a chave privada */

		try {  

			FileInputStream inputStream = new FileInputStream("assinatura.p7s");
			try {
				signed = IOUtils.toByteArray(inputStream);
			} finally {
				inputStream.close();
			}

			byte[] content = "SERPRO".getBytes();

			/* Objeto doSign */
			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();

			/* Valida o conteudo */
			logger.info("Efetuando a validacao da assinatura.");
			boolean checked = signer.check(null, signed);

			if (checked) {
				logger.info("A assinatura foi validada.");
			} else {
				logger.info("A assinatura foi invalidada!");
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
