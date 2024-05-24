java -cp . -Djavax.net.ssl.keyStore=Server_Keystore.p12 \                                            
  5   -Djavax.net.ssl.keyStorePassword=servidorpass \                                                    
  4   -Dfile.encoding=UTF-8 \                                                                            
  3   -Djavax.net.ssl.trustStore=Client_Truststore.p12 \                                                 
  2   -Djavax.net.ssl.trustStorePassword=clientepass \                                                   
  1   -Djava.rmi.server.hostname=$1 \                                                                    
7     instagram.rmi.server.ServerLauncher $1 
