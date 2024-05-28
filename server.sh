#!/bin/bash
if [[ $1 == "-h" || $1 == "--help" ]]; then
  echo "Este es el script que lanza el servidor. Para que funcione correctamente se le debe pasar un parámetro, que es la dirección IP a la que se conectarán los clientes. El servidor debe ser propietario de esa dirección IP."
  echo "Un ejemplo sería el siguiente:"
  echo "$0 127.0.0.1"
  exit 0
fi


if [ -z $1 ]; then
  echo "Uso: $0 <ServerIP>"
  exit 0
fi


java -cp . -Djavax.net.ssl.keyStore=Server_Keystore.p12 \
  -Djavax.net.ssl.keyStorePassword=servidorpass \
  -Dfile.encoding=UTF-8 \
  -Djavax.net.ssl.trustStore=Client_Truststore.p12 \
  -Djavax.net.ssl.trustStorePassword=clientepass \
  -Djava.rmi.server.hostname=$1 \
  instagram.rmi.server.ServerLauncher $1 
