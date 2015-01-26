package example.certificate_applet_web.rest;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.timestamp.connector.TimeStampOperator;

@Path("carimbo")
public class TimestampGeneratorREST {

	@POST
	@LoggedIn
	@Consumes("application/octet-stream")
	@Produces("application/octet-stream")
	public byte[] setTimestamp(byte[] content) {

		PrivateKey privateKey = null;
		Certificate[] certificates = null;

		String configName = "/home/01534562567/drivers.config";
		String password = "*****";

		Provider p = new sun.security.pkcs11.SunPKCS11(configName);
		Security.addProvider(p);

		KeyStore ks = null;
		try {

			ks = KeyStore.getInstance("PKCS11", "SunPKCS11-Provedor");
			ks.load(null, password.toCharArray());

			String alias = "";

			KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(
					password.toCharArray());

			Enumeration<String> e = ks.aliases();
			while (e.hasMoreElements()) {
				alias = e.nextElement();
				certificates = ks.getCertificateChain(alias);
			}

			KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks
					.getEntry(alias, protParam);
			privateKey = pkEntry.getPrivateKey();

		} catch (Exception e) {
			e.printStackTrace();
		}

		TimeStampOperator timeStampOperator = new TimeStampOperator();
		byte[] request = timeStampOperator.createRequest(privateKey,
				certificates, content);

		content = timeStampOperator.invoke(request);

		return content;
	}

}