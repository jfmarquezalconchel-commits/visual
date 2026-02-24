# Visual (ejemplo)

Aplicación de ejemplo en Java Swing con arquitectura MVC.

## Estructura
- src/main/java/org/hardcoder
  - Main.java (arranque y wiring) 
  - DocumentModel.java
  - EditorController.java
  - ImageController.java
  - ImagePanel.java
  - NuevoPanel.java
  - BuscarPanel.java
  - BuscarReemplazarPanel.java
  - ConfigPanel.java
  - SobrePanel.java

- src/test/java/org/hardcoder
  - EditorControllerTest.java
  - ImageControllerTest.java

## Requisitos
- JDK 17
- Maven
- Entorno gráfico para ejecutar la UI (DISPLAY en Linux)

## Comandos
Compilar y empacar:

```bash
mvn -DskipTests package
```

Ejecutar tests:

```bash
mvn test
```

Ejecutar la aplicación:

```bash
java -cp target/classes org.hardcoder.Main
```

## Notas
- Se usa `DocumentModel` y `EditorController` para separar la lógica de negocio de la UI.
- `ImageController` carga imágenes usando `ImageIO`.
- Logging básico con `java.util.logging` en controladores.
