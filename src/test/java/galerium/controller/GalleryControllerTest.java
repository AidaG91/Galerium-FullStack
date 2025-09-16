package galerium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import galerium.config.GlobalExceptionHandler;
import galerium.config.TestSecurityConfig;
import galerium.dto.gallery.GalleryRequestDTO;
import galerium.dto.gallery.GalleryResponseDTO;
import galerium.dto.gallery.GalleryUpdateDTO;
import galerium.dto.photo.PhotoResponseDTO;
import galerium.service.interfaces.GalleryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GalleryController.class)
@Import({TestSecurityConfig.class, GlobalExceptionHandler.class})
class GalleryControllerTest {

    private static final String BASE = "/api/galleries";
    private static final String ID = BASE + "/{id}";
    private static final String PHOTOGRAPHER_ID = BASE + "/by-photographer/{photographerId}";
    private static final String CLIENT_ID = BASE + "/by-client/{clientId}";
    private static final String TITLE = BASE + "/by-title/{title}";
    private static final String DATE_RANGE = BASE + "/by-date-range";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    GalleryService galleryService;

    // ------- TESTING POST /api/galleries -------
    @Test
    @DisplayName("POST /api/galleries -> 201 Created")
    void createGallery_created() throws Exception {
        var req = new GalleryRequestDTO();
        req.setTitle("Luke & Leia's Newborn Photos");
        req.setDate(LocalDate.of(2024, 6, 15));
        req.setDescription("A beautiful collection of newborn photos of Luke and Leia.");
        req.setPhotographerId(1L);
        req.setClientId(2L);

        var created = resp(1L, req.getTitle(), "2024-06-15", "A beautiful collection of newborn photos of Luke and Leia.", 1L, 2L, List.of());
        when(galleryService.createGallery(any())).thenReturn(created);

        var result = mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BASE + "/1"))
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("title").value("Luke & Leia's Newborn Photos"))
                .andExpect(jsonPath("date").value("2024-06-15"))
                .andExpect(jsonPath("description").value("A beautiful collection of newborn photos of Luke and Leia."))
                .andExpect(jsonPath("photographerId").value(1))
                .andExpect(jsonPath("clientId").value(2))
                .andExpect(jsonPath("photos").isEmpty())
                .andReturn();

        ArgumentCaptor<GalleryRequestDTO> captor = ArgumentCaptor.forClass(GalleryRequestDTO.class);

        verify(galleryService).createGallery(captor.capture());

        var sent = captor.getValue();
        assertThat(sent.getTitle()).isEqualTo("Luke & Leia's Newborn Photos");
        assertThat(sent.getDate()).isEqualTo(LocalDate.of(2024, 6, 15));
        assertThat(sent.getDescription()).isEqualTo("A beautiful collection of newborn photos of Luke and Leia.");
        assertThat(sent.getPhotographerId()).isEqualTo(1L);
        assertThat(sent.getClientId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("POST /api/galleries -> 400 Bad Request(missing fields")
    void createGallery_missingFields() throws Exception {
        var req = new GalleryRequestDTO();

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.photographerId").exists())
                .andExpect(jsonPath("$.clientId").exists())
        ;
    }

    // ------- TESTING PUT /api/galleries/{id} -------
    @Test
    @DisplayName("PUT /api/galleries/{id} -> 200 OK")
    void updateGallery() throws Exception {
        var req = new GalleryUpdateDTO();
        req.setTitle("Updated Title");
        req.setDate(LocalDate.of(2024, 7, 20));
        req.setDescription("Updated Description");

        var updated = resp(1L, req.getTitle(), "2024-07-20", req.getDescription(), 1L, 2L, List.of());
        when(galleryService.updateGallery(eq(1L), any())).thenReturn(updated);

        mvc.perform(put(BASE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("title").value("Updated Title"))
                .andExpect(jsonPath("date").value("2024-07-20"))
                .andExpect(jsonPath("description").value("Updated Description"))
                .andExpect(jsonPath("photographerId").value(1))
                .andExpect(jsonPath("clientId").value(2))
                .andExpect(jsonPath("photos").isEmpty());

        ArgumentCaptor<GalleryUpdateDTO> captor = ArgumentCaptor.forClass(GalleryUpdateDTO.class);
        verify(galleryService).updateGallery(eq(1L), captor.capture());
        var sent = captor.getValue();
        assertThat(sent.getTitle()).isEqualTo("Updated Title");
        assertThat(sent.getDate()).isEqualTo(LocalDate.of(2024, 7, 20));
        assertThat(sent.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    @DisplayName("PUT /api/galleries/{id} -> 400 BAD REQUEST (missing fields)")
    void updateGallery_missingFields() throws Exception {
        var req = new GalleryUpdateDTO();

        mvc.perform(put(ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ------- TESTING GET /api/galleries -------
    @Test
    @DisplayName("GET /api/galleries -> 200 OK")
    void getAllGalleries_ok() throws Exception {
        var g1 = resp(1L, "Gallery 1", "2024-06-01", "Description 1", 1L, 2L, List.of());
        var g2 = resp(2L, "Gallery 2", "2024-06-02", "Description 2", 1L, 3L, List.of());

        when(galleryService.getAllGalleries()).thenReturn(List.of(g1, g2));

        mvc.perform(get(BASE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Gallery 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Gallery 2"));

        verify(galleryService).getAllGalleries();
    }

    // ------- TESTING GET /api/galleries/{id} -------
    @Test
    @DisplayName("GET /api/galleries/{id} -> 200 OK")
    void getGalleryById_ok() throws Exception {
        var g1 = resp(1L, "Gallery 1", "2024-06-01", "Description 1", 1L, 2L, List.of());

        when(galleryService.getGalleryById(1L)).thenReturn(g1);

        mvc.perform(get(ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("title").value("Gallery 1"))
                .andExpect(jsonPath("date").value("2024-06-01"))
                .andExpect(jsonPath("description").value("Description 1"))
                .andExpect(jsonPath("photographerId").value(1))
                .andExpect(jsonPath("clientId").value(2))
                .andExpect(jsonPath("photos").isEmpty());

        verify(galleryService).getGalleryById(1L);
    }

    @Test
    @DisplayName("GET /api/galleries/{id} -> 404 Not Found")
    void getGalleryById_notFound() throws Exception {
        when(galleryService.getGalleryById(999L)).thenThrow(new EntityNotFoundException("Gallery not found"));

        mvc.perform(get("/api/galleries/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Gallery not found"));

        verify(galleryService).getGalleryById(999L);
    }

    // ------- TESTING GET /api/galleries/by-photographer/{photographerId} -------
    @Test
    @DisplayName("GET /api/galleries/by-photographer/{photographerId} -> 200 OK")
    void getGalleriesByPhotographerId_ok() throws Exception {
        var g1 = resp(1L, "Gallery 1", "2024-06-01", "Description 1", 1L, 2L, List.of());

        when(galleryService.getGalleriesByPhotographerId(1L)).thenReturn(List.of(g1));

        mvc.perform(get(PHOTOGRAPHER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Gallery 1"))
                .andExpect(jsonPath("$[0].photographerId").value(1));

        verify(galleryService).getGalleriesByPhotographerId(1L);
    }

    @Test
    @DisplayName("GET /api/galleries/by-photographer/{photographerId} -> 404 NOT FOUND")
    void getGalleriesByPhotographerId_notFound() throws Exception {
        when(galleryService.getGalleriesByPhotographerId(999L))
                .thenThrow(new EntityNotFoundException("No galleries found for photographer"));

        mvc.perform(get(PHOTOGRAPHER_ID, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No galleries found for photographer"));

        verify(galleryService).getGalleriesByPhotographerId(999L);
    }

    // ------- TESTING GET /api/galleries/by-client/{clientId} -------
    @Test
    @DisplayName("GET /api/galleries/by-client/{clientId} -> 200 OK")
    void getGalleriesByClientId_ok() throws Exception {
        var g1 = resp(1L, "Gallery 1", "2024-06-01", "Description 1", 1L, 2L, List.of());

        when(galleryService.getGalleriesByClientId(2L)).thenReturn(List.of(g1));

        mvc.perform(get(CLIENT_ID, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Gallery 1"))
                .andExpect(jsonPath("$[0].clientId").value(2));

        verify(galleryService).getGalleriesByClientId(2L);
    }

    @Test
    @DisplayName("GET /api/galleries/by-client/{clientId} -> 404 NOT FOUND")
    void getGalleriesByClientId_notFound() throws Exception {
        when(galleryService.getGalleriesByClientId(999L))
                .thenThrow(new EntityNotFoundException("No galleries found for this client"));

        mvc.perform(get(CLIENT_ID, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No galleries found for this client"));

        verify(galleryService).getGalleriesByClientId(999L);
    }

    // ------- TESTING GET /api/galleries//by-title/{title} -------
    @Test
    @DisplayName("GET /api/galleries/by-title/{title} -> 200 OK")
    void getGalleriesByTitle_ok() throws Exception {
        var g1 = resp(1L, "Gallery 1", "2024-06-01", "Description 1", 1L, 2L, List.of());

        when(galleryService.getGalleriesByTitle("Gallery 1")).thenReturn(List.of(g1));

        mvc.perform(get(TITLE, "Gallery 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Gallery 1"));

        verify(galleryService).getGalleriesByTitle("Gallery 1");
    }

    @Test
    @DisplayName("GET /api/galleries/by-title/{title} -> 404 NOT FOUND")
    void getGalleriesByTitle_notFound() throws Exception {
        when(galleryService.getGalleriesByTitle("NoSuchTitle"))
                .thenThrow(new EntityNotFoundException("No galleries found with this title"));

        mvc.perform(get(TITLE, "NoSuchTitle"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No galleries found with this title"));

        verify(galleryService).getGalleriesByTitle("NoSuchTitle");
    }

    // ------- TESTING GET /api/galleries//by-date-range -------
    @Test
    @DisplayName("GET /api/galleries/by-date-range -> 200 OK")
    void getGalleriesByDateRange_ok() throws Exception {
        var g1 = resp(1L, "Gallery 1", "2024-06-01", "Description 1", 1L, 2L, List.of());

        when(galleryService.getGalleriesByDateRange(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30)))
                .thenReturn(List.of(g1));

        mvc.perform(get(DATE_RANGE)
                        .param("startDate", "2024-06-01")
                        .param("endDate", "2024-06-30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].date").value("2024-06-01"));

        verify(galleryService).getGalleriesByDateRange(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
    }

    @Test
    @DisplayName("GET /api/galleries/by-date-range -> 404 NOT FOUND")
    void getGalleriesByDateRange_notFound() throws Exception {
        when(galleryService.getGalleriesByDateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)))
                .thenThrow(new EntityNotFoundException("No galleries found in this date range"));

        mvc.perform(get(DATE_RANGE)
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No galleries found in this date range"));

        verify(galleryService).getGalleriesByDateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
    }

    // ------- TESTING DELETE /api/galleries/{id} -------
    @Test
    @DisplayName("DELETE /api/galleries/{id} -> 204 No Content")
    void deleteGallery_noContent() throws Exception {
        doNothing().when(galleryService).deleteGallery(1L);

        mvc.perform(delete(ID, 1))
                .andExpect(status().isNoContent());

        verify(galleryService).deleteGallery(1L);
    }

    @Test
    @DisplayName("DELETE /api/galleries/{id} -> 404 Not Found")
    void deleteGallery_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Gallery not found")).when(galleryService).deleteGallery(999L);

        mvc.perform(delete(ID, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Gallery not found"));

        verify(galleryService).deleteGallery(999L);
    }


    // Helper method to create a GalleryResponseDTO object
    private static GalleryResponseDTO resp(Long id, String title, String date, String description,
                                           Long photographerId, Long clientId, List<PhotoResponseDTO> photos) {
        GalleryResponseDTO dto = new GalleryResponseDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDate(LocalDate.parse(date));
        dto.setDescription(description);
        dto.setPhotographerId(photographerId);
        dto.setClientId(clientId);
        dto.setPhotos(photos);
        return dto;
    }
}