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

    strNumWithCommas = (num) => {
        if(num === undefined)
            return "";
        return parseInt(num).toLocaleString();
    }


    render() {
        const statusColor = { 'COMPLETED' : 'blue', 'CANCELLED' : 'red', 'QUEUED': 'black'};
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
                        <p style={{  }}>{'ID: ' + this.props.job.jobId}</p>
                        <p style={{  }}>Status: 
                            <span style={{ color: statusColor[this.props.job.jobStatus] }}>
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
                        class={this.props.job.jobStatus === 'CANCELLED' || this.props.job.jobStatus === 'COMPLETED' ? 'btn-disabled' : 'btn-primary'}
                        disabled={this.props.job.jobStatus === 'CANCELLED' || this.props.job.jobStatus === 'COMPLETED'}
                        onClick={this.cancelJob}>
                            Cancel
                        </button>
                        <button style={{ marginTop: '3%' }} class='btn-primary' onClick={() => this.props.toggleModal(this.props.job)}
                        class={this.props.job.boxPlotData === null ? 'btn-disabled' : 'btn-primary'}
                        disabled={this.props.job.boxPlotData === null}>
                            Graph
                        </button>
                    </div>
                </div>
                {this.state.toggle ?
                    <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                        <div style={colStyle}>
                            <div></div>
                            <p>{'State: ' + this.props.job.state}</p>
                            <p >{'Number of Districtings: ' + this.strNumWithCommas(this.props.job.numDistrictings)}</p>
                            <p style={{ whiteSpace: 'pre' }}>Demographics:                                  </p>
                            {this.props.job.demographicGroups.map((demoEnum) => <p style={{ color: '#438f88' }}>{demoEnum}</p> )}
                            <p >{'Compactness: ' + this.props.job.compactness}</p>
                            <p >{'Population Equation Threshold: ' + this.props.job.popEqThreshold}</p>
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
