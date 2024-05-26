java -cp . -Djavax.net.ssl.keyStore=Server_Keystore.p12 \
  -Djavax.net.ssl.keyStorePassword=servidorpass \
  -Dfile.encoding=UTF-8 \
  -Djavax.net.ssl.trustStore=Client_Truststore.p12 \
  -Djavax.net.ssl.trustStorePassword=clientepass \
  -Djava.rmi.server.hostname=127.0.0.1 \
  instagram.rmi.server.ServerLauncher
