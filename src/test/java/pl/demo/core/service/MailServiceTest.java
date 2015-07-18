package pl.demo.core.service;

import org.apache.velocity.app.VelocityEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import pl.demo.web.dto.EMailDTO;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {

    private MailService testSubject;
    @Mock private JavaMailSender mailSender;
    @Mock private VelocityEngine velocityEngine;

    @Before
    public void setUp() throws Exception {
        this.testSubject = new MailService();

        ReflectionTestUtils.setField(this.testSubject, "mailSender", mailSender);
        ReflectionTestUtils.setField(this.testSubject, "velocityEngine", velocityEngine);
    }

    @Test
    public void testSendMail() throws Exception {

        //given
        EMailDTO emailDTO= new EMailDTO();
        emailDTO.setTitle("title");
        emailDTO.setReceipt("receipt1");
        emailDTO.setSender("sender1");

        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));

        when ( mailSender.createMimeMessage() ).thenReturn(mimeMessage);
        when( velocityEngine.mergeTemplate( anyString(), anyObject(), anyObject()) )
                .thenAnswer(new Answer<Object>() {


                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });
                //when

        this.testSubject.sendMail(emailDTO);

        //then

        verify(mailSender).send(mimeMessage);

        Address []address =  mimeMessage.getFrom();
        Address []address2 =  mimeMessage.getAllRecipients();
    }
}