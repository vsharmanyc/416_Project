import React, { Component } from 'react';

class CreateJob extends Component {

    constructor(props) {
        super(props);
        
        this.state = {
            numDistrings: -1,
            compactness: '',
            popDiff: '',
            popEqThres: 0.0,
            demographics: [],
        }
    }

    onPopEqThresChange = (e) => { this.setState({popEqThres: e.target.value}); }
    onNumDistrictingsChange = (e) => { this.setState({numDistrings: e.target.value}); }
    onCompactnessChange = (e) => { this.setState({compactness: e.target.value}); }
    onPopDiffChange = (e) => { this.setState({popDiff: e.target.value}); }

    onDemographicsSelect = (e) => { 
        let demographics = [];
        for(var option in e.target.selectedOptions)
            console.log(option);
        console.log(e.target.selectedOptions);
        //demographics.map((option, i) => demographics[i] = option.label); 
        this.setState({demographics: demographics}); 
    }

    render() {
        //console.log(this.state);

        const formStyle = {
            height: '100%', width: '95%',
            backgroundColor: '#ebf3f5', padding: '5%', borderRadius: '3%', marginTop: '2%'
        };

        return (
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div style={formStyle}>
                    <form>
                        <div class="form-group">
                            <label for="number-input1 row">Number of Districtings</label>
                            <input class="form-control" type="number" defaultValue="100" min="1" id="number-input1" 
                            onChange={this.onNumDistrictingsChange}/>
                        </div>
                        <div class="form-group">
                            <label for="customRange1">Compactness</label>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                Least Compact
                                <input type="range" class="custom-range" id="customRange1" onChange={this.onCompactnessChange}/>
                                Most Compact
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="customRange2">Population Difference</label>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                Least Difference
                                <input type="range" class="custom-range" id="customRange2" onChange={this.onPopDiffChange}/>
                                Most Difference
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="number-input2 row">Population Equation Threshold</label>
                            <input onChange={this.onPopEqThresChange} class="form-control" type="number" defaultValue="0.5" 
                                step="0.01" min="0" id="number-input2" />
                        </div>
                        <div class="form-group">
                            <label for="FormControlSelect2">Select Demographics</label>
                            <select onChange={this.onDemographicsSelect} multiple class="form-control" id="FormControlSelect2">
                                <option>White</option>
                                <option>Black</option>
                                <option>Asian</option>
                                <option>Hispanic</option>
                                <option>Other</option>
                            </select>
                        </div>
                        <button style={{backgroundColor: '#63BEB6', color: '#ffffff'}} type="submit" class="btn">Submit</button>
                    </form>
                </div>
            </div>
        );
    }
}

export default CreateJob;
