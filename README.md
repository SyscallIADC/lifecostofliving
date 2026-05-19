# Life Cost of Living
## 1. Breve descripción del proyecto
Este proyecto es un sistema de ingeniería de datos diseñado para evaluar la viabilidad económica de emigrar a diferentes países. A través de una arquitectura orientada a eventos (arquitectura Lambda/Kappa), el sistema recopila, procesa y unifica métricas de coste de vida y tasas de cambio de divisas. Toda esta información consolida un Datamart local optimizado para ofrecer respuestas analíticas en tiempo real.
## 2. Propuesta de Valor
A diferencia de los portales de estadística estáticos, este proyecto actúa como un motor inteligente de toma de decisiones financieras. Está diseñado específicamente para **nómadas digitales y trabajadores en remoto** que tienen la libertad de mudarse a cualquier parte del mundo y buscan maximizar su calidad de vida.

El sistema aporta valor directo al usuario final mediante:
* **Análisis de viabilidad personalizado:** Cruza el salario real del usuario (en su moneda local) con el coste de vida del país de destino, indicando exactamente cuánto capital le sobrará o le faltará bajo tres escenarios realistas (Soltero, Nivel Óptimo y Familia).
* **Evaluación de riesgo macroeconómico:** No se limita a medir los gastos del día de hoy. El sistema procesa el historial temporal de los datos para evaluar si la economía del país es estable a largo plazo, alertando al usuario mediante indicadores visuales si existen riesgos de inflación severa o alta volatilidad en los mercados.
* **Toma de decisiones transparente:** Al unificar el coste de los bienes y el valor de las divisas en tiempo real, el usuario obtiene una radiografía económica instantánea y precisa sin tener que cruzar datos de múltiples fuentes de forma manual.

## 3. Justificación de la elección de APIs y estructura del Datamart

### Fuentes de Datos (Feeders)
* **Web Scraping (Numbeo):** Se ha optado por implementar un *scraper* web en lugar de consumir una API convencional debido a la inexistencia de APIs gratuitas que ofrezcan un desglose completo de la cesta de la compra y costes de vida. Se seleccionó Numbeo como fuente principal por ser la mayor base de datos colaborativa del mundo, garantizando información contrastada y altamente actualizada por usuarios locales. Además, el diseño modular del scraper permite su fácil escalabilidad para integrar futuras páginas web de contraste. El listado de países a extraer se gestiona mediante una base de datos SQLite embebida, aportando simplicidad y potencia sin necesidad de infraestructura externa.
* **API REST (AlphaVantage):** Para la obtención de las tasas de cambio de divisas, se consume la API de AlphaVantage. Esta decisión se fundamenta en la necesidad de contar con datos financieros fiables, precisos y respaldados por un proveedor de prestigio en el sector tecnológico y bursátil.

### Persistencia y Reconstrucción (Event Store)
El módulo *Event Store Builder* almacena la información cruda en formato JSON Lines (`.events`). Se ha evitado deliberadamente el uso de motores de bases de datos pesados en esta capa para favorecer una persistencia ligera y rápida en disco. Esto aplica el patrón *Event Sourcing*, asegurando que el estado completo del sistema pueda ser reconstruido desde cero en cualquier momento.

### Estructura del Datamart (Business Unit)
El Datamart analítico se ha implementado sobre **SQLite**. Esta elección se justifica por su extrema ligereza y alto rendimiento en entornos de ejecución local y para equipos pequeños, eliminando la necesidad de levantar un servidor de base de datos pesado.
* **Ingesta Dual:** El Datamart se alimenta de dos vías. Actúa como un suscriptor activo (TCP Failover) del broker para actualizar los precios en tiempo real, pero también es capaz de procesar el Event Store histórico al arrancar.
* **Diseño Time-Series:** Se utiliza una clave primaria compuesta (`country` / `currency_pair` + `capture_timestamp`) para guardar un registro histórico de los datos, permitiendo al sistema realizar cálculos complejos en tiempo real (volatilidad, inflación) sobre una ventana temporal.


## 4. Arquitectura del Sistema y de la Aplicación

### Arquitectura de Infraestructura (Sistema Completo)
El proyecto implementa un flujo de datos basado en el patrón **Publisher/Subscriber** utilizando Apache ActiveMQ como broker de mensajería. Esto desacopla completamente los procesos de extracción (Feeders) de los procesos de transformación y carga (Event Store y Business Unit), permitiendo una arquitectura escalable orientada a eventos.

<img width="1190" height="666" alt="image" src="https://github.com/user-attachments/assets/23217d10-bbb8-4d21-8d48-6bdb6ad5cf3a" />


### Arquitectura de la Aplicación (Módulo Business Unit)
A nivel de software, el módulo principal de análisis se ha estructurado siguiendo el patrón arquitectónico **MVC (Model-View-Controller)**. Esto aísla la capa de presentación (CLI) de la lógica de negocio matemática y de las operaciones de persistencia en la base de datos (DAO).

[AQUÍ INCLUIR IMAGEN DEL DIAGRAMA DE CLASES]

### Arquitectura de los Feeders (LivingCost y ExchangeRate)
Cada uno de los módulos que forman parte del publisher siguiendo un modelo **Model-Controller**. Así cada uno tiene su particularización del feeder, obteniendo datos o de una API o de un WebScrapper.
<img width="1280" height="590" alt="image" src="https://github.com/user-attachments/assets/d9010a04-9fd7-4d6e-9425-36e9166d1170" />

## 5. Principios y Patrones de Diseño Aplicados

Para garantizar que el código sea mantenible, escalable y fácil de testear, se han aplicado rigurosamente diversos principios de ingeniería de software y patrones de diseño en la construcción de los módulos.

### Patrones Arquitectónicos
* **Arquitectura Lambda/Kappa:** Se combinan dos vías de procesamiento. Una capa de velocidad que consume mensajes en tiempo real vía ActiveMQ y una capa batch que reconstruye el estado a partir de archivos estáticos.
* **Event Sourcing:** El Datamart no es la fuente primaria de la verdad, sino una vista optimizada. La fuente real es el *Event Store* (archivos `.events`), lo que permite que el estado de la base de datos pueda ser purgado y reconstruido desde cero sin pérdida de información.
* **Publisher-Subscriber (Pub/Sub):** Desacoplamiento total entre los productores de datos (Feeders) y los consumidores (Event Store Builder y Business Unit) a través de *topics* en el broker de mensajería.
* **MVC (Model-View-Controller):** El módulo de negocio aísla la interfaz de consola (`CLIView`) de la lógica matemática (`BusinessController` / `TrendAnalyzer`) y de los objetos de transferencia de datos (`CountryStats`, `MarketTrend`).

### Patrones de Diseño (GoF y Data)
* **DAO (Data Access Object) y Repository Pattern:** La interfaz `DatamartRepository` define el contrato de persistencia, mientras que `DatamartDAO` encapsula toda la complejidad de las sentencias SQL de SQLite. El resto del sistema ignora por completo qué motor de base de datos se está usando.
* **Factory Method:** Se utiliza `CountryStatsFactory` para centralizar la lógica de creación de los objetos de coste de vida, extrayendo las conversiones de divisas y los cálculos matemáticos de la base de datos.
* **Inyección de Dependencias (Dependency Injection):** Las clases no instancian sus propias dependencias. Por ejemplo, el `TrendAnalyzer` recibe el `DatamartRepository` a través de su constructor en la clase `Main`. Esto permite que el sistema sea modular y completamente testeable mediante *Mocks*.

### Principios de Clean Code y SOLID
* **Single Responsibility Principle (SRP):** Cada clase tiene una única razón para cambiar. `DatabaseHelper` solo gestiona la conexión a SQLite, `TrendAnalyzer` solo realiza análisis estadístico y `BusinessSubscriber` solo gestiona los hilos de red de ActiveMQ.

### Principios de Clean Code y SOLID
* **Single Responsibility Principle (SRP):** Cada clase tiene una única razón para cambiar. Por ejemplo, `DatabaseHelper` se encarga exclusivamente de la conexión a SQLite, `TrendAnalyzer` asume únicamente la carga del análisis estadístico, y `CLIView` se dedica solo a la interacción por terminal.
* **Dependency Inversion Principle (DIP):** Los módulos de alto nivel (`BusinessController`, `TrendAnalyzer`) no dependen de módulos de bajo nivel (`DatamartDAO`). Ambos dependen de abstracciones (la interfaz `DatamartRepository`). Esto hace que el motor matemático sea completamente agnóstico a si los datos vienen de SQLite, PostgreSQL o un archivo de texto.
* **Gestión Segura de Recursos (Clean Code):** Se ha evitado activamente el riesgo de fugas de memoria (memory leaks) o bloqueos en la base de datos utilizando bloques `try-with-resources` nativos de Java en todas las consultas JDBC. Esto asegura el cierre automático de conexiones (`Connection`, `PreparedStatement`, `ResultSet`) sin importar si la ejecución es exitosa o lanza una excepción.
* **Seguridad de Tipos y Ausencia de "Magic Strings":** En lugar de devolver cadenas de texto sueltas para evaluar el mercado, se ha encapsulado el estado en un `enum` (`MarketTrend`). Esto aporta seguridad en tiempo de compilación, centraliza el formato visual (emojis y descripciones) y evita errores tipográficos en la lógica de negocio.
* **Don't Repeat Yourself (DRY):** La lógica de creación de la conexión a la base de datos y la inicialización de las tablas se ha centralizado en una única clase de utilidad (`DatabaseHelper`), eliminando código repetitivo (boilerplate) en los distintos métodos del DAO.

## 6. Instrucciones de Compilación y Ejecución

El proyecto está diseñado para ejecutarse fácilmente desde la línea de comandos, independientemente del IDE utilizado.

### Requisitos Previos
* **Java 21** instalado en el sistema.
* **Apache ActiveMQ** (v5.15.12 o superior) instalado y ejecutándose en `tcp://localhost:61616`.
* **Maven** para la resolución de dependencias y el empaquetado.

### 1. Compilación de los Módulos
Para generar los ejecutables que contienen tanto el código fuente como las librerías necesarias (Fat JARs), sitúate en el directorio raíz de cada módulo y ejecuta:
## 6. Instrucciones de Compilación y Ejecución

El proyecto está diseñado para ejecutarse fácilmente desde la línea de comandos, independientemente del IDE utilizado.

### Requisitos Previos
* **Java 21** instalado en el sistema.
* **Apache ActiveMQ** (v5.15.12 o superior) instalado y ejecutándose en `tcp://localhost:61616`.
* **Maven** para la resolución de dependencias y el empaquetado.

### 1. Compilación de los Módulos
Para generar los ejecutables que contienen tanto el código fuente como las librerías necesarias (Fat JARs), sitúate en el directorio raíz de cada módulo y ejecuta:
```bash
mvn clean package
```

Esto generará un archivo terminado en `-jar-with-dependencies.jar` dentro de la carpeta `target/` de cada componente.

### 2. Configuración de Variables de Entorno
El feeder de divisas requiere la clave de acceso a la API de AlphaVantage. Por motivos de seguridad de la arquitectura, esta clave no se expone en el código fuente. Antes de ejecutar los feeders, inyecta la variable en tu sesión de shell:

```bash
export ALPHAVANTAGE_API_KEY="tu_clave_api_aqui"
```

### 3. Orden de Ejecución de la Arquitectura
Para que el sistema Pub/Sub orqueste los datos de forma fluida, abre tres terminales y ejecuta los módulos en el siguiente orden estricto:

**Paso A: Levantar la persistencia en crudo (Event Store Builder)**
```bash
java -jar target/event-store-builder-1.0-jar-with-dependencies.jar
```

**Paso B: Levantar los recolectores de datos (Feeders)**
*(Asegúrate de haber exportado la variable de entorno en esta terminal específica antes de lanzar el proceso).*
```bash
java -jar target/feeder-1.0-jar-with-dependencies.jar
```

**Paso C: Iniciar el motor analítico (Business Unit)**
El módulo de negocio (interfaz CLI) requiere que se le indique por argumento la ruta al directorio de almacenamiento de eventos para poder reconstruir el historial en el datamart. Asumiendo que la carpeta está en el mismo directorio de ejecución:
```bash
java -jar target/business-unit-1.0-jar-with-dependencies.jar "eventstore"
```
*(Nota: Si se omite el argumento, el sistema buscará por defecto un directorio llamado `eventstore` en la ruta de ejecución actual).*

## 7. Ejemplos de Uso (CLI)

Al arrancar el módulo `business-unit`, el usuario interactúa con un menú interactivo en la consola. A continuación, se muestran ejemplos de las consultas principales que evidencian la propuesta de valor del sistema:

**Ejemplo 1: Análisis de viabilidad de sueldo (Opción 1)**

    > Introduce tu moneda local (Ej. EUR, USD, GBP, MXN): MXN
    > Introduce tu sueldo mensual en MXN: 25000
    > Introduce el país a consultar (Ej. Spain, Japan): Japan
    
    --- RESULTADOS PARA JAPAN ---
    Estado del Mercado: ⚠️ Volatilidad Detectada
    Tu sueldo actual: 25000.00 MXN (Equivale a 1350.00 €)
    Salario medio local neto: 2100.50 €
    
    ✅ Soltero     : VIABLE   (Coste aprox:  950.00€ | Te sobran:  400.00€)
    ❌ Nivel Óptimo: INVIABLE (Coste aprox: 1425.00€ | Te faltan:   75.00€)
    ❌ Familia     : INVIABLE (Coste aprox: 2500.00€ | Te faltan: 1150.00€)

**Ejemplo 2: Recomendación de destinos (Opción 2)**

    > Introduce tu moneda local (Ej. EUR, USD, GBP): EUR
    > Introduce tu sueldo mensual en EUR: 1800
    
    --- TOP PAÍSES PARA 'NIVEL ÓPTIMO' CON 1800.00€ (Convertido) ---
    ✅ Spain           | Coste Vida: 1350.50€ | Ahorro mensual:  449.50€
    ⚠️ Argentina       | Coste Vida: 1100.00€ | Ahorro mensual:  700.00€
    🔵 Greece          | Coste Vida: 1250.00€ | Ahorro mensual:  550.00€

**Ejemplo 3: Comparativa de calidad de vida (Opción 3)**

    > País de origen (Ej. Spain): Spain
    > País de destino (Ej. Puerto Rico): Puerto Rico
    
    --- COMPARATIVA: Spain vs Puerto Rico ---
    [Spain] Salario Medio: 1750.00 €  |  [Puerto Rico] Salario Medio: 2170.00 €  -> (+420.00 €)
    [Spain] Coste Soltero:  850.00 €  |  [Puerto Rico] Coste Soltero: 1165.20 €  -> (+315.20 €)
    [Spain] Coste Familia: 2100.00 €  |  [Puerto Rico] Coste Familia: 2950.50 €
