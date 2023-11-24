# Proyecto de Encriptación y Desencriptación

Este proyecto de Java se enfoca en operaciones de encriptación y desencriptación de archivos. A continuación, se presenta una descripción general del proyecto, sus componentes y cómo usarlo.

## Contenido

1. [Introducción](#introducción)
2. [Componentes del Proyecto](#componentes-del-proyecto)
    - [Carpeta `Files_to_decrypt`](#carpeta-files_to_decrypt)
    - [Carpeta `Files_to_encrypt`](#carpeta-files_to_encrypt)
    - [Carpeta `keys`](#carpeta-keys)
    - [Clase `AES.java`](#clase-aesjava)
    - [Clase `AppConfig.java`](#clase-appconfigjava)
    - [Clase `EncyptionDecryption.java`](#clase-encyptiondecryptionjava)
    - [Interfaz `Interface_proyecto.java`](#interfaz-interface_proyectojava)
    - [Clase `menu.java`](#clase-menujava)
    - [Archivo `paths.env`](#archivo-pathsenv)
3. [Cómo Usar](#cómo-usar)

## Introducción

Este proyecto Java se centra en la encriptación y desencriptación de archivos. Ofrece un menú interactivo para realizar operaciones de encriptación y desencriptación. A continuación, se describen los componentes clave del proyecto y cómo se utilizan.

## Componentes del Proyecto

### Carpeta `Files_to_decrypt`

- Esta carpeta almacena archivos que se pueden desencriptar utilizando el proyecto.

### Carpeta `Files_to_encrypt`

- En esta carpeta se colocan archivos que se pueden encriptar utilizando el proyecto.

### Carpeta `keys`

- Esta carpeta almacena claves secretas utilizadas en las operaciones de encriptación y desencriptación.

### Clase `AES.java`

- Proporciona funciones para encriptar y desencriptar texto utilizando el algoritmo AES (Advanced Encryption Standard).

### Clase `AppConfig.java`

- Gestiona la configuración y las rutas de directorio para el proyecto, cargando variables de entorno desde el archivo `paths.env`.

### Clase `EncyptionDecryption.java`

- Ofrece funciones para abrir archivos, recuperar texto de archivos, almacenar texto en archivos y convertir cadenas en claves secretas.

### Interfaz `Interface_proyecto.java`

- Interfaz que define métodos que se pueden implementar en otras clases del proyecto.

### Clase `menu.java`

- Implementa un menú interactivo que permite al usuario encriptar y desencriptar archivos. Utiliza las clases mencionadas anteriormente.

### Archivo `paths.env`

- Archivo de configuración que almacena las rutas de directorio utilizadas por el proyecto.

## Cómo Usar

1. Asegúrese de configurar las rutas de directorio en el archivo `paths.env`. Estas rutas se utilizan para almacenar archivos encriptados, desencriptados y claves secretas.

2. Ejecute el programa utilizando la clase `menu.java`. El menú interactivo le permitirá seleccionar entre encriptar archivos, desencriptar archivos o salir del programa.

3. Siga las instrucciones proporcionadas por el menú para realizar las operaciones de encriptación y desencriptación.

4. Los archivos a encriptar deben colocarse en la carpeta `Files_to_encrypt`, y los archivos a desencriptar se almacenan en la carpeta `Files_to_decrypt`.

5. Las claves secretas se almacenan en la carpeta `keys`. Asegúrese de mantener estas claves seguras.

Este proyecto proporciona una forma flexible de encriptar y desencriptar archivos utilizando el algoritmo AES y es útil para aplicaciones de seguridad de datos.
