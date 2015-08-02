package pl.demo.core.service;

import pl.demo.core.model.entity.Advert;
import pl.demo.web.dto.DashboardDTO;

import java.util.Collection;

/**
 * Created by robertsikora on 27.07.15.
 */
public interface DashboardService {

    DashboardDTO buildDashboard();

    Collection<Advert> findTop4();
}
