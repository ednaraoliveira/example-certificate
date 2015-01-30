package br.gov.frameworkdemoiselle.servlet;

import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import br.gov.frameworkdemoiselle.timestamp.connector.TimeStampOperator;

@WebServlet("/carimbo")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "teste"))
public class TimestampGeneratorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("DoGET");
		this.doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
				byte[] content = IOUtils.toByteArray(request.getInputStream());

		PrivateKey privateKey = null;
		Certificate[] certificates = null;

		String configName = "/home/01534562567/drivers.config";
		String password = "******";

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

			// requisitando um carimbo de tempo
			TimeStampOperator timeStampOperator = new TimeStampOperator();
			byte[] reqTimestamp = timeStampOperator.createRequest(privateKey,
					certificates, content);

			content = timeStampOperator.invoke(reqTimestamp);
			
			response.setContentType("application/octet-stream");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getOutputStream().write(content);
			response.getOutputStream().flush();
			response.getOutputStream().close();

			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
