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