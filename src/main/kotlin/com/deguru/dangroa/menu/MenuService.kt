package com.deguru.dangroa.menu

import com.deguru.dangroa.model.MenuModel
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MenuService {


    fun getMenuList(): List<MenuModel.MenuDTO>{
        return MenuModel.Menus.selectAll()
            .map { MenuModel.MenuDTO(it) }
    }
}