import React, { Component } from 'react';
import {
    Button, InputLabel, Select, MenuItem, FormControl,
    FormGroup, FormLabel, FormControlLabel, Checkbox, Typography
} from '@material-ui/core/';
import Graph from './Graph';
import CloseIcon from '@material-ui/icons/Close';

class GraphTab extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: window.innerWidth,
            height: window.innerHeight,
            showGraphModal: false,
            graphCriteria: {
                year: { use: true, value: [] },
                state: { use: true, value: [] },
                population: { use: false },
                party: { use: false, value: '' },
                race: { use: false, value: '' },
            }
        };
    }

    componentDidMount() {
        window.addEventListener('resize', this.updateWindowDimensions);
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.updateWindowDimensions);
    }

    updateWindowDimensions = () => {
        this.setState({ width: window.innerWidth, height: window.innerHeight }, this.forceUpdate);
    }

    updateGraphCriteria = (graph, subGraph, graphValue) => {
        let graphCriteria = this.state.graphCriteria;
        graphCriteria[graph][subGraph] = graphValue;
        this.setState({ graphCriteria: graphCriteria });
    }

    resetGraphCriteria = () => {
        this.setState({
            graphCriteria: {
                year: { use: true, value: [] },
                state: { use: true, value: [] },
                population: { use: false },
                party: { use: false, value: '' },
                race: { use: false, value: '' },
            },
            showGraphModal: false
        })
    }

    render() {
        const checkboxStyle = { color: "#63BEB6" }
        const unsetGraphs = {
            year: { use: true, value: [] },
            state: { use: true, value: [] },
            population: { use: false },
            party: { use: false, value: '' },
            race: { use: false, value: '' },
        };

        return (
            <div>
                <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>

                    <div style={{ display: 'flex', flexDirection: 'column', width: '90%', backgroundColor: '#ededed', borderRadius: '5%', marginTop: '5%', padding: '2%' }}>
                        <Typography variant="subtitle1" component="h2">Pick A Voting Year:</Typography>
                        <FormControl margin='dense' style={{ display: 'flex', flexDirection: 'row', minWidth: this.state.width * .02, maxWidth: this.state.width * .02 }}>
                            <FormControl style={{ marginLeft: '50%', minWidth: this.state.width * .15, maxWidth: this.state.width * .15 }}>
                                <InputLabel id="demo-simple-select-label">Voting Year</InputLabel>
                                <Select
                                    multiple
                                    labelId="demo-simple-select-label"
                                    id="demo-simple-select"
                                    value={this.state.graphCriteria.year.value}
                                    onChange={(e) => { this.updateGraphCriteria('year', 'value', e.target.value) }}
                                >
                                    <MenuItem value='2016'>2016</MenuItem>
                                    <MenuItem value='2012'>2012</MenuItem>
                                    <MenuItem value='2008'>2008</MenuItem>
                                </Select>
                            </FormControl>
                        </FormControl>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', width: '90%', backgroundColor: '#ededed', borderRadius: '5%', marginTop: '5%', padding: '2%' }}>
                        <Typography variant="subtitle1" component="h2">Pick States:</Typography>
                        <FormControl margin='dense' style={{ display: 'flex', flexDirection: 'row', minWidth: this.state.width * .02, maxWidth: this.state.width * .02 }}>
                            <FormControl style={{ marginLeft: '50%', minWidth: this.state.width * .15, maxWidth: this.state.width * .15 }}>
                                <InputLabel id="demo-simple-select-label">States</InputLabel>
                                <Select
                                    labelId="demo-simple-select-label"
                                    multiple
                                    id="demo-simple-select"
                                    value={this.state.graphCriteria.state.value}
                                    onChange={(e) => { console.log(e.target.value); this.updateGraphCriteria('state', 'value', e.target.value) }}
                                >
                                    <MenuItem value='New York'>New York</MenuItem>
                                    <MenuItem value='Pennsylvania'>Pennsylvania</MenuItem>
                                    <MenuItem value='Maryland'>Maryland</MenuItem>
                                </Select>
                            </FormControl>
                        </FormControl>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', width: '90%', backgroundColor: '#ededed', borderRadius: '5%', marginTop: '5%', padding: '2%' }}>
                        <FormControl component="fieldset">
                            <FormLabel component="legend">Select Data Points</FormLabel>
                            <FormGroup>
                                <FormControlLabel
                                    control={<Checkbox style={checkboxStyle}
                                        checked={this.state.graphCriteria.party.use}
                                        onChange={() => { this.updateGraphCriteria('party', 'use', !this.state.graphCriteria.party.use) }} />}
                                    label="Political Parties"
                                />
                                <FormControlLabel
                                    control={<Checkbox style={checkboxStyle}
                                        checked={this.state.graphCriteria.race.use}
                                        onChange={() => { this.updateGraphCriteria('race', 'use', !this.state.graphCriteria.race.use) }} />}
                                    label="Race"
                                />
                                <FormControlLabel
                                    control={<Checkbox style={checkboxStyle}
                                        checked={this.state.graphCriteria.population.use}
                                        onChange={() => { this.updateGraphCriteria('population', 'use', !this.state.graphCriteria.population.use) }} />}
                                    label="Population"
                                />
                            </FormGroup>
                        </FormControl>
                    </div>
                    <div style={{ display: 'flex', flexDirection: 'row', marginTop: '5%' }}>
                        <Button variant="contained" disabled={JSON.stringify(this.state.graphCriteria) == JSON.stringify(unsetGraphs)}
                            onClick={this.resetGraphCriteria} >
                            Reset Fields
                    </Button>
                        <Button variant="contained"
                            disabled={JSON.stringify(this.state.graphCriteria) == JSON.stringify(unsetGraphs)}
                            style={{ backgroundColor: '#63BEB6' }} onClick={() => { this.setState({ showGraphModal: true }) }}>
                            Make Graph
                    </Button>
                    </div>
                </div>

                {this.state.showGraphModal ?

                    <div style={{
                        position: 'fixed',
                        left: '25%',
                        top: '25%',
                        zIndex: 100,
                        backgroundColor: 'white',
                        height: '48%',
                        width: '48%',
                        display: 'flex',
                        justifyContent: 'center',
                        padding: '1%',
                        borderStyle: 'solid'
                    }}>
                        <div><Graph graphCriteria={this.state.graphCriteria}></Graph></div>
                        <Button variant="contained"
                            onClick={() => { this.setState({ showGraphModal: false }) }}
                            style={{ position: 'absolute', backgroundColor: '#63BEB6', width: '5%', height: '10%', top: '2%', left: '2%' }}
                        >
                            <CloseIcon />
                        </Button>
                    </div>
                    : <></>
                }

            </div>
        );
    }
}

export default GraphTab;