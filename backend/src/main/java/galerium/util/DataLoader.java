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
                .email("cliente1@example.com")
                .password("clientpass456")
                .fullName("Marcos Lozano")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 123 456")
                .address("Calle Falsa 123, Madrid")
                .build();

        Client client2 = Client.builder()
                .email("cliente2@example.com")
                .password("clientpass789")
                .fullName("Chen Gómez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 654 321")
                .address("Avenida Siempre Viva 456, Barcelona")
                .build();

        Client client3 = Client.builder()
                .email("cliente3@example.com")
                .password("clientpass101")
                .fullName("Luis Fernández")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 987 654")
                .address("Plaza Mayor 789, Valencia")
                .build();

        Client client4 = Client.builder()
                .email("cliente4@example.com")
                .password("clientpass202")
                .fullName("Marta Sánchez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 611 234 567")
                .address("Calle Luna 45, Sevilla")
                .profilePictureUrl("https://i.pravatar.cc/150?img=15")
                .build();

        Client client5 = Client.builder()
                .email("cliente5@example.com")
                .password("clientpass303")
                .fullName("Carlos Ruiz")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 622 345 678")
                .address("Avenida del Mar 22, Málaga")
                .profilePictureUrl("https://i.pravatar.cc/150?img=18")
                .build();

        Client client6 = Client.builder()
                .email("cliente6@example.com")
                .password("clientpass404")
                .fullName("Elena Navarro")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 633 456 789")
                .address("Paseo de Gracia 10, Barcelona")
                .profilePictureUrl("https://i.pravatar.cc/150?img=21")
                .build();

        clientRepository.saveAll(List.of(client1, client2, client3, client4, client5, client6));

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
