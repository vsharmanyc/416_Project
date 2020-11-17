import React, { Component } from 'react';
import { Form } from 'react-bootstrap';
import panther from "./../rsrc/panther.png"
import '../App.css';


class Header extends Component {

    constructor(props) {
        super(props);
        this.state = {
            state: 'Select...',
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    onStateSelect = (e) => {
        this.setState({ state: e.target.value }, this.props.onStateSelect(e.target.value));
    }

    render() {

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
                        }} alt='panther' src={panther} />
                    </div>
                    <p style={{ position: 'absolute', color: 'white', right: '80%', top: '10%', fontSize: '200%' }}
                        class="font-weight-normal">
                        Districting App
                    </p>
                    <p style={{ position: 'absolute', color: 'white', right: '16%', top: '20%', fontSize: '150%' }}
                        class="font-weight-normal">
                        State:
                    </p>
                    <div style={{ position: 'absolute', right: '2%', top: '20%', width: '13%' }}>
                        <Form.Control onChange={this.onStateSelect} as="select">
                            <option selected={this.props.state === 'Select...'}>
                                Select...</option>
                            <option selected={this.state.state === 'New York' || this.props.state === 'New York'}>
                                New York</option>
                            <option selected={this.state.state === 'Pennsylvania' || this.props.state === 'Pennsylvania'}>
                                Pennsylvania</option>
                            <option selected={this.state.state === 'Maryland' || this.props.state === 'Maryland'}>
                                Maryland</option>
                        </Form.Control>
                    </div>
                </div>
            </div>
        );
    }
}

export default Header;
