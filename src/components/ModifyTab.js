import React, { Component } from 'react';
import { Button, InputLabel, Select, MenuItem, FormControl, FormControlLabel, Checkbox, Typography, TextField } from '@material-ui/core/';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import DeleteIcon from '@material-ui/icons/Delete';

class ModifyTab extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: window.innerWidth,
            height: window.innerHeight,
            showUnfilledError: false,
            showDuplicateError: false,
            selectedDistrict: 'Selected District: Maryland - 5th District',
            districtData: {
                'Democrat': 250000,
                'Republican': 307550,
                'Libertarian': 2000,
                'Green Party': 100,
                'Unaffiliated': 1000,
                'Black': 100000,
                'White': 300000,
                'Native American and Alaska Native': 400,
                'Native Hawaiian and Pacific Islander': 250,
                'Asian': 90000,
                'Hispanic': 70000,
            },
            mods: [{ field: '', population: null }]
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

    updateMod = (index, param, value) => {
        let mods = this.state.mods;
        mods[index][param] = value;
        this.setState({ mods: mods });
    }

    clearMods = () => {
        this.setState({ mods: [] })
    }

    addMod = () => {
        let mods = this.state.mods;
        mods.push({ field: '', population: null })
        this.setState({ mods: mods });
    }

    deleteMod = (modIndex) => {
        let mods = this.state.mods;
        mods.splice(modIndex, 1);
        this.setState({ mods: mods });
    }

    validateAndRun = () => {
        let mods = this.state.mods;
        let hasUnfilled = false;
        let hasDuplicates = false;
        let seenFields = [];
        for (let i = 0; i < mods.length; i++){
            if (mods[i].field === '')
                hasUnfilled = true;
            if (seenFields[mods[i].field])
                hasDuplicates = true;
            else
                seenFields[mods[i].field] = true;
        }
        this.setState({ showUnfilledError: hasUnfilled, showDuplicateError: hasDuplicates })
    }

    render() {
        console.log(this.state);
        return (
            <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', marginTop: '5%' }}>
                <Typography variant="caption">{this.state.selectedDistrict}</Typography>
                {this.state.mods.map((data, index) => {
                    return (
                        <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center', marginTop: '5%' }}>
                            <DeleteIcon onClick={() => { this.deleteMod(index) }} />
                            <div style={{ display: 'flex', flexDirection: 'row', width: '90%', backgroundColor: '#ededed', borderRadius: '5%', padding: '2%' }}>
                                <FormControl style={{ minWidth: this.state.width * .1, maxWidth: this.state.width * .1 }}>
                                    <InputLabel id="demo-simple-select-label">Field</InputLabel>
                                    <Select
                                        labelId="demo-simple-select-label"
                                        id="demo-simple-select"
                                        value={data.field}
                                        onChange={(e) => { this.updateMod(index, 'field', e.target.value) }}
                                    >
                                        <MenuItem value='Democrat'>Democrat</MenuItem>
                                        <MenuItem value='Republican'>Republican</MenuItem>
                                        <MenuItem value='Libertarian'>Libertarian</MenuItem>
                                        <MenuItem value='Green Party'>Green Party</MenuItem>
                                        <MenuItem value='Unaffiliated'>Unaffiliated</MenuItem>
                                        <MenuItem value='Black'>Black</MenuItem>
                                        <MenuItem value='White'>White</MenuItem>
                                        <MenuItem value='Native American and Alaska Native'>Native American and Alaska Native</MenuItem>
                                        <MenuItem value='Native Hawaiian and Pacific Islander'>Native Hawaiian and Pacific Islander</MenuItem>
                                        <MenuItem value='Asian'>Asian</MenuItem>
                                        <MenuItem value='Hispanic'>Hispanic</MenuItem>
                                    </Select>
                                </FormControl>
                                <FormControl>
                                    <TextField
                                        value={data.population === null ? this.state.districtData[data.field] : data.population}
                                        onChange={(e) => { this.updateMod(index, 'population', e.target.value) }}
                                        label="Population"
                                        type="number"
                                        InputLabelProps={{ shrink: true }}
                                        InputProps={{ inputProps: { min: 0 } }}
                                    />
                                </FormControl>
                            </div>
                        </div>
                    );
                })}
                <Button onClick={this.addMod}><AddCircleIcon fontSize='large' style={{ color: "#63BEB6" }} /></Button>
                <div style={{ display: 'flex', flexDirection: 'row', marginTop: '5%' }}>
                    <Button variant="contained" disabled={this.state.mods.length === 0} onClick={this.clearMods} >
                        Clear Mods
                    </Button>
                    <Button variant="contained" disabled={this.state.mods.length === 0} style={{ backgroundColor: '#63BEB6' }}
                        onClick={this.validateAndRun}>
                        Apply Mods
                    </Button>
                </div>
                {this.state.showUnfilledError && this.state.mods.length != 0 ?
                    <Typography variant="caption" style={{ color: 'red' }}>Must fill all fields first</Typography> : <></>}
                {this.state.showDuplicateError && this.state.mods.length != 0 ?
                    <Typography variant="caption" style={{ color: 'red' }}>Can't have duplicate fields</Typography> : <></>}
            </div>
        );
    }
}

export default ModifyTab;