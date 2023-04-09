package dev.ergo.practical.common.exception;

import dev.ergo.practical.common.constants.LoggingTypes;
import dev.ergo.practical.common.response.ErrorResponse;
import dev.ergo.practical.model.Log;
import dev.ergo.practical.service.log.LogService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.util.Arrays;

// We are extending the ResponseEntityExceptionHandler to handle exceptions thrown by Spring
// ResponseEntityExceptionHandler is a convenient base class for controller advice classes.
// It provides exception handlers for internal Spring exceptions.
// The annotation @ControllerAdvice allows us to intercept and modify the return values of controller methods,
// in our case to handle exceptions.
@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    private final LogService logService;


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException exception, WebRequest request) {
        log.error("Resource not found at: " + ((ServletWebRequest) request).getRequest().getRequestURI());
        logService.save(
                new Log(
                        0,
                        "Resource not found at: " + getRequestURI(request),
                        LoggingTypes.ERROR.name(),
                        new Timestamp(System.currentTimeMillis()
                        )
                ));

        return buildErrorResponse(
                null,
                exception,
                HttpStatus.NOT_FOUND
        );
    }

    // Specific for delete endpoint since the passed path variable HAS to be integer
    // The user can also pass in other data types which will throw a MethodArgumentTypeMismatchException
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentTypeMismatchException exception, WebRequest request) {
        logService.save(
                new Log(
                        0,
                        "Resource could not be deleted: " + getRequestURI(request),
                        LoggingTypes.ERROR.name(),
                        new Timestamp(System.currentTimeMillis()
                        )
                ));

        return buildErrorResponse(
                null,
                exception,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request){
        log.error("Unknown error occurred: " + exception.getMessage() + " at " + getRequestURI(request));
        return buildErrorResponse(
                "Unknown error occurred",
                exception,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // This exception is thrown when the request body is not valid
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException exception) {
        log.error("Invalid person object received: ");
        return buildErrorResponse(
                null,
                exception,
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildErrorResponse(
                null,
                ex,
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildErrorResponse(
                null,
                ex,
                HttpStatus.BAD_REQUEST
        );
    }

    private ResponseEntity<Object> buildErrorResponse(String message, Exception exception, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                message == null ? exception.getMessage() : message,
                Arrays.toString(exception.getStackTrace())
        );

        if (exception instanceof ConstraintViolationException) {
            for (ConstraintViolation violation : ((ConstraintViolationException) exception).getConstraintViolations()) {
                errorResponse.addValidationError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        } else if (exception instanceof MethodArgumentNotValidException) {
            for (FieldError fieldError : ((MethodArgumentNotValidException) exception).getFieldErrors()) {
                errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private String getRequestURI(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
