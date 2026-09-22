package edu.uees.refactor.service;

import edu.uees.refactor.domain.Reserva;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Ae5 · Refactorización 1 · Extract Class ({@link NotificadorReserva}).
 *
 * Antes de este refactor, la única forma de "ver" la notificación era
 * leer la salida de consola dentro de {@code ServicioReservas.procesar()}.
 * Al inyectar {@link NotificadorReserva} por constructor, ahora se puede
 * reemplazar por un doble de prueba (spy) y verificar el comportamiento
 * sin depender de {@code System.out} — la mejora de testabilidad que
 * proponía {@code docs/06_PLAN_REFACTORIZACION.md} (paso 3), y que la
 * guía del Laboratorio 2 (sección 20) anticipaba como la pregunta
 * "¿necesito observar que se notificó?".
 */
class NotificacionServicioReservasTest {

    /** Spy simple: sin framework de mocks, solo registra llamadas. */
    private static class NotificadorReservaSpy extends NotificadorReserva {
        int llamadas = 0;
        Reserva ultimaReserva;

        @Override
        public void notificar(Reserva reserva) {
            llamadas++;
            ultimaReserva = reserva;
        }
    }

    @Test
    void notificaExactamenteUnaVezParaReservaValida() {

        // Arrange
        NotificadorReservaSpy spy = new NotificadorReservaSpy();
        ServicioReservas servicio = new ServicioReservas(spy);

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 20, 10, 0);
        Reserva reserva = new Reserva(
                "R-SPY-OK",
                "ana@uees.edu.ec",
                inicio,
                inicio.plusHours(1),
                "NORMAL"
        );

        // Act
        servicio.procesar(reserva, 5);

        // Assert
        assertEquals(1, spy.llamadas);
        assertEquals(reserva, spy.ultimaReserva);
    }

    @Test
    void noNotificaCuandoLaReservaEsRechazada() {

        // Arrange
        NotificadorReservaSpy spy = new NotificadorReservaSpy();
        ServicioReservas servicio = new ServicioReservas(spy);

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 20, 10, 0);
        Reserva reservaCorreoInvalido = new Reserva(
                "R-SPY-MAL",
                "correo-invalido",
                inicio,
                inicio.plusHours(1),
                "NORMAL"
        );

        // Act
        servicio.procesar(reservaCorreoInvalido, 5);

        // Assert
        assertEquals(0, spy.llamadas);
    }
}
