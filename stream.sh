#!/bin/bash
if [[ $1 == "-h" || $1 == "--help" ]]; then
  echo "Este es el script que lanza los clientes relacionados con el Streaming de video más un cliente interactivo capaz de realizar todas las operaciones."
  echo "Para lanzarlo correctamente se necesitan 3 argumentos obligatorios, el primer argumento es el nombre de la clase que se quiere lanzar, sin la extensión '.class', el segundo argumento es la dirección IP del cliente, y el tercer argumento la dirección IP del servidor al que se quieren conectar."
  echo "Hay un posible cuarto argumento que solo se usa en la clase 'PlayStory', que es para seleccionar el contenido que se quiere reproducir."
  echo "Las posibles clases junto con los argumentos que reciben son las siguientes:"
  echo "ClienteInteractivo"
  echo "PlayList"
  echo "PlayRandomStory"
  echo "PlayStory <Media>"
  exit 0
fi

if [ -z $1 ]; then
  echo "Uso: $0 <Clase> <IPCliente> <IPServidor> [OptArg]"
  exit 0
fi

java -cp . -Djavax.net.ssl.trustStore=Client_Truststore.p12 \
  -Djavax.net.ssl.trustStorePassword=clientepass \
  -Dfile.encoding=UTF-8 \
  -Djavax.net.ssl.keyStore=Server_Keystore.p12  \
  -Djavax.net.ssl.keyStorePassword=servidorpass \
  -Djava.rmi.server.hostname=$2 \
  instagram.rmi.client.stream.unit.$1 $3 $4

