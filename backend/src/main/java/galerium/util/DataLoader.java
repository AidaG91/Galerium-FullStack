package galerium.util;

import galerium.enums.UserRole;
import galerium.model.Client;
import galerium.model.Gallery;
import galerium.model.Photo;
import galerium.model.Photographer;
import galerium.model.Tag;
import galerium.repository.ClientRepository;
import galerium.repository.GalleryRepository;
import galerium.repository.PhotoRepository;
import galerium.repository.PhotographerRepository;
import galerium.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    // Repositories implementations
    private final ClientRepository clientRepository;
    private final PhotographerRepository photographerRepository;
    private final GalleryRepository galleryRepository;
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {

        // ---------------------------------------
        // 1) Clients:
        // ---------------------------------------

        Tag tagPendientePago = new Tag("Pendiente de pago");
        Tag tagGaleriaEnviada = new Tag("Galería enviada");
        Tag tagPresupuestoEnviado = new Tag("Presupuesto enviado");
        Tag tagContratoFirmado = new Tag("Contrato firmado");
        Tag tagNuevoCliente = new Tag("Nuevo cliente");
        Tag tagClienteHabitual = new Tag("Cliente habitual");
        Tag tagVIP = new Tag("VIP");
        Tag tagBodas = new Tag("Bodas");
        Tag tagCorporativo = new Tag("Corporativo");


        Client client1 = Client.builder()
                .email("marcos.lozano@example.com")
                .password("password123")
                .fullName("Marcos Lozano")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 123 456")
                .address("Calle Ficticia 123, Madrid")
                .internalNotes("Primer contacto. Interesado en pack de bodas premium.")
                .tags(Set.of(tagNuevoCliente, tagPresupuestoEnviado, tagBodas))
                .build();

        Client client2 = Client.builder()
                .email("chen.gomez@example.com")
                .password("password123")
                .fullName("Chen Gómez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 654 321")
                .address("Avenida de Prueba 45, Barcelona")
                .profilePictureUrl("https://api.dicebear.com/9.x/notionists/svg?seed=ChenGomez")
                .internalNotes("Cliente recurrente para eventos corporativos. Paga siempre a tiempo.")
                .tags(Set.of(tagClienteHabitual, tagGaleriaEnviada, tagCorporativo, tagVIP))
                .build();

        Client client3 = Client.builder()
                .email("luis.fernandez@example.com")
                .password("password123")
                .fullName("Luis Fernández")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 600 987 654")
                .address("Plaza Mayor 1, Sevilla")
                .internalNotes("Retraso en el último pago. Hacer seguimiento.")
                .tags(Set.of(tagContratoFirmado, tagPendientePago, tagBodas))
                .build();

        Client client4 = Client.builder()
                .email("marta.sanchez@example.com")
                .password("password123")
                .fullName("Marta Sánchez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 611 234 567")
                .address("Camino de Ronda 78, Granada")
                .profilePictureUrl("https://api.dicebear.com/9.x/notionists/svg?seed=MartaSanchez")
                .internalNotes("Ha referido a dos nuevos clientes. Cliente VIP.")
                .tags(Set.of(tagVIP, tagClienteHabitual, tagGaleriaEnviada))
                .build();

        Client client5 = Client.builder()
                .email("carlos.ruiz@example.com")
                .password("password123")
                .fullName("Carlos Ruiz")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 622 345 678")
                .address("Paseo del Prado 10, Madrid")
                .profilePictureUrl("https://api.dicebear.com/9.x/notionists/svg?seed=CarlosRuiz")
                .internalNotes("Sesión de fotos de producto para su e-commerce.")
                .tags(Set.of(tagNuevoCliente, tagContratoFirmado, tagCorporativo))
                .build();

        Client client6 = Client.builder()
                .email("elena.navarro@example.com")
                .password("password123")
                .fullName("Elena Navarro")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 633 456 789")
                .address("Calle Larios 5, Málaga")
                .tags(Set.of(tagPresupuestoEnviado))
                .build();

        Client client7 = Client.builder()
                .email("javier.gomez@example.com")
                .password("password123")
                .fullName("Javier Gómez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 644 111 222")
                .address("Gran Vía 22, Bilbao")
                .internalNotes("Solicitó presupuesto para fotografía de eventos.")
                .tags(Set.of(tagNuevoCliente, tagPresupuestoEnviado, tagCorporativo))
                .build();

        Client client8 = Client.builder()
                .email("laura.jimenez@example.com")
                .password("password123")
                .fullName("Laura Jiménez")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 655 333 444")
                .address("Calle Colón 30, Valencia")
                .profilePictureUrl("https://api.dicebear.com/9.x/notionists/svg?seed=LauraJimenez")
                .internalNotes("Boda en 2025. Contrato firmado.")
                .tags(Set.of(tagContratoFirmado, tagBodas))
                .build();

        Client client9 = Client.builder()
                .email("david.moreno@example.com")
                .password("password123")
                .fullName("David Moreno")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 666 555 666")
                .address("Paseo de la Independencia 15, Zaragoza")
                .tags(Set.of(tagClienteHabitual, tagGaleriaEnviada))
                .build();

        Client client10 = Client.builder()
                .email("sara.alonso@example.com")
                .password("password123")
                .fullName("Sara Alonso")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 677 777 888")
                .address("Calle Santiago 1, Valladolid")
                .profilePictureUrl("https://api.dicebear.com/9.x/notionists/svg?seed=SaraAlonso")
                .internalNotes("Pendiente de pago de la última sesión familiar.")
                .tags(Set.of(tagPendientePago, tagClienteHabitual))
                .build();

        Client client11 = Client.builder()
                .email("pablo.iglesias@example.com")
                .password("password123")
                .fullName("Pablo Iglesias")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 688 999 000")
                .address("Rúa do Vilar 5, Santiago de Compostela")
                .tags(Set.of(tagNuevoCliente, tagPresupuestoEnviado))
                .build();

        Client client12 = Client.builder()
                .email("ana.ferrer@example.com")
                .password("password123")
                .fullName("Ana Ferrer")
                .userRole(UserRole.CLIENT)
                .phoneNumber("+34 699 123 123")
                .address("Plaza del Castillo 1, Pamplona")
                .internalNotes("Cliente importante, director de una gran empresa.")
                .tags(Set.of(tagVIP, tagCorporativo, tagContratoFirmado))
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
