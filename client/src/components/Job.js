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
                body: JSON.stringify({ jobId: this.props.job.jobId }),
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
                body: JSON.stringify({ jobId: this.props.job.jobId }),
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
        if (num === undefined)
            return "";
        return parseInt(num).toLocaleString();
    }


    render() {
        const statusColor = { 'COMPLETED': 'blue', 'CANCELLED': 'red', 'QUEUED': 'black' };
        const colStyle = { textAlign: 'left', };
        const style = {
            position: 'relative',
            marginTop: '2%',
            height: '20%',
            width: '90%',
            border: 'solid',
            borderWidth: '3px',
            borderColor: '#438f88',
            background: 'white',
            overflow: 'auto',
            padding: '2%',
        };

        return (
            <div style={style}>
                <div style={{  textAlign: 'left' }}>
                    <p>{'ID: ' + this.props.job.jobId}</p>
                    <p>{'State: ' + this.props.job.state}</p>
                    <p>Status:
                            <span style={{ color: statusColor[this.props.job.jobStatus] }}>
                            {" " + this.props.job.jobStatus}
                        </span>
                    </p>

                    <a href="" onClick={this.toggleDetails}>{!this.state.toggle ? 'expand details' : 'see less'}</a>

                    {this.state.toggle ?
                        <>
                            <div></div>
                            <p >{'Number of Districtings: ' + this.strNumWithCommas(this.props.job.numDistrictings)}</p>
                            <p>Demographics:</p>
                            <ul>{this.props.job.demographicGroups.map((demoEnum) => <li>{demoEnum}</li>)}</ul>
                            <p >{'Compactness: ' + this.props.job.compactness}</p>
                            <p >{'Population Equation Threshold: ' + this.props.job.popEqThreshold}</p>
                        </>
                        :
                        <></>
                    }
                </div>

                <div style={{ position: 'absolute', top: '0px', right: '0px', width: '100%', height: '100%', pointerEvents: 'none' }}>
                    <div style={{ width: '25%', height: '15%', float: 'right', pointerEvents: 'all', textAlign: 'start' }}>
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

            </div>
        );
    }
}

export default Job;
