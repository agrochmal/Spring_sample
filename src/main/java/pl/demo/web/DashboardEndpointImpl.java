package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.service.dashboard.DashboardService;
import pl.demo.web.dto.DashboardDTO;

@RestController
public class DashboardEndpointImpl implements DashboardEndpoint {

    private DashboardService dashboardService;

    @Override
    public ResponseEntity<DashboardDTO> getDashboardData(){
        return ResponseEntity.ok().body(this.dashboardService.buildDashboard());
    }

    @Autowired
    public void setDashboardService(final DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
}
