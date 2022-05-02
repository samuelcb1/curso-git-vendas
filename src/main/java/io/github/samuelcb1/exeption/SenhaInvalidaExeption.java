package io.github.samuelcb1.exeption;

public class SenhaInvalidaExeption extends RuntimeException {
    public SenhaInvalidaExeption() {
        super("Senha Invalida");
    }
}
