import React, { Component } from 'react';

class Job extends Component {

    constructor(props) {
        super(props);
    }

    cancelJob = () => {
        let jobs = this.props.jobs;
        for (let i = 0; i < jobs.length; i++) {
            if (jobs[i].jobID === this.props.job.jobID)
                jobs[i].status = 'cancelled';
        }
        this.props.updateJobs(jobs);
    }

    deleteJob = () => {
        let jobs = this.props.jobs;
        for (let i = 0; i < jobs.length; i++) {
            if (jobs[i].jobID === this.props.job.jobID)
                jobs.splice(i, 1);
        }
        this.props.updateJobs(jobs);
    }


    render() {

        const colStyle = { display: 'flex', flexDirection: 'col', justifyContent: 'center', alignContent: 'center' };
        const style = {
            marginTop: '2%',
            height: '20%',
            width: '90%',
           // backgroundColor: '#f7ffff',
            border: 'solid',
            borderWidth: '3px',
            borderColor: '#438f88',
        };

        return (
            <div style={style}>
                <div style={colStyle}>
                    <div>
                        <p style={{ color: '#63BEB6' }}>{'ID: ' + this.props.job.jobID}</p>
                        <p style={{ color: '#63BEB6' }}>{'Status: ' + this.props.job.status}</p>
                        <p style={{ color: '#63BEB6' }}>{'Number of Districtings: ' + this.props.job.numDistrictings}</p>
                        <p style={{ color: '#63BEB6' }}>{'Demographics: ' + this.props.job.demographics}</p>
                        <p style={{ color: '#63BEB6' }}>{'Compactness: ' + this.props.job.compactness}</p>
                        <p style={{ color: '#63BEB6' }}>{'Population Difference ' + this.props.job.populationDifference}</p>
                        <p style={{ color: '#63BEB6' }}>{'Population Equation Threshold: ' + this.props.job.popEqThres}</p>
                    </div>
                    <div style={colStyle}>
                        <div>
                            <button style={{marginTop: '3%'}} class='btn-primary' onClick={this.deleteJob}>
                                Delete
                            </button>
                            <button style={{marginTop: '3%'}} class="btn-primary" onClick={this.cancelJob}>
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Job;
