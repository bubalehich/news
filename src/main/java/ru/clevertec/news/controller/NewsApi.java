package ru.clevertec.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.clevertec.news.exception.ExceptionInformation;
import ru.clevertec.news.model.news.NewsMutationModel;
import ru.clevertec.news.model.news.NewsSearchCriteria;
import ru.clevertec.news.model.news.NewsViewModel;

import java.util.UUID;

@RequestMapping("api/v1/news")
@Validated
@Tag(name = "News API", description = "News api interface")
public interface NewsApi {
    @Operation(
            summary = "Create new news",
            tags = {"News"},
            description = "News creation. Returns the location of a new resource in headers.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "News successfully created"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Payload is incorrect: malformed, missing mandatory attributes etc",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "General application error",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<NewsViewModel> createNews(@RequestBody NewsMutationModel model);

    @Operation(
            summary = "Get news",
            tags = {"News"})
    @GetMapping("/{id}")
    ResponseEntity<NewsViewModel> getNews(@PathVariable UUID id);

    @Operation(
            summary = "Get news",
            tags = {"News"},
            description = "get news. Returns a list news.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of news successfully returned"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Payload is incorrect: malformed, missing mandatory attributes etc",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "General application error",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))
            })
    @PostMapping("/search")
    ResponseEntity<Page<NewsViewModel>> getNews(
            @RequestBody NewsSearchCriteria criteria, Pageable pageRequest);

    @Operation(
            summary = "Update news",
            tags = {"News"})
    @PutMapping("/{id}")
    ResponseEntity<NewsViewModel> updateNews(
            @PathVariable UUID id, @RequestBody NewsMutationModel model);

    @Operation(
            tags = {"News"},
            description = "Unarchives news by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "News is unarchived"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Unable to parse the request: the payload is not valid",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "General application error",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))
            })
    @DeleteMapping("/{id}/archived")
    ResponseEntity<NewsViewModel> unarchive(
            @NotNull(message = "Patient id can't be null") @PathVariable UUID id);

    @Operation(
            tags = {"News"},
            description = "Archives news by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "News is archived"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Unable to parse the request: the payload is not valid",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "General application error",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))
            })
    @PostMapping("/{id}/archived")
    ResponseEntity<NewsViewModel> archive(
            @NotNull(message = "News id can't be null") @PathVariable UUID id);
}
