package com.deguru.dangroa.admin

import com.deguru.dangroa.auth.RoleService
import com.deguru.dangroa.model.CommonRequest
import com.deguru.dangroa.model.CommonResponse
import com.deguru.dangroa.model.RoleModel
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/admin/role")
class RoleManageController(
    val roleService: RoleService,
) {

    @GetMapping("")
    fun searchRole(@RequestParam searchParam: RoleModel.RoleSearchParam, @RequestParam paging: CommonRequest.Paging): ResponseEntity<CommonResponse.PaginationResponse<List<RoleModel.RoleDTO>>> {
        val (totalCount, roleData) = roleService.pagingRoleList(searchParam, paging)

        return ResponseEntity.ok(CommonResponse.PaginationResponse(paging, totalCount, roleData))
    }
    @GetMapping("/list")
    fun getRoles(): ResponseEntity<List<RoleModel.RoleDTO>> {
        return ResponseEntity.ok(roleService.getRoleList());
    }

    @PostMapping("")
    fun insertRole(@Valid @RequestBody newRoleModelDTO: RoleModel.NewRoleDTO): ResponseEntity<String>  {

        return ResponseEntity.created(URI("/admin/role/user/${roleService.addRole(newRoleModelDTO)}")).build()
    }

    @DeleteMapping("/{roleIndex}")
    fun deleteRole(@PathVariable roleIndex: Long): ResponseEntity<String> {
        roleService.deleteRole(roleIndex)
        return ResponseEntity.noContent().build()
    }


    @GetMapping("/user/{userIndex}")
    fun getUserRole(@PathVariable userIndex: Long): ResponseEntity<List<RoleModel.RoleDTO>> {

        return ResponseEntity.ok(roleService.findUserRoles(userIndex))
    }


    @PutMapping("/user/{userIndex}")
    fun updateUserRole(@PathVariable userIndex: Long, @Valid @RequestBody roleModelDTO: RoleModel.RoleListDTO): ResponseEntity<String> {
        //remove existing roles
        roleService.removeUserRoles(userIndex)
        if(roleModelDTO.newRoles.isNullOrEmpty()) {
            return ResponseEntity.created(URI("/admin/role/user/$userIndex")).build()
        }
        //add new roles
        roleService.addUserRoles(userIndex, roleModelDTO.newRoles)

        return ResponseEntity.created(URI("/admin/role/user/$userIndex")).build()

    }
}