java -cp build -Djavax.net.ssl.trustStore=Client_Truststore.p12 \
  -Djavax.net.ssl.trustStorePassword=clientepass \
  -Dfile.encoding=UTF-8 \
  instagram.rmi.client.directory.unit.ClienteNormal
