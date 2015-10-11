package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.service.DashboardService;
import pl.demo.web.dto.DashboardDTO;
import static pl.demo.web.EndpointConst.DASHBOARD.DASHBOARD_ENDPOINT;

@RestController
@RequestMapping(DASHBOARD_ENDPOINT)
public class DashboardResourceImpl implements DashboardResource{

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
