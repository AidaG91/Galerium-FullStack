package galerium.util;

import galerium.enums.UserRole;
import galerium.model.Client;
import galerium.model.Gallery;
import galerium.model.Photo;
import galerium.model.Photographer;
import galerium.repository.ClientRepository;
import galerium.repository.GalleryRepository;
import galerium.repository.PhotoRepository;
import galerium.repository.PhotographerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    // Repositories implementations
    private final ClientRepository clientRepository;
    private final PhotographerRepository photographerRepository;
    private final GalleryRepository galleryRepository;
    private final PhotoRepository photoRepository;

    @Override
    public void run(String... args) {

        // ---------------------------------------
        // 1) Clients:
        // ---------------------------------------
        Client client1 = Client.builder()
                .email("marcos.lozano@rebelmail.com")
                .password("lightsaber456")
                .fullName("Marcos Lozano")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 123 456")
                .address("Sector 7G, Tatooine Outpost")
                .internalNotes("Prefers encrypted messages via holocom.")
                .build();

        Client client2 = Client.builder()
                .email("chen.gomez@magicguild.org")
                .password("arcane789")
                .fullName("Chen Gómez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 654 321")
                .address("Crystal Tower, Elven District")
                .profilePictureUrl("https://i.pravatar.cc/150?img=12")
                .build();

        Client client3 = Client.builder()
                .email("luis.fernandez@dragonmail.com")
                .password("firebreath101")
                .fullName("Luis Fernández")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 987 654")
                .address("Calle del Dragón 789, Ciudadela de Fuego")
                .internalNotes("Requested a magical portrait with animated flames.")
                .build();

        Client client4 = Client.builder()
                .email("marta.sanchez@rebelion.org")
                .password("hope2021")
                .fullName("Marta Sánchez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 611 234 567")
                .address("Base Echo, Hoth System")
                .profilePictureUrl("https://i.pravatar.cc/150?img=14")
                .build();

        Client client5 = Client.builder()
                .email("carlos.ruiz@wandersguild.com")
                .password("seastone303")
                .fullName("Carlos Ruiz")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 622 345 678")
                .address("Avenida del Mar 22, Puerto de los Susurros")
                .profilePictureUrl("https://i.pravatar.cc/150?img=16")
                .internalNotes("Prefers watercolor style and sea-themed compositions.")
                .build();

        Client client6 = Client.builder()
                .email("elena.navarro@jediacademy.net")
                .password("forceflow404")
                .fullName("Elena Navarro")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 633 456 789")
                .address("Paseo de Gracia 10, Coruscant Central")
                .build();

        Client client7 = Client.builder()
                .email("yoda@jediorder.com")
                .password("forceMaster123")
                .fullName("Master Yoda")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 111 111")
                .address("Dagobah Swamp, Outer Rim")
                .internalNotes("Speaks in reverse syntax. Prefers green tones.")
                .build();

        Client client8 = Client.builder()
                .email("aragorn@gondor.gov")
                .password("striderKing456")
                .fullName("Aragorn son of Arathorn")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 222 222")
                .address("Throne Room, Minas Tirith")
                .profilePictureUrl("https://i.pravatar.cc/150?img=31")
                .build();

        Client client9 = Client.builder()
                .email("leia@rebellion.org")
                .password("princessHope789")
                .fullName("Leia Organa")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 333 333")
                .address("Alderaan Embassy, Coruscant")
                .internalNotes("Requested a minimalist portrait with rebellion insignia.")
                .build();

        Client client10 = Client.builder()
                .email("gandalf@middleearth.net")
                .password("youShallNotPass101")
                .fullName("Gandalf the Grey")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 444 444")
                .address("Tower of Orthanc, Isengard")
                .profilePictureUrl("https://i.pravatar.cc/150?img=33")
                .build();

        Client client11 = Client.builder()
                .email("luke@skywalkeracademy.com")
                .password("lightsaber202")
                .fullName("Luke Skywalker")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 555 555")
                .address("Jedi Temple, Ahch-To")
                .build();

        Client client12 = Client.builder()
                .email("daenerys@dragonmail.com")
                .password("motherOfDragons303")
                .fullName("Daenerys Targaryen")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 666 666")
                .address("Dragonstone Castle, Westeros")
                .profilePictureUrl("https://i.pravatar.cc/150?img=35")
                .internalNotes("Requested a dramatic portrait with dragons in the background.")
                .build();

        clientRepository.saveAll(List.of(client1, client2, client3, client4, client5, client6, client7, client8, client9, client10, client11, client12));

        // ---------------------------------------
        // 2) Photographers:
        // ---------------------------------------
        Photographer photographer1 = Photographer.builder()
                .email("fotografa1@example.com")
                .password("securepass123")
                .fullName("Aïda Lens")
                .userRole(UserRole.PHOTOGRAPHER)
                .profilePictureUrl("https://example.com/aida.jpg")
                .build();

        Photographer photographer2 = Photographer.builder()
                .email("fotografo2@example.com")
                .password("securepass456")
                .fullName("Kenny Click")
                .userRole(UserRole.PHOTOGRAPHER)
                .profilePictureUrl("https://example.com/kenny.jpg")
                .build();

        photographerRepository.saveAll(List.of(photographer1, photographer2));

        // ---------------------------------------
        // 3) Galleries:
        // ---------------------------------------
        Gallery gallery1 = Gallery.builder()
                .title("Boda de Anakin y Padmé")
                .date(LocalDate.of(2025, 9, 1))
                .description("Una boda mágica")
                .photographer(photographer1)
                .client(client1)
                .build();

        Gallery gallery2 = Gallery.builder()
                .title("Cumpleaños de María")
                .date(LocalDate.of(2024, 12, 15))
                .description("Celebración sorpresa en Barcelona")
                .photographer(photographer2)
                .client(client2)
                .build();

        galleryRepository.saveAll(List.of(gallery1, gallery2));

        // ---------------------------------------
        // 4) Photos:
        // ---------------------------------------
        Photo photo1 = Photo.builder()
                .gallery(gallery1)
                .url("https://example.com/photos/boda1.jpg")
                .title("Primer beso")
                .description("Captura del momento más romántico")
                .clientComment("¡Nos encanta esta foto!")
                .favorite(true)
                .build();

        Photo photo2 = Photo.builder()
                .gallery(gallery1)
                .url("https://example.com/photos/boda2.jpg")
                .title("Baile nupcial")
                .description("La pareja bailando bajo las luces")
                .favorite(false)
                .build();

        Photo photo3 = Photo.builder()
                .gallery(gallery2)
                .url("https://example.com/photos/boda3.jpg")
                .title("Corte de pastel")
                .clientComment("¡Qué recuerdos tan bonitos!")
                .favorite(true)
                .build();

        photoRepository.saveAll(List.of(photo1, photo2, photo3));

        System.out.println("+++++++++++++++ Sample data inserted correctly into the database");
    }


}
