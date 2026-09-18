package ar.edu.unvime.api_blank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Manejo de 400 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex){
        Map<String, String> erroresCampos = new HashMap<>();

        //Extraemos cada campo invalido y su mensaje correspondiente
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erroresCampos.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Bad Request");
        respuesta.put("mensaje", "Error de validacion en los datos ingresados");
        respuesta.put("errores", erroresCampos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    //Manejo de 404 Recursos no encontrados
    @ExceptionHandler({RestClientException.class, Exception.class})
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex){
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.NOT_FOUND.value());
        respuesta.put("error","Not Found");
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    //Manejo de 5xx
    @ExceptionHandler({RestClientException.class, Exception.class})
    public ResponseEntity<Map<String, Object>> manejarErroresServidor(Exception ex){
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        respuesta.put("error","Internal Server Error");
        respuesta.put("mensaje", "Ocurrio un error en el servidor o al comunicarse con la API externa");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }


}
