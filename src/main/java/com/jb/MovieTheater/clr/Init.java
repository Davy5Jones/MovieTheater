package com.jb.MovieTheater.clr;

import com.jb.MovieTheater.beans.mongo.Category;
import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDao;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.repos.ClerkRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import com.jb.MovieTheater.services.AdminService;
import com.jb.MovieTheater.services.CustomerService;
import com.jb.MovieTheater.services.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Init implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScreeningRepository screeningRepository;
    private final PurchaseRepository purchaseRepository;
    private final AdminService adminService;
    private final HomeService homeService;
    private final CustomerService customerService;
    private final ClerkRepository clerkRepository;


    @Override
    public void run(String... args) throws Exception {


        movieRepository.save(Movie.builder()
                .rating(3)
                .name("V for Vendetta")
                .description("Government bad")
                .category(Category.FICTION)
                .isActive(false)
                .duration(140)
                .img("data:image/jpeg;base64,/9j/4A" +
                        "AQSkZJRgABAQAAAQABAAD/2wCEAAoHCBY" +
                        "UFRISFhYYGRgVGRgVGRgcGBwZGBgYGBUZGRgUGhgc" +
                        "IS4lHR4rHxwZJjgmKy80NTU1HiQ7QDs0Py40NTEBDAwMEA8QHx" +
                        "ISHjEsJSQ2NDQ0NDQ0NDQxNDQxNDExNDE0NDQxND0xMTUxOjE0NDQ0MTQ0" +
                        "NjY0MTQ0MTExPzg2Pf/AABEIAQUAwQMBIgACEQEDEQH/xAAcAAEAAQUBAQAAAAA" +
                        "AAAAAAAAAAwECBAUGBwj/xAA/EAACAQIEAwQHBgMHBQAAAAABAgADEQQSITEFQVEGE2Fx" +
                        "IjJSgZGhsQcUQmLB0YKS8BUWIzNyouEkQ1Oywv/EABkBAQEBAQEBAAAAAAAAAAAAAAABAgME" +
                        "Bf/EACQRAQEAAgICAgICAwAAAAAAAAABAhEhMQNBBBJRYSJxkcHR/9oADAMBAAIRAxEAPwDxmIiA" +
                        "iIgIiICIiAiJcqk6AQLYiICJWSUaRYhQLkmwELJbdRFEzcfQFNsgNyPWPK9tQPATDkl2uWNxtlUi" +
                        "IlZIiICIiAiIgIiICIiAiIgIiICIiBWX03IIINiNpYJteD4MMTUf1E1Pien9eElskdPF47nlJGzp4" +
                        "Ba6LUcZWtdjoMwHOY3eYVAxVC+W1yb21NpXBcXLVrHRX9EDoeR95+s1XEKJpu6Da9x5HUTlMbvVr6" +
                        "Xl8uGOEz8eMvOrbOd/lnf2lSO9BQPAi/0m04bh6R/xaYKnUa3sDz3nJqJveJ1DRp0qK6GwZrdd/rf4S5Y+p7Y+N5+/J5JLJ+pLu/hrMfh2RznGpJN+RudxMSdVhXXE0sr+ta97ajUgEfDWc1XpFWZTupIPumsct8XuPP8AJ8Ew1njdzLr/AJUMRE28hERAREQEREBERAREQEREDouxVKnVrthaqoRiqb4dGYA93WYXo1FO4OcKunJjJcNgVoYHF1qqDvatQYOkGUEoUIqYioA2xACJcajOZzlKoysrKSGUgqRuCDcEe+dJ237RLjKlFqa5ESnmKAWHf1j3mIcc9XYi/wCUQMPszwP721YZqg7pBUtSomvUa9REsqBlOma5N9gZFwfhIrd+9Sp3VKguao+QuwzMEREQEZmZiBYkAC5J0mLhMcaaYimACK6KhPMBaqVLj3oBMngnF/u5qK1NatKsmSpSYsAwBDKwZTdWVgCCPHrAt4lgEprSq0qhqUqhZQxTIwdMpdGTMwBAZToSCD5zou0PAjhMHSOfN3jAG6FCSaa1LrcnMtmAzdRa00HF+LCstOnTpLRo0sxRASxzPlzu7tqzHKo5ABQAJvsZxNsfh7MoD07BSCbhVUWTxW4JHQs3WYzsklv5er4sytymPdlarsW1M4pKNULkxCvhyzKGNNqq5adVSfVZXyHNyF5vMZhlpYbGYqqimqbYBFZQwStmLV6guNGVEKhhsWnDaqeYIPkQRO57W8cTH0KTItjSQVKoAIDYipbv3t0Nh85ctSys+H7ZTLCe5v8Awwv7s9yuGrFqjColOqb0CtIB6ecKtYsQ7DS62Gx6THxHAqlc4auGBp4p1S41ajmrvRTOu4ByMQdjYi9xJKuPVsN3nd5amRKOcOxzrTpimPROi6C+nSYHD+0VWi+FqU8oOHTu7HVaid+9YrUU7jM/uyqRYgETHm2teX+Hixw/PN/0yeA4S9eqhfKuHVsxCZiyisqermGt3B35Sfj3B74rD0gwtiCgR7GxV2Ch7HUWJIIOxBHjMXs5j7YiozUw61wQ65mWwNRalwy66MqzM7QcXK4nDVQq3oFGVBfKopsCqXJJ5Ekkkm5POS6+8064fa/Ey31vj+/003F+GrQrLSLVQpsWapQNJwM5UstMucwsLg3F9RpaV4xw+lQemi1mcMlOozGkEyrUppUWwzm5yvqLjUW8Zj8TxNOo+enS7oEarnZ7tcktdtRuNPCWcQxZqurkAWp0qdh0pUUpg+8KD750eBkcawKUXVVdnzU6VUlkCWFWklVRo7XOVwD4g77yvGuDvhjSDkHvKYfT8Dah6TfnUixHl1llTiWetSrOisKa0FyH1WFClTpgN4MEF/MyfiXH62Ip5Kzd4RUNRXIAZSy2qAWAuGIQm/sjqYGniIgIiICIiAiIgIiICIiAiIgVmdwvG904O4OhHh4eMwRKSWbmq3hncMplj3HS8V4YKg76nqSLkdR1HjMbs+NatMjdfobH6zGo8Ramwym4st1Ox0Hw850GExFJ2zLYPYX5GxAO3PlOOW8cdV9bwzw+bzTPG6u+Z6u/w1nHjkpUafTf3AD6kzSUqZY5QCSeU7HGYSk9mqcuZbKAJjfeqFGwUC5tsLmx2Jb33kx8n8dSctfJ+Hvy/bPKTHid86i3DURhaZdtWNgfP2ROaxFYuzMdybzM4rjDUIvyLC1/HQzXTphje728Hy/NMtePDjGdfv8AakRE6PGREQEREBERAREQEREBERAREQEREBERAvLE8/D4TN4fW/xUZjoOZ5ALoPoJr5USWbjeGdxyl/F2zeJ4kvUY30BsB0F9JCxuy2N/VF/EKBbXpIWN7mWxJqaXPO5ZW33dpK27eZ+sjiJWLyREvpi5t1B+mkIsiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiIF6KDMqnhFPNh7gf1mIBJaUDYUuEoxA721+qfs022D7GuxDJURrHaxBmFg1uVE9F4BRyqPKZtWR5DjMOabvTbdGKn3GQTo+3mH7vG1fz5X+Kj9QZzk1EIiICIiAiIgIiICIiAiItARFotARK2kgwznZW+BgRAy646SZcFUOyOf4TK/can/jf+Rv2gQ5h0l4dfZPxlxwdQfgf+Vv2lpw7j8Lfyn9oEy4hB+E/ES9MWgN8p+UxxhX9kwcK3T5wNvhOM00IJRtOhE6bA9uaCWGR18bA/recB3DdPmJX7q/smTS7b7trxWliqqVaZJIXK11I2Nxv5mc3JTh3H4W+BlvdN7J+BlRZEv7s+yfgZYRAREQEREBERAkD22A+sGsfAeQEjiBdnPWUDGUlybwJaaXmdRoSLCpzm0o0DvsJm1qRLg8PrN5h8DeQcNwYcgBhc+6djg+BuACRcdQbiRWgThslXhw6n4zp/7GflYySnwRz0gcr/AGaPaMt/spuRM7unwCwF7SynhAzFKaF7btsg9/ONmnFpwZ5KvAnOw+Rno1HhhW1yo8lv8z+0n+5tyYeRUfpCvLqnZV23QfScrx7ghpBjYhlBb4eM91e4NmUA8iNjOf7U8JWrTqejqUb6SbSx8/8Aft7R+Mu+9P7TfGRSWslrHqL/ANfKbZ0u++P7R+Ur9+f2v9q/tMaJUZTY0ncKf4BIHqX5AeQtLIgIiICIiAiIgJfS3lkvpxVjPpVwvmf60mbh+IBdWpO3vI/SR8Lpi40ux59PAT0fgHC0dRmCnzAmK3JXN8L47g2IDpUQ9QQwHidj8Lz0Hs/jEYf9PWWqo3S9nA/0mzD4Wmp432Gp1BnRQG94/wBw2nnuJ4dWwdTMCwyG+YXDr+bTW3iPhJxTl9C0aYYA2tfrJ0p2nN/Z9xqpi8KHq2Lo5p5wLCoFCkNppfWxtpcHynUiBg4qmXbuwbL+I+HszQcZ7YYbBH7vTVq1bYUaYuQfznZfr4Sn2icVxGHw4XDIxeq+TOqlmQEG7Ko1LbAdJynY/sk6Xq4i4d9e7v6ZvqWquOvsg+fSWa9joqWPxeIszv3Sm4alSQd4p/NWclR5WUyNMHXT/KxNcG971KyPpb1SgRhb3zpMPwy4GbYbACwHgByE2SYVRyk3TTm+HcXq5lw+MpgZzlp10sabtyVrH0GPK9gdps6tG4amehF/dvNhWwiOrIy3DCxEhpUippqxzEC2bm1tAT4yUfKpWxIO4k1c3VfC4l+NX/Fqjo7f+xkLH0R4GdPbM6qKIiVkiIgIiICIiAiIgJLQW5kUyMLvJVnbYcPJzDz+fSd/wfi9DD2NaqBcbD0jpyFt/dOI4dgjULWbKQPj4Xlx4G5a5DA8wQWU6W0YTP8AbfPp61Q7fYBlIVmIFhqAmv8AGRNXx3G4bEKQGyvYlQwytoOR2I8jOBHZk2saxVL3sVJ99rgE6CRVsDkGRXcoNfSsFv7QXlJqeiW+3Xdmu2VXD5abkPTGwsAy68iNxPXOH4tayJUXZhefO1JbFRPZPs6xBbDlT+E6QrpceTlsu559BztMGn3dFTUqMFA5kzavOR492XGKcmoM63uBmZQLbD0SNpNcm0OJ+0jCK4po2ckhQV9JbnYZtF+cvwf2h4Z3yM4XXKGZCEJ6BwSo8zYTDpfZ1h8uVqZtmvq5bXTmbm2m15scF2EwaNn7kM175mJex6gNcD3Ca4Z5dTSxSOuYMLc9dvOQJUzuGX1QN+R8RI34RSYAWsRswOv/ADK0AUJRjewJB6jeZqvl2u93c9WJ+LXlp2I98o7XLN1JPxN5ew+YnRJ0giIlYIiICIiAiIgIiICT4XeQSbDnWS9LO3S8BT0iZ2uFxVhacXwJ9Z0tOqAJxyt29uOONx5bR0VvSsLzlONOM5tymxxfGVVSoOu05mtiMxJJ3mo8+Wt8LkOonr32apag56tPJKC857R2Cw+TDLf8RvKjpHEsAktpQCGUbtaY5rmZFdZglZK6YyVmU6l5h8YqZEqP7NN2+CEzIoJNN2wxGXCYxhuuHq+4sth9YTLh82CTt6qnzEhTeTH1fI/pOlYnTHMQYlYIiICIiAiIgIiICSUN5HLqZ1EDe8KxGRvS9U8+k32JxiZbhhObwVUhiB+JSLddJAlQgq4A03BFwbciDprOdxdplZNMirVuTaTYSkCwznTeXAKfSX1W1A6dVPlt8JeosQw0KkMD4g3GnOVlNTrKX6AT3ns2gGGpW5qD8RPn7G1wSXIAY+zoCetp0HBe32Kw9IIiKyKwTMxJVb3ty02PPlGjb3UsBubSxxpOJ7P8eWvb7zZw5NiyiyMBqugAy6X8N9Rt2wFgAIs0m0Hekbi4lA6NreMfiUpI1R2CKupJ+g6k8gNTOLocYq4qvUandKFH0Tbd3OysR0GpA2253k0srtXqACykX5Cch9oL5OHY07lgik9S1VAfrNzwZzapUY3AFh7v+Zx32sYvJgqdK/pV6oJ/0oCxPxyyTmmXTxpdxJORHvkabiSX1PlOlZx6RtylsuOwlsqUiIhCIiAiIgIiICIiBn4atlZH6H5c5k4hArkfgf0gfA/tNZSblNvhVFWmaf40uyeI5rM1qVD3bpqt7HmNQfMS77y/QfAyOjiWT3fKZCYq5vYX62t85K3JKjo4SrWYBEd2Olgp+uwE9WTsvSTA/c8ylz6bvca1DuQeg0A8BOR4ThRUsGBsddHcfRp1eC7NUGFzn91Rx/8AUz9mvpWL2fqPRRqbocyDKdCVdR6rKRseYPLUHkZXEYisABTeuq39UPURQLaKB+EeVh4TosN2IwjWJUnzdj+s3WA7N4SgcyUKSsPxZFzfzHWa+zFx086/utj8SafoFQDfvKtUsbHmFLNYjqBO2p8NTCYenhU1bYtaxZ21d/65ATpWYAXmpQZ3aofVTQfr+3vjK+iRVKWVEpjwJ/QTxT7T+NDEYs01N0w47sW2LXu5Hvsv8M9L7d9ovuWHZ1I72rdKY6G3pP5KLe+08CdiSSdSdSf1jGe2cqrT3+P0lVO0pS5+R+ktE0RSJVpSVkiIgIiICIiAiIgIiIATMwWIKMrA6iYcqDJVlb/iNNSFqrs+46HmJjYemCQPnIKGIJUrfQ/WT8P1a3SZaldxwHhzWBz26aDfofCUTthUpVDhzQOZCRpUUg2F7/5W1hMehxf7uliLn8M5xMe7vUrj1jnAP8OvyiTbVyr23sliauIpGtVTICxVAHzFgpsWNlUAXFhOiVQNZyfYbiJqYKlfRqZekw8Va4PvDCbwuzSdJ2nxdS4yrudJDXK0qZLEKqKXdjyUC9z8zMnC0LekdztPJftW7XZ2bAUG9FT/AI7DZmB0pA9ARr46cjLJtLdOK7W8dbG4h6uuQejTX2UB006nc+c0URNsL12b4fOWCXE6WlohQmIiEIiICIiAiIgIiICIiAiIgXK1plUKtiGHL5iYcuDEQsrqOI1M6U3G1rTA4GoNSgOWZs3kQAfkDIMLi/RKcjr5GbHs2gNWmOjVR/MqKB8WPxmY09k7L4QIlUKAL1GuPzDRj/XSdGlC2+wnPdm8WooCozAZ2eqxJAAzuzjXwUge6cp20+0xVDUMG2Zzo1b8C9cntN+bbpeTW6b02H2j9vRhlbCYZga7CzuP+0CNh+c/LztPDyZfUcsSxJJJJJJuSTqSTzMsm2CVHWUAgmFIiIQiIgIiICIiAiIgIiICIiAiIgIiIFQbTO4djnp3ZeVzfoTbX4qJgRAz8VxatUUU2c5FsAg0XTwG/vmBEQEREBeIiAiIgIi" +
                        "ICIiAiIgIiICIiAiIgIiICIiAiIg" +
                        "IiICIiAiIgIiICIiAiIgIiIH/2Q==")
                .trailer("https://www.youtube.com/embed/lSA7mAHolAw")
                .build());



        Movie movie = movieRepository.save(Movie.builder()
                .rating(4)
                .name("Spiderman")
                .description("spiderman saves")
                .category(Category.ACTION)
                .isActive(true)
                .duration(120)
                        .img("https://upload.wikimedia.org/wikipedia/en/f/f3/Sp" +
                                "ider-Man2002Poster.jpg")
                .trailer("https://www.youtube.com/embed/t06RUxPbp_c")
                .build());
        Movie movie1 = movieRepository.save(Movie.builder()
                .rating(3)
                .name("Aquaman")
                .description("aquaman saves")
                .category(Category.FICTION)
                .isActive(true)
                .duration(140)
                        .img("https://cdn.vox-cdn.com/thumbor/Oa3W3ECtpaStQ9" +
                                "UdU9hhUWgrTuU=/0x0:2040x1150/1200x800/filters:focal(857x412:1183x738)/cdn.vox-cdn.com/uploads/chorus_image/image/62939100/aquaman_hero_image_0.0.jpg")
                        .trailer("https://www.youtube.com/embed/WDkg3h8PCVU")
                .build());
        Movie movie2 = movieRepository.save(Movie.builder()
                .rating(4)
                .name("Inglorious bastards")
                .description("bastards saves")
                .category(Category.HORROR)
                .isActive(true)
                .duration(120)
                        .img("https://m.media-amazon.com/images/M/MV5BMTcxODA" +
                                "4NzIyNF5BMl5BanBnXkFtZTgwMDg1MDc3MTE@._V1_.jpg")
                        .trailer("https://www.youtube.com/embed/KnrRy6kSFF0")
                .build());
        Movie movie3 = movieRepository.save(Movie.builder()
                .rating(4)
                .name("Annabel")
                .description("Annabel revenge")
                .category(Category.HORROR)
                .isActive(true)
                .duration(85)
                        .img("https://m.media-amazon.com/images/M/MV5BOTQwZmQyYzEtODk5ZC00OTY3LWExM" +
                                "jAtYzRjNWFhNGM3MzBlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg")
                        .trailer("https://www.youtube.com/embed/paFgQNPGlsg")
                .build());
        Movie movie4 = movieRepository.save(Movie.builder()
                .rating(4)
                .name("Soprano")
                .description("mob movie")
                .category(Category.THRILLER)
                .isActive(true)
                .duration(94)
                        .img("https://flxt.tmsimg.com/assets/p7894127_b1t_v9_ab.jpg")
                        .trailer("https://www.youtube.com/embed/KMx4iFcozK0")
                .build());
        Movie movie5 = movieRepository.save(Movie.builder()
                .rating(5)
                .name("Ex Machina")
                .description("robot goes mad")
                .category(Category.FICTION)
                .isActive(true)
                .duration(103)
                        .img("https://i.guim.co.uk/img/static/sys-images/Guardian/Pix/pictures/2015/1" +
                                "/21/1421844142951/alicia-vikander-ex-machin-012.jpg?width" +
                                "=620&quality=85&auto=format&fit=max&s=9d59e8fede18a9d82582217375238f18")
                        .trailer("https://www.youtube.com/embed/EoQuVnKhxaM")
                .build());
        Movie movie6 = movieRepository.save(Movie.builder()
                .rating(2.5F)
                .name("Adam")
                .description("adam sandler movie saves")
                .category(Category.COMEDY)
                .isActive(true)
                .duration(98)
                        .img("https://upload.wikimedia.org/wikipedia/en/a/a9/Black_Adam_%28film%29_poster.jpg")
                        .trailer("https://www.youtube.com/embed/X0tOpBuYasI")
                .build());
        Theater theater = theaterRepository.save(Theater.builder()
                .rows(List.of(8, 7, 8, 9, 9,8,9,8))
                .name("A1")
                .build());
        Theater theater2 = theaterRepository.save(Theater.builder()
                .rows(List.of(9, 10, 9, 8,8,9,10,9))
                .name("B1")
                .build());
        Theater theater3 = theaterRepository.save(Theater.builder()
                .rows(List.of(9, 8, 9, 10,8,9,8,9))
                .name("C1")
                .build());
        Screening screening0 = adminService.addScreening(new ScreeningModelDao(movie.getId(), Instant.now().plusSeconds(60 * 60*28), theater.getId(), true,true));

        adminService.addScreening(new ScreeningModelDao(movie.getId(), Instant.now().plusSeconds(60 * 60*20), theater.getId(), true,true));
        adminService.addScreening(new ScreeningModelDao(movie.getId(), Instant.now().plusSeconds(60 * 60*40), theater.getId(), true,true));
        adminService.addScreening(new ScreeningModelDao(movie.getId(), Instant.now().plusSeconds(60 * 60*50), theater.getId(), true,true));
        adminService.addScreening(new ScreeningModelDao(movie.getId(), Instant.now().plusSeconds(60 * 60*90), theater.getId(), true,true));


        Screening screening = adminService.addScreening(new ScreeningModelDao(movie.getId(), Instant.now().plusSeconds(60 * 60), theater.getId(), true,true));
        Screening screening2 = adminService.addScreening(new ScreeningModelDao(movie2.getId(), Instant.now().plusSeconds(60 * 60 * 20), theater2.getId(), true,true));

        Screening screening3 = adminService.addScreening(new ScreeningModelDao(movie3.getId(), Instant.now().plusSeconds(60 * 60 * 5), theater.getId(), true,true));
        Screening screening4 = adminService.addScreening(new ScreeningModelDao(movie4.getId(), Instant.now().plusSeconds(60 * 60 * 24 * 6), theater3.getId(), false,true));

        Screening screening5 = adminService.addScreening(new ScreeningModelDao(movie5.getId(), Instant.now().plusSeconds(60 * 60 * 24 * 2), theater.getId(), true,true));

        Screening screening6 = adminService.addScreening(new ScreeningModelDao(movie6.getId(), Instant.now().plusSeconds(60 * 60 * 12), theater3.getId(), false,true));


        homeService.register(new CustomerModelDao("ido@gmail.com", "ido", "1234"));
        homeService.register(new CustomerModelDao("niv@gmail.com", "niv", "1234"));

        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 0, 1), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 2, 2), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 1, 2), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 3, 3), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 1, 3), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 0, 3), 1);


        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 2, 1), 2);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 3, 0), 2);

        adminService.addClerk(new ClerkModelDao("bob@gmail.co", "bob", "1234"));

        System.out.println(clerkRepository.existsByEmail("bob@gmail.co")
        );
    }

}
