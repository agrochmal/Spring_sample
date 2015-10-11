package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.service.DashboardService;
import pl.demo.web.dto.DashboardDTO;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.DASHBOARD.DASHBOARD_ENDPOINT;
import static pl.demo.web.EndpointConst.DASHBOARD.DASHBOARD_STATISTIC;

@RestController
@RequestMapping(DASHBOARD_ENDPOINT)
public class DashboardResourceImpl {

    private DashboardService dashboardService;

    @RequestMapping(value = DASHBOARD_STATISTIC,
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)

    public ResponseEntity<DashboardDTO> getDashboardData(){
        return ResponseEntity.ok().body(this.dashboardService.buildDashboard());
    }

    @Autowired
    public void setDashboardService(final DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
}
