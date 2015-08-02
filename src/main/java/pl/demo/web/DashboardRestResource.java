package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.service.DashboardService;
import pl.demo.web.dto.DashboardDTO;

@RestController
@RequestMapping("/dashboard")
public class DashboardRestResource {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping(value="/statistic",
            method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<DashboardDTO> getDashboardData(){
        return new ResponseEntity<>(this.dashboardService.buildDashboard(), HttpStatus.OK);
    }

    @RequestMapping(value="/top4",
            method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> getDashboardTop4(){
        return new ResponseEntity<>(this.dashboardService.findTop4(), HttpStatus.OK);
    }
}
