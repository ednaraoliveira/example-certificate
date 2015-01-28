package br.gov.frameworkdemoiselle.timestamp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.exception.CertificateCoreException;
import br.gov.frameworkdemoiselle.certificate.timestamp.TimeStampGenerator;
import br.gov.frameworkdemoiselle.timestamp.connector.TimeStampOperator;

public class MyTimestampGeneratorImpl implements TimeStampGenerator {

	private static final Logger logger = LoggerFactory
			.getLogger(MyTimestampGeneratorImpl.class);

	private byte[] content;
	private CloseableHttpClient client;

	/**
	 * Inicializa os atributos necessarios para obter o carimbo de tempo
	 * 
	 * @param content
	 * @param privateKey
	 * @param certificates
	 * @throws CertificateCoreExceptionCredenciais
	 */

	public void initialize(byte[] content, PrivateKey privateKey,
			Certificate[] certificates) throws CertificateCoreException {
		this.content = content;
	}

	/**
	 * Envia a requisicao o conteudo para o serviço que vai retornar o carimbo
	 * tempo
	 * 
	 * @return O carimbo de tempo retornado pelo serviço
	 */
	public byte[] generateTimeStamp() throws CertificateCoreException {
		logger.info("------------- MyTimestampGeneratorImpl.generateTimeStamp() --------------");

		byte[] timestamp = null;

		final String TOKEN = "Token " + Token.getValue();

		System.out.println("--------------- CALL SERVLET ------------");

		HttpURLConnection connection = null;
		try {
			URL url = new URL("http://localhost:8080/certificate-applet-web/carimbo");
			
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
		    connection.setRequestProperty("Content-Type", "application/octet-stream");
		    connection.connect();
		    
		    OutputStream os = connection.getOutputStream();
		    os.write(content);
		    os.flush();
		    os.close();
		    
		    System.out.println("STATUS: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
		    
		    InputStream is = connection.getInputStream();
		    timestamp = IOUtils.toByteArray(is);
		    System.out.println(IOUtils.toString(is));
		    is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("------------- FIM --------------");

		return timestamp;
	}

	/**
	 * Valida um carimnbo de tempo e o documento original
	 * 
	 * @param content
	 *            o conteudo original
	 * @param response
	 *            O carimbo de tempo a ser validado
	 * 
	 */
	public void validateTimeStamp(byte[] content, byte[] response)
			throws CertificateCoreException {
		TimeStampOperator timeStampOperator = new TimeStampOperator();
		timeStampOperator.validate(content, response);
	}
}
