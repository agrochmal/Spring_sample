package pl.demo.core.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.demo.core.util.Assert;
import pl.demo.web.dto.EMailDTO;

import java.util.function.Supplier;

/**
 * Created by robertsikora on 05.11.2015.
 */

@Service
public class MailDTOSupplier {

    private static String ADMIN_EMAIL_ADDRESS;

    @Value("${ADMIN.receipt-email}")
    public void setAdminEmailAddress(String ADMIN_EMAIL_ADDRESS) {
        MailDTOSupplier.ADMIN_EMAIL_ADDRESS = ADMIN_EMAIL_ADDRESS;
    }

    public static Supplier<EMailDTO> get(final String title,
                                         final String content){
        return get(title, content, ADMIN_EMAIL_ADDRESS, ADMIN_EMAIL_ADDRESS);
    }

    public static Supplier<EMailDTO> get(final String title,
                                  final String content,
                                  final String receipt,
                                  final String sender){
        Assert.hasText(title);
        Assert.hasText(content);
        Assert.hasText(receipt);
        Assert.hasText(sender);

        return ()-> {
            EMailDTO eMailDTO = new EMailDTO();
            eMailDTO.setTitle(title);
            eMailDTO.setContent(content);
            eMailDTO.setReceipt(receipt);
            eMailDTO.setSender(sender);
            return eMailDTO;
        };
    }
}
