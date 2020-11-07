import React, { Component } from 'react';
import Map from './Map'
import Header from './Header'
import Sidebar from './Sidebar'

class Project extends Component {

    constructor(props) {
        super(props);
        this.state = {
            stateData: { stateLevel: '', stateName: '' },
            mouseCoords: { lat: 39, lng: -95 },
            geoJSON: null,
            jobs: [ {
                jobID: 0,
                compactness: 'very',
                status : 'in progress', 
            }],
        }
    }

    onMouseCoordsUpdate = (mouseCoords) => {
        this.setState({ mouseCoords: mouseCoords });
    }

    onStateDataUpdate = (stateData) => {
        this.setState({ stateData: stateData });
    }

    updateJobs = (jobs) =>{
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
                        stateData={this.state.stateData}
                        onStateDataUpdate={this.onStateDataUpdate} />
                </div>
                <div style={sidebarStyle}>
                    <Sidebar
                        jobs={this.state.jobs}
                        updateJobs={this.updateJobs}
                        stateData={this.state.stateData}
                        mouseCoords={this.state.mouseCoords} />
                </div>
                <Map 
                    style={mapStyle} 
                    onStateDataUpdate={this.onStateDataUpdate}
                    onMouseCoordsUpdate={this.onMouseCoordsUpdate} 
                    stateData={this.stateData} />
            </div>
        );
    }
}

export default Project;
