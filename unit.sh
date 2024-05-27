java -cp . -Djavax.net.ssl.trustStore=Client_Truststore.p12 \
  -Djavax.net.ssl.trustStorePassword=clientepass \
  -Dfile.encoding=UTF-8 \
  -Djavax.net.ssl.keyStore=Server_Keystore.p12 \
  -Djava.rmi.server.hostname=$2 \
  -Djavax.net.ssl.keyStorePassword=servidorpass instagram.rmi.client.directory.unit.$1 $3 $4 $5
