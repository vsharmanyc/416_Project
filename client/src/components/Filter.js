import React, { Component } from 'react';
import { Form } from 'react-bootstrap';

class Filter extends Component {

    constructor(props) {
        super(props);

        this.state = {
            demographics: [],
        }
    }

    onDemographicsSelect = (selected) => { this.setState({ demographics: selected }); }

    clearForm = () => {
        this.setState({
            demographics: [],
        })
    }

    updateFilter = (level) => {
        let filter = Object.assign({}, this.props.filter);
        filter[level] = !filter[level];
        this.props.updateFilter(filter);
    }

    render() {
        const demographics = [
            { value: 'White', label: 'White' },
            { value: 'Black', label: 'Black' },
            { value: 'Asian', label: 'Asian' },
            { value: 'Hispanic or Latino', label: 'Hispanic or Latino' },
            { value: 'American Indian or Alaska Native', label: 'American Indian or Alaska Native' },
            { value: 'Native Hawaiian or Other Pacific Islander', label: 'Native Hawaiian or Other Pacific Islander' },
            { value: 'Two or More Races', label: 'Two or More Races' },
            { value: 'Other', label: 'Other' }
        ]

        const formStyle = {
            height: '100%', width: '95%',
            backgroundColor: '#ebf3f5', padding: '5%', borderRadius: '3%', marginTop: '2%'
        };

        return (
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div style={formStyle}>
                    <h6>Show Boundaries</h6>
                    <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', paddingBottom: '5%' }}>
                        <div style={{ display: 'flex', flexGrow: 3, flexDirection: 'column', justifyContent: 'space-around' }}>
                            <Form.Check
                                onClick={() => this.updateFilter('Districts')}
                                checked={this.props.filter.Districts}
                                disabled={!this.props.stateIsSelected}
                                type="checkbox"
                                id="district check"
                                label="Districts"
                            />

                            <Form.Check
                                onClick={() => this.updateFilter('Precincts')}
                                checked={this.props.filter.Precincts}
                                disabled={!this.props.stateIsSelected}
                                type="checkbox"
                                id="precinct check"
                                label="Precincts"
                            />

                        </div>
                    </div>

                    <h6>Display Heat Map</h6>
                    <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', paddingBottom: '5%' }}>
                        <div style={{ display: 'flex', flexGrow: 3, flexDirection: 'column', justifyContent: 'space-around' }}>

                            <Form.Check
                                onClick={() => this.updateFilter('Heatmap')}
                                checked={this.props.filter.Heatmap}
                                disabled={!this.props.stateIsSelected}
                                type="checkbox"
                                id="heatmap check"
                                label="Heatmap"
                            />
                        </div>
                    </div>

                    <div style={{color: 'green', fontFamily: 'Arial'}}>
                                {!this.props.stateIsSelected ? "Must select a state first before selecting above" : ""}
                    </div>

                </div>
            </div>
        );
    }
}

export default Filter;
