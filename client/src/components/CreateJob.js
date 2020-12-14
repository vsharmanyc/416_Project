import React, { Component } from 'react';
import MultiSelect from 'react-select';
import Select from 'react-select';

class CreateJob extends Component {

    constructor(props) {
        super(props);

        this.state = {
            numDistrictings: 0,
            compactness: '',
            popDiff: '',
            popEqThres: 0,
            demographics: [],
        }
    }

    onPopEqThresChange = (e) => { this.setState({ popEqThres: e.target.value }); }
    onNumDistrictingsChange = (e) => { this.setState({ numDistrictings: e.target.value }); }
    onCompactnessChange = (selected) => { this.setState({ compactness: selected }); }
    onDemographicsSelect = (selected) => { this.setState({ demographics: selected }); }

    onCreateJob = (e) => {
        e.preventDefault();
        let demographics = [];
        this.state.demographics.map((option) => demographics.push(this.getDemographicEnums(option.value)));
        let states = {'New York': 'NY', 'Pennsylvania': 'PA', 'Maryland': 'MD'}

        let job = {
            state: states[this.props.state],
            numDistrictings: this.state.numDistrictings,
            demographicGroups: demographics,
            popEqThreshold: this.state.popEqThres,
            compactness: this.state.compactness.value,
        };

        this.postReqCreateJob(job);
        this.addJob(job);
        this.clearForm();
    }


    postReqCreateJob = (job) => {
        fetch('http://localhost:8080/api/job/createJob',
            {
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                },
                method: "POST",
                body: JSON.stringify(job),
                mode: 'cors'
            })
            .then(response => response.json())
            .then(response => this.props.updateJobs(response));
    }

    addJob = (job) => {
        let jobs = this.props.jobs;
        jobs.push(job);
        this.props.updateJobs(jobs);
    }

    getDemographicEnums = (demographic) => {
        switch (demographic) {
            case 'White':
                return 'WHITE';
            case 'Black':
                return 'AFRICAN_AMERICAN';
            case 'Asian':
                return 'ASIAN';
            case 'Hispanic or Latino':
                return 'HISPANIC_LATINO';
            case 'American Indian or Alaska Native':
                return 'AM_INDIAN_AK_NATIVE';
            case 'Native Hawaiian or Other Pacific Islander':
                return 'NH_OR_OPI';
            default:
                return 'OTHER'
        }
    }

    clearForm = () => {
        this.setState({
            numDistrictings: 0,
            compactness: '',
            popDiff: '',
            popEqThres: 0.0,
            demographics: [],
        })
    }

    render() {
        const demographics = [
            { value: 'White', label: 'White' },
            { value: 'Black', label: 'Black' },
            { value: 'Asian', label: 'Asian' },
            { value: 'Hispanic or Latino', label: 'Hispanic or Latino' },
            { value: 'American Indian or Alaska Native', label: 'American Indian or Alaska Native' },
            { value: 'Native Hawaiian or Other Pacific Islander', label: 'Native Hawaiian or Other Pacific Islander' },
            { value: 'Other', label: 'Other' }
        ];

        const compactness = [
            { value: 'Slightly Compact', label: 'Slightly Compact' },
            { value: 'Somewhat Compact', label: 'Somewhat Compact' },
            { value: 'Highly Compact', label: 'Highly Compact' },
        ];

        const formStyle = {
            height: '100%', width: '95%',
            backgroundColor: '#ebf3f5', padding: '5%', borderRadius: '3%', marginTop: '2%'
        };

        let disableSubmit = this.state.demographics === null || this.state.demographics.length === 0 || this.numDistrictings <= 0.0 || this.state.popEqThres <= 0 || !this.props.stateIsSelected;

        return (
            <div style={{ display: 'flex', justifyContent: 'center', textAlign: 'start'}}>
                <div style={formStyle}>
                    <form>
                        <div class="form-group">
                            <label for="number-input1 row">Number of Districtings</label>
                            <input class="form-control" type="number" defaultValue="0" value={this.state.numDistrictings}
                                min="1" id="number-input1" disabled={!this.props.stateIsSelected}
                                onChange={this.onNumDistrictingsChange} />
                        </div>
                        <div class="form-group">
                            <label for="FormControlSelect2">Select Demographics</label>
                            <div style={{ textAlign: 'start' }}>
                                <MultiSelect value={this.state.demographics} onChange={this.onDemographicsSelect} isMulti options={demographics} isDisabled={!this.props.stateIsSelected} />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="FormControlSelect2">Compactness</label>
                            <Select options={compactness} value={this.state.compactness} onChange={this.onCompactnessChange} isDisabled={!this.props.stateIsSelected} />
                        </div>
                        <div class="form-group">
                            <label for="number-input2 row">Population Equation Threshold</label>
                            <input onChange={this.onPopEqThresChange} class="form-control" type="number" defaultValue="0" value={this.state.popEqThres} disabled={!this.props.stateIsSelected}
                                step="0.01" min="0" id="number-input2" />
                        </div>
                        <button onClick={this.onCreateJob} style={{ backgroundColor: '#63BEB6', color: '#ffffff' }} type="submit" class="btn" disabled={disableSubmit}>Submit</button>
                    </form>

                    <strong style={{ fontFamily: 'Arial', background: 'white' }}>
                        {!this.props.stateIsSelected ? "Must select a state before creating a job" : ""}
                    </strong>
                </div>
            </div>
        );
    }
}

export default CreateJob;
