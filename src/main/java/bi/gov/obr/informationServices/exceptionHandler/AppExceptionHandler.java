
package bi.gov.obr.informationServices.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import
        org.springframework.http.HttpStatus;
import
        org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import
        org.springframework.web.bind.annotation.ExceptionHandler;
import javax.persistence.EntityExistsException;

@ControllerAdvice

@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String>
    handleConstraintViolationException(DataIntegrityViolationException e) {
        log.error("Eroor error : " + e.getMessage());
        log.error("Eroor error 2 : " +
                e.getCause().getMessage());
        log.error("Eroor error 3 : " +
                e.getRootCause().getMessage());
        String message =
                e.getRootCause().getMessage();
        if (message != null &&
                message.contains("values")) {
            String value =
                    message.split("values")[1].replace("\\n", "");
            System.out.println("Value : " +
                    value);
            return new ResponseEntity<>(value, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>
                ("Une erreur de sauvegarde a eu lieu. Veuillez contacter l'administrateur pour plus de détails."
                        , HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseTemplate>
    handleIllegalArgException(IllegalArgumentException illegalArgumentException) {
        log.error("Error error : " + illegalArgumentException.getMessage());
        ResponseTemplate responseTemplate = new ResponseTemplate();
        responseTemplate.setMessage(illegalArgumentException.getMessage());
        responseTemplate.setStatus("error");
        ResponseEntity<ResponseTemplate>
                responseEntity = new ResponseEntity<>(responseTemplate,
                HttpStatus.UNPROCESSABLE_ENTITY); //
        return
                responseEntity;
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ResponseTemplate>
    handleEntityExistsException(EntityExistsException entityExistsException) {
        log.error("Error error : " + entityExistsException.getMessage());
        ResponseTemplate responseTemplate = new ResponseTemplate();
        responseTemplate.setMessage(entityExistsException.getMessage());
        responseTemplate.setStatus("error");
        ResponseEntity<ResponseTemplate>
                responseEntity = new ResponseEntity<>(responseTemplate,
                HttpStatus.UNPROCESSABLE_ENTITY); //
        return
                responseEntity;
    }

}
