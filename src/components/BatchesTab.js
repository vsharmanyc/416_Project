import React, { Component } from 'react';
import { FormControl, TextField, Typography, Button, Checkbox } from '@material-ui/core';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import DeleteIcon from '@material-ui/icons/Delete';


class BatchesTab extends Component {
    constructor(props) {
        super(props);
        this.state = {
            batches: [{
                'Number of Plans': '',
                'Compact Threshold': '',
                'Population Equality Threshold': '',
                'use': false
            }]
        };
    }

    updateBatch = (batchIndex, field, value) => {
        let batches = this.state.batches;
        batches[batchIndex][field] = value;
        this.setState({ batches: batches })
    }

    deleteBatch = (batchIndex) => {
        let batches = this.state.batches;
        batches.splice(batchIndex, 1);
        this.setState({ batches: batches });
    }

    addBatch = () => {
        let batches = this.state.batches;
        batches.push({
            'Number of Plans': '',
            'Compact Threshold': '',
            'Population Equality Threshold': '',
            'use': false
        })
        this.setState({ batches: batches });
    }

    clearBatches = () => {
        this.setState({ batches: [] })
    }

    render() {
        const checkboxStyle = { color: "#63BEB6" }
        return (
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',

            }}>
                {
                    this.state.batches.map((batch, index) => {
                        return (
                            <div style={{
                                display: 'flex',
                                flexDirection: 'row',
                                justifyContent: 'center',
                                alignItems: 'center',
                                width: '100%'
                            }}>
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                }}>
                                    <DeleteIcon onClick={() => { this.deleteBatch(index) }} />
                                    <Checkbox
                                        checked={this.state.batches.use}
                                        style={checkboxStyle}
                                        onChange={(e) => { this.updateBatch(index, 'use', e.target.value) }}
                                    />
                                </div>
                                <div style={{
                                    marginTop: '2%',
                                    display: 'flex',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                    backgroundColor: '#ededed',
                                    borderRadius: '5%',
                                    width: '70%',
                                    padding: '3%'
                                }}>
                                    <FormControl>
                                        <TextField
                                            value={batch['Number of Plans']}
                                            onChange={(e) => { this.updateBatch(index, 'Number of Plans', e.target.value) }}
                                            label="Number of Plans"
                                            type="number"
                                            InputLabelProps={{ shrink: true }}
                                            InputProps={{ inputProps: { min: 0 } }}
                                        />
                                        <TextField
                                            value={batch['Compact Threshold']}
                                            onChange={(e) => { this.updateBatch(index, 'Compact Threshold', e.target.value) }}
                                            label="Compact Threshold"
                                            type="number"
                                            InputLabelProps={{ shrink: true }}
                                            InputProps={{ inputProps: { min: 0 } }}
                                        />
                                        <TextField
                                            value={batch['Population Equality Threshold']}
                                            onChange={(e) => { this.updateBatch(index, 'Population Equality Threshold', e.target.value) }}
                                            label="Population Equality Threshold"
                                            type="number"
                                            InputLabelProps={{ shrink: true }}
                                            InputProps={{ inputProps: { min: 0 } }}
                                        />
                                    </FormControl>
                                </div>
                            </div>
                        );
                    })
                }
                <Button onClick={this.addBatch}><AddCircleIcon fontSize='large' style={{ color: "#63BEB6" }} /></Button>
                <div style={{ display: 'flex', width:'75%', height: '10%', flexDirection: 'row', marginTop: '5%' }}>
                    <Button variant="contained" 
                        onClick={this.clearBatches} >
                        Clear Batches
                    </Button>
                    <Button variant="contained" style={{ backgroundColor: '#63BEB6' }}>
                        Generate Summary
                    </Button>
                </div>
            </div>
        );
    }
}

export default BatchesTab;