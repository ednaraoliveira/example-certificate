package br.gov.frameworkdemoiselle.certificate.sample.applet;

import java.applet.Applet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.applet.action.AbstractAppletExecute;
import br.gov.frameworkdemoiselle.certificate.applet.certificate.ICPBrasilCertificate;
import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;
import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory;

public class App extends AbstractAppletExecute{
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	@Override
	public void execute(KeyStore keystore, String alias, Applet applet) {
		try {
            /* Exibe alguns dados do certificado */
            ICPBrasilCertificate certificado = super.getICPBrasilCertificate(keystore, alias, false);
            AbstractAppletExecute.setFormField(applet, "mainForm", "cpf", certificado.getCpf());
            AbstractAppletExecute.setFormField(applet, "mainForm", "nome", certificado.getNome());
            AbstractAppletExecute.setFormField(applet, "mainForm", "nascimento", certificado.getDataNascimento());
            AbstractAppletExecute.setFormField(applet, "mainForm", "email", certificado.getEmail());
            
            
            /* Carregando o conteudo a ser assinado */
            String documento = AbstractAppletExecute.getFormField(applet, "mainForm", "documento");

            if (documento.length() == 0) {
                JOptionPane.showMessageDialog(applet, "Por favor, escolha um documento para assinar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String path = new File(documento).getAbsolutePath();
            byte[] content = readContent(path);
            logger.info("Path.........: {}", path );

            
            /* Parametrizando o objeto doSign */
            PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
            signer.setCertificates(keystore.getCertificateChain(alias));
            signer.setPrivateKey((PrivateKey) keystore.getKey(alias, null));
            signer.setAttached(true);
            signer.setSignaturePolicy(PolicyFactory.Policies.AD_RT_CADES_2_1);
            
            /* Realiza a assinatura do conteudo */
            logger.info("Efetuando a  assinatura do conteudo");
            byte[] signed = signer.doSign(content);

            /* Grava o conteudo assinado no disco */
            writeContent(signed, documento.concat(".p7s"));

            /* Valida o conteudo */
            logger.info("Efetuando a validacao da assinatura.");
            boolean checked = signer.check(content, signed);

            if (checked) {
                JOptionPane.showMessageDialog(applet, "O arquivo foi assinado e validado com sucesso.", "Mensagem", JOptionPane.INFORMATION_MESSAGE);
            } else {
            	JOptionPane.showMessageDialog(applet, "Assinatura inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            }            
            
        } catch (KeyStoreException e) {
            JOptionPane.showMessageDialog(applet, e.getMessage(), "Error",  JOptionPane.ERROR_MESSAGE);
        } catch (UnrecoverableKeyException e) {
        	JOptionPane.showMessageDialog(applet, e.getMessage(), "Error",  JOptionPane.ERROR_MESSAGE);
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(applet, e.getMessage(), "Error",  JOptionPane.ERROR_MESSAGE);
		}
		
	}

	@Override
	public void cancel(KeyStore keystore, String alias, Applet applet) {
		// TODO Auto-generated method stub
		
	}
	
    private byte[] readContent(String arquivo) {
        byte[] result = null;
        try {
            File file = new File(arquivo);
            FileInputStream is = new FileInputStream(file);
            result = new byte[(int) file.length()];
            is.read(result);
            is.close();

        } catch (IOException ex) {
            logger.info(ex.getMessage());
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
        } catch (IOException ex) {
            logger.info(ex.getMessage());
        }
    }	
}
