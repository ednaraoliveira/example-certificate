<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link
	href="/demoiselle-certificate-applet-customizada-web/resources/styles/default.css"
	rel="stylesheet" type="text/css" />
<title>Projeto Exemplo</title>
</head>
<script type="text/javascript">
	function foo() {
		alert("foo() method called!");
	}

	function teste(){
		alert("Token");
	}
</script>

<body>
	<form id="mainForm" name="mainForm" method="post" action="">
		<table>
			<tr>
				<td><h1>Applet Exemplo de Assinatura Digital</h1></td>
			</tr>
			<tr>
				<td><applet
				code="br.gov.frameworkdemoiselle.certificate.applet.view.JPanelApplet"
				width=480 height=350 MAYSCRIPT
				archive="certificate-applet-1.0.1-assinado.jar,
                                demoiselle-certificate-applet-2.0.0-SNAPSHOT-assinado.jar,
                                demoiselle-certificate-core-2.0.0-SNAPSHOT-assinado.jar,
                                demoiselle-certificate-signer-2.0.0-SNAPSHOT-assinado.jar,
                                demoiselle-certificate-policy-engine-2.0.0-SNAPSHOT-assinado.jar,
                                demoiselle-certificate-timestamp-2.0.0-SNAPSHOT-assinado.jar,
                                demoiselle-certificate-criptography-2.0.0-SNAPSHOT-assinado.jar,
                                demoiselle-certificate-ca-icpbrasil-2.0.0-SNAPSHOT-assinado.jar,
                                demoiselle-certificate-ca-icpbrasil-homologacao-2.0.0-SNAPSHOT-assinado.jar,
                                bcprov-jdk15on-1.51-assinado.jar,
                                bcpkix-jdk15on-1.51-assinado.jar,
                                bcmail-jdk15on-1.51-assinado.jar,
                                log4j-1.2.17-assinado.jar,
                                slf4j-api-1.6.1-assinado.jar,
                                slf4j-log4j12-1.6.1-assinado.jar,
                                plugin-assinado.jar,
                                httpclient-4.3.4-assinado.jar,
                                httpcore-4.3.2-assinado.jar,
                                commons-io-1.3.2-assinado.jar,
                                commons-logging-1.1.3-assinado.jar">

						<param name="factory.applet.action"
							value="br.gov.frameworkdemoiselle.applet.App" />
						<param name="applet.javascript.postaction.failure" value="foo" />

						<param name="label.dialog.button.run" value="Assinar" />
						<param name="config.dialog.button-run.height" value="40" />
						<param name="config.dialog.button-cancel.height" value="40" />
						<param name="config.dialog.button-run.width" value="130" />
						<param name="config.dialog.button-cancel.width" value="130" />

						<param name="credentials"
							value="javascript:sessionStorage.getItem('credentials')" />

						No Applet
					</applet></td>
			</tr>
			<tr>
				<td>
					<table width="100%">
						<tr>
							<th colspan="2">Escolha o arquivo a ser assinado</th>
						</tr>
						<tr>
							<td>Documento:</td>
							<td><input type="file" name="documento" value=""></td>
							<td><input type="hidden" name="credentials" value="xxx"></td>
						</tr>
						<tr>
							<th colspan="2">Dados extraídos do certificado digital</th>
						</tr>
						<tr>
							<td>CPF:</td>
							<td><input type="text" name="cpf" value="" size="11"
								disabled="true"></td>
						</tr>
						<tr>
							<td>Nome:</td>
							<td><input type="text" name="nome" value="" size="30"
								disabled="true"></td>
						</tr>
						<tr>
							<td>Email:</td>
							<td><input type="text" name="email" value="" size="30"
								disabled="true"></td>
						</tr>
						<tr>
							<td>Nascimento:</td>
							<td><input type="text" name="nascimento" value="" size="8"
								disabled="true"></td>
						</tr>
					</table>
			</tr>
		</table>

		<button onclick="myFunction()">Try it</button>

		<script>
			function myFunction() {
				txt = sessionStorage.getItem('credentials');
				alert(">> " + txt);
			}
		</script>

	</form>
</body>
</html>
