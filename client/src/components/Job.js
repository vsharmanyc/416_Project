import React, { Component } from 'react';

class Job extends Component {

    constructor(props) {
        super(props);

        this.state = {
            toggle: false
        }
    }

    cancelJob = () => {
        fetch('http://localhost:8080/api/job/cancelJob',
            {
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                },
                method: "POST",
                body: JSON.stringify({jobId: this.props.job.jobId}),
                mode: 'cors'
            })
        .then(response => response.json())
        .then(response => this.props.updateJobs(response));
    }

    deleteJob = () => {
        fetch('http://localhost:8080/api/job/deleteJob',
            {
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                },
                method: "POST",
                body: JSON.stringify({jobId: this.props.job.jobId}),
                mode: 'cors'
            })
        .then(response => response.json())
        .then(response => this.props.updateJobs(response));
    }

    toggleDetails = (e) => {
        e.preventDefault();
        this.setState({ toggle: !this.state.toggle })
    }


    render() {

        const colStyle = { display: 'flex', flexDirection: 'column', textAlign: 'left', };
        const style = {
            display: 'flex', flexDirection: 'column',
            marginTop: '2%',
            height: '20%',
            width: '90%',
            border: 'solid',
            borderWidth: '3px',
            borderColor: '#438f88',
            overflow: 'auto'
        };

        return (
            <div style={style}>
                <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-around' }}>
                    <div style={colStyle}>
                        <p style={{ color: '#63BEB6' }}>{'ID: ' + this.props.job.jobId}</p>
                        <p style={{ color: '#63BEB6' }}>Status: 
                            <span style={{ color: 'red' }}>
                                {" " + this.props.job.jobStatus}
                            </span>
                        </p>
                        <a href="" onClick={this.toggleDetails}>{!this.state.toggle ? 'expand details' : 'see less'}</a>
                    </div>

                    <div style={colStyle}>
                        <button style={{ marginTop: '3%' }} class='btn-primary' onClick={this.deleteJob}>
                            Delete
                            </button>
                        <button style={{ marginTop: '3%' }} 
                        disabled={this.props.job.jobStatus === 'CANCELLED'}
                        class={this.props.job.jobStatus === 'CANCELLED' ? 'btn-disabled' : 'btn-primary'}
                        onClick={this.cancelJob}>
                            Cancel
                        </button>
                    </div>
                </div>
                {this.state.toggle ?
                    <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                        <div style={colStyle}>
                            <div></div>
                            <p style={{ color: '#63BEB6' }}>{'Number of Districtings: ' + this.props.job.numDistrictings}</p>
                            <p style={{ color: '#63BEB6' }}>{'Demographics: ' + this.props.job.demographicGroups}</p>
                            <p style={{ color: '#63BEB6' }}>{'Compactness: ' + this.props.job.compactness}</p>
                            <p style={{ color: '#63BEB6' }}>{'Population Equation Threshold: ' + this.props.job.popEqThreshold}</p>
                        </div>
                    </div>
                    :
                    <div></div>
                }
            </div>
        );
    }
}

export default Job;
