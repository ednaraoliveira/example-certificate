package simple.example.linux;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.extension.BasicCertificate;

public class DadosCertificado {

	private static final Logger logger = LoggerFactory
			.getLogger(DadosCertificado.class);

	public static void main(String[] args) throws KeyStoreException {

		String configName = "/home/01534562567/drivers.config";
		String PIN = "qwaszx12!";

		/* Obtendo a chave privada */

		String alias;
		try {

			Provider p = new sun.security.pkcs11.SunPKCS11(configName);
			Security.addProvider(p);

			KeyStore keyStore = KeyStore.getInstance("PKCS11","SunPKCS11-Provedor");
			keyStore.load(null, PIN.toCharArray());

			alias = (String) keyStore.aliases().nextElement();

			X509Certificate c = (X509Certificate) keyStore
					.getCertificate(alias);
			
			BasicCertificate bc = new BasicCertificate(c);
			logger.info("Nome....................{}", bc.getNome());
			logger.info("E-mail..................{}", bc.getEmail());
			logger.info("Numero de serie.........{}", bc.getSerialNumber());
			logger.info("Nivel do Certificado....{}", bc.getNivelCertificado());

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
