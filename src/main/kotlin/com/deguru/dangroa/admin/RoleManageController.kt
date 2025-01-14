package com.deguru.dangroa.admin

import com.deguru.dangroa.auth.RoleService
import com.deguru.dangroa.model.Role
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@Validated
@RestController
@RequestMapping("/admin/role")
class RoleManageController(
    val roleService: RoleService,
) {
    @GetMapping("/list")
    fun getRoles(): ResponseEntity<List<Role.RoleDTO>> {
        return ResponseEntity.ok(roleService.getRoleList());
    }


    @GetMapping("/user/{userIndex}")
    fun getUserRole(@PathVariable userIndex: Long): ResponseEntity<List<Role.RoleDTO>> {

        return ResponseEntity.ok(roleService.findUserRoles(userIndex))
    }


    @PutMapping("/user/{userIndex}")
    fun updateUserRole(@PathVariable userIndex: Long, @Valid @RequestBody roleDTO: Role.RoleListDTO): ResponseEntity<String> {
        //remove existing roles
        roleService.removeUserRoles(userIndex)
        if(roleDTO.newRoles.isNullOrEmpty()) {
            return ResponseEntity.created(URI("/admin/role/user/$userIndex")).build()
        }
        //add new roles
        roleService.addRoles(userIndex, roleDTO.newRoles)

        return ResponseEntity.created(URI("/admin/role/user/$userIndex")).build()

    }
}