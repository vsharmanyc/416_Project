import React, { Component } from 'react';
import Map from './Map'
import Header from './Header'
import Sidebar from './Sidebar'

class Project extends Component {

    constructor(props) {
        super(props);
        this.state = {
            state: '',
            geoJSON: null,
            geoData: {},
            jobs: [{
                jobID: 0,
                compactness: 'very',
                status: 'in progress',
            }],
        }
    }

    onComponentDidMount() {
    }

    onGeoDataUpdate = (geoData) => {
        this.setState({ geoData: geoData });
    }

    onStateSelect = (state) => {
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
                        geoData={this.state.geoData} />
                </div>
                <Map
                    style={mapStyle}
                    onGeoDataUpdate={this.onGeoDataUpdate}
                    onStateSelect={this.onStateSelect}
                    state={this.state.state} />
            </div>
        );
    }
}

export default Project;
