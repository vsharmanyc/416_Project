import React, { Component } from 'react';
import ReactMapGL from 'react-map-gl';
import Tab from './Tab.js'

class Map extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: props.width,
            height: props.height,
            latitude: props.latitude,
            longitude: props.longitude,
            zoom: props.zoom,
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
                />
            </div>
        );
    }
}

export default Map;