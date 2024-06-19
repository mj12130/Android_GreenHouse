package com.example.greenhouse

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="response")
data class XmlResponse2(
    @Element
    val body : myXmlBody2
)

@Xml(name="body")
data class myXmlBody2(
    @Element
    val item : myXmlItem2
)

@Xml(name="item")
data class myXmlItem2(
    @PropertyElement
    val imgUrl:String?,
    @PropertyElement
    val familyKorNm:String?,
    @PropertyElement
    val genusKorNm:String?,
    @PropertyElement
    val plantGnrlNm:String?,
    @PropertyElement
    val shpe:String?,
    @PropertyElement
    val spft:String?,
    @PropertyElement
    val grwEvrntDesc:String?,
    @PropertyElement
    val useMthdDesc:String?,
    @PropertyElement
    val dstrb:String?,
    @PropertyElement
    val farmSpftDesc:String?,
    @PropertyElement
    val brdMthdDesc:String?,
    @PropertyElement
    val bugInfo:String?,
    @PropertyElement
    val bfofMthod:String?
) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null, null, null)
}
