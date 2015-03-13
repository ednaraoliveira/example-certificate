package simple.example.linux;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.CertificateLoader;
import br.gov.frameworkdemoiselle.certificate.CertificateLoaderImpl;
import br.gov.frameworkdemoiselle.certificate.extension.BasicCertificate;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.implementation.FileSystemKeyStoreLoader;

public class DadosCertificadoA1 {

	private static final Logger logger = LoggerFactory
			.getLogger(DadosCertificadoA1.class);

	public static void main(String[] args) throws KeyStoreException {

		String PIN = "teste123";

		String alias;
		try {

			File file = new File("/home/01534562567/A1Teste.pfx");
			KeyStore keyStore = (new FileSystemKeyStoreLoader(file)).getKeyStore(PIN);

			alias = (String) keyStore.aliases().nextElement();

			X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
			
			BasicCertificate bc = new BasicCertificate(certificate);
			logger.info("Nome....................{}", bc.getNome());
			logger.info("E-mail..................{}", bc.getEmail());
			logger.info("Numero de serie.........{}", bc.getSerialNumber());
			logger.info("Nivel do Certificado....{}", bc.getNivelCertificado());

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
