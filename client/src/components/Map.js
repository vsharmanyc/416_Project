import React, { Component } from 'react';
import mapboxgl from 'mapbox-gl';
import States from './../geoJSON/states.geojson'
import NY from './../geoJSON/NY.geojson'
import '../App.css';


class Map extends Component {

    constructor(props) {
        super(props);

        mapboxgl.accessToken = 'pk.eyJ1IjoidmFzZWdvZCIsImEiOiJja2ZiZXNnOHQxMXI1MnRvOG1yY25icHZrIn0.8eLTRoe92V02KENueM7PqQ';
        this.map = null;
        this.state = {
            currentLayer: 'states',
            lng: -95,
            lat: 39,
            zoom: 3.5,
        }
    }

    componentDidMount() {
        this.map = new mapboxgl.Map({
            container: this.mapContainer,
            style: 'mapbox://styles/vasegod/ckfocvfiu02jw19kjulasl3dq',
            center: [this.state.lng, this.state.lat],
            zoom: this.state.zoom
        });

        this.map.on('load', () => {
            this.addGeoJsonLayer('NY', NY);
        });
    }

    componentWillUnmount() {

    }

    componentDidUpdate(prevProps) {
        if (this.props.state !== prevProps.state) {
            this.zoomTo(this.props.state);
        }
    }

    addGeoJsonLayer = (sourceName, geoJSON) => {
        let hoveredStateId = null;

        this.map.addSource(sourceName, {
            'type': 'geojson',
            'data':
                geoJSON
        });

        // The feature-state dependent fill-opacity expression will render the hover effect
        // when a feature's hover state is set to true.
        this.map.addLayer({
            'id': 'state-fills',
            'type': 'fill',
            'source': sourceName,
            'layout': {},
            'paint': {
                'fill-color': '#627BC1',
                'fill-opacity': [
                    'case',
                    ['boolean', ['feature-state', 'hover'], false],
                    1,
                    0.5
                ]
            }
        });

        this.map.addLayer({
            'id': 'state-borders',
            'type': 'line',
            'source': sourceName,
            'layout': {},
            'paint': {
                'line-color': '#627BC1',
                'line-width': 2
            }
        });

        // When the user moves their mouse over the state-fill layer, we'll update the
        // feature state for the feature under the mouse.
        this.map.on('mousemove', 'state-fills', (e) => {
            if (e.features.length > 0) {
                if (hoveredStateId) {
                    this.map.setFeatureState(
                        { source: sourceName, id: hoveredStateId },
                        { hover: false }
                    );
                }
                hoveredStateId = e.features[0].properties.GEOID10;
                this.map.setFeatureState(
                    { source: sourceName, id: hoveredStateId },
                    { hover: true }
                );
                
                if(this.props.state === e.features[0].properties.STATE)
                    this.props.onGeoDataUpdate(e.features[0].properties)
            }
        });

        this.map.on('click', 'state-fills', (e) => {
            let stateName = e.features[0].properties.STATE;
            if (this.props.state != stateName) {
                this.zoomTo(stateName);
                this.props.onStateSelect(stateName);
            }
        });

        // When the mouse leaves the state-fill layer, update the feature state of the
        // previously hovered feature.
        this.map.on('mouseleave', 'state-fills', () => {
            if (hoveredStateId) {
                this.map.setFeatureState(
                    { source: sourceName, id: hoveredStateId },
                    { hover: false }
                );
            }
            hoveredStateId = null;
        });
    }

    zoomTo = (state) => {
        if (state === 'New York')
            this.map.flyTo({
                center: [-74.2179, 43.2994],
                zoom: 6,
                essential: true
            });
        else if (state === 'Pennsylvania')
            this.map.flyTo({
                center: [-77.1945, 41.2033],
                zoom: 6.8,
                essential: true
            });
        else if (state === 'Maryland')
            this.map.flyTo({
                center: [-76.6413, 39.0458],
                zoom: 7,
                essential: true
            });
    }

    render() {

        return (
            <div style={this.props.style}
                ref={el => this.mapContainer = el} />
        );
    }
}

export default Map;
