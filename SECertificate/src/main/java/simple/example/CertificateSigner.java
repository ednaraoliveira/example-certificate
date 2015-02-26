package simple.example;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import br.gov.frameworkdemoiselle.certificate.CertificateLoader;
import br.gov.frameworkdemoiselle.certificate.CertificateLoaderImpl;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;

public class CertificateSigner {

	public static void main(String[] args) {

		KeyStoreLoader keyStoreLoader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
		KeyStore keyStore = keyStoreLoader.getKeyStore();
		CertificateLoader certificateLoader = new CertificateLoaderImpl();
		certificateLoader.setKeyStore(keyStore);
		X509Certificate certificate = certificateLoader.loadFromToken();
		
		try {
			String certificateAlias = keyStore.aliases().nextElement();
			PrivateKey chavePrivada = (PrivateKey) keyStore.getKey(certificateAlias, "qaszx12!".toCharArray());			
		} catch (KeyStoreException e) {
			// TODO: handle exception
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(certificate.getPublicKey());
		
	}

}
