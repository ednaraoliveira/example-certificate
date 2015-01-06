package simple.example.linux;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.signer.SignerAlgorithmEnum;
import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.bc.policies.ADRBCMS_2_1;

public class CheckCertificateSignerLinux {

	private static final Logger logger = LoggerFactory
			.getLogger(CheckCertificateSignerLinux.class);

	public static void main(String[] args) throws KeyStoreException {

		String configName = "/home/01534562567/drivers.config";
		String PIN = "******";
		Certificate[] certificates = null;
		byte[] signed = null;

		/* Obtendo a chave privada */

		String alias;
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
			boolean checked = signer.check(content, signed);

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
