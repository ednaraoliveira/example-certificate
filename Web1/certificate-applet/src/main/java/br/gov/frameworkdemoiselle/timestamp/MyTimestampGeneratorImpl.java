package br.gov.frameworkdemoiselle.timestamp;

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
		String url = "http://localhost:8080/certificate-applet-web/api/carimbo";
		
		final String TOKEN = "Token " + Token.getValue();

		client = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(url);

		try {
			httpPost.addHeader("Content-Type", "application/octet-stream");
			httpPost.addHeader("Authorization", TOKEN);
			
			ByteArrayEntity bFile = new ByteArrayEntity(content);
			httpPost.setEntity(bFile);

			HttpResponse response = client.execute(httpPost);
			
			timestamp = IOUtils.toByteArray(response.getEntity().getContent());
			
			logger.info("TimeStamp: {} ", timestamp.toString());
			
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
