import React, { Component } from 'react';
import ReactMapGL from 'react-map-gl';
import Tab from './Tabs.js'

class Map extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: props.width,
            height: props.height,
            latitude: props.latitude,
            longitude: props.longitude,
            zoom: props.zoom,
            visibility: {
                water: true,
                parks: true,
                buildings: true,
                roads: true,
                labels: true,
                background: true
              },
              color: {
                water: 'blue',
                parks: '#E6EAE9',
                buildings: '#c0c0c8',
                roads: '#ffffff',
                labels: '#78888a',
                background: '#EBF0F0'
              }
        };
    }

    onViewportChange = (viewport) => {
        this.setState({
            longitude: viewport.longitude,
            latitude: viewport.latitude,
            zoom: viewport.zoom
        });
    }

    render() {
        return (
            <div>
                <ReactMapGL
                    mapboxApiAccessToken='pk.eyJ1IjoidmFzZWdvZCIsImEiOiJja2ZiZXNnOHQxMXI1MnRvOG1yY25icHZrIn0.8eLTRoe92V02KENueM7PqQ'
                    {...this.state}
                    onViewportChange={this.onViewportChange}
                    visibility={this.state.visibility}
                    color={this.state.color}
                />
            </div>
        );
    }
}

export default Map;