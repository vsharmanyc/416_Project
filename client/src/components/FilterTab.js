import React, { Component } from 'react';
import { Button, InputLabel, Select, MenuItem, FormControl, FormControlLabel, Checkbox, Typography } from '@material-ui/core/';

class FilterTab extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: window.innerWidth,
            height: window.innerHeight,
            filterCriteria: {
                state: { use: true, value: '' },
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

    updateFilterCriteria = (filter, subFilter, filterValue) => {
        let filterCriteria = this.state.filterCriteria;
        filterCriteria[filter][subFilter] = filterValue;
        this.setState({ filterCriteria: filterCriteria });
    }

    resetFilterCriteria = () => {
        this.setState({
            filterCriteria: {
                state: { use: true, value: '' },
                population: { use: false },
                party: { use: false, value: '' },
                race: { use: false, value: '' },
            }
        })
    }

    render() {
        const checkboxStyle = { color: "#63BEB6" }
        const unsetFilters = {
            state: { use: true, value: '' },
            population: { use: false },
            party: { use: false, value: '' },
            race: { use: false, value: '' },
        };

        return (
            <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>

                <div style={{ display: 'flex', flexDirection: 'column', width: '90%', backgroundColor: '#ededed', borderRadius: '5%', marginTop: '5%', padding: '2%' }}>
                    <Typography variant="subtitle1" component="h2">Pick A State:</Typography>
                    <FormControl margin='dense' style={{ display: 'flex', flexDirection: 'row', minWidth: this.state.width * .02, maxWidth: this.state.width * .02 }}>
                        <FormControl style={{ marginLeft: '50%', minWidth: this.state.width * .15, maxWidth: this.state.width * .15 }}>
                            <InputLabel id="demo-simple-select-label">State</InputLabel>
                            <Select
                                labelId="demo-simple-select-label"
                                id="demo-simple-select"
                                value={this.state.filterCriteria.state.value}
                                onChange={(e) => { this.updateFilterCriteria('state', 'value', e.target.value) }}
                            >
                                <MenuItem value='New York'>New York</MenuItem>
                                <MenuItem value='Pennsylvania'>Pennsylvania</MenuItem>
                                <MenuItem value='Maryland'>Maryland</MenuItem>
                            </Select>
                        </FormControl>
                    </FormControl>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', width: '90%', backgroundColor: '#ededed', borderRadius: '5%', marginTop: '5%', padding: '2%' }}>
                    <Typography variant="subtitle1" component="h2">Pick One:</Typography>
                    <FormControl margin='dense' style={{ display: 'flex', flexDirection: 'row', minWidth: this.state.width * .02, maxWidth: this.state.width * .02 }}>
                        <Checkbox checked={this.state.filterCriteria.party.use}
                            style={checkboxStyle}
                            onChange={() => { this.updateFilterCriteria('party', 'use', !this.state.filterCriteria.party.use) }} />
                        <FormControl style={{ minWidth: this.state.width * .15, maxWidth: this.state.width * .15 }}>
                            <InputLabel id="demo-simple-select-label">Political Party</InputLabel>
                            <Select
                                labelId="demo-simple-select-label"
                                id="demo-simple-select"
                                value={this.state.filterCriteria.party.value}
                                onChange={(e) => { this.updateFilterCriteria('party', 'value', e.target.value) }}
                            >
                                <MenuItem value='Democrat'>Democrat</MenuItem>
                                <MenuItem value='Republican'>Republican</MenuItem>
                                <MenuItem value='Libertarian'>Libertarian</MenuItem>
                                <MenuItem value='Green Party'>Green Party</MenuItem>
                                <MenuItem value='Unaffiliated'>Unaffiliated</MenuItem>
                            </Select>
                        </FormControl>
                    </FormControl>

                    <FormControl margin='dense' style={{ display: 'flex', flexDirection: 'row', minWidth: this.state.width * .02, maxWidth: this.state.width * .02 }}>
                        <Checkbox checked={this.state.filterCriteria.race.use}
                            style={checkboxStyle}
                            onChange={() => { this.updateFilterCriteria('race', 'use', !this.state.filterCriteria.race.use) }} />
                        <FormControl style={{ minWidth: this.state.width * .15, maxWidth: this.state.width * .15 }}>
                            <InputLabel id="demo-simple-select-label">Race</InputLabel>
                            <Select
                                overflow='auto'
                                labelId="demo-simple-select-label"
                                id="demo-simple-select"
                                value={this.state.filterCriteria.race.value}
                                onChange={(e) => { this.updateFilterCriteria('race', 'value', e.target.value) }}
                            >
                                <MenuItem value='Black'>Black</MenuItem>
                                <MenuItem value='White'>White</MenuItem>
                                <MenuItem value='Native American and Alaska Native'>Native American and Alaska Native</MenuItem>
                                <MenuItem value='Native Hawaiian and Pacific Islander'>Native Hawaiian and Pacific Islander</MenuItem>
                                <MenuItem value='Asian'>Asian</MenuItem>
                                <MenuItem value='Hispanic'>Hispanic</MenuItem>
                            </Select>
                        </FormControl>
                    </FormControl>

                    <FormControl margin='dense' style={{ display: 'flex', flexDirection: 'row', }}>
                        <Checkbox checked={this.state.filterCriteria.population.use} style={checkboxStyle}
                            onChange={() => { this.updateFilterCriteria('population', 'use', !this.state.filterCriteria.population.use) }} />
                        <Typography variant="subtitle1" component="h2" style={{ paddingTop: '2.5%' }}>Population</Typography>
                    </FormControl>
                </div>
                <div style={{ display: 'flex', flexDirection: 'row', marginTop: '5%' }}>
                    <Button variant="contained" disabled={JSON.stringify(this.state.filterCriteria) == JSON.stringify(unsetFilters)} 
                        onClick={this.resetFilterCriteria} >
                        Reset Filter
                    </Button>
                    <Button variant="contained" style={{ backgroundColor: '#63BEB6' }}>
                        Run Filter
                    </Button>
                </div>
            </div>
        );
    }
}

export default FilterTab;