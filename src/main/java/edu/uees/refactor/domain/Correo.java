package edu.uees.refactor.domain;

/**
 * Value Object para una dirección de correo.
 *
 * Ae5 · Refactorización 2 · Introducir Value Object.
 *
 * Antes, {@code ServicioReservas.procesar()} validaba el correo con la
 * expresión repetible {@code r.getCorreo() == null || !r.getCorreo().contains("@")}.
 * Esa regla (Primitive Obsession, fila 3 de
 * {@code docs/03_MATRIZ_DIAGNOSTICO.md}) queda ahora centralizada en un
 * solo tipo, reutilizable y probable de forma aislada.
 *
 * Decisión de diseño deliberada (ver {@code docs/04_MATRIZ_RIESGO.md},
 * fila "Introducir Correo"): el constructor SÍ lanza
 * {@link IllegalArgumentException} para un valor inválido — igual que
 * el ejemplo de la guía de Ae5 — pero {@code ServicioReservas} solo
 * construye un {@code Correo} DESPUÉS de comprobar {@link #esValido},
 * nunca antes. Así el contrato observable de
 * {@code procesar(reserva-con-correo-invalido, ...)} no cambia: sigue
 * devolviendo {@code 0} en silencio, tal como fija la prueba
 * {@code correoInvalidoNoProcesaReserva()}. {@link Reserva} tampoco se
 * modifica en este paso — su campo {@code correo} sigue siendo
 * {@code String} — para no combinar dos riesgos distintos en un mismo
 * commit.
 */
public record Correo(String valor) {

    public Correo {
        if (valor == null || !valor.contains("@")) {
            throw new IllegalArgumentException(
                    "Correo invalido: " + valor
            );
        }
    }

    /**
     * Comprueba la validez sin lanzar excepción, para usarla como
     * guard clause antes de construir el Value Object.
     */
    public static boolean esValido(String valor) {
        return valor != null && valor.contains("@");
    }
}
