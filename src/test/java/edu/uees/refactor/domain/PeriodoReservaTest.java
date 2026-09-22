package edu.uees.refactor.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ae5 · Refactorización 3 · Value Object {@link PeriodoReserva}.
 *
 * A diferencia de {@link CorreoTest}, aquí NO se prueba
 * {@code assertThrows}: es una decisión de diseño deliberada (ver el
 * javadoc de {@link PeriodoReserva}) que el constructor no valide, así
 * que estas pruebas verifican {@link PeriodoReserva#esValido()} como
 * una consulta booleana, no como una excepción.
 */
class PeriodoReservaTest {

    private static final LocalDateTime INICIO =
            LocalDateTime.of(2026, 9, 20, 10, 0);

    @Test
    void periodoConFinPosteriorAInicioEsValido() {

        // Arrange & Act
        PeriodoReserva periodo =
                new PeriodoReserva(INICIO, INICIO.plusHours(1));

        // Assert
        assertTrue(periodo.esValido());
    }

    @Test
    void periodoConFinAnteriorAInicioNoEsValido() {

        // Arrange & Act
        PeriodoReserva periodo =
                new PeriodoReserva(INICIO, INICIO.minusHours(1));

        // Assert
        assertFalse(periodo.esValido());
    }

    @Test
    void periodoConFechasIgualesNoEsValido() {

        // Arrange & Act — fin == inicio no es "posterior"
        PeriodoReserva periodo = new PeriodoReserva(INICIO, INICIO);

        // Assert
        assertFalse(periodo.esValido());
    }

    @Test
    void periodoConFechasNulasNoEsValido() {

        // Assert
        assertFalse(new PeriodoReserva(null, INICIO).esValido());
        assertFalse(new PeriodoReserva(INICIO, null).esValido());
        assertFalse(new PeriodoReserva(null, null).esValido());
    }
}
