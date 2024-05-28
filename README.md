# Segunda práctica de Sistemas Distribuidos

## Introducción

Este proyecto es la segunda práctica de la asignatura Sistemas Distribuidos de la Universidad de Valladolid, España. Usa la tecnología Java RMI.
Consiste en una aplicación cliente - servidor de plataforma de contenidos. El servidor dispone de unos videos que los usuarios pueden solicitar. 
Los usuarios también pueden subir al servidor objetos 'Media' que representan una publicación abstracta. Para interactuar con el Servidor se disponen de dos scripts para ejecutar los archivos de forma sencilla. El servidor dispone de unos videos iniciales, ya disponibles desde el lanzamiento, con los siguientes nombres: SalsaDelGallo, Patatas y CreeperZombie.

## Requerimientos

Funciona en cualquier versión de Java desde la 11, por lo que es necesario tener jdk11 y el soporte de ejecución de Java 11.

## Instalación

Primero, clona el repositorio en tu máquina local. Requiere tener git instalado en local.

```
git clone https://miguelachaotic/PracticaSDIS2
```

Una vez clonado el repositorio en tu máquina, sitúate en el repositorio clonado y compila el proyecto con el siguiente comando:

```
javac $(find . -name "*.java") -d .
```

# Uso de los programas

## Servidor

Para lanzar el servidor dispones del script **_server.sh_**. Este script recibe un argumento obligatorio que es la dirección IP que usa el servidor para recibir conexiones. El proyecto debe estar compilado antes de ejecutar este comando.

Ante cualquier duda dispone del argumento *_-h_* o *_--help_*.

Un ejemplo de ejecución en local de este script es de la siguiente forma:

```
./server.sh 127.0.0.1
```


## Cliente

### Stream

Para usar las funciones de Stream, más una función adicional que consiste en un cliente interactivo usaremos el script **_stream.sh_**.

Este script es ligeramente más complicado. Para que funcione correctamente son necesarios 3 argumentos obligatorios siempre. El primero de ellos es la clase a ejecutar, o el cliente que se desea ejecutar. Hay 4 de ellos:

- ClienteInteractivo
- PlayList
- PlayRandomStory
- PlayStory \<Media\>

Los otros dos argumentos son respectivamente la IP del cliente y la IP del servidor. En el caso de PlayStory recibe un argumento adicional que es el contenido que se quiere reproducir.

Importante pasar el nombre de la clase sin la extensión '.class'.  Un ejemplo de ejecución suponiendo que el cliente tiene la IP *_192.168.1.20_* y la IP del servidor es *_192.168.1.50_*:
```
./stream.sh ClienteInteractivo 192.168.1.20 192.168.1.50
```

También admite los argumentos *_-h_* y *_--help_* para información adicional.

### Directory

Para usar el resto de funciones, distintas del streaming, se usa el script **_unit.sh_**.

Este script requiere los mismos argumentos obligatorios que el anterior, nombre de la clase, dirección IP del cliente y dirección IP del servidor. Si coloco un argumento entre los símbolos \< \> es que es un argumento obligatorio para esa clase. Si en cambio está entre [ ] entonces es un argumento opcional.
Las clases disponibles junto con sus argumentos son las siguientes:

- Add2l \<Media\> [Username]
- AddComment \<Media\> "\<Comment\>"
- AddLike \<Media\>
- Auth \<Username\> \<Password\>
- Deletel \<Username\>
- GetDirectoryList
- Hello
- Peekl [Username]
- Readl [Username]
- RetrieveMedia \<Media\>


Este script también dispone de los argumentos *_-h_* y *_--help_* para más información.


























