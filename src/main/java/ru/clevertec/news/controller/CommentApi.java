package ru.clevertec.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import ru.clevertec.news.model.comment.CommentMutationModel;
import ru.clevertec.news.model.comment.CommentSearchCriteria;
import ru.clevertec.news.model.comment.CommentViewModel;

import java.util.UUID;

@RequestMapping("api/v1/news/{newsId}/comments")
@Validated
public interface CommentApi {
    @Operation(
            summary = "Create new comment",
            tags = {"Comments"},
            description = "Comment creation. Returns the location of a new resource in headers.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Comment successfully created"),
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
    ResponseEntity<CommentViewModel> createComment(@PathVariable UUID newsId, @RequestBody CommentMutationModel model);

    @Operation(
            summary = "Get comment",
            tags = {"Comments"})
    @GetMapping("/{id}")
    ResponseEntity<CommentViewModel> getComment(@PathVariable UUID newsId, @PathVariable UUID id);

    @Operation(
            summary = "Get comments",
            tags = {"Comments"},
            description = "Get comments. Returns a list comments.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of comments successfully returned"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Payload is incorrect: malformed, missing mandatory attributes etc",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "General application error",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))
            })
    @GetMapping
    ResponseEntity<Page<CommentViewModel>> getComments(
            @PathVariable UUID newsId, @RequestBody CommentSearchCriteria criteria, Pageable pageRequest);

    @Operation(
            summary = "Update comment",
            tags = {"Comments"})
    @PutMapping("/{commentId}")
    ResponseEntity<CommentViewModel> updateComment(
            @PathVariable UUID newsId, @PathVariable UUID commentId, @RequestBody CommentMutationModel model);

    @Operation(
            tags = {"Comments"},
            description = "Delete comment by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Comment deleted"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Unable to parse the request: the payload is not valid",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "General application error",
                            content = @Content(schema = @Schema(implementation = ExceptionInformation.class)))
            })
    @DeleteMapping("/{commentId}")
    ResponseEntity<CommentViewModel> deleteComment(
            @PathVariable UUID newsId,
            @NotNull(message = "Comment id can't be null") @PathVariable UUID commentId);
}
