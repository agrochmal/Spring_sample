package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.web.dto.DashbordDTO;
import pl.demo.core.service.DashbordService;

@RestController
@RequestMapping("/dashbord")
public class MainRestResource {

    @Autowired
    private DashbordService dashbordService;

    @RequestMapping(method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<DashbordDTO> getDashbordData(){
        return new ResponseEntity<>(this.dashbordService.getData(), HttpStatus.OK);
    }
}
