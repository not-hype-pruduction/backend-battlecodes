package com.battlecodes.backend.controllers;

import com.battlecodes.backend.models.UserAlgorithm;
import com.battlecodes.backend.models.UserModel;
import com.battlecodes.backend.models.requests.AlgorithmIdsRequest;
import com.battlecodes.backend.models.requests.UploadAlgorithmRequest;
import com.battlecodes.backend.services.UserAlgorithmService;
import com.battlecodes.backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/algorithms")
@Tag(name = "UserAlgorithmController", description = "Контроллер для управления алгоритмами пользователей")
public class UserAlgorithmController {
    private final UserAlgorithmService userAlgorithmService;
    private final UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Operation(summary = "Загрузить алгоритм", description = "Загружает алгоритм для пользователя с ролью STUDENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Алгоритм успешно загружен"),
            @ApiResponse(responseCode = "400", description = "Ошибка загрузки алгоритма"),
            @ApiResponse(responseCode = "403", description = "Пользователь не состоит в группе")
    })
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> uploadAlgorithm(@RequestBody UploadAlgorithmRequest request, Principal principal) {
        UserModel user = userService.getUserByEmail(principal.getName());

        if (user.getGroup() == null) {
            return ResponseEntity.status(403).body("User is not part of any group");
        }

        if (userAlgorithmService.saveAlgorithm(user, request.getFile(), request.getLanguage())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Получить алгоритмы группы", description = "Получает все алгоритмы пользователей в указанной группе для пользователя с ролью TEACHER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список алгоритмов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAlgorithm.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/group/{groupId}/algorithms")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> getAlgorithmsByGroup(@PathVariable Long groupId, Principal principal) {
        List<Long> algorithmIds = userAlgorithmService.getAlgorithmsByGroupId(groupId);

        AlgorithmIdsRequest algorithmIdsRequest = new AlgorithmIdsRequest();
        algorithmIdsRequest.setAlgorithmIds(algorithmIds);

        ResponseEntity<?> response = restTemplate.postForEntity("http://other-microservice/api/process", algorithmIdsRequest, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
