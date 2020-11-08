import React, { Component } from 'react';
import mapboxgl from 'mapbox-gl';
import States from './../geoJSON/states.geojson'
import '../App.css';


class Map extends Component {

    constructor(props) {
        super(props);

        mapboxgl.accessToken = 'pk.eyJ1IjoidmFzZWdvZCIsImEiOiJja2ZiZXNnOHQxMXI1MnRvOG1yY25icHZrIn0.8eLTRoe92V02KENueM7PqQ';
        this.map = null;
        this.state = {
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

        this.map.on('mousemove', (e) => this.props.onMouseCoordsUpdate({ lat: e.lngLat.lat, lng: e.lngLat.lng }));
        let map = this.map;
        let hoveredStateId = null;
        map.on('load', () => {
            map.addSource('states', {
                'type': 'geojson',
                'data':
                    States
            });

            // The feature-state dependent fill-opacity expression will render the hover effect
            // when a feature's hover state is set to true.
            map.addLayer({
                'id': 'state-fills',
                'type': 'fill',
                'source': 'states',
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

            map.addLayer({
                'id': 'state-borders',
                'type': 'line',
                'source': 'states',
                'layout': {},
                'paint': {
                    'line-color': '#627BC1',
                    'line-width': 2
                }
            });

            // When the user moves their mouse over the state-fill layer, we'll update the
            // feature state for the feature under the mouse.
            map.on('mousemove', 'state-fills', function (e) {
                if (e.features.length > 0) {
                    if (hoveredStateId) {
                        map.setFeatureState(
                            { source: 'states', id: hoveredStateId },
                            { hover: false }
                        );
                    }
                    hoveredStateId = e.features[0].id;
                    map.setFeatureState(
                        { source: 'states', id: hoveredStateId },
                        { hover: true }
                    );
                }
            });

            map.on('click', 'state-fills', (e) => {
                let stateName = e.features[0].properties.STATE_NAME;
                this.zoomTo(stateName);
                this.props.onStateSelect({ state: stateName, stateLevel: 'state' });
            });

            // When the mouse leaves the state-fill layer, update the feature state of the
            // previously hovered feature.
            map.on('mouseleave', 'state-fills', function () {
                if (hoveredStateId) {
                    map.setFeatureState(
                        { source: 'states', id: hoveredStateId },
                        { hover: false }
                    );
                }
                hoveredStateId = null;
            });
        });
    }

    componentWillUnmount() {
        this.map.off('mousemove', (e) => this.props.onMouseCoordsUpdate({ lat: e.lngLat.lat, lng: e.lngLat.lng }));
    }

    componentDidUpdate(prevProps) {
        console.log(prevProps);
        console.log(this.props);
        if (this.props.state !== prevProps.state) {
          this.zoomTo(this.props.state);
        }
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
