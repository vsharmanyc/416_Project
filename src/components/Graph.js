import React, { Component } from 'react';

class Graph extends Component {
    constructor(props) {
        super(props);
        this.state = {
            graphCriteria: props.graphCriteria
        };
    }

    getformattedGraphData = () => {
        let data = 'Data:';
        data += '\nVoting Year(s): ' + this.state.graphCriteria.year.value;
        data += '\nStates: ' + this.state.graphCriteria.state.value;
        data += '\nData Points: '
        if (this.state.graphCriteria.party.use)
            data += ' Political Parties,';
        if (this.state.graphCriteria.race.use)
            data += ' Race,';
        if (this.state.graphCriteria.population.use)
            data += ' Population'
        return data;
    }

    render() {
        const checkboxStyle = { color: "#63BEB6" }
        return (
            <div style={{ width: '100%', height: '100%', display:'flex', flexDirection: 'column', backgroundColor: 'white' }}>
                <img 
                    style={{height: '80%', width: '90%'}}
                    src='https://clipartstation.com/wp-content/uploads/2018/10/line-graph-clipart-1.jpg'
                />
                {this.getformattedGraphData()}
            </div>
        );
    }
}

export default Graph;