#!/bin/bash

clear

echo $JAVA_HOME

SCRIPT=$(readlink -f "$0")
SCRIPTPATH=$(dirname "$SCRIPT")

PATH=$JAVA_HOME/bin:$PATH

#NAO ALTERAR!
DSANAME="SERPRO"

#PS: Mudar para um certificado de máquina para assinatura de código
DIGITAL_CERTIFICATE="applet_keystore.jks"

#Efetua a leitura do Pin do Token/Smartcard
echo -n "Digite a senha do seu Token /Smartcard, seguido de [ENTER]:"
read -s PASSWORD

ARTIFACT_VERSION="2.0.0"

JARPATH=$SCRIPTPATH"/assinados/"

#Parametros customizados a serem incluidos no manifesto
manifest_addition="manifest-addition.txt"

jar_certificate_applet="certificate-applet-1.0.0.jar"
jar_security_applet="demoiselle-certificate-applet-2.0.0-SNAPSHOT.jar"
jar_security_core="demoiselle-certificate-core-2.0.0-SNAPSHOT.jar"
jar_security_signer="demoiselle-certificate-signer-2.0.0-SNAPSHOT.jar"
jar_security_policy_engine="demoiselle-certificate-policy-engine-2.0.0-SNAPSHOT.jar"
jar_security_timestamp="demoiselle-certificate-timestamp-2.0.0-SNAPSHOT.jar"
jar_security_criptography="demoiselle-certificate-criptography-2.0.0-SNAPSHOT.jar"
jar_security_ca_icpbrasil="demoiselle-certificate-ca-icpbrasil-2.0.0-SNAPSHOT.jar"
jar_security_ca_icpbrasil_hom="demoiselle-certificate-ca-icpbrasil-homologacao-2.0.0-SNAPSHOT.jar"

echo $JARPATH

#remove os artefatos antigos da aplicacao
echo "Removendo os artefatos antigos."
for jarfile in $(ls $JARPATH/*.jar); do
	rm $jarfile
done

#Copia os componentes de seguranca demoiselle
echo "Copiando os componentes de seguranca demoiselle."
cp $SCRIPTPATH/$jar_certificate_applet $JARPATH
#cp $SCRIPTPATH/$jar_security_applet $JARPATH
#cp $SCRIPTPATH/$jar_security_core $JARPATH
#cp $SCRIPTPATH/$jar_security_signer $JARPATH
#cp $SCRIPTPATH/$jar_security_policy_engine $JARPATH
#cp $SCRIPTPATH/$jar_security_timestamp $JARPATH
#cp $SCRIPTPATH/$jar_security_criptography $JARPATH
#cp $SCRIPTPATH/$jar_security_ca_icpbrasil $JARPATH
#cp $SCRIPTPATH/$jar_security_ca_icpbrasil_hom $JARPATH


#Copia o plugin.jar
echo "Copiando o plugin.jar"
#cp $JAVA_HOME/jre/lib/plugin.jar $JARPATH

#copia o Bouncy Castle
#echo "Copiando o Bouncy Castle"
#cp $SCRIPTPATH/bcprov-jdk15on-1.51.jar $JARPATH
#cp $SCRIPTPATH/bcmail-jdk15on-1.51.jar $JARPATH
#cp $SCRIPTPATH/bcpkix-jdk15on-1.51.jar $JARPATH

#Copia o slf4j e log4j
#echo "Copiando demais dependencias"
#cp $SCRIPTPATH/log4j-1.2.17.jar $JARPATH
#cp $SCRIPTPATH/slf4j-api-1.6.1.jar $JARPATH
#cp $SCRIPTPATH/slf4j-log4j12-1.6.1.jar $JARPATH
#cp $SCRIPTPATH/commons-codec-1.6.jar $JARPATH
#cp $SCRIPTPATH/commons-io-1.3.2.jar $JARPATH
#cp $SCRIPTPATH/commons-logging-1.1.3.jar $JARPATH
#cp $SCRIPTPATH/httpclient-4.3.4.jar $JARPATH
#cp $SCRIPTPATH/httpcore-4.3.2.jar $JARPATH
#cp $SCRIPTPATH/myTimestampImpl-0.0.1-SNAPSHOT.jar $JARPATH
#cp $SCRIPTPATH/jackson-core-asl-1.9.9.jar $JARPATH
#cp $SCRIPTPATH/jackson-mapper-asl-1.9.9.jar $JARPATH

for jarfile in $(ls $JARPATH/*.jar); do

	jarfile_signed="${jarfile%.jar}-assinado.jar"
	
	if [[ "${jarfile}" == *bc* ]]; then
		echo "Removendo as assinaturas anteriores que possam existir nos artefatos"
        	zip -d $jarfile /META-INF/BCKEY.* /META-INF/SERPRO.*
	fi

	#Inclui os dados adicionais de seguranca do jar
	$JAVA_HOME/bin/jar uvfm $jarfile $manifest_addition

	$JAVA_HOME/bin/jarsigner -keystore applet_keystore.jks -keypass $PASSWORD -signedjar $jarfile_signed $jarfile applet_alias

	#Remove os artefatos nao assinados
	rm $jarfile
done

exit 0
