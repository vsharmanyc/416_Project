import React, { Component } from 'react';

class CreateJob extends Component {

    constructor(props) {
        super(props);
    }


    render() {

        const formStyle = {
            height: '100%', width: '95%',
            backgroundColor: '#ebf3f5', padding: '5%', borderRadius: '3%', marginTop: '2%'
        };

        return (
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div style={formStyle}>
                    <form>
                        <div class="form-group">
                            <label for="number-input row">Number of Districtings</label>
                            <input class="form-control" type="number" defaultValue="100" min="1" id="example-number-input" />
                        </div>
                        <div class="form-group">
                            <label for="customRange1">Compactness</label>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                Least Compact
                                <input type="range" class="custom-range" id="customRange1" />
                                Most Compact
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="customRange2">Population Difference</label>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                Least Difference
                                <input type="range" class="custom-range" id="customRange1" />
                                Most Difference
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="exampleFormControlSelect1">Population Equation Threshold</label>
                            <select class="form-control" id="exampleFormControlSelect1">
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                                <option>4</option>
                                <option>5</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="exampleFormControlSelect2">Select Demographics</label>
                            <select multiple class="form-control" id="exampleFormControlSelect2">
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
