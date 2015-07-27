package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.service.DashboardService;
import pl.demo.web.dto.DashbordDTO;

@RestController
@RequestMapping("/dashbord")
public class MainRestResource {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping(method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<DashbordDTO> getDashbordData(){
        return new ResponseEntity<>(this.dashboardService.buildDashboard(), HttpStatus.OK);
    }
}
