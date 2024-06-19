package com.example.greenhouse

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="response")
data class XmlResponse(
    @Element
    val body : myXmlBody
)

@Xml(name="body")
data class myXmlBody(
    @Element
    val items : myXmlItems
)

@Xml(name="items")
data class myXmlItems(
    @Element
    val item : MutableList<myXmlItem>
)

@Xml(name="item")
data class myXmlItem(
    @PropertyElement
    val familyKorNm:String?,
    @PropertyElement
    val genusKorNm:String?,
    @PropertyElement
    val plantGnrlNm:String?,
    @PropertyElement
    val imgUrl:String?,
    @PropertyElement
    val plantPilbkNo:Int?,
    @PropertyElement
    val detailYn:String?
) {
    constructor() : this(null, null, null, null, null, null)
}