package br.gov.frameworkdemoiselle.certificate.sample;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import br.gov.frameworkdemoiselle.certificate.exception.CertificateCoreException;
import br.gov.frameworkdemoiselle.timestamp.connector.TimeStampOperator;

@WebServlet("/carimbo")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
public class TimestampGeneratorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private PrivateKey privateKey = null;
	private Certificate[] certificates = null;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		byte[] content;
		byte[] timestamp;

		try {

			loadCertificate();

			// Lendo o Conteúdo enviado
			content = IOUtils.toByteArray(request.getInputStream());

			// requisitando um carimbo de tempo
			if (content.length > 0) {
				TimeStampOperator timeStampOperator = new TimeStampOperator();
				byte[] reqTimestamp = timeStampOperator.createRequest(
						privateKey, certificates, content);

				timestamp = timeStampOperator.invoke(reqTimestamp);

				response.setContentType("application/octet-stream");
				response.getOutputStream().write(timestamp);
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else {
				response.setContentType("text/plain");
				response.setStatus(500);
				response.getOutputStream().write(
						"Conteúdo não enviado".getBytes());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}

		} catch (CertificateCoreException e) {
			response.setContentType("text/plain");
			response.setStatus(500);
			response.getOutputStream().write(e.getMessage().getBytes());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/plain");
			response.setStatus(500);
			response.getOutputStream().write(e.getMessage().getBytes());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}

	private void loadCertificate() throws Exception {

		String configName = "/home/01534562567/drivers.config";
		String password = "****";
		String alias = "";

		Provider p = new sun.security.pkcs11.SunPKCS11(configName);
		Security.addProvider(p);

		KeyStore ks = null;

		try {
			ks = KeyStore.getInstance("PKCS11", "SunPKCS11-Provedor");
			ks.load(null, password.toCharArray());

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
		} catch (KeyStoreException e) {
			throw new Exception(e.getMessage(), e.getCause());
		} catch (NoSuchProviderException e) {
			throw new Exception(e.getMessage(), e.getCause());
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e.getMessage(), e.getCause());
		} catch (CertificateException e) {
			throw new Exception(e.getMessage(), e.getCause());
		} catch (IOException e) {
			throw new Exception(e.getMessage(), e.getCause());
		} catch (UnrecoverableEntryException e) {
			throw new Exception(e.getMessage(), e.getCause());
		}
	}
}
