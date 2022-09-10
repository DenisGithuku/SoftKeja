package com.denisgithuku.softkeja.presentation.components.map

object MapStyle {
    val json = """ [
    {
        "featureType": "all",
        "elementType": "labels.text",
        "stylers": [
        {
            "color": "#878787"
        }
        ]
    },
    {
        "featureType": "all",
        "elementType": "labels.text.stroke",
        "stylers": [
        {
            "visibility": "off"
        }
        ]
    },
    {
        "featureType": "landscape",
        "elementType": "all",
        "stylers": [
        {
            "color": "#abf9b1"
        }
        ]
    },
    {
        "featureType": "road.highway",
        "elementType": "all",
        "stylers": [
        {
            "color": "#f5f5f5"
        }
        ]
    },
    {
        "featureType": "road.highway",
        "elementType": "geometry.stroke",
        "stylers": [
        {
            "color": "#3f51b6"
        }
        ]
    },
    {
        "featureType": "water",
        "elementType": "all",
        "stylers": [
        {
            "color": "#d8dcef"
        }
        ]
    }
    ]
    """
}
