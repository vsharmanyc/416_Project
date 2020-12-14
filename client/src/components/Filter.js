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

        const boxStyle = { width: '8%', height: '100%', border: 'solid' };

        const colorRange = this.props.filter.Heatmap.colorRange;

        let jobs = this.props.jobs.filter((job) => (job.jobStatus === 'COMPLETED' && job.state === this.stateInitials(this.props.state)));
        jobs = jobs.map((job) => ({ value: job.jobId, label: "Job " + job.jobId }));
        jobs.unshift({ value: 'Select...', label: 'Select...' });

        let percentages = [];
        for (let i = 0; i <= 100; i += 10)
            percentages.push(<div style={{ fontFamily: 'monospace' }}>{i + '%'}</div>);
        let normRange = [];
        for (let i = 1; i <= 11; i++)
            normRange.push(<div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: i / 11 * .75, background: colorRange.high }} /> {percentages[i - 1]}</div>);


        return (
            <div style={{ display: 'flex', justifyContent: 'center', textAlign: 'start' }}>
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
                    <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', paddingBottom: this.props.filter.Heatmap.show ? '10%' : '5%' }}>
                        <div style={{ display: 'flex', flexGrow: 3, flexDirection: 'column' }}>

                            <div style={{ textAlign: 'start' }}>
                                <Select options={demographics} value={this.props.filter.Heatmap.popType}
                                    onChange={this.updateHeatMap} isDisabled={!this.props.stateIsSelected} />
                            </div>

                            {this.props.filter.Heatmap.show ?
                                <div style={{ width: '100%', height: '25%', marginTop: '2%' }}>
                                    <div style={{ fontFamily: 'sans-serif', fontSize: '85%', textAlign: 'center' }}>Population Color Range</div>
                                    <div style={{
                                        marginTop: '1%',
                                        width: '100%',
                                        height: '100%',
                                        fontFamily: 'monospace'
                                    }}>
                                        <div style={{
                                            display: 'flex',
                                            flexDirection: 'row',
                                            justifyContent: 'space-between',
                                            width: '100%',
                                            height: '100%',
                                        }}>
                                            {colorRange.avg !== '' ?
                                                <>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 1.0 * .75, background: colorRange.low }} /> min </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.8 * .75, background: colorRange.low }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.6 * .75, background: colorRange.low }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.4 * .75, background: colorRange.low }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.2 * .75, background: colorRange.low }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 1.0 * .75, background: colorRange.avg }} /> avg </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.2 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.4 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.6 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 0.8 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 1.0 * .75, background: colorRange.high }} /> max </div>
                                                </>
                                                :
                                                <>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 1 / 11 * .75, background: colorRange.high }} /> min </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 2 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 3 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 4 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 5 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 6 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 7 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 8 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 9 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 10 / 11 * .75, background: colorRange.high }} /> </div>
                                                    <div style={boxStyle}> <div style={{ width: '100%', height: '100%', opacity: 11 / 11 * .75, background: colorRange.high }} /> max </div>
                                                </>
                                            }
                                        </div>
                                    </div>

                                </div>
                                : <></>
                            }



                        </div>
                    </div>

                    <h6>{`Display ${this.props.state !== 'Select...' ? this.stateInitials(this.props.state) : ''} Districtings`}</h6>
                    <div style={{ textAlign: 'start' }}>
                        <Select options={jobs} value={this.props.filter.Districting.job} onChange={(selected) => this.updateDistrictingFilter('job', selected)}
                            isDisabled={!this.props.stateIsSelected || jobs.length === 0} />

                        { this.props.filter.Districting.importStatus !== '' ? 
                        <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
                            <div>
                                <Form.Check
                                    onClick={() => this.updateDistrictingFilter('random', !this.props.filter.Districting.random)}
                                    checked={this.props.filter.Districting.random}
                                    disabled={!this.props.stateIsSelected}
                                    type="checkbox"
                                    id="random check"
                                    label="Random"
                                />
                                <div style={{ background: this.props.filter.Districting.color.random, width: '100%', height: '20%', border: 'solid', opacity: this.props.stateIsSelected ? 1 : 0 }} />
                            </div>
                            <div>
                                <Form.Check
                                    onClick={() => this.updateDistrictingFilter('avg', !this.props.filter.Districting.avg)}
                                    checked={this.props.filter.Districting.avg}
                                    disabled={!this.props.stateIsSelected}
                                    type="checkbox"
                                    id="avg check"
                                    label="Average"
                                />
                                <div style={{ background: this.props.filter.Districting.color.avg, width: '100%', height: '20%', border: 'solid', opacity: this.props.stateIsSelected ? 1 : 0 }} />
                            </div>
                            <div>
                                <Form.Check
                                    onClick={() => this.updateDistrictingFilter('extreme', !this.props.filter.Districting.extreme)}
                                    checked={this.props.filter.Districting.extreme}
                                    disabled={!this.props.stateIsSelected}
                                    type="checkbox"
                                    id="extreme check"
                                    label="Extreme"
                                />
                                <div style={{ background: this.props.filter.Districting.color.extreme, width: '100%', height: '20%', border: 'solid', opacity: this.props.stateIsSelected ? 1 : 0 }} />
                            </div>
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
