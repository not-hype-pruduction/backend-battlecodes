package com.battlecodes.backend.controllers;

import com.battlecodes.backend.models.GroupModel;
import com.battlecodes.backend.models.requests.AddUserToGroupRequest;
import com.battlecodes.backend.models.requests.CreateGroupRequest;
import com.battlecodes.backend.models.requests.EditGroupRequest;
import com.battlecodes.backend.models.requests.RemoveUserFromGroupRequest;
import com.battlecodes.backend.services.GroupService;
import com.battlecodes.backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
@Tag(name = "GroupController", description = "Контроллер для управления группами")
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    @Operation(summary = "Получить все группы пользователя", description = "Получает все группы, созданные пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список групп",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupModel.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GroupModel>> getAllGroups(Principal principal) {
        return ResponseEntity.ok(groupService.getUserGroups(userService.getUserByEmail(principal.getName()).getId()));
    }

    @Operation(summary = "Создать новую группу", description = "Создает новую группу с указанными параметрами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Группа успешно создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupModel.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка создания группы")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createGroup(
            @Valid @RequestBody CreateGroupRequest createGroupRequest,
            Principal principal) {

        if (groupService.createGroup(createGroupRequest.getGroupName(), createGroupRequest.getPassword(), createGroupRequest.getGame(), userService.getUserByEmail(principal.getName()).getId())) {
            return ResponseEntity.ok(groupService.getGroupByName(createGroupRequest.getGroupName()));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Добавить пользователя в группу", description = "Добавляет пользователя в указанную группу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно добавлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupModel.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка добавления пользователя в группу")
    })
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUserToGroup(
            @Valid @RequestBody AddUserToGroupRequest addUserToGroupRequest,
            Principal principal) {

        if (groupService.addUserToGroup(addUserToGroupRequest.getName(), addUserToGroupRequest.getPassword(), userService.getUserByEmail(principal.getName()).getId())) {
            return ResponseEntity.ok(groupService.getGroupByName(addUserToGroupRequest.getName()));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Удалить пользователя из группы", description = "Удаляет пользователя из указанной группы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupModel.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка удаления пользователя из группы")
    })
    @PostMapping("/remove")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeUserFromGroup(
            @Valid @RequestBody RemoveUserFromGroupRequest removeUserFromGroupRequest) {

        if (groupService.removeUserFromGroup(removeUserFromGroupRequest.getGroupName(), removeUserFromGroupRequest.getUserId())) {
            return ResponseEntity.ok(groupService.getGroupByName(removeUserFromGroupRequest.getGroupName()));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Удалить группу", description = "Удаляет указанную группу, если пользователь является её создателем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Группа успешно удалена"),
            @ApiResponse(responseCode = "400", description = "Ошибка удаления группы"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping("/remove/{groupName}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeGroup(@PathVariable String groupName, Principal principal) {
        if (groupService.removeGroup(groupName, userService.getUserByEmail(principal.getName()).getId())) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Изменить данные группы", description = "Изменяет данные указанной группы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные группы изменены успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupModel.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка изменения данных группы"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PutMapping("/edit/{groupId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody EditGroupRequest editGroupRequest) {

        if (groupService.editGroup(groupId, editGroupRequest.getName(), editGroupRequest.getGame(), editGroupRequest.getPassword())) {
            return ResponseEntity.ok(groupService.getGroupById(groupId));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Получить все группы", description = "Получает все группы в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список групп",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupModel.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GroupModel>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }
}
