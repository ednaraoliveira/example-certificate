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
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.exception.CertificateCoreException;
import br.gov.frameworkdemoiselle.timestamp.connector.TimeStampOperator;

@WebServlet("/carimbo")
//@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
public class TimestampGeneratorServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(TimestampGeneratorServlet.class);

	
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

		
		PrivateKey privateKey = null;
		Certificate[] certificates = null;
		byte[] content = null;

		String configName = "/home/01534562567/drivers.config";
		String password = "qwaszx12!";

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

			//Lendo o Conteúdo enviado
			String contentType = request.getContentType();
			if ("application/octet-stream".equals(contentType)|| contentType.isEmpty() || "application/x-www-form-urlencoded".equals(contentType)) {
				content = IOUtils.toByteArray(request.getInputStream());
			} else{
				response.setContentType("text/plain");
				response.setStatus(415);
			}
			
			//requisitando um carimbo de tempo
			TimeStampOperator timeStampOperator = new TimeStampOperator();
			byte[] reqTimestamp = timeStampOperator.createRequest(privateKey,
					certificates, content);

			content = timeStampOperator.invoke(reqTimestamp);

			response.setContentType("application/octet-stream");
			response.getOutputStream().write(content);
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (CertificateCoreException e) {
			response.setContentType("text/plain");
			response.setStatus(500);
			response.getOutputStream().write(e.getMessage().getBytes());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			response.setContentType("text/plain");
			response.setStatus(500);
			response.getOutputStream().write("Erro ao fazer load do certificado habilitado para requisitar carimbo de tempo".getBytes());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} finally {
			// TODO

		}
	}
}
