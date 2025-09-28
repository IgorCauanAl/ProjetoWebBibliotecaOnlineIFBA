package br.ifba.edu.BibliotecaOnline.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.ResponseEntity;
import br.ifba.edu.BibliotecaOnline.excecao.EmailJaExisteException;
import br.ifba.edu.BibliotecaOnline.excecao.LivroDuplicadoException;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.LoginIncorretoException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TratadorGlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(TratadorGlobalControllerAdvice.class);

    // Captura requisições para endpoints que não existem.
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.warn("Endpoint não encontrado: {} {}", request.getMethod(), request.getRequestURI());

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("error", "Endpoint não encontrado");
        modelAndView.addObject("message", String.format("O endpoint %s %s não foi encontrado",
                request.getMethod(), request.getRequestURI()));
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // Captura erros de validação de entrada.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Coleta todos os erros de validação
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Monta o corpo da resposta de erro em um formato JSON amigável
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", errors);

        // Pega a primeira mensagem de erro para ser a mensagem principal
        String defaultMessage = errors.values().stream().findFirst().orElse(ex.getMessage());
        body.put("message", defaultMessage);

        logger.warn("Erro de validação: {}", defaultMessage);

        // Retorna uma ResponseEntity com status 400 (Bad Request) e o corpo em JSON
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Captura erros de tipo inválido nos parâmetros da requisição.
    @ExceptionHandler({ TypeMismatchException.class, MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(TypeMismatchException ex,
            HttpServletRequest request) {
        logger.warn("Erro de tipo inválido: {}", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parâmetro inválido",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Captura erros de método HTTP não suportado.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        logger.warn("Método não suportado: {} {}", request.getMethod(), request.getRequestURI());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Método não permitido",
                ex.getMessage());
        errorResponse.put("supportedMethods", ex.getSupportedHttpMethods());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    // Captura parâmetros obrigatórios ausentes.
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        logger.warn("Parâmetro obrigatório ausente: {}", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parâmetro obrigatório ausente",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Captura exceções de email já existente
    @ExceptionHandler(EmailJaExisteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleEmailJaExisteException(EmailJaExisteException ex) {
        logger.warn("Email já existe: {}", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Email já cadastrado",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Captura exceções de livro duplicado.
    @ExceptionHandler(LivroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleLivroDuplicadoException(LivroDuplicadoException ex) {
        logger.warn("Livro duplicado: {}", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Livro já cadastrado",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Captura exceções de ano de publicação inválido.
    @ExceptionHandler(AnoPublicacaoInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleAnoPublicacaoInvalidoException(AnoPublicacaoInvalidoException ex) {
        logger.warn("Ano de publicação inválido: {}", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Ano de publicação inválido",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Captura exceções de login incorreto
    @ExceptionHandler(LoginIncorretoException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleLoginIncorretoException(LoginIncorretoException ex,
            HttpServletRequest request) {
        logger.warn("Tentativa de login incorreto para IP: {}", request.getRemoteAddr());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Credenciais inválidas",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Captura todas as outras exceções não tratadas (catch-all).
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Erro inesperado em {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        String errorMessage = "Ocorreu um erro inesperado. Por favor, entre em contato com o suporte.";

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                errorMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Cria um mapa de resposta de erro padronizado.
    private Map<String, Object> createErrorResponse(int status, String error, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDateTime.now());
        return errorResponse;
    }
}
