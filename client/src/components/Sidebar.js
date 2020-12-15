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
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    changeTab = (tabNum) => {
        this.setState({ tabNum: tabNum });
    }

    render() {
        let toggleStyle = {
            position: "absolute",
            left: this.props.showSideBar ? '99.5%' : '',
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

        if (this.props.showSideBar)
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
                            <Tab eventKey={0} title="Data"><Data geoData={this.props.geoData} stateIsSelected={this.props.state !== 'Select...'} /></Tab>

                            <Tab eventKey={1} title="Filter">
                                <Filter updateFilter={this.props.updateFilter} filter={this.props.filter} state={this.props.state} stateIsSelected={this.props.state !== 'Select...'} jobs={this.props.jobs}/>
                            </Tab>

                            <Tab eventKey={2} title="Create Job">
                                <CreateJob jobs={this.props.jobs} updateJobs={this.props.updateJobs} stateIsSelected={this.props.state !== 'Select...'} state={this.props.state}/>
                            </Tab>

                            <Tab eventKey={3} title="Jobs"><Jobs updateJobs={this.props.updateJobs} toggleModal={this.props.toggleModal} jobs={this.props.jobs} 
                                getJobHistoryAndUpdateJobs={this.props.getJobHistoryAndUpdateJobs}/></Tab>
                        </Tabs>
                    </div>
                    <div style={toggleStyle} onClick={this.props.toggleSideBar}>
                        {'🡄'}
                    </div>
                </div>
            );
        else
            return (
                <div style={toggleStyle} onClick={this.props.toggleSideBar}>
                    {'🡆'}
                </div>
            );

    }
}

export default Sidebar;