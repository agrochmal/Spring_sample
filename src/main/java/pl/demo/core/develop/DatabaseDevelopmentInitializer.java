package pl.demo.core.develop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Role;
import pl.demo.core.model.entity.RoleName;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.RoleNameRepository;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Only for development time.
 */
@Component
@Transactional
public class DatabaseDevelopmentInitializer {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private RoleNameRepository roleNameRepository;
  @Autowired
  private AdvertRepository advertRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  private @Value("${develop.init-users}") boolean initUsers;

  private @Value("${develop.init-adverts}") boolean initAdverts;

    private @Value("${develop.adverts-count}") long advertCount;

  @PostConstruct
  private void init() {
      if(initUsers) {
          initUsers();
      }
      if(initAdverts){
          for(int i=0; i<advertCount;i++){
              initAdverts(i, null);
          }
      }
  }

  private void initUsers() {
    User userExists = userRepository.findByUsername("user");
      User adminUser = null;
    if (userExists == null) {
      // 2 examples of roles: user and admin
      RoleName roleNameUser = new RoleName("user");
      RoleName roleNameAdmin = new RoleName("admin");
      roleNameUser = roleNameRepository.save(roleNameUser);
      roleNameAdmin = roleNameRepository.save(roleNameAdmin);

      // 2 examples of users: 1) user with role user, 2) admin with role user and admin

      Role roleForUser = new Role(roleNameUser);
      roleForUser = roleRepository.save(roleForUser);

      User plainUser = new User("user@user.pl", passwordEncoder.encode("user"));
      plainUser.setLocation("Rzeszów");
      plainUser.setName("Robert Sikora");
      plainUser.setPhone("123 456 789");
      plainUser.setLat(50.0411867d);
      plainUser.setLng(21.999119599999972d);
      plainUser.addRole(roleForUser);
      userRepository.save(plainUser);

      adminUser = new User("admin@admin.pl", passwordEncoder.encode("admin"));
      Role roleForAdmin = new Role(roleNameAdmin);
      adminUser.setLocation("Rzeszów");
      adminUser.setName("Robert Sikora");
      adminUser.setPhone("661 333 222");
      adminUser.setLat(50.0411867d);
      adminUser.setLng(21.999119599999972d);
      roleForAdmin = roleRepository.save(roleForAdmin);

      adminUser.addRole(roleForUser);
      adminUser.addRole(roleForAdmin);
      userRepository.save(adminUser);
    }
  }

  private void initAdverts(int nr, User adminUser){

    List<Advert> adverts = new ArrayList<>();

    Advert advert= new Advert();
    advert.setTitle("Remonty,wykonczenia mieszkan");
    advert.setDescription("Witam;Wykonujemy tanio i solidnie remonty pomieszczen mieszkalnych i nie tylko,zaktes wykonywanych czynnosci:suche zabudowy,gladzie,przerobki elektryczne,malowanie,ukladanie paneli,glazura terakota i inne prace montarzowo wykonczeniowe.Serdecznie zapraszamy przyszlych klientow. ");
    advert.setEndDate(new Date());
    advert.setContact("Robert Sikora");
    advert.setEmail("robertsikora@interia.pl");
    advert.setPhone("605 854 368");
    advert.setLocationName("Rzeszów, podkarpackie");
      advert.setLatitude(50.0411867d);
      advert.setLongitude(21.999119599999972d);
    advert.setUser(adminUser);
    adverts.add(advert);

    Advert advert2= new Advert();
    advert2.setTitle("Sprzedam tarcze do ciecia 1mm 1zl za sztuke mam 100 sztuk ");
    advert2.setDescription("Witam mam do sprzedania tarcze do ciecia male 1mm 100 sztuk 1zl za sztuke ");
    advert2.setEndDate(new Date());
    advert2.setContact("Robert Sikora");
    advert2.setEmail("robertsikora@interia.pl");
    advert2.setPhone("605 854 368");
    advert2.setLocationName("Rzeszów, podkarpackie");
      advert2.setLatitude(50.0411867d);
      advert2.setLongitude(21.999119599999972d);
    adverts.add(advert2);

    Advert advert3= new Advert();
    advert3.setTitle("Pranie tapicerki samochodowej ");
    advert3.setDescription("ODNAWIANIE I ODŚWIEŻANIE SAMOCHODÓW\n" +
            "- pranie: tapicerki, boczków, podsufitki, podłogi, dywaników i wykładzin, bagażnika,\n" +
            "- mycie: lakieru, tapicerki skórzanej, szyb, plastików, felg,\n" +
            "- impregnowanie: plastików i tapicerki skórzanej,\n" +
            "- nabłyszczanie lakieru,\n" +
            "- usuwanie: drobnych rys, smoły, obcych farb, naklejonych reklam, naklejek.\n" +
            "\n" +
            "Fotele, kanapy i wszystko co da się bezpiecznie wymontować - piorę i myję na zewnątrz samochodu.\n" +
            "\n" +
            "Używam bardzo wysokiej jakości środków, które są antyalergiczne i ekologiczne.\n" +
            "\n" +
            "Możliwy odbiór samochodu od klienta.  ");
    advert3.setEndDate(new Date());
    advert3.setContact("Robert Sikora");
    advert3.setEmail("robertsikora@interia.pl");
    advert3.setPhone("605 854 368");
    advert3.setLocationName("Rzeszów, podkarpackie");
      advert3.setLatitude(50.0411867d);
      advert3.setLongitude(21.999119599999972d);
    adverts.add(advert3);


    Advert advert4= new Advert();
    advert4.setTitle("Przewóz osób do Holandii Piła, Złotów, Wyrzysk, Łobżenica, Debrzno ");
    advert4.setDescription("Tony Transport jest profesjonalną firmą przewozową, która świadczy usługi przewozowe na wysokim poziomie na rynku krajowym jak i międzynarodowym (Polska, Niemcy, Holandia)\n" +
            "\n" +
            "OFERUJEMY:\n" +
            "-Przejazd z podanego adresu na podany adres\n" +
            "-ubezpieczenie na czas podróży NNW-MAX\n" +
            "\n" +
            "Przejazd do Holandii od 240zł do 260zł/ osoba\n" +
            "\n" +
            "Wyjazdy do Holandii:\n" +
            "Wtorek, Środa, Czwartek, Piątek, Niedziela\n" +
            "\n" +
            "Wyjazdy do Polski:\n" +
            "Poniedziałek, Wtorek, Środa, Czwartek, Piątek, Niedziela\n" +
            "\n" +
            "Przykładowe Powiaty-miejscowości obsługiwane przez firmę Tony Transport:\n" +
            "\n" +
            "Polska-Województwa- Powiaty:\n" +
            "\n" +
            "Kujawsko-Pomorskie: Bydgoski, Nakielski, Sępoleński, Tucholski, (Sępólno Krajeńskie, Więcbork, Nakło nad Notecią, Bydgoszcz)\n" +
            "\n" +
            "Lubuskie: Gorzowski, Międzyrzecki, Słubicki, Drezdenecki, Sulęciński, Świebodziński (Dobiegniew, Strzelce Krajeńskie, Gorzów Wielkopolski, Kostrzyn nad Odrą, Ośno Lubuskie, Sulęcin, Torzym, Rzepin, Świecki)\n" +
            "\n" +
            "Pomorskie: Chojnicki, Człuchowski, (Chojnice, Człuchów, Debrzno)\n" +
            "\n" +
            "Wielkopolskie: Chodzieski, Czarnkowski, Międzychodzki, Obornicki, Pilski, Złotowski. (Jastrowie, Piła, Złotów, Zakrzewo, Wyrzysk, Łobżenica, Okonek, Chodzież, Szamocin, Ujście, Trzcianka, Poznań)\n" +
            "\n" +
            "Zachodniopomorskie: Choszczeński, Drawski, Gryfiński, Łobeski, Myśliborski, Policki, Pyrzycki, Stargardzki, Szczecinecki, Świdwiński, Wałecki. (Wałcz, Szczecinek, Człopa, Mirosławiec, Kalisz Pomorski, Recz, Stargard Szczeciński, Pyrzyce, Gryfino, Szczecin.) ");
    advert4.setEndDate(new Date());
    advert4.setContact("Robert Sikora");
    advert4.setEmail("robertsikora@interia.pl");
    advert4.setPhone("605 854 368");
    advert4.setLocationName("Rzeszów, podkarpackie");
      advert4.setLatitude(50.0411867d);
      advert4.setLongitude(21.999119599999972d);
    adverts.add(advert4);

    Advert advert5= new Advert();
    advert5.setTitle("Malowanie, tapetowanie drobne prace remontowe.  ");
    advert5.setDescription("Malowanie, tapetowanie drobne prace remontowe.\n" +
            "info . pod. nr tel. ");
    advert5.setEndDate(new Date());
    advert5.setContact("Robert Sikora");
    advert5.setEmail("robertsikora@interia.pl");
    advert5.setPhone("605 854 368");
    advert5.setLocationName("Rzeszów, podkarpackie");
      advert5.setLatitude(50.0411867d);
      advert5.setLongitude(21.999119599999972d);
    adverts.add(advert5);

    Advert advert6= new Advert();
    advert6.setTitle("Cudne Zaproszenia Ślubne Studniówkowe Alma-art ");
    advert6.setDescription("Firma Alma – art oferuje Państwu oryginalne zaproszenia i gustowne dodatki na tak wyjątkowe okazje, jak ślub, wesele, studniówka, itp.\n" +
            "\n" +
            "Zajmujemy się projektowaniem, drukowaniem i zdobieniem zaproszeń ślubnych i okolicznościowych. W naszej ofercie znajdują się zaproszenia ręcznie robione i laserowo wycinane, tradycyjne i nowoczesne, skromne i bogato zdobione. Każdy znajdzie coś dla siebie:)\n" +
            "\n" +
            "Ceny zaczynają się już od 1zł./szt. - zaproszenia z nadrukiem, personalizacją i kopertą.\n" +
            "\n" +
            "Zaproszenia wykonujemy przy użyciu profesjonalnych drukarek oraz sprzętu do cięcia i zaginania papieru. Korzystamy tylko z najwyższej jakości papierów i sprawdzonych materiałów ozdobnych, by w efekcie uzyskać eleganckie i efektowne zaproszenie.\n" +
            "\n" +
            "Specjalizujemy się również w wykonywaniu niepowtarzalnych Ksiąg Gości - księga taka może być wspaniałą pamiątką z najbardziej wyjątkowych i uroczystych dni w życiu.\n" +
            "\n" +
            "Zapraszamy na stronę alma-art.pl ");
    advert6.setEndDate(new Date());
    advert6.setContact("Robert Sikora");
    advert6.setEmail("robertsikora@interia.pl");
    advert6.setPhone("605 854 368");
    advert6.setLocationName("Rzeszów, podkarpackie");
      advert6.setLatitude(50.0411867d);
      advert6.setLongitude(21.999119599999972d);
    adverts.add(advert6);


    Advert advert7= new Advert();
    advert7.setTitle("Pranie, czyszczenie tapicerki samochodowej, meblowej ");
    advert7.setDescription("Świadcze usługi związane z praniem tapicerki ;samochodowej\n" +
            "; meblowej\n" +
            "; oraz dywany ");
    advert7.setEndDate(new Date());
    advert7.setContact("Robert Sikora");
    advert7.setEmail("robertsikora@interia.pl");
    advert7.setPhone("605 854 368");
    advert7.setLocationName("Rzeszów, podkarpackie");
      advert7.setLatitude(50.0411867d);
      advert7.setLongitude(21.999119599999972d);
    adverts.add(advert7);


    Advert advert8= new Advert();
    advert8.setTitle("KOPARKO-ŁADOWARKA Wrocław usługi wynajem");
    advert8.setDescription("Usługi koparką budowlane i nietylko możliwość wynajmu na godziny lub od roboty  ");
    advert8.setEndDate(new Date());
    advert8.setContact("Robert Sikora");
    advert8.setEmail("robertsikora@interia.pl");
    advert8.setPhone("605 854 368");
    advert8.setLocationName("Rzeszów, podkarpackie");
      advert8.setLatitude(50.0411867d);
      advert8.setLongitude(21.999119599999972d);
    adverts.add(advert8);

    Advert advert9= new Advert();
    advert9.setTitle(" Wykończenia wnętrz_ Docieplenia poddaszy ");
    advert9.setDescription("- Gładzie gipsowe\n" +
            "- Malowanie\n" +
            "- Tapetowanie\n" +
            "- Panele ścienne i podłogowe\n" +
            "- Tynki ozdobne\n" +
            "- Ścianki/Sufity karton gips\n" +
            "- Docieplenia poddaszy i ścian\n" +
            "- Murowania (cegła, kamień ozdobny)\n" +
            "\n" +
            "i wiele innych... ");
    advert9.setEndDate(new Date());
    advert9.setContact("Robert Sikora");
    advert9.setEmail("robertsikora@interia.pl");
    advert9.setPhone("605 854 368");
    advert9.setLocationName("Rzeszów, podkarpackie");
      advert9.setLatitude(50.0411867d);
      advert9.setLongitude(21.999119599999972d);
    adverts.add(advert9);

    Advert advert10= new Advert();
    advert10.setTitle(" Usługi informatyczne");
    advert10.setDescription("Pisanie stron www, programowanie, java, C, C#, web");
    advert10.setEndDate(new Date());
    advert10.setContact("Robert Sikora");
    advert10.setEmail("robertsikora@interia.pl");
    advert10.setPhone("605 854 368");
    advert10.setLocationName("Rzeszów, podkarpackie");
    advert10.setLatitude(50.0411867d);
    advert10.setLongitude(21.999119599999972d);
    adverts.add(advert10);

    for(int i=0; i <adverts.size(); i++){
       Advert t = adverts.get(i);
       t.setTitle(t.getTitle().concat(" ").concat(String.valueOf(nr)).concat(String.valueOf(i)));
       advertRepository.save(t);
    }
  }
}
