package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.demo.web.dto.DashboardDTO;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.DASHBOARD.ENDPOINT;

/**
 * Created by robertsikora on 11.10.15.
 */

@RequestMapping(ENDPOINT)
public interface DashboardEndpoint {

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<DashboardDTO> getDashboardData();
}
