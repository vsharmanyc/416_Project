import React, { Component } from 'react';
import { Form, Button, ButtonGroup } from 'react-bootstrap';
import panther from "./../rsrc/panther.png"
import '../App.css';


class Header extends Component {

    constructor(props) {
        super(props);
        this.state = {
            state: 'Select...',
            stateLevel: 'State'
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    onStateSelect = (e) => {
        this.setState({ state: e.target.value }, this.props.onStateDataUpdate(this.state));
    }
    stateLevelClick = (e) => {
        this.setState({ stateLevel: e.target.textContent }, this.props.onStateDataUpdate(this.state));
    }

    render() {
        let stateLevel = this.state.stateLevel;
        const selectedBtn = {backgroundColor: 'white', color: 'black'};

        return (
            <div style={{
                position: 'absolute',
                backgroundColor: '#63BEB6',
                height: '10%',
                width: '100%'
            }} >
                <div style={{ display: 'flex', flexDirection: 'row' }}>
                    <div style={{ position: 'absolute', right: '95%' }}>
                        <img style={{
                            height: '90%',
                            width: '90%',
                        }} src={panther} />
                    </div>
                    <p style={{ position: 'absolute', color: 'white', right: '80%', top: '10%', fontSize: '200%' }}
                        class="font-weight-normal">
                        Districting App
                    </p>
                    <ButtonGroup onClick={this.stateLevelClick} style={{ position: 'absolute', top: '20%', right: '22%', }}
                        className="mb-2">
                        <Button style={stateLevel == 'State'? selectedBtn : {}} variant="outline-light">State</Button>
                        <Button style={stateLevel == 'District'? selectedBtn : {}} variant="outline-light">District</Button>
                        <Button style={stateLevel == 'Precinct'? selectedBtn : {}} variant="outline-light">Precinct</Button>
                    </ButtonGroup>
                    <p style={{ position: 'absolute', color: 'white', right: '16%', top: '20%', fontSize: '150%' }}
                        class="font-weight-normal">
                        State:
                    </p>
                    <div style={{ position: 'absolute', right: '2%', top: '20%', width: '13%' }}>
                        <Form.Control onChange={this.onStateSelect} as="select">
                            <option selected={this.props.stateData.state === 'Select...'}>
                                Select...</option>
                            <option selected={this.state.state === 'New York' || this.props.stateData.state === 'New York'}>
                                New York</option>
                            <option selected={this.state.state === 'Pennsylvania' || this.props.stateData.state === 'Pennsylvania'}>
                                Pennsylvania</option>
                            <option selected={this.state.state === 'Maryland' || this.props.stateData.state === 'Maryland'}>
                                Maryland</option>
                        </Form.Control>
                    </div>
                </div>
            </div>
        );
    }
}

export default Header;
