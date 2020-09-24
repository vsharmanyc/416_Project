import React, { Component } from 'react';
import Map from './Map.js'
import Sidebar from './Sidebar.js'
import Tabs from './Tabs.js'

class HomeScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: window.innerWidth,
            height: window.innerHeight,
            latitude: 38.9618303,
            longitude: -96.6980505,
            zoom: 3.5
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
        console.log(this.state);
        return (
            <div>
                <Sidebar
                    height={this.state.height * .75}
                    width={this.state.width * .20}
                    side='left'
                >
                    <Tabs
                        tabsNames={['Sort', 'Filter', 'Modify']}
                        tabsInnerJSX={[
                            <div>Sorting Data Fields</div>,
                            <div>Filtering Data Fields</div>,
                            <div>Modification Data Feilds</div>,
                        ]}
                    />
                </Sidebar>

                <Sidebar
                    height={this.state.height * .75}
                    width={this.state.width * .20}
                    side='right'
                >
                    <Tabs
                        tabsNames={['Genenrate', 'Settings']}
                        tabsInnerJSX={[
                            <div>Map Generation Criteria Fields</div>,
                            <div>Adjust App Settings</div>,
                        ]}
                    />
                </Sidebar>

                <Map
                    width={this.state.width}
                    height={this.state.height * .875}
                    longitude={this.state.longitude}
                    latitude={this.state.latitude}
                    zoom={this.state.zoom}
                >
                </Map>
            </div>
        );
    }
}

export default HomeScreen;