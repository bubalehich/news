package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import integration.builder.CommentMutationModelTestBuilder;
import integration.controller.AbstractTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;


@AutoConfigureMockMvc
@WireMockTest(httpPort = 8082)
@RequiredArgsConstructor
public class CommentControllerTest extends AbstractTest {
    private static final String BASE_URL ="api/v1/comments";
    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    void save() throws Exception {
        String content = objectMapper.writeValueAsString(CommentMutationModelTestBuilder.aComment().build());
        String outcoming =
        mockMvc.perform(post(BASE_URL)
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.text").value(request.getText()))
                .andExpect(jsonPath("$.username").value(request.getUsername()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class SavePostEndpointTest {
        void testShouldReturnExpectedJsonAndStatus201ForSubscriberAndAdmin(Long expectedId,
                                                                           TokenValidationResponse response) throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String content = JsonFormat.printer().print(request);
            String json = JsonFormat.printer().print(response);

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedId))
                    .andExpect(jsonPath("$.text").value(request.getText()))
                    .andExpect(jsonPath("$.username").value(request.getUsername()))
                    .andExpect(jsonPath("$.email").value(response.getEmail()));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user is unauthorized")
        void testShouldReturnExpectedJsonAndStatus401IfUserIsUnauthorized() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String content = JsonFormat.printer().print(request);
            String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(401)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 403 for Journalist")
        void testShouldReturnExpectedJsonAndStatus403ForJournalist() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String content = JsonFormat.printer().print(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name()).build();
            String json = JsonFormat.printer().print(response);
            String expectedJson = CommonErrorJsonSupplier.getForbiddenForJournalistErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest()
                    .withNewsId(122L).build();
            String content = JsonFormat.printer().print(request);
            String expectedJson = NewsJsonSupplier.getNotFoundGetNewsResponse();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name()).build();
            String json = JsonFormat.printer().print(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if username is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfUserNameIsOutOfPattern() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest()
                    .withUsername("Ali - Muhammed")
                    .build();
            String content = JsonFormat.printer().print(request);
            String json = CommentJsonSupplier.getPatternCommentWithNewsErrorResponse();

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }
}
