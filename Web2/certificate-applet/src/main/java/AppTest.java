import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;
import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory;


public class AppTest {

	public static void main(String[] args) {
		try {
			String configName = "/home/01534562567/drivers.config";
			String password = "***";

			Provider p = new sun.security.pkcs11.SunPKCS11(configName);
			Security.addProvider(p);

			KeyStore ks = KeyStore.getInstance("PKCS11", "SunPKCS11-Provedor");
						
			ks.load(null, password.toCharArray());

			Certificate[] certificates = null;

			String alias = "";

			Enumeration<String> e = ks.aliases();
			while (e.hasMoreElements()) {
				alias = e.nextElement();
				System.out.println("alias..............: {}" + alias);
					certificates = ks.getCertificateChain(alias);
			}

			X509Certificate c = (X509Certificate) certificates[0];
			System.out.println("Número de série....: {}" + c.getSerialNumber()
					.toString());

			byte[] content = "SERPRO".getBytes();
					//readContent("/home/01534562567/Desktop/check109.txt");

			/* Parametrizando o objeto doSign */
			   /* Parametrizando o objeto doSign */
            PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
            signer.setCertificates(ks.getCertificateChain(alias));
            signer.setPrivateKey((PrivateKey) ks.getKey(alias, null));
            signer.setSignaturePolicy(PolicyFactory.Policies.AD_RT_CADES_2_1);
            signer.setAttached(true);

			/* Realiza a assinatura do conteudo */
			System.out.println("Efetuando a  assinatura do conteudo");
			byte[] signed = signer.doSign(content);

			/* Valida o conteudo */
			System.out.println("Efetuando a validacao da assinatura.");
			boolean checked = signer.check(content, signed);

			if (checked) {
				System.out.println("A assinatura foi validada.");
			} else {
				System.out.println("A assinatura foi invalidada!");
			}

			try (FileOutputStream fos = new FileOutputStream(new File(
					"/home/01534562567/assinaturaRT.p7s"))) {
				fos.write(signed);
			}

		} catch (KeyStoreException | NoSuchProviderException | IOException
				| NoSuchAlgorithmException | CertificateException
				| UnrecoverableKeyException ex) {
			Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE,	null, ex);
		}
	}

	private byte[] readContent(String arquivo) {

		byte[] result = null;
		try {
			File file = new File(arquivo);
			FileInputStream is = new FileInputStream(file);
			result = new byte[(int) file.length()];
			is.read(result);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void writeContent(byte[] conteudo, String arquivo) {

		try {
			File file = new File(arquivo);
			FileOutputStream os = new FileOutputStream(file);
			os.write(conteudo);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
