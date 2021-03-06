package pl.demo.core.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.service.advert.AdvertServiceImpl;
import pl.demo.core.service.mail.MailServiceImpl;
import pl.demo.core.service.searching.SearchService;
import pl.demo.core.service.user.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class AdvertServiceImplTest {

    private AdvertServiceImpl testSubject;

    @Mock private AdvertRepository advertRepo;
    @Mock private SearchService searchService;
    @Mock private UserService userService;
    @Mock private MailServiceImpl mailService;

    @Before
    public void setUp() throws Exception {
       // testSubject = new AdvertServiceImpl(advertRepo, genericRepository, searchService, userService, mailService);
    }

    @After
    public void tearDown() throws Exception {

    }
    @Ignore
    @Test
    public void testFindOne() throws Exception {
        //give
        Advert expected = new Advert();
        expected.setTitle("title");

        when(advertRepo.findOne(1L)).thenReturn( expected );
        //when
        Advert result = testSubject.findOne(1L);
        //then
        assertEquals(expected, result);
    }

    @Ignore
    @Test(expected = RuntimeException.class)
    public void testEdit() throws Exception {

        //given
        //when
        testSubject.edit(null, null);
        //then
    }

    @Ignore
    @Test
    public void testSave() throws Exception {
        //given
        Advert expected = new Advert();
        expected.setTitle("title");

        User user = createTestUser();

     //   when(userService.getLoggedUser()).thenReturn(user);
        when(advertRepo.save(expected)).thenReturn(expected);

        //when

        Advert result = testSubject.save(expected);

        //then
        verify(advertRepo).save(expected);
        assertEquals(expected, result);
        assertEquals(result.getUser(), user);
    }

    @Test
    public void testFindAll() throws Exception {

    }

    @Test
    public void testFindByUserName() throws Exception {

        //given

        //when

//        testSubject.findByUserName("test");

        //then

    }

    @Ignore
    @Test
    public void testCreateNew() throws Exception {

    }

    @Test
    public void testFindBySearchCriteria() throws Exception {

    }

    @Ignore
    @Test
    public void testUpdateActive() throws Exception {

        //given

        //ArgumentCaptor<Advert> argument = ArgumentCaptor.forClass(Advert.class);

        Advert testAdvert = new Advert();

        when(advertRepo.findOne(1L)).thenReturn(testAdvert);

        //when

      //  testSubject.updateActive(1L, Boolean.TRUE);

        //then

        verify(advertRepo).save(testAdvert);

        assertEquals(Boolean.TRUE, testAdvert.getActive());

    }

    @Test
    public void testSendMail() throws Exception {

    }

    @Test
    public void testPostComment() throws Exception {

    }

    private User createTestUser(){

        User user = new User();
        user.setName("RS");
        return user;
    }
}