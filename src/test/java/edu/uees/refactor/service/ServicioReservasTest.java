package edu.uees.refactor.service;

import edu.uees.refactor.domain.EstadoReserva;
import edu.uees.refactor.domain.Reserva;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Red de seguridad de caracterización para {@link ServicioReservas}.
 *
 * Estas pruebas NO afirman que el comportamiento actual sea el ideal;
 * afirman cuál es el comportamiento actual, para que cualquier
 * refactorización futura (Ae5) pueda demostrar, con evidencia
 * ejecutable, que no lo cambió sin querer.
 */
class ServicioReservasTest {

    private ServicioReservas servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioReservas();
    }

    @Test
    void normalActualmenteRetornaCuarenta() {

        // Arrange
        Reserva reserva = reservaNormalValida();

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertAll(
                () -> assertEquals(40, total, 0.001),
                () -> assertEquals(
                        EstadoReserva.CONFIRMADA,
                        reserva.getEstado()
                )
        );
    }

    @Test
    void vipActualmenteRetornaTreintaYCuatro() {

        // Arrange
        Reserva reserva = reservaVipValida();

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertEquals(34, total, 0.001);
        assertEquals(
                EstadoReserva.CONFIRMADA,
                reserva.getEstado()
        );
    }

    @Test
    void correoInvalidoNoProcesaReserva() {

        // Arrange
        LocalDateTime inicio =
                LocalDateTime.of(2026, 9, 20, 10, 0);

        Reserva reserva = new Reserva(
                "R-EMAIL",
                "correo-invalido",
                inicio,
                inicio.plusHours(1),
                "NORMAL"
        );

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertEquals(0, total, 0.001);
        assertEquals(
                EstadoReserva.PENDIENTE,
                reserva.getEstado()
        );
    }

    @Test
    void periodoConFinAnteriorNoProcesa() {

        // Arrange
        LocalDateTime inicio =
                LocalDateTime.of(2026, 9, 20, 10, 0);

        Reserva reserva = new Reserva(
                "R-PERIODO",
                "ana@uees.edu.ec",
                inicio,
                inicio.minusHours(1),
                "NORMAL"
        );

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertEquals(0, total, 0.001);
        assertEquals(
                EstadoReserva.PENDIENTE,
                reserva.getEstado()
        );
    }

    @Test
    void dosHorasExactasPermitenProcesar() {

        // Arrange
        Reserva reserva = reservaNormalValida();

        // Act
        double total = servicio.procesar(reserva, 2);

        // Assert
        assertEquals(40, total, 0.001);
        assertEquals(
                EstadoReserva.CONFIRMADA,
                reserva.getEstado()
        );
    }

    @Test
    void unaHoraNoPermiteProcesar() {

        // Arrange
        Reserva reserva = reservaNormalValida();

        // Act
        double total = servicio.procesar(reserva, 1);

        // Assert
        assertEquals(0, total, 0.001);
        assertEquals(
                EstadoReserva.PENDIENTE,
                reserva.getEstado()
        );
    }

    @Test
    void reservaNulaRetornaCero() {

        // Act
        double total = servicio.procesar(null, 5);

        // Assert
        assertEquals(0, total, 0.001);
    }

    private Reserva reservaNormalValida() {
        LocalDateTime inicio =
                LocalDateTime.of(2026, 9, 20, 10, 0);

        return new Reserva(
                "R-NORMAL",
                "ana@uees.edu.ec",
                inicio,
                inicio.plusHours(1),
                "NORMAL"
        );
    }

    private Reserva reservaVipValida() {
        LocalDateTime inicio =
                LocalDateTime.of(2026, 9, 20, 10, 0);

        return new Reserva(
                "R-VIP",
                "vip@uees.edu.ec",
                inicio,
                inicio.plusHours(1),
                "VIP"
        );
    }
}
