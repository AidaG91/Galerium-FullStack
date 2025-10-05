## Parámetros de Ordenación y Filtrado en APIs REST

Los parámetros `sort=createdAt|title&dir=asc|desc` y el filtrado por `tags` se relacionan directamente con el diseño de APIs REST y las prácticas de optimización de bases de datos que soportan estas consultas.

En el contexto de Arquitectura Web y plataformas que generan APIs, estos parámetros se gestionan de la siguiente manera:

### 1. Gestión de la Ordenación y Filtros mediante Query Parameters

En la arquitectura web moderna, el acceso a información hipertextual (WWW) se realiza mediante protocolos como HTTP/HTTPS, y la comunicación entre el cliente y el servidor se basa en el modelo Cliente-Servidor.

Los parámetros de ordenación y filtrado se implementan típicamente a través de query parameters en la solicitud que el cliente envía al servidor.

#### 1.1 Ordenación

La estructura `sort=createdAt|title&dir=asc|desc` es un patrón estándar para permitir al usuario (o al frontend) controlar el orden en que se devuelven los recursos:

- **Parámetro `sort`:** Permite especificar la columna (o columnas) por las que se debe ordenar el conjunto de datos. Por ejemplo, se podría ordenar por la fecha de creación (`createdAt`) o por el título (`title`).
- **Parámetro `dir` (direction):** Indica si la ordenación debe ser ascendente (`asc`) o descendente (`desc`).

#### 1.2 Filtrado por Tags

El filtrado por tags (etiquetas) es esencial para refinar el conjunto de datos y evitar devolver respuestas masivas. Este filtro se pasa como un parámetro de consulta y permite al usuario acotar los resultados que cumplen con una o más etiquetas específicas.

Si el proyecto utiliza una plataforma BaaS como Supabase, los filtros se aplican a **todas** las llamadas REST, y la Row Level Security (RLS) se aplica sobre ellas, asegurando que el usuario solo pueda filtrar y ver los datos que tiene derecho a acceder.

### 2. Optimización y Rendimiento

Cuando se implementan ordenaciones y filtros frecuentes (como por tags, `createdAt` o `title`), la velocidad de respuesta del backend depende fundamentalmente de la eficiencia de la base de datos.

Para optimizar el rendimiento de estos filtros y ordenaciones:

- **Indexación:** Es crucial indexar las columnas usadas en los filtros (como `tags`) y las columnas utilizadas para ordenación (`createdAt`, `title`). Crear un índice (`CREATE INDEX ... ON ...`) acelera significativamente la búsqueda en la base de datos PostgreSQL de Supabase.
- **Selección de Campos:** Se recomienda usar `SELECT` con columnas concretas y evitar el uso de `SELECT *` en consultas críticas.
- **Paginación:** La paginación (`.range()` o cursores) debe utilizarse en combinación con la ordenación y el filtrado para evitar la transferencia de grandes cantidades de datos.

La capacidad de instrumentar el sistema para decidir con datos y medir la latencia p95 (percentil 95, ignorando el 5% de los peores casos) es parte de las prácticas de DevOps (Measurement), asegurando que las consultas, incluso las filtradas y ordenadas, cumplan con los objetivos de servicio (SLOs).