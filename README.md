# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de Software. Proyecto Spring Boot (`compras-comportamiento`) que resuelve los requerimientos del backend de **ComprasUDES**, el sistema interno de solicitudes de compra corporativas de la universidad.

El sistema modela cuatro necesidades reales aplicando patrones de comportamiento del catálogo GoF, garantizando el bajo acoplamiento, la extensibilidad, la reversibilidad de operaciones y el manejo riguroso del ciclo de vida de los estados del negocio sin modificar el código dado.

---

## Cómo ejecutar

### Prerrequisitos
- **Java 17** o superior instalado y configurado en el `PATH`.
- **Apache Maven 3.8+** instalado.

### Comandos de ejecución y pruebas
```bash
# Compilar y empaquetar el proyecto
mvn clean package

# Ejecutar la aplicación Spring Boot
mvn spring-boot:run

# Ejecutar las pruebas unitarias (JUnit 5)
mvn test
```

---

## Estructura del Proyecto

```text
gallo-post1-u4/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/universidad/compras/
    │   ├── ComprasApp.java
    │   ├── modelo/
    │   │   └── Solicitud.java
    │   ├── aprobacion/                   # Necesidad 1 (Chain of Responsibility)
    │   │   ├── ServicioAprobacion.java
    │   │   ├── ResultadoAprobacion.java
    │   │   ├── ControladorSolicitudes.java
    │   │   ├── NivelAprobacion.java
    │   │   ├── SupervisorArea.java
    │   │   ├── GerenteArea.java
    │   │   ├── DirectorFinanciero.java
    │   │   ├── RevisorCumplimientoNormativo.java
    │   │   └── ServicioAprobacionImpl.java
    │   ├── ejecucion/                    # Necesidad 2 (Command)
    │   │   ├── PresupuestoService.java
    │   │   ├── OrdenCompraService.java
    │   │   ├── Comando.java
    │   │   ├── ReservarPresupuestoCommand.java
    │   │   ├── GenerarOrdenCompraCommand.java
    │   │   └── EjecutorSolicitudes.java
    │   ├── notificacion/                 # Necesidad 3 (Observer)
    │   │   ├── ClientesNotificacion.java
    │   │   ├── SuscriptorEstado.java
    │   │   ├── NotificadorCorreoSuscriptor.java
    │   │   ├── ActualizadorDashboardSuscriptor.java
    │   │   ├── RegistradorAuditoriaSuscriptor.java
    │   │   └── GestorNotificacionesEstado.java
    │   └── estado/                       # Necesidad 4 (State)
    │       ├── EstadoSolicitud.java
    │       ├── EstadoSolicitudBase.java
    │       ├── EstadoPendiente.java
    │       ├── EstadoAprobada.java
    │       ├── EstadoEjecutada.java
    │       ├── EstadoRechazada.java
    │       ├── EstadoCancelada.java
    │       ├── EstadoSolicitudFactory.java
    │       └── SolicitudContexto.java
    └── test/java/com/universidad/compras/
        ├── aprobacion/
        │   └── AprobacionNivelesTest.java
        ├── ejecucion/
        │   └── EjecucionSolicitudTest.java
        ├── notificacion/
        │   └── NotificacionEstadoTest.java
        └── estado/
            └── TransicionEstadoTest.java
```

---

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos
* **Patrón aplicado:** **Chain of Responsibility (Cadena de Responsabilidad)**
  - **Implementación:** Clase abstracta `NivelAprobacion` con manejadores concretos (`SupervisorArea`, `GerenteArea`, `DirectorFinanciero` y `RevisorCumplimientoNormativo`), orquestados por `ServicioAprobacionImpl`.
* **Alternativa considerada y descartada:** **Command (Comando)**
* **Justificación técnica:**
  1. **Naturaleza del problema:** Una solicitud de compra debe transitar por una serie de entidades decisoras independientes que evalúan si poseen la autoridad suficiente para resolverla (aprobarla/rechazarla) o si deben remitirla automáticamente al siguiente nivel jerárquico. Además, la cadena debe poder reconfigurarse (incorporando niveles especiales como `RevisorCumplimientoNormativo` para compras internacionales) sin que el emisor de la petición (`ControladorSolicitudes`) conozca cuántos niveles existen ni en qué orden se consultan.
  2. **Por qué Chain of Responsibility resuelve el problema:** Desacopla al emisor de los múltiples posibles receptores. Cada eslabón encapsula su propio umbral de autoridad y delega al sucesor mediante `pasarAlSiguiente()`. La cadena puede ampliarse o reordenarse dinámicamente sin modificar el controlador ni los otros niveles (Principio Abierto/Cerrado).
  3. **Por qué se descarta Command:** El patrón Command encapsula una petición como un objeto ejecutable autónomo con soporte para parámetros, colas y operaciones reversibles (`undo`). En este requerimiento no se busca empaquetar una acción para diferir o revertir su ejecución, sino encaminar un flujo de evaluación condicional distribuido a lo largo de una jerarquía de decisores.

---

### Necesidad 2 — Ejecución reversible de solicitudes aprobadas
* **Patrón aplicado:** **Command (Comando)**
  - **Implementación:** Interfaz `Comando` con implementaciones concretas `ReservarPresupuestoCommand` y `GenerarOrdenCompraCommand`, y el invocador `EjecutorSolicitudes` con pila de reversión (`undo`) e historial completo.
* **Alternativa considerada y descartada:** **Chain of Responsibility (Cadena de Responsabilidad)**
* **Justificación técnica:**
  1. **Naturaleza del problema:** Tras aprobarse una solicitud, el equipo de compras debe ejecutar dos operaciones discretas (`PresupuestoService.reservar()` y `OrdenCompraService.generar()`). Ambas operaciones deben poder deshacerse de forma individual e independiente si se detecta un error (por ejemplo, cancelar la orden sin liberar el presupuesto), y se requiere mantener una bitácora/historial ordenado y consultable de todas las operaciones ejecutadas.
  2. **Por qué Command resuelve el problema:** Encapsula cada operación y su correspondiente acción inversa (`deshacer()`) dentro de un objeto dedicado. El invocador `EjecutorSolicitudes` almacena los comandos ejecutados en una colección cronológica (`historial`) y gestiona una pila (`pilaDeshacer`) para revertir operaciones en orden LIFO sin acoplarse a la implementación de los servicios subyacentes. Al completarse con éxito, actualiza el estado de la solicitud a `EJECUTADA`.
  3. **Por qué se descarta Chain of Responsibility:** En la Necesidad 2 no existe una secuencia de evaluadores independientes decidiendo si procesan o delegan una petición entrante. Hay un único actor (el gestor de compras) ejecutando y eventualmente revirtiendo acciones ya definidas. Chain of Responsibility no provee mecanismos para registrar un historial de acciones realizadas ni para ejecutar operaciones de compensación/reversión (`undo`).

---

### Necesidad 3 — Notificaciones ante cambio de estado
* **Patrón aplicado:** **Observer (Observador)**
  - **Implementación:** Sujeto `GestorNotificacionesEstado` e interfaz `SuscriptorEstado` con implementaciones desacopladas (`NotificadorCorreoSuscriptor`, `ActualizadorDashboardSuscriptor`, `RegistradorAuditoriaSuscriptor`) que consumen `ClientesNotificacion`.
* **Alternativa considerada y descartada:** **State (Estado)**
* **Justificación técnica:**
  1. **Naturaleza del problema:** Cada vez que una solicitud cambia de estado, se deben disparar automáticamente tres reacciones independientes (correo al solicitante, actualización de tablero de contabilidad y auditoría). El código que modifica el estado no debe conocer a los receptores, y debe ser posible agregar nuevos suscriptores (por ejemplo, notificaciones a Slack) sin modificar el publicador.
  2. **Por qué Observer resuelve el problema:** Modela una relación uno-a-muchos basada en suscripciones. Los observadores se registran ante el sujeto y son notificados ante el evento de cambio de estado. Permite suscribir y desuscribir módulos en tiempo de ejecución cumpliendo estrictamente con el principio Open/Closed.
  3. **Por qué se descarta State (Comparación con Necesidad 4):** State está pensado para alterar el comportamiento *interno* de un objeto según su estado propio, no para comunicar cambios a módulos externos. En la Necesidad 3, la solicitud no cambia cómo opera internamente; son subsistemas ajenos los que deben enterarse del evento. Si usáramos State para esto, contaminaríamos las clases de estado del dominio con detalles de infraestructura externa (correos, APIs y bases de datos).

---

### Necesidad 4 — Reglas de transición según el estado actual
* **Patrón aplicado:** **State (Estado)**
  - **Implementación:** `SolicitudContexto` que delega las operaciones (`aprobar`, `rechazar`, `ejecutar`, `cancelar`) en una jerarquía polimórfica de estados (`EstadoPendiente`, `EstadoAprobada`, `EstadoEjecutada`, `EstadoRechazada`, `EstadoCancelada`).
* **Alternativa considerada y descartada:** **Strategy (Estrategia)**
* **Justificación técnica:**
  1. **Naturaleza del problema:** Las reglas de transición entre estados estaban dispersas en múltiples condicionales `if/else` comprobando `getEstado()`. Cada nuevo estado obligaba a modificar múltiples métodos, dificultando el mantenimiento. Además, las operaciones inválidas deben rechazarse limpiamente sin cambiar el estado actual.
  2. **Por qué State resuelve el problema:** Encapsula el comportamiento y las transiciones válidas dentro de objetos de estado específicos. Cuando se ejecuta una acción válida, el propio estado transiciona el contexto a su nuevo estado (`PENDIENTE` a `APROBADA` o `APROBADA` a `EJECUTADA`). Si una operación es inválida (como ejecutar una solicitud en estado `PENDIENTE` o re-ejecutar una ya `EJECUTADA`), el estado la rechaza sin alterar la solicitud.
  3. **Por qué se descarta Strategy:** Aunque estructuralmente ambos patrones delegan en una interfaz polimórfica, en **Strategy** es el cliente externo quien selecciona e inyecta explícitamente el algoritmo a utilizar en cada llamada puntual. En **State**, es el objeto el que cambia de estado automáticamente como consecuencia de su ciclo de vida interno; los estados se conocen y transicionan entre sí, garantizando que el comportamiento dependa del momento histórico de la entidad y no de la elección arbitraria de un cliente externo.

---

### Reflexión — Otros tres patrones (No rubricada)
1. **Iterator (Iterador):** Encaja de forma directa en la generación de reportes por centro de costo. Permite recorrer secuencialmente todas las solicitudes sin exponer si la estructura subyacente es una lista, un mapa indexado o un conjunto en base de datos.
2. **Template Method (Método Plantilla):** Encaja en los distintos tipos de comprobantes (orden de compra, reserva presupuestal, acta de rechazo). Permite definir un esqueleto de impresión común (`imprimirEncabezado()`, `imprimirCuerpo()`, `imprimirPie()`) en una clase base abstracta, dejando que cada comprobante implemente únicamente el contenido particular del cuerpo.
3. **Memento (Recuerdo):** Encajaría si se requiere guardar y restaurar instantáneas completas del estado interno de una `Solicitud` en cualquier punto sin violar su encapsulamiento. Se diferencia del patrón Command en que Memento captura el estado completo del objeto como una instantánea pasiva, mientras que Command almacena operaciones atómicas activas con su lógica reversible (`undo`).

---

## Herramientas utilizadas
- **Java 17** (LTS)
- **Spring Boot 3.2.0**
- **Apache Maven 3.8+**
- **JUnit 5**
- **Visual Studio Code / IntelliJ IDEA**
- **Git & GitHub**

---

## Conclusiones
La implementación de esta unidad demostró el poder de los patrones de comportamiento para gestionar flujos complejos desacoplados en el backend. Chain of Responsibility permitió desacoplar emisores de receptores mediante una cadena reconfigurable de evaluación; Command facilitó la ejecución atómica y reversible de operaciones con bitácora cronológica completa; Observer independizó la notificación de eventos a módulos externos del ciclo de vida del negocio; y State transformó lógica condicional dispersa en un autómata robusto y mantenible donde cada estado gobierna sus propias transiciones.