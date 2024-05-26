java -cp . -Djavax.net.ssl.trustStore=Client_Truststore.p12 \
  -Djavax.net.ssl.trustStorePassword=clientepass \
  -Dfile.encoding=UTF-8 \
  -Djavax.net.ssl.keyStore=Server_Keystore.p12  \
  -Djavax.net.ssl.keyStorePassword=servidorpass \
  -Djava.rmi.server.hostname=127.0.0.1 \
  instagram.rmi.client.stream.unit.PlayList