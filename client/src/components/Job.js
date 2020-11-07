import React, { Component } from 'react';

class Job extends Component {

    constructor(props) {
        super(props);
    }


    render() {
        const style = {
            marginTop: '2%', 
            height: '20%', 
            width: '90%', 
            backgroundColor: 'white', 
            border: 'solid',
            borderWidth: '3px',
            borderColor: '#63BEB6',
        };

        return (
            <div style={style}>
                <p style={{color: '#63BEB6' }}>{'ID: ' + this.props.job.jobID}</p>
                <p style={{color: '#63BEB6' }}>{'Compactness: ' + this.props.job.compactness}</p>
                <p style={{color: '#63BEB6' }}>{'Status: ' + this.props.job.status}</p>
            </div>
        );
    }
}

export default Job;
