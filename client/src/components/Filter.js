import React, { Component } from 'react';
import MultiSelect from 'react-select';

class Filter extends Component {

    constructor(props) {
        super(props);

        this.state = {
            demographics: [],
        }
    }

    onDemographicsSelect = (selected) => { this.setState({ demographics: selected }); }

    clearForm = () => {
        this.setState({
            demographics: [],
        })
    }

    onFilter = (e) =>{
        e.preventDefault();
        this.clearForm();
    }

    render() {
        const demographics = [
            { value: 'White', label: 'White' },
            { value: 'Black', label: 'Black' },
            { value: 'Asian', label: 'Asian' },
            { value: 'Hispanic or Latino', label: 'Hispanic or Latino'},
            { value: 'American Indian or Alaska Native', label: 'American Indian or Alaska Native'},
            { value: 'Native Hawaiian or Other Pacific Islander', label: 'Native Hawaiian or Other Pacific Islander'},
            { value: 'Two or More Races', label: 'Two or More Races'},
            { value: 'Other', label: 'Other'}
        ]

        const formStyle = {
            height: '100%', width: '95%',
            backgroundColor: '#ebf3f5', padding: '5%', borderRadius: '3%', marginTop: '2%'
        };

        return (
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div style={formStyle}>
                    <form>
                        <div class="form-group">
                            <label for="FormControlSelect2">Filter By Demographics</label>
                            <MultiSelect  value={this.state.demographics} onChange={this.onDemographicsSelect} isMulti options={demographics}/>
                        </div>
                        <button onClick={this.onFilter} style={{ backgroundColor: '#63BEB6', color: '#ffffff' }} type="submit" class="btn">Filter</button>
                    </form>
                </div>
            </div>
        );
    }
}

export default Filter;
