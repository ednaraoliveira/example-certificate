package simple.example.windows;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simple.example.util.SimpleExampleUtil;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;
import br.gov.frameworkdemoiselle.certificate.signer.SignerAlgorithmEnum;
import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.bc.policies.ADRBCMS_2_1;

public class CertificateSignerWin {

	private static final Logger logger = LoggerFactory.getLogger(CertificateSignerWin.class);

	public static void main(String[] args) throws KeyStoreException {

		String PIN = "qwaszx12!";
		Certificate[] certificates = null;
		/* Obtendo a chave privada */

		String alias;
		try {
			logger.info("-------- Fabrica do certificate --------");
			KeyStore keyStore = KeyStoreLoaderFactory.factoryKeyStoreLoader().getKeyStore();
			alias = (String) keyStore.aliases().nextElement();
			logger.info("alias ...........: {}", alias);
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, PIN.toCharArray());
			logger.info("privateKey ......: {}", privateKey);

			X509Certificate c = (X509Certificate) keyStore.getCertificate(alias);

			byte[] content = "SERPRO".getBytes();

			/* Parametrizando o objeto doSign */
			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
			signer.setCertificates(keyStore.getCertificateChain(alias));
			signer.setPrivateKey((PrivateKey) keyStore.getKey(alias, null));
			signer.setAlgorithm(SignerAlgorithmEnum.SHA256withRSA);
			signer.setSignaturePolicy(new ADRBCMS_2_1());
			signer.setAttached(true);

			/* Realiza a assinatura do conteudo */
			logger.info("Efetuando a  assinatura do conteudo");
			byte[] signed = signer.signer(content);

			/* Valida o conteudo */
			logger.info("Efetuando a validacao da assinatura.");
			boolean checked = signer.check(content, signed);

			if (checked) {
				logger.info("A assinatura foi validada.");
			} else {
				logger.info("A assinatura foi invalidada!");
			}

		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
