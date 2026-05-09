# Sistema de Gestión de Estudiantes y Notas (Spring Boot)

Una aplicación full-stack en Spring Boot para la gestión de estudiantes y sus calificaciones.

## Características

- **Gestión de Estudiantes:** Crear, leer, actualizar y eliminar estudiantes (operaciones CRUD).
- **Cálculo de Notas:** Cálculo automático de la nota final ponderada de cada estudiante basada en sus calificaciones.
- **API REST:** Backend limpio y estructurado que utiliza métodos HTTP estándar.
- **Integración Frontend:** Vistas usando Thymeleaf y recursos estáticos (HTML/CSS/JS) que se comunican con la API del backend.
- **Validación:** Validación de datos de entrada manejada tanto en el backend como en el frontend.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.6**
  - Spring Web MVC (Controladores REST)
  - Spring Data JPA
  - Thymeleaf (Plantillas)
  - Spring Boot Validation
- **Base de Datos H2:** Base de datos en memoria para configuración y pruebas rápidas.
- **Lombok:** Reducción de código repetitivo (boilerplate).
- **Maven:** Gestión de dependencias y construcción del proyecto.
- **Frontend:** HTML, CSS y JavaScript.

## Empezando

### Requisitos Previos

- Java 17 o superior
- Maven (opcional, se incluye el wrapper en el proyecto)

### Ejecutar la Aplicación

1. Clona o descarga el repositorio.
2. Abre una terminal en el directorio raíz del proyecto.
3. Ejecuta la aplicación usando el wrapper de Maven:

   ```bash
   # En Windows
   mvnw.cmd spring-boot:run

   # En Linux/macOS
   ./mvnw spring-boot:run
   ```

4. La aplicación se iniciará en `http://localhost:8080`.

### Acceso a la Base de Datos (Consola H2)

Puedes ver la base de datos en memoria durante la ejecución usando la consola H2:
- **URL:** `http://localhost:8080/h2-console`
- **URL JDBC:** `jdbc:h2:mem:testdb`
- **Usuario:** `sa`
- **Contraseña:** *(dejar en blanco)*

## Endpoints de la API

El backend expone los siguientes endpoints de la API REST bajo la ruta `/api/estudiantes`:

- `GET /` - Obtener todos los estudiantes
- `GET /{id}` - Obtener un estudiante por ID
- `POST /` - Crear un nuevo estudiante
- `PUT /{id}` - Actualizar un estudiante existente
- `DELETE /{id}` - Eliminar un estudiante
- `GET /{id}/nota-final` - Calcular la nota final de un estudiante