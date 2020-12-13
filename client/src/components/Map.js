import React, { Component } from 'react';
import mapboxgl from 'mapbox-gl';
import NY_Districts from './../geoJSON/NY_district.geojson'
import PA_Districts from './../geoJSON/PA_district.geojson'
import MD_Districts from './../geoJSON/MD_district.geojson'
import NY_Precincts from './../geoJSON/NY_normalized.geojson'
import PA_Precincts from './../geoJSON/PA_normalized.geojson'
import MD_Precincts from './../geoJSON/MD_normalized.geojson'
import average from './../geoJSON/jobID-1_MD_average_districting.geojson'
import random from './../geoJSON/jobID-1_MD_random_districting.geojson'
import extreme from './../geoJSON/jobID-1_MD_extreme_districting.geojson'
import '../App.css';


class Map extends Component {

    constructor(props) {
        super(props);

        mapboxgl.accessToken = 'pk.eyJ1IjoidmFzZWdvZCIsImEiOiJja2ZiZXNnOHQxMXI1MnRvOG1yY25icHZrIn0.8eLTRoe92V02KENueM7PqQ';
        this.map = null;
        this.state = {
            geoLevel: 'Districts',
            appliedLayers: [],
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
            this.addGeoJsonLayer('NY_Precincts', NY_Precincts, "black", "none");
            this.addGeoJsonLayer('PA_Precincts', PA_Precincts, "black", "none");
            this.addGeoJsonLayer('MD_Precincts', MD_Precincts, "black", "none");
            this.addGeoJsonLayer('NY_Districts', NY_Districts, "orange", "visible");
            this.addGeoJsonLayer('PA_Districts', PA_Districts, "orange", "visible");
            this.addGeoJsonLayer('MD_Districts', MD_Districts, "orange", "visible");
        });
    }

    componentWillUnmount() {

    }

    componentDidUpdate(prevProps) {
        if (this.props.state !== prevProps.state)
            this.changeState(prevProps.state, this.props.state);
        else
            this.applyGeoFilter(this.props.filter, prevProps.filter);

        console.log("prev : " + prevProps.filter.Districting.job.value);
        console.log("this : " + this.props.filter.Districting.job.value);
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

    addGeoJsonLayer = (sourceName, geoJSON, boundaryColor, visibility) => {
        if (this.state.appliedLayers.includes(sourceName))
            return;
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
            'layout': {'visibility': visibility},
            'paint': {
                'fill-color': '', // #627BC1
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
            'layout': {'visibility': visibility},
            'paint': {
                'line-color': boundaryColor,
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

                if (this.props.state === this.stateFromGeoProps(e.features[0].properties))
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

        let appliedLayers = this.state.appliedLayers;
        appliedLayers.push(sourceName);
        this.setState({ appliedLayers: appliedLayers });
    }

    removeGeoJsonLayer(sourceName) {
        if (!this.state.appliedLayers.includes(sourceName))
            return;
        this.map.removeLayer(sourceName + ' state-fills');
        this.map.removeLayer(sourceName + ' state-borders');
        this.map.removeSource(sourceName);
        let appliedLayers = this.state.appliedLayers;
        appliedLayers.splice(appliedLayers.indexOf(sourceName), 1);
        this.setState({ appliedLayers: appliedLayers });

    }

    setVisibilty = (sourceName, visibility) =>{
        this.map.setLayoutProperty(sourceName + ' state-fills', 'visibility', visibility);
        this.map.setLayoutProperty(sourceName + ' state-borders', 'visibility', visibility);
        console.log(sourceName +  '       ' + visibility);
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

    getGeoJsonFile = (fileName) => {
        switch (fileName) {
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

    applyGeoFilter = (filter, prevFilter) => {
        let appliedLayers = this.state.appliedLayers;
        let stateInitials = this.stateInitials(this.props.state);
        let districts = stateInitials + '_Districts';
        let precincts = stateInitials + '_Precincts';
        let heatmap = stateInitials + '_Heatmap';

        console.log(filter);

        if (!filter.Districts)
            this.setVisibilty(districts, "none");
        else if (filter.Districts)
            this.setVisibilty(districts, "visible");
        
        if (!filter.Precincts)
            this.setVisibilty(precincts, "none");
        else if (filter.Precincts)
            this.setVisibilty(precincts, "visible");

        // Heatmap filter logic
        if (!filter.Heatmap.show && appliedLayers.includes(heatmap))
            this.removeGeoJsonLayer(heatmap);
        else if (filter.Heatmap.show && !appliedLayers.includes(heatmap))
            this.addHeatMap(heatmap, this.getGeoJsonFile(precincts), filter.Heatmap.colorRange, filter.Heatmap.popType);
        else if (filter.Heatmap.show && appliedLayers.includes(heatmap))
            this.updateHeatMapCriteria(heatmap, filter.Heatmap)

        if(filter.Districting.job.value !== 'Select...' && prevFilter.Districting.job.value !== 'filter.Districting.job.value'){
            this.addGeoJsonLayer('average', average, filter.Districting.color.avg, "none");
            this.addGeoJsonLayer('random', random, filter.Districting.color.random, "none");
            this.addGeoJsonLayer('extreme', extreme, filter.Districting.color.extreme, "none");
        }
        if(filter.Districting.job.value === prevFilter.Districting.job.value){
            this.setVisibilty('average', filter.Districting.avg ? "visible" : "none");
            this.setVisibilty('random', filter.Districting.random ? "visible" : "none");
            this.setVisibilty('extreme', filter.Districting.extreme ? "visible" : "none");
        }


    }

    updateHeatMapCriteria = (sourceName, criteria) => {
        let mapColorRange = [
            'interpolate',
            ['linear'],
            ['var', 'density'],
            274,
            ['to-color', criteria.colorRange.low], // '#001769'
            638,
            ['to-color', criteria.colorRange.avg],
            1551,
            ['to-color', criteria.colorRange.high]
        ];

        if (criteria.colorRange.avg == '') {
            mapColorRange.splice(5, 2);
        }

        this.map.setPaintProperty(sourceName + ' state-fills', 'fill-color',
            [
                'let',
                'density',
                criteria.popType.value != "MTOT" ? ['/', ['get', criteria.popType.value], 1] : ['-', ['get', 'TOTAL'], ['get', 'WTOT']],
                [
                    'interpolate',
                    ['linear'],
                    ['zoom'],
                    8,
                    mapColorRange
                ]
            ]
        );
    }

    addHeatMap = (sourceName, geoJSON, colorRange, popType) => {
        console.log("CHALLO");

        if (this.state.appliedLayers.includes(sourceName))
            return;
        let hoveredStateId = null;

        this.map.addSource(sourceName, {
            'type': 'geojson',
            'data':
                geoJSON
        });

        let mapColorRange = [
            'interpolate',
            ['linear'],
            ['var', 'density'],
            274,
            ['to-color', colorRange.low], // '#001769'
            638,
            ['to-color', colorRange.avg],
            1551,
            ['to-color', colorRange.high]
        ];

        if (colorRange.avg == '') {
            mapColorRange.splice(5, 2);
        }

        // The feature-state dependent fill-opacity expression will render the hover effect
        // when a feature's hover state is set to true.
        this.map.addLayer({
            'id': sourceName + ' state-fills',
            'type': 'fill',
            'source': sourceName,
            'layout': {},
            'paint': {
                'fill-color': [
                    'let',
                    'density',
                    popType.value != "MTOT" ? ['/', ['get', popType.value], 1] : ['-', ['get', 'TOTAL'], ['get', 'WTOT']],
                    [
                        'interpolate',
                        ['linear'],
                        ['zoom'],
                        8,
                        mapColorRange,
                    ]
                ],
                'fill-opacity': 0.75
            },
        });

        this.map.addLayer({
            'id': sourceName + ' state-borders',
            'type': 'line',
            'source': sourceName,
            'layout': {},
            'paint': {
                'line-color': 'white',
                'line-width': 0.2
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

                if (this.props.state === this.stateFromGeoProps(e.features[0].properties))
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

        let appliedLayers = this.state.appliedLayers;
        appliedLayers.push(sourceName);
        this.setState({ appliedLayers: appliedLayers });
    }

    changeState = (currentState, requestState) => {
        this.postReqChangeState(requestState);

        let currentStateInitials = this.stateInitials(currentState);
        let requestStateInitials = this.stateInitials(requestState);

        if (currentState !== 'Select...') {
            this.setVisibilty(currentStateInitials + '_Districts', "visible");
            this.setVisibilty(currentStateInitials + '_Precincts', "none");
        }

        if (requestState !== 'Select...') {
            this.setVisibilty(requestStateInitials + '_Precincts', "visible");
            this.setVisibilty(requestStateInitials + '_Districts', "none");
        }

        this.removeGeoJsonLayer(currentStateInitials + '_Heatmap')

        this.props.resetFilter();
        this.zoomTo(requestState);
    }

    stateFromGeoProps(properties) {
        let state = properties.statename;
        if (state === undefined)
            state = properties.STATE;
        if (state.length > 2)
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
