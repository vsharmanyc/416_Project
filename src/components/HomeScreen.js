import React, { Component } from 'react';
import Map from './Map.js'
import Sidebar from './Sidebar.js'
import Tabs from './Tabs.js'
import FilterTab from './FilterTab'
import ModifyTab from './ModifyTab'
import GraphTab from './GraphTab'
import SettingsTab from './SettingsTab'

class HomeScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: window.innerWidth,
            height: window.innerHeight,
            latitude: 38.9618303,
            longitude: -96.6980505,
            zoom: 3.5,
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
        const checkboxStyle = { color: "#63BEB6" }
        console.log(this.state.mapColor);
        return (
            <div>
                <Sidebar
                    height={this.state.height * .75}
                    width={this.state.width * .20}
                    side='left'
                >
                    <Tabs tabsNames={['Filter', 'Modify']}>
                        <FilterTab></FilterTab>
                        <ModifyTab></ModifyTab>
                    </Tabs>
                </Sidebar>

                <Sidebar
                    height={this.state.height * .75}
                    width={this.state.width * .20}
                    side='right'
                >
                    <Tabs tabsNames={['Graph', 'Settings']}>
                        <GraphTab></GraphTab>
                        <SettingsTab></SettingsTab>
                    </Tabs>
                </Sidebar>

                <Map
                    width={this.state.width}
                    height={this.state.height * .875}
                    longitude={this.state.longitude}
                    latitude={this.state.latitude}
                    zoom={this.state.zoom}
                    mapColor={this.state.mapColor}
                    triggerRepaint={true}
                >
                </Map>
            </div>
        );
    }
}

export default HomeScreen;