package pl.demo.core.service;

import org.springframework.transaction.annotation.Transactional;
import pl.demo.web.dto.DashboardDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 27.07.15.
 */

@Transactional(readOnly = true)
public interface DashboardService {
    @NotNull
    DashboardDTO buildDashboard();
}
