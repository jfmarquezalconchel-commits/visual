# Instalación de FlatLaf - Guía de Configuración

## Dependencia Maven

La librería **FlatLaf** (com.formdev:flatlaf) se ha añadido al `pom.xml` con la siguiente configuración:

```xml
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.2.5</version>
</dependency>
```

## Instalación Automática

Maven descarga automáticamente la dependencia cuando ejecutas:

```bash
# Descargar dependencias
mvn dependency:resolve

# Compilar (descarga dependencias si falta)
mvn clean compile

# Empacar la aplicación (incluye dependencias)
mvn clean package
```

## Ubicación Local

Una vez descargada, la librería FlatLaf se encuentra en:
```
~/.m2/repository/com/formdev/flatlaf/3.2.5/flatlaf-3.2.5.jar
```

## Verificación

Para verificar que FlatLaf está instalado correctamente:

```bash
# Comprobar si existe el JAR
find ~/.m2/repository -name "flatlaf*.jar"

# Compilar el proyecto (verificará que la dependencia es accesible)
mvn clean compile
```

## Uso en el Código

La librería se importa y configura en `Main.java`:

```java
import com.formdev.flatlaf.FlatDarkLaf;

// En el método main()
UIManager.setLookAndFeel(new FlatDarkLaf());
```

## Temas Disponibles

FlatLaf proporciona varios temas que puedes usar cambiando la clase:

- `FlatLightLaf` - Tema claro
- `FlatDarkLaf` - Tema oscuro (actual)
- `FlatIntelliJLaf` - Tema IntelliJ
- `FlatDarculaLaf` - Tema Dracula

## Solución de Problemas

Si Maven no descarga la dependencia:

1. Verifica conexión a internet
2. Limpia la caché de Maven:
   ```bash
   mvn clean
   rm -rf ~/.m2/repository/com/formdev
   ```
3. Ejecuta nuevamente:
   ```bash
   mvn clean package
   ```

## Referencias

- Sitio oficial: https://www.formdev.com/flatlaf/
- Repositorio Maven Central: https://mvnrepository.com/artifact/com.formdev/flatlaf
- GitHub: https://github.com/JFormDesigner/FlatLaf

