# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de Software. Proyecto Spring Boot (`compras-comportamiento`) que resuelve los requerimientos del backend de **ComprasUDES**, el sistema interno de solicitudes de compra corporativas de la universidad.

El sistema aborda cuatro necesidades reales aplicando patrones de comportamiento del catálogo GoF, garantizando el bajo acoplamiento, la flexibilidad en la toma de decisiones, la reversibilidad de operaciones y el respeto al código legado sin modificar las clases dadas.

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
    │   └── ejecucion/                    # Necesidad 2 (Command)
    │       ├── PresupuestoService.java
    │       ├── OrdenCompraService.java
    │       ├── Comando.java
    │       ├── ReservarPresupuestoCommand.java
    │       ├── GenerarOrdenCompraCommand.java
    │       └── EjecutorSolicitudes.java
    └── test/java/com/universidad/compras/
        ├── aprobacion/
        │   └── AprobacionNivelesTest.java
        └── ejecucion/
            └── EjecucionSolicitudTest.java
```

---

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos
* **Patrón aplicado:** **Chain of Responsibility (Cadena de Responsabilidad)**
  - **Implementación:** Clase abstracta `NivelAprobacion` con manejadores concretos (`SupervisorArea`, `GerenteArea`, `DirectorFinanciero` y `RevisorCumplimientoNormativo`).
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
*(Pendiente de implementación en Parte 2 - Patrón Observer)*

---

### Necesidad 4 — Reglas de transición según el estado actual
*(Pendiente de implementación en Parte 2 - Patrón State)*

---

## Herramientas utilizadas
- **Java 17** (LTS)
- **Spring Boot 3.2.0**
- **Apache Maven 3.8+**
- **JUnit 5**
- **Visual Studio Code / IntelliJ IDEA, Git, GitHub**