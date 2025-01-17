package com.deguru.dangroa.admin

import com.deguru.dangroa.menu.MenuService
import com.deguru.dangroa.model.MenuModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin/menu")
class MenuManageController(
    val menuService: MenuService
) {

    @GetMapping()
    fun list(): List<MenuModel.MenuDTO>{
        return menuService.getMenuList()
    }
}