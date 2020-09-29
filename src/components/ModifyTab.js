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
            mods: []
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
        this.setState({mods: []})
    }

    addMod = () =>{
        let mods = this.state.mods;
        mods.push({field: '', population: 0})
        this.setState({mods: mods});
    }

    deleteMod = (modIndex) => {
        let mods = this.state.mods;
        mods.splice(modIndex, 1);
        this.setState({mods: mods});
    }

    render() {
        return (
            <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', marginTop: '5%' }}>
                {this.state.mods.map((data, index) => {
                    return (
                        <div  style={{ display: 'flex', flexDirection: 'row', justifyContent:'center', alignItems:'center', marginTop: '5%' }}>
                            <DeleteIcon onClick={() => { this.deleteMod(index) }}/>
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
                                        value={data.population}
                                        onChange={(e) => { this.updateMod(index, 'population', e.target.value) }}
                                        label="Population"
                                        type="number"
                                        InputLabelProps={{shrink: true}}
                                        InputProps={{ inputProps: { min: 0 } }}
                                    />
                                </FormControl>
                            </div>
                        </div>
                    );
                })}
                <AddCircleIcon fontSize='large' style={{ color: "#63BEB6" }} onClick={this.addMod}/>
                <div style={{ display: 'flex', flexDirection: 'row', marginTop: '5%' }}>
                    <Button variant="contained" disabled={this.state.mods.length === 0}onClick={this.clearMods} >
                        Clear Mods
                    </Button>
                    <Button variant="contained" style={{ backgroundColor: '#63BEB6' }}>
                        Make Mods
                    </Button>
                </div>
            </div>
        );
    }
}

export default ModifyTab;