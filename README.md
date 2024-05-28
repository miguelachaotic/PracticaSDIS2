# Segunda práctica de Sistemas Distribuidos

## Introducción

Este proyecto es la segunda práctica de la asignatura Sistemas Distribuidos de la Universidad de Valladolid, España. Usa la tecnología Java RMI.
Consiste en una aplicación cliente - servidor de plataforma de contenidos. El servidor dispone de unos videos que los usuarios pueden solicitar. 
Los usuarios también pueden subir al servidor objetos 'Media' que representan una publicación abstracta. Para interactuar con el Servidor se disponen de dos scripts para ejecutar los archivos de forma sencilla.

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

Para usar las funciones de Stream, más una función adicional que consiste en un cliente interactivo usaremos el script




























