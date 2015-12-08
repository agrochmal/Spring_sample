package pl.demo.web.lookup;

import pl.demo.core.model.dictionary.PaymentType;

import java.util.Collections;
import java.util.List;

/**
 * Created by robertsikora on 04.12.2015.
 */
public class PaymentLookup implements Lookup<PaymentType> {

    @Override
    public List<PaymentType> getData() {
        PaymentType paymentType = new PaymentType();
        paymentType.setType("Gotowka");
        return Collections.singletonList(paymentType);
    }
}
