package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.service.AdvertService;
import pl.demo.core.util.Assert;
import pl.demo.core.util.EntityUtils;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.ADVERT.*;

@RestController
@RequestMapping(ADVERT_ENDPOINT)
public class AdvertRestResource extends AbstractCRUDResource<Long, Advert> {

    private final AdvertService advertService;

    @Autowired
    public AdvertRestResource(final AdvertService advertService) {
        super(advertService);
        this.advertService = advertService;
    }

    @RequestMapping(value = ADVERT_NEW,
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)

    public ResponseEntity<Advert> createNew() {
        return ResponseEntity.ok().body(advertService.createNew());
    }

    @RequestMapping(value = ADVERT_SEARCH,
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)

    public ResponseEntity<?> search(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
        return ResponseEntity.ok().body(this.advertService.findBySearchCriteria(searchCriteriaDTO, pageable));
    }

    @RequestMapping(value = ADVERT_UPDATE_STATUS,
            method = RequestMethod.POST)

    public ResponseEntity<?> updateStatus(@PathVariable("id") final Long id, @RequestParam(value = "status") final String status) {
        this.advertService.updateActiveStatus(id, Boolean.valueOf(status));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = ADVERT_SEND_MAIL,
            method = RequestMethod.POST)

    public ResponseEntity<?> sendEmail(@PathVariable("id") final Long id, @Valid @RequestBody final EMailDTO email,
                                       final BindingResult bindingResult) {
        Assert.hasErrors(bindingResult);
        this.advertService.sendMail(email);
        return ResponseEntity.noContent().build();
    }
}
