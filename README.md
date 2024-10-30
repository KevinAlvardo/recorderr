# Recorder - Aplicación de Grabación y Reproducción de Videos en Android

Esta aplicación permite a los usuarios grabar videos, almacenarlos en la memoria interna del dispositivo, y reproducirlos dentro de la misma aplicación. También incluye una funcionalidad para ver una lista de videos grabados y reproducir cualquiera de ellos.

## Funcionalidades

- **Grabar Video**: Utiliza la cámara del dispositivo para capturar videos.
- **Guardar Videos**: Los videos se guardan en una carpeta específica de la aplicación en la memoria interna del dispositivo.
- **Reproducir Videos**: Permite reproducir los videos almacenados desde la aplicación.
- **Ver Lista de Videos**: Muestra todos los videos grabados por la aplicación en una lista, permitiendo seleccionar y reproducir cualquiera.
- **Reproducir Último Video**: Reproduce directamente el video más reciente grabado.

## Requerimientos Técnicos

- **Versión mínima de Android**: 7.0 (Nougat)
- **Herramientas**: 
  - **CameraX** para la grabación de video.
  - **VideoView** para la reproducción de video.
  - **FileProvider** para acceso seguro a los archivos.
- **Permisos**:
  - Cámara (`android.permission.CAMERA`)
  - Grabación de audio (`android.permission.RECORD_AUDIO`)
  - Almacenamiento (lectura y escritura para versiones anteriores a Android 13)

## Arquitectura

La aplicación está dividida en varias actividades:

- **MainActivity**: Pantalla principal con botones para grabar un video, ver la lista de videos y reproducir el último video.
- **RecordVideoActivity**: Permite grabar un video utilizando CameraX y almacenarlo en la carpeta específica de la aplicación.
- **VideoListActivity**: Muestra una lista de todos los videos grabados, donde el usuario puede seleccionar uno para reproducir o eliminar.
- **LastVideoPlayerActivity**: Reproduce el video más reciente.
- **PlayVideoActivity**: Reproduce un video seleccionado de la lista.

## Instalación

1. Clonar este repositorio en tu máquina local:
   ```bash
   git clone https://github.com/KevinAlvardo/recorderr.git
2.Abrir el proyecto en Android Studio.
Conectar un dispositivo Android o usar un emulador compatible.
Ejecutar la aplicación desde Android Studio.
Permisos
La aplicación requiere permisos para acceder a la cámara, grabar audio y leer/guardar archivos en el almacenamiento del dispositivo. Estos permisos se solicitarán al usuario en tiempo de ejecución.

Uso
Grabar un Video: En la pantalla principal, presionar "Grabar Video" para abrir la cámara y comenzar la grabación.
Ver Videos Guardados: Seleccionar "Ver Videos Guardados" para acceder a una lista de los videos grabados. Desde aquí, puedes seleccionar y reproducir cualquiera de los videos o eliminarlos.
Reproducir Último Video: Presionar "Reproducir Último Video" para ver el video más reciente grabado.
Capturas de Pantalla
(afuera en la carpeta del proyecto.)

Consideraciones
Gestión de Permisos: La aplicación maneja permisos en tiempo de ejecución para asegurar una experiencia segura y sin interrupciones.
Almacenamiento de Archivos: Los videos se guardan en la carpeta Android/media/com.example.rocord/Rocord para facilitar el acceso y la gestión.
Eliminación de Videos: La aplicación incluye una opción para eliminar videos directamente desde la lista.
Contribuciones
Las contribuciones son bienvenidas. Por favor, crea una nueva rama y envía un pull request con tus mejoras.

Licencia
Este proyecto está bajo la licencia MIT.