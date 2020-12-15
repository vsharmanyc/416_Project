import React, { Component } from 'react';
import { Form } from 'react-bootstrap';
import Select from 'react-select';
const clonedeep = require('lodash/cloneDeep')


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
        let filter = clonedeep(this.props.filter);
        filter[level] = !filter[level];
        this.props.updateFilter(filter);
    }

    updateHeatMap = (selected) => {
        let filter = clonedeep(this.props.filter);
        filter.Heatmap.show = selected.value !== 'NONE';
        filter.Heatmap.popType = selected;
        if (selected.value === 'TOTVAP')
            filter.Heatmap.colorRange = { low: '#e6f0ee', avg: '', high: '#006952' };
        else
            filter.Heatmap.colorRange = { low: '#0015d6', avg: 'white', high: '#d60000' };
        this.props.updateFilter(filter);
    }

    updateDistrictingFilter = (key, val) => {
        let filter = clonedeep(this.props.filter);
        filter.Districting[key] = val;
        if (key === 'job') {
            filter.Districting.jobObj = this.props.jobs.find(job => job.jobId === parseInt(val.value));
            console.log(filter.Districting['jobObj']);
            filter.Districting.importStatus = '';
            filter.Districting.random = false;
            filter.Districting.avg = false;
            filter.Districting.extreme = false;
        }
        this.props.updateFilter(filter);
    }

    stateInitials = (stateName) => {
        if (stateName === 'Maryland')
            return 'MD';
        else if (stateName === 'New York')
            return 'NY';
        else if (stateName === 'Pennsylvania')
            return 'PA';
        return stateName;
    }

    render() {
        const demographics = [
            { value: 'NONE', label: 'Select...' },
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

        let jobs = this.props.jobs.filter((job) => (job.jobStatus === 'COMPLETED' && job.state === this.stateInitials(this.props.state)));
        jobs = jobs.map((job) => ({ value: job.jobId, label: "Job " + job.jobId }));
        jobs.unshift({ value: 'Select...', label: 'Select...' });

        return (
            <div style={{ display: 'flex', justifyContent: 'center', textAlign: 'start' }}>
                <div style={formStyle}>
                    <h6>Boundaries</h6>
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

                    <h6>Heat Map</h6>
                    <div style={{ paddingBottom: '5%' }}>
                        <Select options={demographics} value={this.props.filter.Heatmap.popType}
                            onChange={this.updateHeatMap} isDisabled={!this.props.stateIsSelected} />
                    </div>

                    <h6>{`${this.props.state !== 'Select...' ? this.stateInitials(this.props.state) : ''} Districtings`}</h6>
                    <div style={{ textAlign: 'start' }}>
                        <Select options={jobs} value={this.props.filter.Districting.job} onChange={(selected) => this.updateDistrictingFilter('job', selected)}
                            isDisabled={!this.props.stateIsSelected || jobs.length === 0} />

                        {this.props.filter.Districting.importStatus !== '' ?
                            <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
                                <Form.Check
                                    onClick={() => this.updateDistrictingFilter('random', !this.props.filter.Districting.random)}
                                    checked={this.props.filter.Districting.random}
                                    disabled={!this.props.stateIsSelected}
                                    type="checkbox"
                                    id="random check"
                                    label="Random"
                                />
                                <Form.Check
                                    onClick={() => this.updateDistrictingFilter('avg', !this.props.filter.Districting.avg)}
                                    checked={this.props.filter.Districting.avg}
                                    disabled={!this.props.stateIsSelected}
                                    type="checkbox"
                                    id="avg check"
                                    label="Average"
                                />
                                <Form.Check
                                    onClick={() => this.updateDistrictingFilter('extreme', !this.props.filter.Districting.extreme)}
                                    checked={this.props.filter.Districting.extreme}
                                    disabled={!this.props.stateIsSelected}
                                    type="checkbox"
                                    id="extreme check"
                                    label="Extreme"
                                />
                            </div>
                            : <></>
                        }
                    </div>

                    <strong style={{ fontFamily: 'Arial', background: 'white' }}>
                        {!this.props.stateIsSelected ? "Must select a state first before selecting above" : ""}
                    </strong>

                </div>
            </div>
        );
    }
}

export default Filter;
