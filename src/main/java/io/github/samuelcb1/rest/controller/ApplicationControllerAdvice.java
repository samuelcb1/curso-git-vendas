package io.github.samuelcb1.rest.controller;

import io.github.samuelcb1.exeption.PedidoNaoEncontradoExeption;
import io.github.samuelcb1.exeption.RegraNegocioExeption;
import io.github.samuelcb1.rest.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler (RegraNegocioExeption.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegraNegocioExeption(RegraNegocioExeption ex){
        String messagemErro = ex.getMessage();
        return new ApiErrors(messagemErro);
    }

    @ExceptionHandler(PedidoNaoEncontradoExeption.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handlePedidoNotFoundExeption(PedidoNaoEncontradoExeption ex  ){
        return new ApiErrors(ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodNotValidExeption(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(errors);
    }

}
