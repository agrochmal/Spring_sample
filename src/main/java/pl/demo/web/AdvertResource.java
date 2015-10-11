package pl.demo.web;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Advert;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.ADVERT.*;

/**
 * Created by robertsikora on 11.10.15.
 */
public interface AdvertResource {

    @RequestMapping(value = ADVERT_NEW, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Advert> createNew();


    @RequestMapping(value = ADVERT_SEARCH, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> search(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable);

    @RequestMapping(value = ADVERT_UPDATE_STATUS, method = RequestMethod.POST)
    ResponseEntity<?> updateStatus(@PathVariable("id") Long id, @RequestParam(value = "status") String status);

    @RequestMapping(value = ADVERT_SEND_MAIL, method = RequestMethod.POST)
    ResponseEntity<?> sendEmail(@PathVariable("id") Long id, @Valid @RequestBody EMailDTO email, BindingResult bindingResult);
}
