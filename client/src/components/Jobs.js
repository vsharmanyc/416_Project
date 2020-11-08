import React, { Component } from 'react';
import Job from './Job'

class Jobs extends Component {

    constructor(props) {
        super(props);
    }


    render() {
        const style = { 
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            flexDirection: 'column',
            height: '100%', 
            width: '100%', 
        };

        return (
            <div style={style}>
                {this.props.jobs.map((job) => <Job updateJobs={this.props.updateJobs} jobs={this.props.jobs} job={job}/>)}
            </div>
        );
    }
}

export default Jobs;
