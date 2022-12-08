package org.tf.training.circuitbreaker.servicec;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/servicec")
public class ServiceCDemoController {

    public static final String SERVICE_C_CALLED_WITH_SUCCESS = "Service C called with success for request: ";
    public static final String SERVICE_C_CALLED_WITH_INTERNAL_SERVER_ERROR = "Service C called with internal server error";
    public static final String SERVICE_C_CALLED_WITH_TIMEOUT = "Service C called with timeout";
    final Random rand = new Random();

    @GetMapping("/echo")
    public ResponseEntity<String> echo(){
        log.info("Request received at service C :: Echo");
        return ResponseEntity.ok("Service C called");
    }

    @GetMapping("/random/{id}")
    public ResponseEntity<String> serviceRandomBehaviour(@PathVariable int id) throws InterruptedException {
        log.info("Request received at service C :: Random");
        int randomNumber = this.rand.nextInt(10);
        log.debug("Random number value : {}", randomNumber);

        if(randomNumber < 2){
            log.info(SERVICE_C_CALLED_WITH_SUCCESS);
            return new ResponseEntity<>(SERVICE_C_CALLED_WITH_SUCCESS + id +"\n", HttpStatus.OK);
        }else if(randomNumber < 4){
            log.info(SERVICE_C_CALLED_WITH_INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(SERVICE_C_CALLED_WITH_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            log.info(SERVICE_C_CALLED_WITH_TIMEOUT);
            Thread.sleep(3000);
            return new ResponseEntity<>(SERVICE_C_CALLED_WITH_TIMEOUT, HttpStatus.REQUEST_TIMEOUT);
        }
    }

}
