#!/bin/bash
if [[ $1 == "-h" || $1 == "--help" ]]; then

  echo "Para usar este script de lanzado de clientes unitarios, es necesario especificar siempre tres (3) argumentos, el nombre de la clase que se va a lanzar, sin la extensión '.class', la IP que usa el cliente, y la IP que usa el servidor. Un posible uso sería el siguiente:"
  echo "./unit.sh Add2l 127.0.0.1 127.0.0.1 <Media> [Username]"
  echo "Es posible que el comando que se quiera lanzar también tenga hasta 2 argumentos adicionales, aquí se le mostrará la lista con las clases y los argumentos que recibe cada una:"
  echo "Add2l <Media> [Username]"
  echo "Readl [Username]" 
  echo "Peekl [Username]"
  echo "Auth <Username> <Password>"
  echo "Deletel <Username>"
  echo "GetDirectoryList"
  echo "RetrieveMedia <Media>"
  echo "AddLike <Media>"
  echo "AddComment <Media> <Comment>"
  exit 0
fi

if [ -z $1 ]; then
  echo "Uso: $0 <Clase> <ClientIP> <ServerIP> [OptArgs] ..."
  exit 0
fi


java -cp . -Djavax.net.ssl.trustStore=Client_Truststore.p12 \
  -Djavax.net.ssl.trustStorePassword=clientepass \
  -Dfile.encoding=UTF-8 \
  -Djavax.net.ssl.keyStore=Server_Keystore.p12 \
  -Djava.rmi.server.hostname=$2 \
  -Djavax.net.ssl.keyStorePassword=servidorpass instagram.rmi.client.directory.unit.$1 $3 $4 $5
