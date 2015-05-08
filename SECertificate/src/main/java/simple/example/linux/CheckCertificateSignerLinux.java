package simple.example.linux;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.cms.CMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.signer.SignerException;
import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;

public class CheckCertificateSignerLinux {

	private static final Logger logger = LoggerFactory
			.getLogger(CheckCertificateSignerLinux.class);

	public static void main(String[] args) throws KeyStoreException,
			CMSException {

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
			boolean checked = false;
			try{
				/* Objeto doSign */
				PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
				/* Valida o conteudo */
				
				checked = signer.check(content, signed);
			} catch (SignerException e) {
				if (e.getMessage().equals("O Atributo signingCertificate não pode ser nulo.")) {
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>> SignerException " + e.getMessage());
					checked = true;
				}
			}
			logger.info("Efetuando a validacao da assinatura.");
				
			if (checked == true) {
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
