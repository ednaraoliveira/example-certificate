package br.gov.frameworkdemoiselle.timestamp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;

import br.gov.frameworkdemoiselle.certificate.Priority;
import br.gov.frameworkdemoiselle.certificate.exception.CertificateCoreException;
import br.gov.frameworkdemoiselle.certificate.timestamp.TimeStampGenerator;
import br.gov.frameworkdemoiselle.timestamp.connector.TimeStampOperator;

@Priority(Priority.MAX_PRIORITY)
public class MyTimestampGeneratorImpl implements TimeStampGenerator {

	private ResourceBundle bundle = ResourceBundle.getBundle("config");
	
	private byte[] content;
	

	public void initialize(byte[] content, PrivateKey privateKey,
			Certificate[] certificates) throws CertificateCoreException {
		this.content = content;
	}

	public byte[] generateTimeStamp() throws CertificateCoreException {

		byte[] timestamp = null;
		HttpURLConnection connection = null;
		
		try {
			// Cria a conexão com o serviço que requisita o carimbo de Tempo
			URL url = new URL(bundle.getString("url"));

			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type","application/octet-stream");
			
			// Envia o conteúdo
			OutputStream os = connection.getOutputStream();
			os.write(content);
			os.flush();
			os.close();
			
			// Trata o status da conexão
			int status = connection.getResponseCode();
			System.err.println(status + " - "+ bundle.getString("url"));
			
			if (status == 200) {
				if (connection.getContentType().equals("application/octet-stream")) {
					InputStream is = connection.getInputStream();
					timestamp = IOUtils.toByteArray(is);
					is.close();
				}
			}
			
			if (status == 500) {
				if (connection.getContentType().equals("text/plain")) {
					String message = IOUtils.toString(connection.getErrorStream());
					throw new CertificateCoreException(message);
				}
			}

			if ((status == 403) || (status == 401)){
				throw new CertificateCoreException("Acesso negado: " + bundle.getString("url"));
			}

			if ((status == 404)){
				throw new CertificateCoreException("URL não encontrada: " + bundle.getString("url"));
			}			
			
			if (timestamp == null){
				throw new CertificateCoreException("O carimbo de tempo não foi gerado");
			}
			
			
		} catch ( ConnectException e) {
			throw new CertificateCoreException("Erro ao conectar: " + bundle.getString("url"));
		} catch ( IOException e) {
			throw new RuntimeException(e);			
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

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
