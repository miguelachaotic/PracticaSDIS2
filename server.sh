java -cp ./build -Djavax.net.ssl.keyStore=Server_Keystore.p12 \
  -Djavax.net.ssl.keyStorePassword=servidorpass \
  -Dfile.encoding=UTF-8 \
  instagram.rmi.server.ServerLauncher
