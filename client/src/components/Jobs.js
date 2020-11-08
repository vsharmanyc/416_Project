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
            height: '100%', 
            width: '100%', 
        };

        return (
            <div style={style}>
                {this.props.jobs.map((job) => <Job job={job}/>)}
            </div>
        );
    }
}

export default Jobs;
