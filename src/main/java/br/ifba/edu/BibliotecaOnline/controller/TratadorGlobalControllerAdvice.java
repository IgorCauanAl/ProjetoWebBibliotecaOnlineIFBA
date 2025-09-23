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

import br.ifba.edu.BibliotecaOnline.excecao.RecursoNaoEncontradoException;
import br.ifba.edu.BibliotecaOnline.excecao.EmailJaExisteException;
import br.ifba.edu.BibliotecaOnline.excecao.LivroDuplicadoException;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.LoginIncorretoException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class TratadorGlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(TratadorGlobalControllerAdvice.class);

    // Em revisão: 
    // Captura exceções do tipo RecursoNaoEncontradoException
    /*
     * @ExceptionHandler(RecursoNaoEncontradoException.class)
     * 
     * @ResponseStatus(HttpStatus.NOT_FOUND)
     * public ModelAndView
     * handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
     * logger.warn("Recurso não encontrado: {}", ex.getMessage());
     * 
     * ModelAndView modelAndView = new ModelAndView("error");
     * modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
     * modelAndView.addObject("error", "Recurso não encontrado");
     * modelAndView.addObject("message", ex.getMessage());
     * modelAndView.addObject("timestamp", LocalDateTime.now());
     * return modelAndView;
     * }
     */

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));

        logger.warn("Erro de validação: {}", errorMessage);

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("error", "Dados inválidos");
        modelAndView.addObject("message", errorMessage);
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // Captura erros de tipo inválido nos parâmetros da requisição.
    @ExceptionHandler({ TypeMismatchException.class, MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request) {
        String expectedType = "tipo desconhecido";
        if (ex.getRequiredType() != null) {
            expectedType = ex.getRequiredType().getSimpleName();
        }
        String error = String.format("O valor '%s' não é um valor válido para o parâmetro '%s'. Esperado: %s",
                ex.getValue(), ex.getPropertyName(), expectedType);

        logger.warn("Erro de tipo inválido: {}", error);

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("error", "Parâmetro inválido");
        modelAndView.addObject("message", error);
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // Captura erros de método HTTP não suportado.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        String error = String.format("Método %s não suportado para este endpoint", request.getMethod());

        logger.warn("Método não suportado: {} {}", request.getMethod(), request.getRequestURI());

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        modelAndView.addObject("error", "Método não permitido");
        modelAndView.addObject("message", error);
        modelAndView.addObject("supportedMethods", ex.getSupportedHttpMethods());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // Captura parâmetros obrigatórios ausentes.
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        String error = String.format("O parâmetro '%s' é obrigatório e deve ser do tipo %s",
                ex.getParameterName(), ex.getParameterType());

        logger.warn("Parâmetro obrigatório ausente: {}", error);

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("error", "Parâmetro obrigatório ausente");
        modelAndView.addObject("message", error);
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // Captura exceções de email já existente
    @ExceptionHandler(EmailJaExisteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleEmailJaExisteException(EmailJaExisteException ex) {
        logger.warn("Email já existe: {}", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Email já cadastrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Captura exceções de livro duplicado.
    @ExceptionHandler(LivroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleLivroDuplicadoException(LivroDuplicadoException ex, HttpServletRequest request) {
        logger.warn("Livro duplicado: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.CONFLICT.value());
        modelAndView.addObject("error", "Livro já cadastrado");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // Captura exceções de ano de publicação inválido.
    @ExceptionHandler(AnoPublicacaoInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleAnoPublicacaoInvalidoException(AnoPublicacaoInvalidoException ex,
            HttpServletRequest request) {
        logger.warn("Ano de publicação inválido: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("error", "Ano de publicação inválido");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // Captura exceções de login incorreto
    @ExceptionHandler(LoginIncorretoException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleLoginIncorretoException(LoginIncorretoException ex, HttpServletRequest request) {
        logger.warn("Tentativa de login incorreto para IP: {}", request.getRemoteAddr());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Credenciais inválidas",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Captura todas as outras exceções não tratadas (catch-all).
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Erro inesperado em {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        String errorMessage = "Ocorreu um erro inesperado. Por favor, entre em contato com o suporte.";

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("error", "Erro interno do servidor");
        modelAndView.addObject("message", errorMessage);
        modelAndView.addObject("timestamp", LocalDateTime.now());

        return modelAndView;
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
