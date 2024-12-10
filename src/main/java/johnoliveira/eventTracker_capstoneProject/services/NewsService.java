package johnoliveira.eventTracker_capstoneProject.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import johnoliveira.eventTracker_capstoneProject.dto.EventDTO;
import johnoliveira.eventTracker_capstoneProject.dto.NewsCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.NewsDTO;
import johnoliveira.eventTracker_capstoneProject.entities.Event;
import johnoliveira.eventTracker_capstoneProject.entities.News;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Value("${newsapi.base_url}")
    private String newsApiBaseUrl;

    @Value("${newsapi.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;


    // recupera una notizia tramite il suo ID
    public NewsDTO getNewsById(UUID newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new NotFoundException("News not found with ID: " + newsId));
        return toNewsDTO(news);
    }


    // recupera tutte le notizie con paginazione
    public Page<NewsDTO> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable).map(this::toNewsDTO);
    }


    // cerca notizie tramite parola chiave con paginazione
    public Page<NewsDTO> searchNewsByKeyword(String keyword, Pageable pageable) {
        return newsRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, pageable).map(this::toNewsDTO);
    }

    // mapping del DTO
    private NewsDTO toNewsDTO(News news) {
        return new NewsDTO(
                news.getNewsId(),
                news.getTitle(),
                news.getDescription(),
                news.getImageUrl(),
                news.getPublishedDate(),
                news.getSource(),
                news.getUrl()
        );
    }

    // metodo per creare news di test
    public NewsDTO createNews(NewsCreateDTO dto) {
        News news = new News();
        news.setTitle(dto.title());
        news.setDescription(dto.description());
        news.setImageUrl(dto.imageUrl());
        news.setPublishedDate(dto.publishedDate());
        news.setSource(dto.source());
        news.setUrl(dto.url());
        return toNewsDTO(newsRepository.save(news));
    }

    // Metodo per recuperare notizie da NewsAPI
    public List<NewsDTO> fetchNewsFromAPI(String query, String country, int pageSize) {
        String url = newsApiBaseUrl + "/top-headlines?apiKey=" + apiKey +
                "&q=" + (query != null ? query : "") +
                "&country=" + (country != null ? country : "it") +
                "&pageSize=" + pageSize;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return parseResponseToNews(response.getBody());
        } catch (Exception e) {
            System.err.println("Errore durante il recupero delle notizie: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<NewsDTO> parseResponseToNews(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("Risposta JSON completa: " + responseBody);

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode articlesNode = rootNode.path("articles");

            if (!articlesNode.isArray()) {
                System.out.println("Nessun array di articoli trovato.");
                return Collections.emptyList();
            }

            List<NewsDTO> newsList = new ArrayList<>();
            for (JsonNode articleNode : articlesNode) {
                NewsDTO news = new NewsDTO(
                        UUID.randomUUID(),
                        articleNode.path("title").asText("Titolo non disponibile"),
                        articleNode.path("description").asText("Descrizione non disponibile"),
                        articleNode.path("urlToImage").asText("https://via.placeholder.com/400x200"),
                        LocalDate.now(),
                        articleNode.path("source").path("name").asText("Fonte non disponibile"),
                        articleNode.path("url").asText("URL non disponibile")
                );
                newsList.add(news);
            }

            return newsList;

        } catch (Exception e) {
            System.err.println("Errore durante l'analisi della risposta: " + e.getMessage());
            return Collections.emptyList();
        }
    }


}


