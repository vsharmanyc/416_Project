import React, { Component } from 'react';
import { Form } from 'react-bootstrap';
import Select from 'react-select';

class Filter extends Component {

    constructor(props) {
        super(props);
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

    updateHeatMap = (selected) => {
        let filter = Object.assign({}, this.props.filter);
        filter.Heatmap.show = selected.value !== 'NONE';
        filter.Heatmap.popType = selected;
        if (selected.value === 'TOTVAP')
            filter.Heatmap.colorRange = { low: '#e6f0ee', avg: '', high: '#006952' };
        else
            filter.Heatmap.colorRange = { low: '#0015d6', avg: 'white', high: '#d60000' };
        this.props.updateFilter(filter);
    }

    render() {
        const demographics = [
            { value: 'NONE', label: 'Select' },
            { value: 'TOTVAP', label: 'Total Voting Age Population' },
            { value: 'MTOT', label: 'Total Minority' },
            { value: 'WTOT', label: 'White' },
            { value: 'BTOT', label: 'Black' },
            { value: 'ATOT', label: 'Asian' },
            { value: 'HTOT', label: 'Hispanic or Latino' },
            { value: 'AIANTOT', label: 'American Indian or Alaska Native' },
            { value: 'NHOPTOT', label: 'Native Hawaiian or Other Pacific Islander' },
            { value: 'OTHERTOT', label: 'Other' }
        ]

        const formStyle = {
            height: '100%', width: '95%',
            backgroundColor: '#ebf3f5', padding: '5%', borderRadius: '3%', marginTop: '2%'
        };

        const colorRange = this.props.filter.Heatmap.colorRange;

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

                            <Select options={demographics} value={this.props.filter.Heatmap.popType}
                                onChange={this.updateHeatMap} isDisabled={!this.props.stateIsSelected} />

                            {this.props.filter.Heatmap.show ?
                                <>
                                    <div style={{ marginTop: '3%', fontFamily: 'sans-serif', fontSize: '75%' }}>Population Color Range</div>
                                    <div style={{
                                        marginTop: '1%',
                                        width: '100%',
                                        height: '100%',
                                        display: 'flex',
                                        flexDirection: 'row',
                                        justifyContent: 'space-between',
                                        fontFamily: 'monospace'
                                    }}>
                                        Zero
                                <div style={{
                                            marginLeft: '2%',
                                            marginRight: '2%',
                                            width: '100%',
                                            height: '100%',
                                            content: "",
                                            whiteSpace: "pre",
                                            opacity: "0.75",
                                            background: "linear-gradient(to right," + colorRange.low 
                                            + (colorRange.avg === '' ? '' : ("," + colorRange.avg))
                                            + "," + colorRange.high,
                                        }}>   </div>
                                Max
                                </div>
                                    <div style={{ fontFamily: 'monospace' }}>Avg</div>
                                </>
                                : <></>
                            }

                        </div>
                    </div>

                    <div style={{ color: 'green', fontFamily: 'Arial' }}>
                        {!this.props.stateIsSelected ? "Must select a state first before selecting above" : ""}
                    </div>

                </div>
            </div>
        );
    }
}

export default Filter;
