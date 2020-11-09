import React, { Component } from 'react';
import { Tab, Tabs} from 'react-bootstrap';
import Jobs from './Jobs';
import CreateJob from './CreateJob';
import Data from './Data';
import Filter from './Filter';
import '../App.css';


class Sidebar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            tabNum: 0
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    changeTab = (tabNum) =>{
        this.setState({tabNum: tabNum});
    }


    render() {

        return (
            <div style={{
                height: '100%',
                width: '100%',
                overflow: 'auto',
                borderStyle: 'solid',
                borderWidth: '2px',
                borderColor: '#63BEB6'
            }}>
                <Tabs activeKey={this.state.tabNum} onSelect={this.changeTab} id="controlled-tab-example">
                    <Tab eventKey={0} title="Data"><Data geoData={this.props.geoData}/></Tab>

                    <Tab eventKey={1} title="Filter"><Filter/></Tab>

                    <Tab eventKey={2} title="Create Job">
                        <CreateJob jobs={this.props.jobs} updateJobs={this.props.updateJobs}/>
                    </Tab>

                    <Tab eventKey={3} title="Jobs"><Jobs updateJobs={this.props.updateJobs} jobs={this.props.jobs}/></Tab>
                </Tabs>
            </div>
        );
    }
}

export default Sidebar;
