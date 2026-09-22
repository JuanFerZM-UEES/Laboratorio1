package edu.uees.refactor.domain;

import java.time.LocalDateTime;

/**
 * Value Object que agrupa el Data Clump {@code inicio}/{@code fin}.
 *
 * Ae5 · Refactorización 3 · Agrupar Data Clumps (fila 4 de
 * {@code docs/03_MATRIZ_DIAGNOSTICO.md}): las dos fechas siempre
 * viajaban juntas —como parámetros, como campos, y como la misma
 * condición de validez— sin que el código lo dijera explícitamente.
 *
 * Decisión de diseño deliberada (ver {@code docs/04_MATRIZ_RIESGO.md},
 * fila "Introducir PeriodoReserva" = Alto riesgo): a diferencia de
 * {@link Correo}, este constructor NO valida ni lanza excepción. La
 * matriz de riesgo advertía que mover esa validación al constructor
 * cambiaría CUÁNDO se detecta un periodo inválido — de "procesar()
 * devuelve 0" a "el objeto ni siquiera se puede construir"— y ese es
 * justamente el cambio de contrato observable que Ae5 pide evitar
 * mientras no exista una decisión explícita y una prueba dedicada para
 * ese nuevo comportamiento. Por eso este Value Object se limita a
 * agrupar el dato y ofrecer {@link #esValido()} como consulta, dejando
 * esa decisión más grande (y el cambio del constructor de
 * {@link Reserva}) para una iteración futura, fuera del alcance
 * acotado de esta entrega.
 */
public record PeriodoReserva(LocalDateTime inicio, LocalDateTime fin) {

    public boolean esValido() {
        return inicio != null
                && fin != null
                && fin.isAfter(inicio);
    }
}
