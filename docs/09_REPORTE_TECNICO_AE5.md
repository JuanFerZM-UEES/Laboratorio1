# Reporte técnico · Ae5 — Refactorización respaldada por pruebas unitarias

**Universidad Espíritu Santo · Diseño de Software · UCOM0310**
**Actividad Evaluada 3 (Ae5) · Semana 6**

## 1. Portada y datos del estudiante

- Estudiante: Juan Fernando Zhingri Medina
- Repositorio: `JuanFerZM-UEES/Laboratorio1`
- Proyecto base: `semana6-lab-diagnostico` (código heredado `ServicioReservas`)
- Actividades previas que sustentan esta entrega: Actividad 1 (diagnóstico de código heredado) y Actividad 2 / Laboratorio 2 (JUnit 5 y red de seguridad)

## 2. Descripción del problema inicial

El proyecto parte de una clase heredada, `ServicioReservas.procesar(Reserva, int)`, que mezclaba en un único método: validaciones de datos de entrada (correo, periodo, anticipación), cálculo de tarifa (con descuento VIP), efectos de salida simulados (`System.out.println` para "persistencia" y "notificación"), y la confirmación del estado del dominio. El diagnóstico de la Actividad 1 identificó **seis problemas de diseño** distintos en esa única clase (`docs/03_MATRIZ_DIAGNOSTICO.md`): Long Method, Feature Envy, Primitive Obsession sobre `correo`/`tipo`, Data Clump + Long Parameter List sobre `inicio`/`fin`, condicionales con literales mágicos, y testabilidad reducida por E/S mezclada con lógica.

## 3. Línea base

La línea base original (`docs/01_LINEA_BASE.md`) registró seis escenarios manuales (LB-01..LB-06) sobre el comportamiento observable de `procesar()`. El escenario ejecutado por `Main.java` (reserva VIP, 5 h de anticipación) produce:

```
Guardando reserva R-001
Correo enviado a ana@uees.edu.ec
Estado: CONFIRMADA
Total: 34.0
```

Esta misma salida se volvió a verificar de forma real **después** de aplicar las tres refactorizaciones de esta actividad (`docs/evidencia_ae5/04_main_end_to_end_sin_cambios.txt`), y es idéntica carácter por carácter. Ese es el criterio de éxito de todo el ejercicio: cambiar el diseño interno sin cambiar el comportamiento observable.

## 4. Diagnóstico resumido

De los seis problemas del diagnóstico original, esta actividad aborda tres de forma directa (los de menor riesgo según `docs/04_MATRIZ_RIESGO.md`), dejando explícitamente para una iteración futura el de mayor riesgo (introducir `PeriodoReserva` como tipo del constructor de `Reserva`, que podría cambiar el contrato observable de "retorna 0" a "lanza excepción al construir"):

| # | Problema (Actividad 1) | Abordado en Ae5 |
|---|---|---|
| 1 | Long Method (`procesar`) | Reducido — ahora delega en 4 colaboradores |
| 2 | Feature Envy / notificación mezclada | **Sí** — Refactorización 1 |
| 3 | Primitive Obsession (`correo`) | **Sí** — Refactorización 2 |
| 4 | Data Clump (`inicio`/`fin`) | **Sí** — Refactorización 3 (parcial, ver sección 7) |
| 5 | Literales mágicos / condicionales | Ya resuelto en Laboratorio 2 (`calcularTotal`) |
| 6 | Testabilidad reducida por E/S | **Sí** — consecuencia directa de la Refactorización 1 |

## 5. Pruebas utilizadas como red de seguridad

Las 7 pruebas de caracterización construidas en el Laboratorio 2 (`ServicioReservasTest`) **no se modificaron en ningún momento** durante esta actividad — siguen verdes, sin tocar, en cada una de las tres refactorizaciones. Son la referencia estable ("¿sigo haciendo lo mismo?") mientras cambia la estructura interna. A esas 7 se sumaron 10 pruebas nuevas, específicas de cada refactorización (ver secciones 6–8), para un total de **17 pruebas**, todas verdes (`docs/evidencia_ae5/03_refactor3_value_object_periodo.txt`).

## 6. Refactorización 1 · Extract Class (`NotificadorReserva`)

| Elemento | Detalle |
|---|---|
| Problema de diseño | `procesar()` mezclaba reglas de negocio con dos `System.out.println` que simulan persistencia y notificación (Feature Envy / SRP — fila 2 y 6 de la matriz de diagnóstico) |
| Código antes | `System.out.println("Guardando reserva " + r.getId()); System.out.println("Correo enviado a " + r.getCorreo());` dentro de `procesar()` |
| Técnica aplicada | Extract Class + inyección por constructor (`NotificadorReserva`, con constructor por defecto para no romper a `Main`/`LineaBaseRunner`) |
| Prueba que protege | Las 7 pruebas de caracterización (verifican estado/total sin depender de consola) **más** 2 pruebas nuevas con un spy: `notificaExactamenteUnaVezParaReservaValida()`, `noNotificaCuandoLaReservaEsRechazada()` (`NotificacionServicioReservasTest.java`) |
| Resultado después | 9/9 pruebas verdes (`docs/evidencia_ae5/01_refactor1_extract_class_notificador.txt`); la notificación ahora es sustituible y observable sin leer consola |
| Commit | `refactor: extraer notificacion a NotificadorReserva` (pendiente — ver instrucciones de entrega) |

## 7. Refactorización 2 · Introducir Value Object (`Correo`)

| Elemento | Detalle |
|---|---|
| Problema de diseño | Primitive Obsession: el correo era un `String` validado con una expresión inline repetible (`r.getCorreo() == null \|\| !r.getCorreo().contains("@")`) |
| Código antes | `if (r.getCorreo() == null \|\| !r.getCorreo().contains("@")) { return 0; }` |
| Técnica aplicada | Introducir Value Object `Correo` (record) con validación propia. Decisión explícita de diseño: el constructor SÍ lanza `IllegalArgumentException` (como pide el ejemplo de la guía), pero `ServicioReservas` solo construye un `Correo` **después** de comprobar `Correo.esValido(...)` — así el contrato observable de `procesar()` no cambia |
| Prueba que protege | `correoInvalidoNoProcesaReserva()` (sin modificar, sigue verde) **más** 4 pruebas nuevas y aisladas sobre el Value Object: `correoValidoSeAcepta()`, `correoInvalidoLanzaExcepcion()`, `correoNuloLanzaExcepcion()`, `esValidoNoLanzaExcepcionParaCorreoInvalido()` (`CorreoTest.java`) |
| Resultado después | 13/13 pruebas verdes (`docs/evidencia_ae5/02_refactor2_value_object_correo.txt`) |
| Commit | `refactor: introducir value object Correo` (pendiente — ver instrucciones de entrega) |

## 8. Refactorización 3 · Agrupar Data Clump (`PeriodoReserva`)

| Elemento | Detalle |
|---|---|
| Problema de diseño | Data Clump: `inicio` y `fin` siempre se usaban juntos (como parámetros, como campos, como una sola condición de validez) sin que el código lo expresara |
| Código antes | `if (r.getInicio() == null \|\| r.getFin() == null \|\| !r.getFin().isAfter(r.getInicio())) { return 0; }` |
| Técnica aplicada | Introducir Value Object `PeriodoReserva` (record) con `esValido()`. Decisión explícita, distinta a la de `Correo`: **no** lanza excepción en el constructor, porque la matriz de riesgo (`docs/04_MATRIZ_RIESGO.md`) marcó ese cambio como Alto — movería la detección del error desde `procesar()` (retorna 0) hacia la construcción del objeto (excepción). `Reserva` tampoco se modifica en este paso |
| Prueba que protege | `periodoConFinAnteriorNoProcesa()` (sin modificar, sigue verde) **más** 4 pruebas nuevas sobre el Value Object: `periodoConFinPosteriorAInicioEsValido()`, `periodoConFinAnteriorAInicioNoEsValido()`, `periodoConFechasIgualesNoEsValido()`, `periodoConFechasNulasNoEsValido()` (`PeriodoReservaTest.java`) |
| Resultado después | 17/17 pruebas verdes (`docs/evidencia_ae5/03_refactor3_value_object_periodo.txt`); `Main.java` produce exactamente la misma salida que antes de las tres refactorizaciones (`docs/evidencia_ae5/04_main_end_to_end_sin_cambios.txt`) |
| Commit | `refactor: agrupar inicio y fin en PeriodoReserva` (pendiente — ver instrucciones de entrega) |

## 9. Comparación antes/después

| Dimensión | Antes | Después | Evidencia |
|---|---|---|---|
| Responsabilidades | `ServicioReservas` validaba, calculaba, "persistía" y notificaba — 5 razones de cambio distintas (`docs/02_MAPA_RESPONSABILIDADES.md`) | La notificación vive en `NotificadorReserva`; `ServicioReservas` orquesta colaboradores en vez de hacerlo todo | `NotificadorReserva.java` |
| Cohesión | Un único método con 4 responsabilidades entrelazadas | `procesar()` delega en `Correo.esValido`, `PeriodoReserva.esValido`, `calcularTotal` y `notificador.notificar` — cada colaborador con un solo motivo de cambio | `ServicioReservas.java` final |
| Acoplamiento | Acoplado directamente a `System.out` y a `String` sueltos | Acoplado a una abstracción inyectable (`NotificadorReserva`) y a tipos con significado (`Correo`, `PeriodoReserva`); sustituible en pruebas sin tocar producción | `NotificacionServicioReservasTest.java` (spy) |
| Datos del dominio | `correo`: `String` suelto; `inicio`/`fin`: dos `LocalDateTime` sueltos siempre usados juntos | `Correo` y `PeriodoReserva` encapsulan la regla de validez junto con el dato, reutilizables y probables por separado | `Correo.java`, `PeriodoReserva.java` |
| Condicionales | Guard clauses con expresiones booleanas largas e inline | Mismas guard clauses, ahora leyendo `esValido()` — la intención es explícita en el nombre, no en el detalle de la expresión | `ServicioReservas.java` |
| Pruebas | 7 pruebas, todas sobre `ServicioReservas` (fin del Laboratorio 2) | 17 pruebas: las 7 originales sin tocar + 10 nuevas específicas de cada refactorización | `docs/evidencia_ae5/*.txt` |
| Git | 4 commits (línea base, pruebas, Extract Method, docs) | +3 commits, uno por refactorización, siguiendo PRUEBA VERDE → CAMBIO PEQUEÑO → PRUEBA VERDE → COMMIT | Ver sección 10 |

## 10. Historial Git

Resultado real de `git log --oneline -8` en mi máquina, después de completar los tres commits de refactorización y el commit de documentación:

```
94fa3e0 (HEAD -> main, origin/main) docs: actualizar readme con estado real de Ae5
fee709f docs: reporte tecnico y comparacion Ae5
63c3e0d refactor: agrupar inicio y fin en PeriodoReserva
1878402 refactor: introducir value object Correo
a43e7c3 refactor: extraer notificacion a NotificadorReserva
52ad8b2 docs: registrar evidencia de Laboratorio 2 y reflexion tecnica
b686cf6 refactor: extraer calculo de total
46d2124 test: caracterizar comportamiento heredado de reservas
```

## 11. Conclusiones

Las tres refactorizaciones de esta actividad comparten una misma disciplina: cada una se apoyó en una prueba que ya existía (de Laboratorio 2 o de la línea base) y sumó pruebas nuevas y específicas para el colaborador recién extraído, sin modificar ni una sola de las pruebas anteriores. Eso es lo que permite afirmar, con evidencia y no por confianza, que el comportamiento observable de `ServicioReservas` no cambió: la salida de `Main.java` es idéntica antes y después, y la suite pasó de 7 a 17 pruebas verdes sin que ninguna de las 7 originales necesitara ajustarse.

La decisión más importante no fue una técnica de refactorización, sino una de alcance: dejar fuera, a propósito, el cambio de mayor riesgo identificado desde la Actividad 1 (mover `PeriodoReserva` al constructor de `Reserva`, con validación que lanza excepción). Extender `PeriodoReserva` hasta ahí habría alterado el contrato observable actual, y esta actividad exige precisamente lo contrario: verificar antes de afirmar. Esa decisión queda documentada explícitamente en el código (`PeriodoReserva.java`) y en este reporte, para que la iteración futura que sí la aborde parta de una prueba dedicada a ese nuevo comportamiento, en vez de asumirlo.

## 12. Declaración de uso de IA

Utilicé Claude como apoyo en esta actividad: para revisar errores de código en las clases `NotificadorReserva`, `Correo` y `PeriodoReserva` a partir del plan de refactorización que construí en la Actividad 1 (`docs/06_PLAN_REFACTORIZACION.md`), para generar las pruebas JUnit 5 correspondientes a cada refactorización, y para redactar este reporte técnico. Yo ejecuté personalmente cada compilación, cada corrida de la suite de pruebas y cada commit de Git, verificando en cada paso que la suite se mantuviera en verde antes de continuar, siguiendo el ciclo seguro exigido por la actividad (PRUEBA VERDE → CAMBIO PEQUEÑO → PRUEBA VERDE → COMMIT).

## 13. Enlace al repositorio

https://github.com/JuanFerZM-UEES/Laboratorio1
