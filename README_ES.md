# Krypzo Client — compilación en la nube

Este paquete conserva los archivos fuente que se recibieron y añade los archivos Gradle y un workflow de GitHub Actions.

## Compilar sin instalar Java ni Gradle en tu PC

1. Crea un repositorio en GitHub y sube el contenido completo de esta carpeta, incluida la carpeta oculta `.github`.
2. Abre **Actions** y selecciona **Build Krypzo Client**. Pulsa **Run workflow** si está disponible, o espera a que el primer push ejecute el flujo.
3. Si la compilación termina correctamente, abre esa ejecución y descarga el artefacto `krypzo-client-jar`.
4. Extrae el ZIP del artefacto para encontrar el JAR en su interior.

## Importante

- La configuración usa Minecraft 26.2, Java 25, Fabric Loader 0.19.3, Fabric API 0.155.2+26.2, Loom 1.17.13 y Gradle 9.5.1; si el repositorio de Maven no ofrece una de esas versiones, habrá que ajustar la configuración.
- El código recibido aún no se ha compilado ni probado. Puede tener errores de API para Minecraft 26.2; el log de GitHub Actions mostrará los errores exactos.
- El código actual muestra HUD/menú y algunas utilidades visuales de prototipo; no todas las opciones del menú están implementadas como funciones completas.
- El workflow genera un JAR, no un instalador EXE.
