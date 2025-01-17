package com.deguru.dangroa.model

import com.deguru.dangroa.global.BaseEntity
import com.deguru.dangroa.global.BaseEntityClass
import com.deguru.dangroa.global.BaseLongIdTable
import com.deguru.dangroa.model.UserModel.Users
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow


class MenuModel {
    object Menus: BaseLongIdTable("menu", "menu_index") {
        val name = varchar("menu_name", 255)
        val description = varchar("menu_description", 255)
        val icon = varchar("menu_icon", 25500)
        val isDeleted = bool("is_deleted").default(false)
        val upperMenuId = long("upper_menu_id").default(0)
    }

    class Menu(id: EntityID<Long>): BaseEntity(id, Menus){
        companion object : BaseEntityClass<Menu>(Menus)
        var name by Menus.name
        var description by Menus.description
        var icon by Menus.icon
        var isDeleted by Menus.isDeleted
        var upperMenuId by Menus.upperMenuId
    }

    data class MenuDTO(
        var menuIndex :Long,
        var name :String,
        var description: String,
        var icon: String,
        var isDeleted: Boolean,
        var upperMenuId: Long
    ){
        constructor(menuEntity: Menu): this(
            menuEntity.id.value,
            menuEntity.name,
            menuEntity.description,
            menuEntity.icon,
            menuEntity.isDeleted,
            menuEntity.upperMenuId,
        )

        constructor(menuRow: ResultRow): this(
            menuRow[Menus.id].value,
            menuRow[Menus.name],
            menuRow[Menus.description],
            menuRow[Menus.icon],
            menuRow[Menus.isDeleted],
            menuRow[Menus.upperMenuId],
        )

    }



    data class MenuCreateDTO(
        @field:NotNull(message = "이름은 필수 값 입니다.")
        @field:NotBlank(message = "이름은 필수 값 입니다.")
        val name: String,

        @field:NotNull(message = "설명은 필수 값 입니다.")
        @field:NotBlank(message = "설명은 필수 값 입니다.")
        val description: String,

        val menuIcon: String?,
        val isDeleted: Boolean,
        val upperMenuId: Long?,
    )

}