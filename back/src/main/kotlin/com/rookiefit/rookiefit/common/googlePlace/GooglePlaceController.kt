package com.rookiefit.rookiefit.common.googlePlace

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/api/places")
class GooglePlaceController(private val restTemplate: RestTemplate) {
    @Value("\${google.places.api.key}")
    private lateinit var googleApiKey: String
    private val googlePlacesUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json"

    @GetMapping("/autocomplete")
    fun autocomplete(@RequestParam query: String?): ResponseEntity<List<String>> {
        val url = "$googlePlacesUrl?input=$query&key=$googleApiKey&components=country:KR&types=geocode"
        return try {
            val response = restTemplate.getForObject(url, GooglePlacesResponse::class.java)
            val predictions = response?.predictions?.map{it.description}?: emptyList()
            ResponseEntity.ok(predictions)}
        catch(ex: Exception){
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(emptyList())
        }
    }
}
