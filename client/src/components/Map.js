import React, { Component } from 'react';
import mapboxgl from 'mapbox-gl';
import NY_Districts from './../geoJSON/NY_district.geojson'
import PA_Districts from './../geoJSON/PA_district.geojson'
import MD_Districts from './../geoJSON/MD_district.geojson'
import NY_Precincts from './../geoJSON/NY_normalized.geojson'
import PA_Precincts from './../geoJSON/PA_normalized.geojson'
import MD_Precincts from './../geoJSON/MD_normalized.geojson'
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
            this.addGeoJsonLayer('NY_Districts', NY_Districts);
            this.addGeoJsonLayer('PA_Districts', PA_Districts);
            this.addGeoJsonLayer('MD_Districts', MD_Districts);
        });
    }

    componentWillUnmount() {

    }

    componentDidUpdate(prevProps) {
        if (this.props.state !== prevProps.state) {
            this.changeState(prevProps.state, this.props.state);
        }
    }

    postReqChangeState = (stateName) => {
        let state = this.stateInitials(stateName);

        fetch('http://localhost:8080/api/map/changeState',
            {
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                },
                method: "POST",
                body: JSON.stringify({ "state": state }),
                mode: 'cors'
            })
            .then(response => response.json())
            .then(response => console.log(response));
    }

    stateInitials = (stateName) => {
        if (stateName === 'Maryland')
            return 'MD';
        else if (stateName === 'New York')
            return 'NY';
        else if (stateName === 'Pennsylvania')
            return 'PA';
        return stateName;
    }

    stateName = (stateInitials) => {
        if (stateInitials === 'MD')
            return 'Maryland';
        else if (stateInitials === 'NY')
            return 'New York';
        else if (stateInitials === 'PA')
            return 'Pennsylvania';
        return stateInitials;
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
            'id': sourceName + ' state-fills',
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
            'id': sourceName + ' state-borders',
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
        this.map.on('mousemove', sourceName + ' state-fills', (e) => {
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

                if (this.props.state  === this.stateFromGeoProps(e.features[0].properties))
                    this.props.onGeoDataUpdate(e.features[0].properties)
            }
        });

        this.map.on('click', sourceName + ' state-fills', (e) => {
            let stateName = this.stateFromGeoProps(e.features[0].properties);
            if (this.props.state !== stateName) {
                this.props.onStateSelect(stateName);
            }
        });

        // When the mouse leaves the state-fill layer, update the feature state of the
        // previously hovered feature.
        this.map.on('mouseleave', sourceName + ' state-fills', () => {
            if (hoveredStateId) {
                this.map.setFeatureState(
                    { source: sourceName, id: hoveredStateId },
                    { hover: false }
                );
            }
            hoveredStateId = null;
        });
    }

    removeGeoJsonLayer(sourceName){
        this.map.removeLayer(sourceName + ' state-fills');
        this.map.removeLayer(sourceName + ' state-borders');
        this.map.removeSource(sourceName);
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
        else
            this.map.flyTo({
                center: [-95, 39],
                zoom: 3.5,
                essential: true
            });
    }

    getGeoJsonFile = (fileName) =>{
        switch(fileName) {
            case 'MD_Districts':
                return MD_Districts;
            case 'NY_Districts':
                return NY_Districts;
            case 'PA_Districts':
                return PA_Districts
            case 'MD_Precincts':
                return MD_Precincts;
            case 'NY_Precincts':
                return NY_Precincts;
            case 'PA_Precincts':
                return PA_Precincts;
            default:
              return;
          }
    }

    changeState = (currentState, requestState) =>{
        this.postReqChangeState(requestState);

        let currentStateInitials = this.stateInitials(currentState);
        let requestStateInitials = this.stateInitials(requestState);

        if(currentState !== 'Select...'){
            this.addGeoJsonLayer(currentStateInitials + '_Districts', this.getGeoJsonFile(currentStateInitials + '_Districts'));
            this.removeGeoJsonLayer(currentStateInitials + '_Precincts');
        }

        if(requestState !== 'Select...'){
            this.addGeoJsonLayer(requestStateInitials + '_Precincts', this.getGeoJsonFile(requestStateInitials + '_Precincts'));
            this.removeGeoJsonLayer(requestStateInitials + '_Districts');  
        }
        
        this.zoomTo(requestState);
    }

    stateFromGeoProps(properties){
        let state = properties.statename;
        if(state === undefined)
            state = properties.STATE;
        console.log(state);
        if(state.length > 2)
            return state;
        return this.stateName(state);
    }

    render() {

        return (
            <div style={this.props.style}
                ref={el => this.mapContainer = el} />
        );
    }
}

export default Map;
