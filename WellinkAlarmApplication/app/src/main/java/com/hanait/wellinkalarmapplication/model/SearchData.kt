package com.hanait.wellinkalarmapplication.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import java.io.Serializable

@Xml(name = "response")
data class SearchData(
    @Element(name="header")
    val header: Header,
    @Element(name = "body")
    val body: Body
)

@Xml(name="header")
data class Header(
    @PropertyElement(name="resultCode")
    val resultCode: Int,
    @PropertyElement(name="resultMsg")
    val resultMsg: String
)

@Xml(name = "body")
data class Body(
    @Element(name="items")
    val items: Items,
    @PropertyElement(name="numOfRows")
    val numOfRows: Int,
    @PropertyElement(name="pageNo")
    val pageNo: Int,
    @PropertyElement(name="totalCount")
    val totalCount: Int
)

@Xml(name= "items")
data class Items(
    @Element(name="item")
    val item: List<Item>
)

@Xml
data class Item(
    @PropertyElement(name = "entpName")
    var entpName: String?,
    @PropertyElement(name = "itemName")
    var itemName: String?,
    @PropertyElement(name="itemSeq")
    var itemSeq: Int?,
    @PropertyElement(name="efcyQesitm")
    var efcyQesitm: String?,
    @PropertyElement(name="useMethodQesitm")
    var useMethodQesitm: String?,
    @PropertyElement(name="atpnWarnQesitm")
    var atpnWarnQesitm: String?,
    @PropertyElement(name="atpnQesitm")
    var atpnQesitm: String?,
    @PropertyElement(name="intrcQesitm")
    var intrcQesitm: String?,
    @PropertyElement(name="seQesitm")
    var seQesitm: String?,
    @PropertyElement(name="depositMethodQesitm")
    var depositMethodQesitm: String?,
    @PropertyElement(name="openDe")
    var openDe: String?,
    @PropertyElement(name="updateDe")
    var updateDe: String?,
    @PropertyElement(name="itemImage")
    var itemImage: String?,
): Serializable {
    constructor() : this(null,null,null,null,null,null,null,null, null, null, null, null, null)
}