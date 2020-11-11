import React, { Component } from 'react';
import MultiSelect from 'react-select';

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
    onCompactnessChange = (e) => { this.setState({ compactness: e.target.value }); }
    onPopDiffChange = (e) => { this.setState({ popDiff: e.target.value }); }
    onDemographicsSelect = (selected) => { this.setState({ demographics: selected }); }

    onCreateJob = (e) => {
        e.preventDefault();
        let demographics = [];
        this.state.demographics.map((option) => demographics.push(option.value) );

        let compactness =  'low';
        if(this.state.compactness >=35 && this.state.compactness <=75)
            compactness = 'somewhat';
        else if(this.state.compactness > 75)
            compactness = 'high'; 

        let job = {
            jobID: new Date().getTime(),
            demographics: demographics,
            numDistrictings: this.state.numDistrictings,
            compactness: compactness,
            populationDifference:  this.state.popDiff,
            popEqThres: this.state.popEqThres,
            status: 'pending'
        };

        this.postReqCreateJob(job);
        this.addJob(job);
        this.clearForm();
    }


    postReqCreateJob = (variable) => {
        console.log('good shit');

        fetch('http://localhost:8080/api/job/createJob',
            {
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                },
                method: "POST",
                body: JSON.stringify( {
                    "numDistrictings": 20000,
                    "demographicGroups": [
                    "AFRICAN_AMERICAN",
                    "ASIAN",
                    "HISPANIC_LATINO"],
                    "popEqThreshold": 0.98,
                    "compactness": "Somewhat Compact"
                } ),
                mode: 'cors'
            })
            .then(response => response.json())
            .then(response => console.log(response));
    }

    addJob = (job) =>{
        let jobs = this.props.jobs;
        jobs.push(job);
        this.props.updateJobs(jobs);
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
            { value: 'Hispanic or Latino', label: 'Hispanic or Latino'},
            { value: 'American Indian or Alaska Native', label: 'American Indian or Alaska Native'},
            { value: 'Native Hawaiian or Other Pacific Islander', label: 'Native Hawaiian or Other Pacific Islander'},
            { value: 'Two or More Races', label: 'Two or More Races'},
            { value: 'Other', label: 'Other'}
        ]

        const formStyle = {
            height: '100%', width: '95%',
            backgroundColor: '#ebf3f5', padding: '5%', borderRadius: '3%', marginTop: '2%'
        };

        return (
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div style={formStyle}>
                    <form>
                        <div class="form-group">
                            <label for="number-input1 row">Number of Districtings</label>
                            <input class="form-control" type="number" defaultValue="0" value={this.state.numDistrictings}
                            min="1" id="number-input1"
                                onChange={this.onNumDistrictingsChange} />
                        </div>
                        <div class="form-group">
                            <label for="FormControlSelect2">Select Demographics</label>
                            <MultiSelect  value={this.state.demographics} onChange={this.onDemographicsSelect} isMulti options={demographics}/>
                        </div>
                        <div class="form-group">
                            <label for="customRange1">Compactness</label>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                Least Compact
                                <input type="range" class="custom-range" id="customRange1" value={this.state.compactness} onChange={this.onCompactnessChange} />
                                Most Compact
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="customRange2">Population Difference</label>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                Least Difference
                                <input type="range" class="custom-range" id="customRange2" value={this.state.popDiff} onChange={this.onPopDiffChange} />
                                Most Difference
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="number-input2 row">Population Equation Threshold</label>
                            <input onChange={this.onPopEqThresChange} class="form-control" type="number" defaultValue="0" value={this.state.popEqThres}
                                step="0.01" min="0" id="number-input2" />
                        </div>
                        <button onClick={this.onCreateJob} style={{ backgroundColor: '#63BEB6', color: '#ffffff' }} type="submit" class="btn">Submit</button>
                    </form>
                </div>
            </div>
        );
    }
}

export default CreateJob;
