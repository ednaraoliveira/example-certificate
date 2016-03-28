package br.gov.serpro.jnlp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 07721825741
 */
@WebServlet(name = "testServlet", urlPatterns = {"/generatedjnlp"})
public class JnlpServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("br.gov.serpro.jnlp.servlet.JnlpServlet.doGet()");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("br.gov.serpro.jnlp.servlet.JnlpServlet.doPost()");
        response.setHeader("content-type", "application/x-java-jnlp-file");

        String identificador = request.getParameter("hash");
        String servico = request.getParameter("service");

        System.out.println("Identificador ...: " + identificador);
        System.out.println("Servico .........: " + servico);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(generate(identificador, servico));
        } catch (IOException ex) {
            Logger.getLogger(JnlpServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String generate(String identificador, String servico) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        sb.append("<jnlp codebase=\"http://localhost:8080/certificate-jws-web/\" spec=\"1.0+\">\n");
        sb.append("<information>\n");
        sb.append("<title>Assinador Digital de Documentos</title>\n");
        sb.append("<vendor>Servico Federal de Processamento de Dados</vendor>\n");
        sb.append("<description>Descricao...</description>\n");
        sb.append("<homepage href=\"http://www.serpro.gov.br\"/>\n");
        sb.append("</information>\n");
        sb.append("<security>\n");
        sb.append("<all-permissions/>\n");
        sb.append("</security>\n");
        sb.append("<resources>\n");
        sb.append("<j2se version=\"1.7+\"/>\n");
        sb.append("<jar href=\"certificate-jws-1.0.0-SNAPSHOT.jar\" main=\"true\"/>\n");
        sb.append("<jar href=\"demoiselle-certificate-core-1.0.12-SNAPSHOT.jar\"/>\n");
        sb.append("<jar href=\"demoiselle-certificate-signer-1.0.12-SNAPSHOT.jar\"/>\n");
        sb.append("<jar href=\"demoiselle-certificate-criptography-1.0.12-SNAPSHOT.jar\"/>\n");
        sb.append("<jar href=\"demoiselle-certificate-ca-icpbrasil-1.0.12-SNAPSHOT.jar\"/>\n");
        sb.append("<jar href=\"demoiselle-certificate-desktop-1.0.12-SNAPSHOT.jar\"/>\n");
        sb.append("<jar href=\"slf4j-api-1.6.1.jar\"/>\n");
        sb.append("<jar href=\"bcprov-jdk15-1.45.jar\"/>\n");
        sb.append("<jar href=\"bcmail-jdk15-1.45.jar\"/>\n");
        sb.append("<property name=\"jnlp.identifier\" value=\"").append(identificador).append("\"/>\n");
        sb.append("<property name=\"jnlp.service\" value=\"").append(servico).append("\"/>\n");
        sb.append("<!-- O parametro abaixo define a classe customizada de implementacao. Se ausente, sera usada a implementacao pre-definida no componente -->").append("\n");
        sb.append("<property name=\"jnlp.myClassName\" value=\"").append("sample.App").append("\"/>\n");
        sb.append("</resources>\n");
        sb.append("<application-desc main-class=\"br.gov.frameworkdemoiselle.certificate.ui.view.Principal\"/>\n");
        sb.append("</jnlp>");

        return sb.toString();
    }
}
