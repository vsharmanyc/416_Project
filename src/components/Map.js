import React, { Component } from 'react';
import { Map as LeafletMap, TileLayer, GeoJSON } from 'react-leaflet';
import {
    FormControl, FormLabel, RadioGroup, Radio, FormControlLabel,
    Button, InputLabel, Select, MenuItem, Checkbox, Typography
} from '@material-ui/core';
import NY_State from '../geojson/NY_State';
import NY_Counties from '../geojson/NY_Counties';
import NY_Precincts from './../geojson/NY_Precincts';
import MD_Precincts from './../geojson/MD_Precincts';
import PA_Precincts from './../geojson/PA_Precincts';


class Map extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: props.width,
            height: props.height,
            position: [props.latitude, props.longitude],
            zoom: props.zoom,
            state: props.state,
            level: props.level,
            geoJSONData: {
                'NY': { 'State': NY_State, 'County': NY_Counties, 'District': null, 'Precinct': NY_Precincts },
                'MD': { 'State': null, 'County': null, 'District': null, 'Precinct': MD_Precincts },
                'PA': { 'State': null, 'County': null, 'District': null, 'Precinct': PA_Precincts },
            }
        };
    }

    onViewportChanged = (viewport) => {
        //this.setState({ position: viewport.center, zoom: viewport.zoom })
    }



    render() {
        return (
            <div>
                <div style={{
                    position: 'absolute',
                    zIndex: 100,
                    right: 0,
                    backgroundColor: 'white',
                    width: '10%',
                    borderStyle: 'solid'
                }}>
                    <FormControl component="fieldset">
                        <div style={{ width: '90%', paddingBottom: '2%' }}>
                            <InputLabel id="demo-simple-select-label">State</InputLabel>
                            <Select
                                labelId="demo-simple-select-label"
                                id="demo-simple-select"
                                value={this.state.state}
                                onChange={(e) => { this.setState({ state: e.target.value }, this.forceUpdate) }}
                            >
                                <MenuItem value='NY'>New York</MenuItem>
                                <MenuItem value='PA'>Pennsylvania</MenuItem>
                                <MenuItem value='MD'>Maryland</MenuItem>
                            </Select>
                        </div>
                        <div>
                            <FormLabel component="legend">Levels</FormLabel>
                            <RadioGroup aria-label="Levels" name="levels" value={this.state.level} onChange={(e) => { this.setState({level: e.target.value }, this.forceUpdate) }}>
                                <FormControlLabel value="State" control={<Radio style={{ color: '#63BEB6' }} />} label="State" />
                                <FormControlLabel value="County" control={<Radio style={{ color: '#63BEB6' }} />} label="County" />
                                <FormControlLabel value="District" control={<Radio style={{ color: '#63BEB6' }} />} label="District" />
                                <FormControlLabel value="Precinct" control={<Radio style={{ color: '#63BEB6' }} />} label="Precinct" />
                            </RadioGroup>
                        </div>
                    </FormControl>
                </div>

                <div style={{
                    position: 'absolute',
                    zIndex: 0,
                    height: this.state.height,
                    width: this.state.width
                }}
                >
                    <LeafletMap
                        style={{ height: '100%', width: '100%' }}
                        center={this.state.position}
                        zoom={this.state.zoom}
                        onViewportChanged={this.onViewportChanged}
                    >
                        <TileLayer
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                            attribution="&copy; <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
                        />
                        <GeoJSON key={this.state.state + '_' + this.state.level}
                        data={this.state.geoJSONData[this.state.state][this.state.level]}></GeoJSON>
                    </LeafletMap>
                </div>
            </div>
        );
    }
}

export default Map;