# UEES | Diseño de Software | UCOM0310
## Semana 6 | Actividad 1, Laboratorio 2 y Ae5
### De diagnóstico a refactorización respaldada por pruebas

Este repositorio contiene la secuencia completa de la Semana 6:

1. **Actividad 1** — Diagnóstico de código heredado (`docs/01`–`07`).
2. **Laboratorio 2** — Red de seguridad con JUnit 5 y la primera refactorización protegida (`docs/08`, `docs/evidencia/`).
3. **Ae5** — Refactorización integradora respaldada por pruebas: 3 refactorizaciones de mayor alcance (`docs/09`, `docs/10`, `docs/evidencia_ae5/`).

---

## Requisitos

- **Java 17** (el proyecto usa `maven.compiler.source/target=17`)
- Maven
- Git
- IDE de preferencia: STS, IntelliJ IDEA, Eclipse o VS Code

Verifica:

```bash
java -version
mvn -version
git --version
```

---

## Compilar

```bash
mvn clean compile
```

Debes obtener `BUILD SUCCESS`.

---

## Ejecutar las pruebas

```bash
mvn clean test
```

Al día de hoy la suite tiene **17 pruebas**, todas verdes: las 7 originales de caracterización (`ServicioReservasTest`) sin modificar desde el Laboratorio 2, más 10 nuevas agregadas durante Ae5 (`NotificacionServicioReservasTest`, `CorreoTest`, `PeriodoReservaTest`).

---

## Ejecutar la demo

```bash
mvn exec:java -Dexec.mainClass="edu.uees.refactor.app.Main"
```

La salida esperada (idéntica antes y después de las tres refactorizaciones de Ae5):

```text
Guardando reserva R-001
Correo enviado a ana@uees.edu.ec
Estado: CONFIRMADA
Total: 34.0
```

---

## Estructura

```text
.
├── pom.xml
├── README.md
├── src/main/java/edu/uees/refactor/
│   ├── app/Main.java
│   ├── app/LineaBaseRunner.java
│   ├── domain/EstadoReserva.java
│   ├── domain/Reserva.java
│   ├── domain/Correo.java              (Ae5 · Value Object)
│   ├── domain/PeriodoReserva.java      (Ae5 · Value Object)
│   └── service/
│       ├── ServicioReservas.java
│       └── NotificadorReserva.java     (Ae5 · Extract Class)
├── src/test/java/edu/uees/refactor/
│   ├── service/ServicioReservasTest.java              (Laboratorio 2)
│   ├── service/NotificacionServicioReservasTest.java  (Ae5)
│   ├── domain/CorreoTest.java                         (Ae5)
│   └── domain/PeriodoReservaTest.java                 (Ae5)
└── docs/
    ├── 01_LINEA_BASE.md … 07_REFLEXION_TECNICA.md      (Actividad 1)
    ├── 08_REFLEXION_LABORATORIO2.md                    (Laboratorio 2)
    ├── evidencia/                                       (Laboratorio 2)
    ├── 09_REPORTE_TECNICO_AE5.md                        (Ae5)
    ├── 10_PREGUNTAS_DEFENSA_AE5.md                       (Ae5)
    └── evidencia_ae5/                                    (Ae5)
```

---

## Refactorizaciones aplicadas en Ae5

1. **Extract Class** — la notificación (antes dos `System.out.println` inline) se movió a `NotificadorReserva`, inyectable por constructor.
2. **Introducir Value Object** — `Correo` centraliza la regla de validación del correo, antes duplicada como expresión inline.
3. **Agrupar Data Clump** — `PeriodoReserva` agrupa `inicio`/`fin`, que siempre viajaban juntos.

Detalle completo, justificación y evidencia real de cada una: `docs/09_REPORTE_TECNICO_AE5.md`.

**Deliberadamente fuera de alcance:** mover `PeriodoReserva` al constructor de `Reserva` con validación que lanza excepción — identificado como riesgo Alto en `docs/04_MATRIZ_RIESGO.md` porque cambiaría el contrato observable actual. Queda documentado como trabajo futuro.

---

## Git

Historial esperado (ciclo seguro: PRUEBA VERDE → CAMBIO PEQUEÑO → PRUEBA VERDE → COMMIT):

```bash
git log --oneline -10
```