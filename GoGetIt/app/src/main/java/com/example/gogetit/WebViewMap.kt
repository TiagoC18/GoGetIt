package com.example.gogetit

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewMap(clientLocation: LatLng, estafetaLocation: LatLng) {
    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Leaflet Map</title>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
            <style>
                #map { height: 100vh; width: 100%; margin: 0; padding: 0; }
                html, body { height: 100%; margin: 0; padding: 0; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
            <script>
                var map = L.map('map').setView([${clientLocation.latitude}, ${clientLocation.longitude}], 13);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19
                }).addTo(map);
                
                var clientMarker = L.marker([${clientLocation.latitude}, ${clientLocation.longitude}]).addTo(map)
                    .bindPopup('Your Location')
                    .openPopup();

                var estafetaMarker = L.marker([${estafetaLocation.latitude}, ${estafetaLocation.longitude}]).addTo(map)
                    .bindPopup('Estafeta Location')
                    .openPopup();

                var latlngs = [
                    [${clientLocation.latitude}, ${clientLocation.longitude}],
                    [${estafetaLocation.latitude}, ${estafetaLocation.longitude}]
                ];
                var polyline = L.polyline(latlngs, {color: 'blue'}).addTo(map);
            </script>
        </body>
        </html>
    """.trimIndent()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.databaseEnabled = true
                settings.setGeolocationEnabled(true)
                webViewClient = WebViewClient()
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
