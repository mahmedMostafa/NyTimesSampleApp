package com.assessment.nytimessampleapp.models

import com.squareup.moshi.Json
import java.io.Serializable


data class NewsResponse(
    @Json(name = "num_results")
    val numResults: Int? = null,
    @Json(name = "results")
    val newsModels: List<NewsModel>? = null,
    val status: String? = null
)

data class NewsModel(
    @Json(name = "abstract")
    val `abstract`: String? = null,
    @Json(name = "adx_keywords")
    val adxKeywords: String? = null,
    @Json(name = "asset_id")
    val assetId: Long? = null,
    val byline: String? = null,
    val column: Any? = null,
    @Json(name = "des_facet")
    val desFacet: List<String>? = null,
    @Json(name = "eta_id")
    val etaId: Int? = null,
    @Json(name = "geo_facet")
    val geoFacet: List<String>? = null,
    val id: Long? = null,
    @Json(name = "nytdsection")
    val nytdsection: String? = null,
    @Json(name = "org_facet")
    val orgFacet: List<String>? = null,
    @Json(name = "per_facet")
    val perFacet: List<String>? = null,
    @Json(name = "published_date")
    val publishedDate: String? = null,
    val section: String? = null,
    val source: String? = null,
    val subsection: String? = null,
    val title: String? = null,
    val uri: String? = null,
    val url: String? = null
) : Serializable