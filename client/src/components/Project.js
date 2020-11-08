import React, { Component } from 'react';
import mapboxgl from 'mapbox-gl';
import Map from './Map'
import Header from './Header'
import Sidebar from './Sidebar'

class Project extends Component {

    constructor(props) {
        super(props);
        this.state = {
            state: '',
            mouseCoords: { lat: 39, lng: -95 },
            geoJSON: null,
            jobs: [{
                jobID: 0,
                compactness: 'very',
                status: 'in progress',
            }],
        }
    }

    onComponentDidMount() {
    }

    onMouseCoordsUpdate = (mouseCoords) => {
        this.setState({ mouseCoords: mouseCoords });
    }

    onStateSelect = (state) => {
        console.log(state);
        this.setState({ state: state });
    }

    updateJobs = (jobs) => {
        this.setState({ jobs: jobs });
    }



    render() {
        const headerStyle = { height: '10%', width: '100%', backgroundColor: '#63BEB6' };
        const sidebarStyle = { position: 'absolute', top: '10%', height: '90%', width: '25%' }
        const mapStyle = { position: 'absolute', left: '25%', top: '10%', height: '90%', width: '75%' };


        return (
            <div>
                <div style={headerStyle}>
                    <Header
                        state={this.state.state}
                        onStateSelect={this.onStateSelect} />
                </div>
                <div style={sidebarStyle}>
                    <Sidebar
                        jobs={this.state.jobs}
                        updateJobs={this.updateJobs}
                        state={this.state.state}
                        mouseCoords={this.state.mouseCoords} />
                </div>
                <Map
                    style={mapStyle}
                    onStateSelect={this.onStateSelect}
                    onMouseCoordsUpdate={this.onMouseCoordsUpdate}
                    state={this.state.state} />
            </div>
        );
    }
}

export default Project;
