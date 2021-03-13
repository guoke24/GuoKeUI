package com.guohao.guokeui.smallapp.model

open class ItemModel {

    var Id: Int? = null
    var Name: String? = null
    var Age: Int? = null
    var IsSelect: Boolean? = null


    constructor(Id: Int?, Name: String?, Age: Int?, IsSelect: Boolean?) {
        this.Id = Id
        this.Name = Name
        this.Age = Age
        this.IsSelect = IsSelect
    }

}