package com.deguru.dangroa.admin

import com.deguru.dangroa.model.CommonRequest
import com.deguru.dangroa.model.CommonResponse
import com.deguru.dangroa.model.User
import com.deguru.dangroa.user.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@Tag(name = "User Management")
@RestController
@RequestMapping("/admin/user")
class UserManageController(
    val userService: UserService
) {
    val log = logger()
    @GetMapping()
    fun searchUserList(
        paging: CommonRequest.Paging,
        @Valid searchParam: User.UserSearchParam): ResponseEntity<CommonResponse.PaginationResponse<List<User.UserDTO>>> {
        val (totalCount, userData) = userService.searchUsers(paging, searchParam)
        return ResponseEntity.ok(CommonResponse.PaginationResponse(paging, totalCount, userData))
    }


    @Operation(summary = "Create User")
    @PostMapping
    fun insertUser(@Valid @RequestBody userData: User.SignUpUserDTO): ResponseEntity<User.UserDTO> {
        val userIndex = userService.insertUser(userData)

        return ResponseEntity.created(URI.create("/admin/user/${userIndex}")).build()
    }


    @GetMapping("/{userIndex}")
    fun getUser(@PathVariable userIndex: Long): ResponseEntity<User.UserDTO> {


        return ResponseEntity.ok(userService.findUserByUserIndex(userIndex))
    }

    @PutMapping("/{userIndex}")
    fun updateUser(@PathVariable userIndex: Long, @Valid @RequestBody updateDTO: User.UserUpdateDTO): ResponseEntity<String> {
        userService.updateUser(updateDTO)
        return ResponseEntity.noContent().build()
    }

}