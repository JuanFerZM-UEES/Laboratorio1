package edu.uees.refactor.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ae5 · Refactorización 2 · Value Object {@link Correo}.
 *
 * Prueba el contrato del Value Object en aislamiento, siguiendo el
 * patrón de {@code assertThrows} que enseñó la sección 14 del
 * Laboratorio 2 (record {@code Dinero}).
 */
class CorreoTest {

    @Test
    void correoValidoSeAcepta() {

        // Arrange & Act
        Correo correo = new Correo("ana@uees.edu.ec");

        // Assert
        assertEquals("ana@uees.edu.ec", correo.valor());
        assertTrue(Correo.esValido("ana@uees.edu.ec"));
    }

    @Test
    void correoInvalidoLanzaExcepcion() {

        // Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Correo("correo-invalido")
        );
    }

    @Test
    void correoNuloLanzaExcepcion() {

        // Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Correo(null)
        );
    }

    @Test
    void esValidoNoLanzaExcepcionParaCorreoInvalido() {

        // Assert — esValido() es la puerta segura antes de construir
        assertFalse(Correo.esValido("correo-invalido"));
        assertFalse(Correo.esValido(null));
    }
}
