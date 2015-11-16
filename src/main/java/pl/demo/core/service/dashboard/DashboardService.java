package pl.demo.core.service.dashboard;

import org.springframework.validation.annotation.Validated;
import pl.demo.web.dto.DashboardDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 27.07.15.
 */

@Validated
public interface DashboardService {

    @NotNull
    DashboardDTO buildDashboard();
}
