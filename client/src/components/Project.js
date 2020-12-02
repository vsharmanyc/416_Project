import React, { Component } from 'react';
import Map from './Map'
import Header from './Header'
import Sidebar from './Sidebar'
import GraphModal from './GraphModal'

class Project extends Component {

    constructor(props) {
        super(props);
        this.state = {
            state: 'Select...',
            filter: {
                Districts: true,
                Precincts: false,
                Heatmap: false,
            },
            geoJSON: null,
            geoData: {},
            jobs: [],
            toggleModal: false
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

    updateFilter = (filter) => {
        this.setState({ filter: filter });
    }

    toggleModal = () =>{
        console.log("hello0fdsfsf00");
        this.setState({toggleModal: !this.state.toggleModal})
    }

    render() {
        const headerStyle = { height: '10%', width: '100%', backgroundColor: '#63BEB6', zIndex: 3 };
        const sidebarStyle = { position: 'absolute', top: '10%', height: '90%', width: '25%', zIndex: 2 }
        const mapStyle = { position: 'absolute', top: '10%', height: '90%', width: '100%', zIndex: 1 };
        const modalStyle = {
            position: 'absolute', height: '100%', width: '100%', zIndex: 4,
            display: 'flex', justifyContent: 'center', alignItems: 'center',
        };

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
                        geoData={this.state.geoData}
                        updateFilter={this.updateFilter}
                        filter={this.state.filter}
                        toggleModal={this.toggleModal}
                    />
                </div>
                <Map
                    style={mapStyle}
                    onGeoDataUpdate={this.onGeoDataUpdate}
                    onStateSelect={this.onStateSelect}
                    state={this.state.state}
                    updateFilter={this.updateFilter}
                    filter={this.state.filter}
                />
                {this.state.toggleModal ?
                    <div style={modalStyle} >
                        <GraphModal toggleModal={this.toggleModal}/>
                    </div>
                    :
                    <></>
                }
            </div>
        );
    }
}

export default Project;
