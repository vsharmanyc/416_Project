import React, { Component } from 'react';
import { Button, Typography } from '@material-ui/core/';

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
        let dataPoints = [];
        if (this.state.graphCriteria.party.use)
            dataPoints.push('Political Parties');
        if (this.state.graphCriteria.race.use)
            dataPoints.push('Race');
        if (this.state.graphCriteria.population.use)
            dataPoints.push('Population');

        return (
            <div>
                <img
                    style={{ height: '75%', width: '75%' }}
                    src='https://clipartstation.com/wp-content/uploads/2018/10/line-graph-clipart-1.jpg'
                />
                <div>
                    <div style={{ display: 'flex', flexDirection: 'row' }}>
                        <Typography variant='h6'>Years: </Typography>
                        <div style={{ display: 'flex', flexDirection: 'row', marginLeft: '2%', alignItems: 'flex-end' }}>
                            {this.state.graphCriteria.year.value.length !== 0 ?
                                this.state.graphCriteria.year.value.map((year) => {
                                    return (
                                        <Typography style={{ whiteSpace: 'pre-wrap' }} variant='subtitle1'>{year + '   '}</Typography>
                                    );
                                })
                                :
                                <Typography variant='subtitle1'> None Selected</Typography>
                            }
                        </div>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'row' }}>
                        <Typography variant='h6'>States: </Typography>
                        <div style={{ display: 'flex', flexDirection: 'row', marginLeft: '2%', alignItems: 'flex-end' }}>
                            {this.state.graphCriteria.state.value.length !== 0 ?
                                this.state.graphCriteria.state.value.map((state) => {
                                    return (
                                        <Typography style={{ whiteSpace: 'pre-wrap' }} variant='subtitle1'>{state + '   '}</Typography>
                                    );
                                })
                                :
                                <Typography variant='subtitle1'>None Selected</Typography>
                            }
                        </div>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'row' }}>
                        <Typography variant='h6'>Data Points: </Typography>
                        <div style={{ display: 'flex', flexDirection: 'row', marginLeft: '2%', alignItems: 'flex-end' }}>
                            {dataPoints.length !== 0 ?
                                dataPoints.map((dataPoint) => {
                                    return (
                                        <Typography style={{ whiteSpace: 'pre-wrap' }} variant='subtitle1'>{dataPoint + '   '}</Typography>
                                    );
                                })
                                :
                                <Typography variant='subtitle1'>None Selected</Typography>
                            }
                        </div>
                    </div>

                </div>
            </div>
        );
    }
}

export default Graph;