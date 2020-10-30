import React, { Component } from 'react';
import Map from './Map.js'

class HomeScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: window.innerWidth,
            height: window.innerHeight,
            state: 'PA',
            level: 'Precinct',
            latitude: 37.15,
            longitude: -102.46,
            zoom: 4,
        };
    }

    componentDidMount() {
        window.addEventListener('resize', this.updateWindowDimensions);
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.updateWindowDimensions);
    }

    updateWindowDimensions = () => {
        this.setState({ width: window.innerWidth, height: window.innerHeight }, this.forceUpdate);
    }

    render() {
        return (
            <div>

                <Map
                    width={this.state.width}
                    height={this.state.height * .875}
                    latitude={this.state.latitude}
                    longitude={this.state.longitude}
                    zoom={this.state.zoom}
                    mapColor={this.state.mapColor}
                    triggerRepaint={true}
                    levelColoring={this.state.levels}
                    state={this.state.state}
                    level={this.state.level}
                >
                </Map>
            </div>
        );
    }
}

export default HomeScreen;