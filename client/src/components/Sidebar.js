import React, { Component } from 'react';
import { Tab, Tabs } from 'react-bootstrap';
import Jobs from './Jobs';
import CreateJob from './CreateJob';
import Data from './Data';
import Filter from './Filter';
import '../App.css';


class Sidebar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            tabNum: 0,
            toggle: false,
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    changeTab = (tabNum) => {
        this.setState({ tabNum: tabNum });
    }

    toggle = () => {
        this.setState({ toggle: !this.state.toggle });
    }


    render() {
        let toggleStyle = {
            position: "absolute",
            left: !this.state.toggle ? '99.5%' : '',
            top: '40%',
            height: '10%',
            width: '10%',
            borderStyle: 'solid',
            borderRadius: '10%',
            borderWidth: '2px',
            borderColor: '#63BEB6',
            backgroundColor: 'white',
            color: '#63BEB6',
            fontSize: '150%',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center'
        };

        if (!this.state.toggle)
            return (
                <div style={{
                    height: '100%',
                    width: '100%',
                    overflow: 'auto',
                    borderStyle: 'solid',
                    borderWidth: '2px',
                    borderColor: '#63BEB6',
                    backgroundColor: 'white',
                }}>
                    <div>
                        <Tabs activeKey={this.state.tabNum} onSelect={this.changeTab} id="controlled-tab-example">
                            <Tab eventKey={0} title="Data"><Data geoData={this.props.geoData} stateIsSelected={this.props.state !== 'Select...'}/></Tab>

                            <Tab eventKey={1} title="Filter">
                                <Filter updateFilter={this.props.updateFilter} filter={this.props.filter} stateIsSelected={this.props.state !== 'Select...'}/>
                            </Tab>

                            <Tab eventKey={2} title="Create Job">
                                <CreateJob jobs={this.props.jobs} updateJobs={this.props.updateJobs} />
                            </Tab>

                            <Tab eventKey={3} title="Jobs"><Jobs updateJobs={this.props.updateJobs} toggleModal={this.props.toggleModal} jobs={this.props.jobs} /></Tab>
                        </Tabs>
                    </div>
                    <div style={toggleStyle} onClick={this.toggle}>
                        {'🡄'}
                    </div>
                </div>
            );
        else
            return (
                <div style={toggleStyle} onClick={this.toggle}>
                    {'🡆'}
                </div>
            );

    }
}

export default Sidebar;